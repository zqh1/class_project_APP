package ca.dal.csci3130.quickcash.userlisting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.dal.csci3130.quickcash.R;

import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAO;
import ca.dal.csci3130.quickcash.jobmanagement.JobDAOAdapter;

public class ViewApplicantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewApplicantAdapter viewApplicantAdapter;

    private String jobKey;

    /**
     * OnCreate method, Initialize activity call. Connect this class with activity_view_applicants.
     * Getting JOB_KEY from intent.extra and call methods: init() and connectToFBDB()
     *
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicants);

        //Load jobKey
        jobKey = getIntent().getExtras().getString("JOB_KEY");

        init();
        connectToFBDB();
    }

    //Link recycler and set manager
    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    //Query and pass job table in firebase
    private void connectToFBDB() {
        //get applicantsID from each job
        new JobDAOAdapter(new JobDAO()).getDatabaseReference().child(jobKey).child("applicantsID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //If job has value inside child. turn String into String array
                String[] applicantsID = Objects.requireNonNull(snapshot.getValue()).toString().split(",");

                //Link adapter and recycler with query
                viewApplicantAdapter = new ViewApplicantAdapter(applicantsID, jobKey);
                recyclerView.setAdapter(viewApplicantAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewApplicantActivity.this, "Error reading applicants", Toast.LENGTH_SHORT).show();
            }
        });
    }
}