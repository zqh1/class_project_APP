package ca.dal.csci3130.quickcash.applicationslisting;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

/**
 * Activity to display all applications from a specific user
 */
public class ViewApplicationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewApplicationAdapter viewApplicationAdapter;

    /**
     * OnCreate method, Initialize activity and link elements from screen, then start Firebase
     * connection and retrieve all jobs in which the applicant have applied.
     *
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);

        init();
        connectToFBDB();
    }

    //Private method that link screen elements
    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    //Connect and retrieve jobs where applicant have applied
    private void connectToFBDB() {

        DAO.getJobReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> jobList = new ArrayList<>();

                for (DataSnapshot job : snapshot.getChildren()) {

                    if (Objects.requireNonNull(job.child("applicantsID").getValue()).toString().contains(SessionManager.getUserID())) {
                        jobList.add(job.getKey());
                    }
                }

                //Link screen adapter and recycler
                viewApplicationAdapter = new ViewApplicationAdapter(jobList, false);
                recyclerView.setAdapter(viewApplicationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewApplicationActivity.this, "Error reading applications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
