package ca.dal.csci3130.quickcash.applicationslisting;

import android.annotation.SuppressLint;
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
import ca.dal.csci3130.quickcash.joblisting.ViewJobAdapter;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAO;
import ca.dal.csci3130.quickcash.jobmanagement.JobInterface;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class ViewApplicationAdapter extends RecyclerView.Adapter<ViewJobAdapter.JobViewHolder> {

    private final List<String> jobList;

    public ViewApplicationAdapter (List<String> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewJobAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new ViewJobAdapter.JobViewHolder(view);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ViewJobAdapter.JobViewHolder holder, int position) {

        int jobPosition = holder.getBindingAdapterPosition();

        //Disable buttons not related to employee
        holder.applyBtn.setVisibility(View.GONE);
        holder.applicantBtn.setVisibility(View.GONE);

        //Query job details
        new JobDAO().getDatabaseReference().child(jobList.get(jobPosition)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                JobInterface job = snapshot.getValue(Job.class);
                if (job == null) {
                    Toast.makeText(holder.context, "Error reading job details", Toast.LENGTH_SHORT).show();
                    return;
                }

                holder.jobTitleTV.setText(job.getTitle());
                holder.descriptionTV.setText(job.getDescription());
                holder.durationTV.setText("Duration: " + job.getDuration() + " hours");
                holder.salaryTV.setText("Salary: $" + job.getSalary() + "/hour");
                holder.dateTV.setText("Date: " + job.getDay() + "/" + job.getMonth() + "/" + job.getYear());

                if (job.getAcceptedID().isEmpty()) {
                    holder.statusTV.setText("Status: Waiting for employer answer");
                }
                else if (job.getAcceptedID().equals(SessionManager.getUserID())) {
                    holder.statusTV.setText("Status: Accepted");
                    holder.deleteBtn.setVisibility(View.GONE);
                }
                else {
                    holder.statusTV.setText("Status: Rejected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.context, "Error reading job details", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteBtn.setOnClickListener(view ->
                new JobDAO().getDatabaseReference().child(jobList.get(jobPosition)).child("applicantsID")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String applicants = Objects.requireNonNull(snapshot.getValue()).toString();

                String newApplicants = applicants.replace(SessionManager.getUserID() + ",","");

                new JobDAO().getDatabaseReference().child(jobList.get(jobPosition)).child("applicantsID").setValue(newApplicants);

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
