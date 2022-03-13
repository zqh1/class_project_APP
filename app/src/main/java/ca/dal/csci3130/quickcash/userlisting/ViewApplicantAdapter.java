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

    /**
     *
     * Assigning each local variable to each parameter, and jobKey
     *
     * @param applicants: array of applicants who sign for the job
     * @param jobKey: Key of the job (job id)
     */
    public ViewApplicantAdapter(String[] applicants, String jobKey) {
        this.applicantsID = applicants;
        this.jobKey = jobKey;
    }

    /**
     * onCreateViewHolder of recycler, links layout in screen with jobs item and return the view
     *
     * @param parent:   Parent view of the screen
     * @param viewType: Type of view selected to the screen
     * @return ApplicantViewHolder: Return view adapter with view inflater linked
     */
    @NonNull
    @Override
    public ApplicantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);
        return new ApplicantViewHolder(view);
    }

    /**
     * onBindViewHolder of recycler, read and fill each job information to each element on screen
     *
     * @param holder:   Pass the job item with screen objects linked
     * @param position: Pass the location of the job in the list
     */
    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ApplicantViewHolder holder, int position) {

        //Load general information of job
        int jobPosition = holder.getBindingAdapterPosition();

        //Connect to firebase
        DAO.getUserReference().child(applicantsID[jobPosition]).addListenerForSingleValueEvent(new ValueEventListener() {

            //Get data from each applicantsID
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


        //Connect to firebase
        DatabaseReference jobDatabase = DAO.getJobReference().child(jobKey);
        //Choosing which applicant will be apply to the position
        holder.accept.setOnClickListener(view ->
            jobDatabase.child("acceptedID").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //If the acceptedID is empty then first applicant select will be assign for this job
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

    /**
     *
     * This method will return how many applicant(s) apply for this job
     *
     * @return int: number of applicants
     */
    @Override
    public int getItemCount() {
        return applicantsID.length;
    }


    /**
     * ApplicantViewHolder, class that link a Job item on screen
     */
    public static class ApplicantViewHolder extends RecyclerView.ViewHolder {

        private final TextView applicantName;
        private final Button accept;
        private final Context context;

        /**
         * ApplicantViewHolder constructor, link all item on screen
         *
         * @param itemView: item to link
         */
        public ApplicantViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            applicantName = itemView.findViewById(R.id.applicantNameTV);

            accept = itemView.findViewById(R.id.acceptBtn);
        }
    }
}
