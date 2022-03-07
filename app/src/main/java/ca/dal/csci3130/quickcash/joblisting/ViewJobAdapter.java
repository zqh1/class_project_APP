package ca.dal.csci3130.quickcash.joblisting;

import android.annotation.SuppressLint;
import android.content.Context;
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
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

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

        if (job.isUrgent()){
            holder.urgentTV.setText("Urgent!");
        }else{
            holder.urgentTV.setText("");
        }

        //Check if user is employee or employer and disable buttons not related
        if (SessionManager.getUser().getIsEmployee().equals("y")) {
            holder.deleteBtn.setVisibility(View.GONE);
            holder.applicantBtn.setVisibility(View.GONE);

            //Set Apply listener
            holder.applyBtn.setOnClickListener(view ->
                    FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                    .getReference().child("Job")
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
            holder.applyBtn.setVisibility(View.GONE);

            //Set delete and applicants buttons listeners
            holder.deleteBtn.setOnClickListener(view ->
                    FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                    .getReference().child("Job")
                    .child(Objects.requireNonNull(getRef(position).getKey()))
                    .removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(holder.context, "Application deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(holder.context, "Application delete failed", Toast.LENGTH_SHORT).show()));

            //TODO


        }
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        private final TextView jobTitleTV;
        private final Context context;
        private final TextView descriptionTV;
        private final TextView durationTV;
        private final TextView salaryTV;
        private final TextView urgentTV;
        private final TextView dateTV;
        private final Button deleteBtn;
        private final Button applicantBtn;
        private final Button applyBtn;

        public JobViewHolder(@NonNull View itemView) {

            super(itemView);
            context = itemView.getContext();

            jobTitleTV = itemView.findViewById(R.id.jobTitleTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            durationTV = itemView.findViewById(R.id.durationTV);
            salaryTV = itemView.findViewById(R.id.salaryTV);
            urgentTV = itemView.findViewById(R.id.urgentTV);
            dateTV = itemView.findViewById(R.id.dateTV);

            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            applicantBtn = itemView.findViewById(R.id.applicantBtn);
            applyBtn = itemView.findViewById(R.id.applyBtn);
        }
    }
}
