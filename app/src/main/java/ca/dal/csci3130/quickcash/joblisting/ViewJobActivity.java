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

/**
 * Activity that display all jobs in firebase
 */
public class ViewJobActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewJobAdapter viewJobAdapter;


    /**
     * OnCreate method, Initialize activity and recycler. Query the Job table on firebase and
     * send data to recycler to be populated
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

    //Link recycler and set manager
    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    //Query and pass job table in firebase
    private void connectToFBDB() {

        //Verify the user is valid, if user not loaded correctly, restart application from main
        if (SessionManager.getUser() == null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        //Query firebase depending on if user is employee or employer
        final FirebaseRecyclerOptions<Job> options;

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

        //Link adapter and recycler with query
        viewJobAdapter = new ViewJobAdapter(options);
        recyclerView.setAdapter(viewJobAdapter);
    }


    /**
     * onStart method listen for firebase util stop is called
     */
    @Override
    protected void onStart() {
        super.onStart();
        viewJobAdapter.startListening();
    }

    /**
     * Stop hearing of firebase
     */
    @Override
    protected void onStop() {
        super.onStop();
        viewJobAdapter.stopListening();
    }
}
