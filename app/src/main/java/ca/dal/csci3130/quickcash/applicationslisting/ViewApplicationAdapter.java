package ca.dal.csci3130.quickcash.applicationslisting;

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
import ca.dal.csci3130.quickcash.joblisting.ViewJobAdapter;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobInterface;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class ViewApplicationAdapter extends RecyclerView.Adapter<ViewJobAdapter.JobViewHolder> {

    private final List<String> jobList;
    private final boolean search;

    public ViewApplicationAdapter (List<String> jobList, boolean search) {
        this.jobList = jobList;
        this.search = search;
    }

    @NonNull
    @Override
    public ViewJobAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new ViewJobAdapter.JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewJobAdapter.JobViewHolder holder, int position) {

        int jobPosition = holder.getBindingAdapterPosition();

        //Disable buttons not related to employee
        if (!search) holder.applyBtn.setVisibility(View.GONE);
        holder.applicantBtn.setVisibility(View.GONE);

        //Query job details
        DAO.getJobReference().child(jobList.get(jobPosition)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                JobInterface job = snapshot.getValue(Job.class);
                if (job == null) {
                    Toast.makeText(holder.context, "Error reading job details", Toast.LENGTH_SHORT).show();
                    return;
                }

                String duration = "Duration: " + job.getDuration() + " hours";
                String salary = "Salary: $" + job.getSalary() + "/hour";
                String date = "Date: " + job.getDay() + "/" + job.getMonth() + "/" + job.getYear();

                holder.jobTitleTV.setText(job.getTitle());
                holder.descriptionTV.setText(job.getDescription());
                holder.durationTV.setText(duration);
                holder.salaryTV.setText(salary);
                holder.dateTV.setText(date);

                String statusLabel;

                if (job.getAcceptedID().isEmpty()) {

                    if (job.getApplicantsID().contains(SessionManager.getUserID())) {
                        statusLabel = "Status: Waiting for employer answer";
                    }
                    else statusLabel = "Status: Open position";
                }
                else if (job.getAcceptedID().equals(SessionManager.getUserID())) {
                    statusLabel = "Status: Accepted";
                    holder.deleteBtn.setVisibility(View.GONE);
                }
                else {
                    statusLabel = "Status: Rejected";
                }

                holder.statusTV.setText(statusLabel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.context, "Error reading job details", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteBtn.setOnClickListener(view ->
                DAO.getJobReference().child(jobList.get(jobPosition)).child("applicantsID")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String applicants = Objects.requireNonNull(snapshot.getValue()).toString();

                String newApplicants = applicants.replace(SessionManager.getUserID() + ",","");

                DAO.getJobReference().child(jobList.get(jobPosition)).child("applicantsID").setValue(newApplicants);

                Toast.makeText(holder.context, "Application removed, exit to refresh", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.context, "Error deleting application", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }
}
