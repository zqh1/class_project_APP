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
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;

public class ViewApplicantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewApplicantAdapter viewApplicantAdapter;

    private String jobKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicants);

        //Load jobKey
        jobKey = getIntent().getExtras().getString("JOB_KEY");

        init();
        connectToFBDB();
    }

    private void init() {
        recyclerView = findViewById(R.id.applicantRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void connectToFBDB() {

        DAO.getJobReference().child(jobKey).child("applicantsID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] applicantsID = Objects.requireNonNull(snapshot.getValue()).toString().split(",");

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