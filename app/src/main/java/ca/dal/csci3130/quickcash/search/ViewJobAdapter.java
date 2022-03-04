package ca.dal.csci3130.quickcash.search;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

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

import org.w3c.dom.Text;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.Job;

public class ViewJobAdapter extends FirebaseRecyclerAdapter<Job, ViewJobAdapter.JobViewHolder> {//extends FirebaseRecyclerAdapter<Job, ViewJobAdapter.JobViewHolder> {

    public ViewJobAdapter(@NonNull FirebaseRecyclerOptions<Job> options) {

        super(options);
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    public int getItemCount(){return super.getItemCount();}

    @Override
    protected void onBindViewHolder(@NonNull JobViewHolder holder, int position, @NonNull Job job) {


        holder.jobTitleTV.setText(job.getTitle());
        /*holder.descriptionTV.setText("");
        holder.durationTV.setText("");
        holder.salaryTV.setText("");
        holder.locationTV.setText("");
        holder.dateTV.setText("");
        holder.tagsTV.setText("");

        if (job.isUrgent()){
            holder.urgentTV.setText("Urgent!");
        }else{
            holder.urgentTV.setText("");
        }*/


        /*holder.applyBtn.setOnClickListener(view -> {
            Context context = getApplicationContext();
            Toast.makeText(holder.context, "please", Toast.LENGTH_SHORT).show();
        });*/

    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitleTV;
        private final Context context;

        /*private final TextView descriptionTV;
        private final TextView durationTV;
        private final TextView salaryTV;
        private final TextView urgentTV;
        private final TextView locationTV;
        private final TextView dateTV;
        private final TextView tagsTV;*/
        //private final Button applyBtn;


        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTV = itemView.findViewById(R.id.jobTitleTV);
            context = itemView.getContext();

            /*descriptionTV = itemView.findViewById(R.id.descriptionTV);
            durationTV = itemView.findViewById(R.id.durationTV);
            salaryTV = itemView.findViewById(R.id.salaryTV);
            urgentTV = itemView.findViewById(R.id.urgentTV);
            locationTV = itemView.findViewById(R.id.locationTV);
            tagsTV = itemView.findViewById(R.id.tagsTV);
            dateTV = itemView.findViewById(R.id.dateTV);*/
            //applyBtn = itemView.findViewById(R.id.applyBtn);

        }
    }
}
