package ca.dal.csci3130.quickcash.userlisting;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;

public class ViewApplicantAdapter extends RecyclerView.Adapter<ViewApplicantAdapter.ApplicantViewHolder> {

    private final String[] applicantsID;
    private final String jobKey;

    public ViewApplicantAdapter(String[] applicants, String jobKey) {
        this.applicantsID = applicants;
        this.jobKey = jobKey;
    }

    @NonNull
    @Override
    public ApplicantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);
        return new ApplicantViewHolder(view);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ApplicantViewHolder holder, int position) {

        int jobPosition = holder.getBindingAdapterPosition();

        DAO.getUserReference().child(applicantsID[jobPosition]).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = Objects.requireNonNull(snapshot.child("firstName").getValue()).toString();
                String lastName = Objects.requireNonNull(snapshot.child("lastName").getValue()).toString();
                holder.applicantName.setText(firstName + " " + lastName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.context, "Error reading applicant information", Toast.LENGTH_LONG).show();
            }
        });


        DatabaseReference jobDatabase = DAO.getJobReference().child(jobKey);

        holder.accept.setOnClickListener(view ->
            jobDatabase.child("acceptedID").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (Objects.requireNonNull(snapshot.getValue()).toString().isEmpty()) {
                        jobDatabase.child("acceptedID").setValue(applicantsID[jobPosition]);

                        Toast.makeText(holder.context, "Applicant accepted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(holder.context, "An Applicant was already accepted for this job", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(holder.context, "Error accepting applicant", Toast.LENGTH_LONG).show();
                }
            })
        );
    }

    @Override
    public int getItemCount() {
        return applicantsID.length;
    }


    public static class ApplicantViewHolder extends RecyclerView.ViewHolder {

        private final TextView applicantName;
        private final Button accept;
        private final Context context;

        public ApplicantViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            applicantName = itemView.findViewById(R.id.applicantNameTV);

            accept = itemView.findViewById(R.id.acceptBtn);
        }
    }
}
