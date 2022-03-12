package ca.dal.csci3130.quickcash.joblisting;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.applicationslisting.ViewApplicationAdapter;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;

/**
 * Activity that manage and contains recycler to view jobs based on search parameters
 */
public class ViewJobSearchWithPreferences extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> jobList;

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
        linkAndLoadRecycle();
    }

    //Link recycler and load job list from intent
    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        jobList = getIntent().getExtras().getStringArrayList("JOBLIST");
    }

    //Link view adapter with jobList
    private void linkAndLoadRecycle() {
        ViewApplicationAdapter viewApplicationAdapter = new ViewApplicationAdapter(jobList, false);
        recyclerView.setAdapter(viewApplicationAdapter);
    }
}
