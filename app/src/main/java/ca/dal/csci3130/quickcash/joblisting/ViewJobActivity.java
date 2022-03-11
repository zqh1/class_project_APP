package ca.dal.csci3130.quickcash.joblisting;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class ViewJobActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewJobAdapter viewJobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        init();
        connectToFBDB();
    }

    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void connectToFBDB() {

        final FirebaseRecyclerOptions<Job> options;

        if (SessionManager.getUser() == null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        if (SessionManager.getUser().getIsEmployee().equals("y")) {
            options = new FirebaseRecyclerOptions.Builder<Job>()
                    .setQuery(FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                            .getReference().child("Job"), Job.class).build();
        } else {
            options = new FirebaseRecyclerOptions.Builder<Job>()
                    .setQuery(FirebaseDatabase.getInstance(Constants.FIREBASE_URL)
                            .getReference().child("Job")
                            .orderByChild("employerID").equalTo(SessionManager.getUserID()), Job.class).build();
        }

        viewJobAdapter = new ViewJobAdapter(options);
        recyclerView.setAdapter(viewJobAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewJobAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewJobAdapter.stopListening();
    }
}
