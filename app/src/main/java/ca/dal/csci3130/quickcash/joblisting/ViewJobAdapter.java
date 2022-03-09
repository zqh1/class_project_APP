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
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAO;
import ca.dal.csci3130.quickcash.userlisting.ViewApplicantActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;

public class ViewJobAdapter extends FirebaseRecyclerAdapter<Job, ViewJobAdapter.JobViewHolder> {

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
    @SuppressLint("SetTextI18n")
    protected void onBindViewHolder(@NonNull JobViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Job job) {

        holder.jobTitleTV.setText(job.getTitle());
        holder.descriptionTV.setText(job.getDescription());
        holder.durationTV.setText("Duration: " + job.getDuration() + " hours");
        holder.salaryTV.setText("Salary: $" + job.getSalary() + "/hour");
        holder.dateTV.setText("Date: " + job.getDay() + "/" + job.getMonth() + "/" + job.getYear());
        holder.urgentTV.setText("Urgent!");

        if (!job.isUrgent()){
            holder.urgentTV.setVisibility(View.GONE);
        }

        //Check if user is employee or employer and disable buttons not related
        if (SessionManager.getUser().getIsEmployee().equals("y")) {

            if (job.getAcceptedID().isEmpty()) {
                holder.statusTV.setText("Status: Open position");
            }
            else if (job.getAcceptedID().equals(SessionManager.getUserID())) {
                holder.statusTV.setText("Status: Accepted");
            }
            else {
                holder.statusTV.setText("Status: Rejected");
            }

            holder.deleteBtn.setVisibility(View.GONE);
            holder.applicantBtn.setVisibility(View.GONE);

            //Set Apply listener
            holder.applyBtn.setOnClickListener(view ->
                    new JobDAO().getDatabaseReference()
                    .child(Objects.requireNonNull(getRef(position).getKey()))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String current = Objects.requireNonNull(snapshot.child("applicantsID").getValue()).toString();

                            if (current.contains(SessionManager.getUserID())) {
                                Toast.makeText(holder.context, "Already applied, waiting for response", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                current += SessionManager.getUserID() + ",";

                                FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                                        .getReference().child("Job")
                                        .child(Objects.requireNonNull(getRef(position).getKey())).child("applicantsID").setValue(current);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(holder.context, "Error while applying", Toast.LENGTH_SHORT).show();
                        }
                    })
            );
        }
        else {

            if (job.getAcceptedID().isEmpty()) {
                holder.statusTV.setText("Status: Open position");
            }
            else {

                holder.applicantBtn.setVisibility(View.GONE);

                new UserDAO().getDatabaseReference().child(job.getAcceptedID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String firstName = Objects.requireNonNull(snapshot.child("firstName").getValue()).toString();
                        String lastName = Objects.requireNonNull(snapshot.child("lastName").getValue()).toString();

                        holder.statusTV.setText("Status: " + firstName + " " + lastName + " accepted");
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
                    new JobDAO().getDatabaseReference().child(Objects.requireNonNull(getRef(position).getKey()))
                            .child("applicantsID").addListenerForSingleValueEvent(new ValueEventListener() {
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
