package ca.dal.csci3130.quickcash.joblisting;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.applicationslisting.ViewApplicationAdapter;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;

public class ViewJobSearchWithPreferences extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<String> jobList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);

        init();
        linkAndLoadRecycle();
    }

    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        jobList = getIntent().getExtras().getStringArrayList("JOBLIST");


        recyclerView = findViewById(R.id.jobsRecyclerView);
    }

    private void linkAndLoadRecycle() {
        ViewApplicationAdapter viewApplicationAdapter = new ViewApplicationAdapter(jobList, false);
        recyclerView.setAdapter(viewApplicationAdapter);
    }
}
