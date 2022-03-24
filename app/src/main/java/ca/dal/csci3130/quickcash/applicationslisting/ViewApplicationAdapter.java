package ca.dal.csci3130.quickcash.applicationslisting;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.feedback.Feedback;
import ca.dal.csci3130.quickcash.feedback.FeedbackInterface;
import ca.dal.csci3130.quickcash.joblisting.ViewJobAdapter;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobInterface;
import ca.dal.csci3130.quickcash.jobmanagement.JobMap;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

/**
 * Adapter that create and populate Jobs in applications list
 */
public class ViewApplicationAdapter extends RecyclerView.Adapter<ViewJobAdapter.JobViewHolder> {

    private final List<String> jobList;
    private final boolean search;

    /**
     * Public constructor on applications adapter. Read job information and populate the recycler
     *
     * @param jobList: String arraylist with the jobs IDs
     * @param search:  Search boolean that separate the preference saving vs search
     */
    public ViewApplicationAdapter(List<String> jobList, boolean search) {
        this.jobList = jobList;
        this.search = search;
    }

    /**
     * onCreateViewHolder of recycler, links layout in screen with jobs item and return the view
     *
     * @param parent:   Parent view of the screen
     * @param viewType: Type of view selected to the screen
     * @return JobViewHolder: Return view adapter with view inflater linked
     */
    @NonNull
    @Override
    public ViewJobAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new ViewJobAdapter.JobViewHolder(view);
    }

    /**
     * onBindViewHolder of recycler, read and fill each job information to each element on screen
     *
     * @param holder:   Pass the job item with screen objects linked
     * @param position: Pass the location of the job in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewJobAdapter.JobViewHolder holder, int position) {

        int jobPosition = holder.getBindingAdapterPosition();

        //Disable buttons not related to employee or search
        if (!search) holder.applyBtn.setVisibility(View.GONE);
        holder.applicantBtn.setVisibility(View.GONE);

        //Query job details
        DAO.getJobReference().child(jobList.get(jobPosition)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Retrieve job from database
                JobInterface job = snapshot.getValue(Job.class);

                //If job failed to load, display error and return to prevent crash
                if (job == null) {
                    Toast.makeText(holder.context, "Error reading job details", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Read and create screen labels
                String duration = "Duration: " + job.getDuration() + " hours";
                String salary = "Salary: $" + job.getSalary() + "/hour";
                String date = "Date: " + job.getDay() + "/" + job.getMonth() + "/" + job.getYear();
                String statusLabel;

                if (job.getAcceptedID().isEmpty()) {

                    if (job.getApplicantsID().contains(SessionManager.getUserID())) {
                        statusLabel = "Status: Waiting for employer answer";
                    } else statusLabel = "Status: Open position";

                } else if (job.getAcceptedID().equals(SessionManager.getUserID())) {

                    statusLabel = "Status: Accepted";
                    holder.deleteBtn.setVisibility(View.GONE);

                } else {
                    statusLabel = "Status: Rejected";
                }

                //Set screen labels
                holder.jobTitleTV.setText(job.getTitle());
                holder.descriptionTV.setText(job.getDescription());
                holder.durationTV.setText(duration);
                holder.salaryTV.setText(salary);
                holder.dateTV.setText(date);
                holder.statusTV.setText(statusLabel);

                holder.mapBtn.setOnClickListener(view -> {

                    Intent mapIntent = new Intent(holder.context, JobMap.class);
                    mapIntent.putExtra("JOBNAME", job.getTitle());
                    mapIntent.putExtra("LONGITUDE", job.getLongitude());
                    mapIntent.putExtra("LATITUDE", job.getLatitude());

                    holder.context.startActivity(mapIntent);
                });

                DAO.getUserReference().child(job.getEmployerID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String firstName = Objects.requireNonNull(snapshot.child("firstName").getValue()).toString();
                        String lastName = Objects.requireNonNull(snapshot.child("lastName").getValue()).toString();

                        String employerName = "Employer name: " + firstName + " " + lastName;
                        holder.employerName.setText(employerName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(holder.context, "Error while retrieving employee name", Toast.LENGTH_SHORT).show();
                    }
                });

                setFeedback(holder, job);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.context, "Error reading job details", Toast.LENGTH_SHORT).show();
            }
        });


        //Set delete button listener
        holder.deleteBtn.setOnClickListener(view ->
                DAO.getJobReference().child(jobList.get(jobPosition)).child("applicantsID")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                //Read current applicants
                                String applicants = Objects.requireNonNull(snapshot.getValue()).toString();

                                //Delete user id from list
                                applicants = applicants.replace(SessionManager.getUserID() + ",", "");

                                //Set new applicant list
                                DAO.getJobReference().child(jobList.get(jobPosition)).child("applicantsID").setValue(applicants);

                                //Message to user that deletion was successfully
                                Toast.makeText(holder.context, "Application removed, exit to refresh", Toast.LENGTH_SHORT).show();

                                //Disable button to avoid user to click again
                                holder.deleteBtn.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(holder.context, "Error deleting application", Toast.LENGTH_SHORT).show();
                            }
                        }));
    }

    private void setFeedback(@NonNull ViewJobAdapter.JobViewHolder holder, JobInterface job){
        //Connect to firebase
        DAO.getFeedbackDatabase().orderByChild("id").equalTo(job.getEmployerID()).addListenerForSingleValueEvent(new ValueEventListener() {
            //Get rating from each employee
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() == 1) {

                    DataSnapshot data = snapshot.getChildren().iterator().next();

                    FeedbackInterface feedback = Objects.requireNonNull(data.getValue(Feedback.class));

                    float starNum = ((float) feedback.getRating()) / ((float) feedback.getCount());

                    holder.ratingBar.setRating(starNum);

                } else holder.ratingBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.context, "Error reading rating information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * getItemCount for recycler, return number of items the recycler would need to load
     *
     * @return int: total jobs to populate on recycler
     */
    @Override
    public int getItemCount() {
        return jobList.size();
    }
}
