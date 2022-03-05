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
        holder.descriptionTV.setText(job.getDescription());
        holder.durationTV.setText("Duration: " + job.getDuration() + " hours");
        holder.salaryTV.setText("Salary: $" + job.getSalary() + "/hour");
        holder.dateTV.setText("Date: " + job.getDay() + "/" + job.getMonth() + "/" + job.getYear());

        if (job.isUrgent()){
            holder.urgentTV.setText("Urgent!");
        }else{
            holder.urgentTV.setText("");
        }

        //TODO apply button
        /*holder.applyBtn.setOnClickListener(view -> {
            Context context = getApplicationContext();
        });*/

    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitleTV;
        private final Context context;
        private final TextView descriptionTV;
        private final TextView durationTV;
        private final TextView salaryTV;
        private final TextView urgentTV;
        private final TextView dateTV;


        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTV = itemView.findViewById(R.id.jobTitleTV);
            context = itemView.getContext();

            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            durationTV = itemView.findViewById(R.id.durationTV);
            salaryTV = itemView.findViewById(R.id.salaryTV);
            urgentTV = itemView.findViewById(R.id.urgentTV);
            dateTV = itemView.findViewById(R.id.dateTV);


        }
    }
}
