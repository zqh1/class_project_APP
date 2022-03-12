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

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.userlisting.ViewApplicantActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class ViewJobAdapter extends FirebaseRecyclerAdapter<Job, ViewJobAdapter.JobViewHolder> {

    private static final String APPLICANT_ID = "applicantsID";

    public ViewJobAdapter(@NonNull FirebaseRecyclerOptions<Job> options) {
        super(options);
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Job job) {

        String durationText = "Duration: " + job.getDuration() + " hours";
        String salaryText = "Salary: $" + job.getSalary() + "/hour";
        String dateText = "Date: " + job.getDay() + "/" + job.getMonth() + "/" + job.getYear();
        String urgentText = "Urgent!";

        holder.jobTitleTV.setText(job.getTitle());
        holder.descriptionTV.setText(job.getDescription());
        holder.durationTV.setText(durationText);
        holder.salaryTV.setText(salaryText);
        holder.dateTV.setText(dateText);
        holder.urgentTV.setText(urgentText);

        if (!job.isUrgent()){
            holder.urgentTV.setVisibility(View.GONE);
        }

        //Check if user is employee or employer and bind the object
        if (SessionManager.getUser().getIsEmployee().equals("y")) {
            bindEmployee(holder, position, job);
        }
        else {
            bindEmployer(holder, position, job);
        }
    }

    private void bindEmployee(JobViewHolder holder, int position, Job job) {

        if (job.getAcceptedID().isEmpty()) {
            String label = "Status: Open position";
            holder.statusTV.setText(label);
        }
        else if (job.getAcceptedID().equals(SessionManager.getUserID())) {
            String label = "Status: Accepted";
            holder.statusTV.setText(label);
        }
        else {
            String label = "Status: Rejected";
            holder.statusTV.setText(label);
        }

        holder.deleteBtn.setVisibility(View.GONE);
        holder.applicantBtn.setVisibility(View.GONE);

        //Set Apply listener
        holder.applyBtn.setOnClickListener(view ->
                DAO.getJobReference().child(Objects.requireNonNull(getRef(position).getKey()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String current = Objects.requireNonNull(snapshot.child(APPLICANT_ID).getValue()).toString();

                                if (current.contains(SessionManager.getUserID())) {
                                    Toast.makeText(holder.context, "Already applied, waiting for response", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    current += SessionManager.getUserID() + ",";

                                    FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                                            .getReference().child("Job")
                                            .child(Objects.requireNonNull(getRef(position).getKey())).child(APPLICANT_ID).setValue(current);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(holder.context, "Error while applying", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    private void bindEmployer(JobViewHolder holder, int position, Job job) {

        if (job.getAcceptedID().isEmpty()) {
            String statusText = "Status: Open position";
            holder.statusTV.setText(statusText);
        }
        else {

            holder.applicantBtn.setVisibility(View.GONE);

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
                    Toast.makeText(holder.context, "Error while retrieving employee names", Toast.LENGTH_SHORT).show();
                }
            });
        }

        holder.applyBtn.setVisibility(View.GONE);

        //Set delete and applicants buttons listeners
        holder.deleteBtn.setOnClickListener(view ->
                FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                        .getReference().child("Job")
                        .child(Objects.requireNonNull(getRef(position).getKey()))
                        .removeValue()
                        .addOnSuccessListener(aVoid -> Toast.makeText(holder.context, "Application deleted successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(holder.context, "Application delete failed", Toast.LENGTH_SHORT).show()));

        holder.applicantBtn.setOnClickListener(view ->
                DAO.getJobReference().child(Objects.requireNonNull(getRef(position).getKey()))
                        .child(APPLICANT_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!Objects.requireNonNull(snapshot.getValue()).toString().isEmpty()) {
                            holder.context.startActivity(new Intent(holder.context, ViewApplicantActivity.class).putExtra("JOB_KEY", getRef(position).getKey()));
                        }
                        else {
                            Toast.makeText(holder.context, "No applicants yet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(holder.context, "Error reading applicants", Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        public final TextView jobTitleTV;
        public final Context context;
        public final TextView descriptionTV;
        public final TextView durationTV;
        public final TextView salaryTV;
        public final TextView urgentTV;
        public final TextView dateTV;
        public final TextView statusTV;
        public final Button deleteBtn;
        public final Button applicantBtn;
        public final Button applyBtn;

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

            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            applicantBtn = itemView.findViewById(R.id.applicantBtn);
            applyBtn = itemView.findViewById(R.id.applyBtn);
        }
    }
}
