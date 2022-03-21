package ca.dal.csci3130.quickcash.joblisting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobMap;
import ca.dal.csci3130.quickcash.userlisting.ViewApplicantActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

/**
 * Adapter to view all jobs using job firebase recycler
 */
public class ViewJobAdapter extends FirebaseRecyclerAdapter<Job, ViewJobAdapter.JobViewHolder> {

    private static final String APPLICANT_ID = "applicantsID";

    /**
     * Constructor of adapter, call super constructor of firebase
     *
     * @param options: Options of firebase
     */
    public ViewJobAdapter(@NonNull FirebaseRecyclerOptions<Job> options) {
        super(options);
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
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    /**
     * onBindViewHolder of recycler, read and fill each job information to each element on screen
     *
     * @param holder:   Pass the job item with screen objects linked
     * @param position: Pass the location of the job in the list
     */
    @Override
    protected void onBindViewHolder(@NonNull JobViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Job job) {

        //Load general information of job
        String durationText = "Duration: " + job.getDuration() + " hours";
        String salaryText = "Salary: $" + job.getSalary() + "/hour";
        String dateText = "Date: " + job.getDay() + "/" + job.getMonth() + "/" + job.getYear();
        String urgentText = "Urgent!";

        //Populate job fields
        holder.jobTitleTV.setText(job.getTitle());
        holder.descriptionTV.setText(job.getDescription());
        holder.durationTV.setText(durationText);
        holder.salaryTV.setText(salaryText);
        holder.dateTV.setText(dateText);
        holder.urgentTV.setText(urgentText);

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


        //Hide or not the urgent field
        if (!job.isUrgent()) {
            holder.urgentTV.setVisibility(View.GONE);
        }

        //Check if user is employee or employer and bind the view
        if (SessionManager.getUser().getIsEmployee().equals("y")) {
            bindEmployee(holder, position, job);
        } else {
            bindEmployer(holder, position, job);
        }
    }

    //Bind an employee view
    private void bindEmployee(JobViewHolder holder, int position, Job job) {

        //Disable buttons not related to employee
        holder.deleteBtn.setVisibility(View.GONE);
        holder.applicantBtn.setVisibility(View.GONE);

        //Set status label
        String label;

        if (job.getAcceptedID().isEmpty()) {
            label = "Status: Open position";
        } else if (job.getAcceptedID().equals(SessionManager.getUserID())) {
            label = "Status: Accepted";
        } else {
            label = "Status: Rejected";
        }

        holder.statusTV.setText(label);

        //Set Apply listener
        holder.applyBtn.setOnClickListener(view ->
                DAO.getJobReference().child(Objects.requireNonNull(getRef(position).getKey()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String current = Objects.requireNonNull(snapshot.child(APPLICANT_ID).getValue()).toString();

                                if (current.contains(SessionManager.getUserID())) {
                                    Toast.makeText(holder.context, "Already applied, waiting for response", Toast.LENGTH_SHORT).show();
                                } else {
                                    current += SessionManager.getUserID() + ",";

                                    FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                                            .getReference().child("Job")
                                            .child(Objects.requireNonNull(getRef(position).getKey())).child(APPLICANT_ID).setValue(current);

                                    Toast.makeText(holder.context, "Applied, wait for employer response", Toast.LENGTH_SHORT).show();
                                }

                                holder.applyBtn.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(holder.context, "Error while applying", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    //Bind the employer view
    private void bindEmployer(JobViewHolder holder, int position, Job job) {

        //Disable buttons not related to employer
        holder.applyBtn.setVisibility(View.GONE);

        //If an employee has not been accepted, set open status and set applicants button listener
        if (job.getAcceptedID().isEmpty()) {

            String statusText = "Status: Open position";
            holder.statusTV.setText(statusText);

            //Set applicants button listener
            holder.applicantBtn.setOnClickListener(view ->
                    DAO.getJobReference().child(Objects.requireNonNull(getRef(position).getKey()))
                            .child(APPLICANT_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!Objects.requireNonNull(snapshot.getValue()).toString().isEmpty()) {
                                holder.context.startActivity(new Intent(holder.context, ViewApplicantActivity.class).putExtra("JOB_KEY", getRef(position).getKey()));
                            } else {
                                Toast.makeText(holder.context, "No applicants yet", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(holder.context, "Error reading applicants", Toast.LENGTH_SHORT).show();
                        }
                    })
            );
        } else {

            //Disable applicants button because an applicant was already selected
            holder.applicantBtn.setVisibility(View.GONE);

            //Query and load employee name
            DAO.getUserReference().child(job.getAcceptedID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstName = Objects.requireNonNull(snapshot.child("firstName").getValue()).toString();
                    String lastName = Objects.requireNonNull(snapshot.child("lastName").getValue()).toString();

                    String statusText = "Status: " + firstName + " " + lastName + " accepted";
                    holder.statusTV.setText(statusText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(holder.context, "Error while retrieving employee name", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Set delete button listeners
        holder.deleteBtn.setOnClickListener(view ->
                FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                        .getReference().child("Job")
                        .child(Objects.requireNonNull(getRef(position).getKey()))
                        .removeValue()
                        .addOnSuccessListener(aVoid -> Toast.makeText(holder.context, "Application deleted successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(holder.context, "Application delete failed", Toast.LENGTH_SHORT).show()));
    }

    /**
     * JobViewHolder, class that link a Job item on screen
     */
    public static class JobViewHolder extends RecyclerView.ViewHolder {

        public final TextView jobTitleTV;
        public final Context context;
        public final TextView descriptionTV;
        public final TextView durationTV;
        public final TextView salaryTV;
        public final TextView urgentTV;
        public final TextView dateTV;
        public final TextView statusTV;
        public final TextView employerName;
        public final Button deleteBtn;
        public final Button applicantBtn;
        public final Button applyBtn;
        public final Button mapBtn;

        /**
         * JobViewHolder constructor, link all item on screen
         *
         * @param itemView: item to link
         */
        public JobViewHolder(@NonNull View itemView) {

            super(itemView);
            context = itemView.getContext();

            jobTitleTV = itemView.findViewById(R.id.jobTitleTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            durationTV = itemView.findViewById(R.id.durationTV);
            salaryTV = itemView.findViewById(R.id.salaryTV);
            urgentTV = itemView.findViewById(R.id.urgentTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            employerName = itemView.findViewById(R.id.employerName);

            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            applicantBtn = itemView.findViewById(R.id.applicantBtn);
            applyBtn = itemView.findViewById(R.id.applyBtn);
            mapBtn = itemView.findViewById(R.id.mapBtn);
        }
    }
}
