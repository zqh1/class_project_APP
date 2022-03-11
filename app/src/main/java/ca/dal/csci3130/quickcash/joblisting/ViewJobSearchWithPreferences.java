package ca.dal.csci3130.quickcash.joblisting;

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
import ca.dal.csci3130.quickcash.applicationslisting.ViewApplicationAdapter;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class ViewJobSearchWithPreferences extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewApplicationAdapter viewApplicationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perferences_filling);

        init();
        connectToFBDB();
    }

    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void connectToFBDB() {

        DAO.getJobReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> jobList = new ArrayList<>();

                for (DataSnapshot job: snapshot.getChildren()) {

                    if (Objects.requireNonNull(job.child("applicantsID").getValue()).toString().contains(SessionManager.getUserID())) {
                        jobList.add(job.getKey());
                    }
                }

                viewApplicationAdapter = new ViewApplicationAdapter(jobList, false);
                recyclerView.setAdapter(viewApplicationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewJobSearchWithPreferences.this, "Error reading applications", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkWithPreference(DataSnapshot job){
        boolean[] fieldContent = {true, true, true, true, true};
        boolean allField = true;

        if(jobMatchWithPreference(job)){
            fieldContent[0] = false;
            allField = false;
        }
        return allField;
    }

    private boolean jobMatchWithPreference(DataSnapshot job){
        String jobTitle = job.child("title").getValue().toString();
        String jobPrefer = findViewById(R.id.preferencesFilling).toString();

        return jobTitle.contains(jobPrefer) && validJob(jobPrefer);
    }

    private boolean salaryMatchWithPreference(DataSnapshot job){
        double jobSalary;
        double salaryPrefer;

        try{
            jobSalary = Double.parseDouble(job.child("salary").getValue().toString());
        }
        catch (Exception e){
            jobSalary = 0.00;
        }

        try{
            salaryPrefer = Double.parseDouble(findViewById(R.id.salaryMinimal).toString());
        }
        catch (Exception e){
            salaryPrefer = 0.00;
        }

        return jobSalary >= salaryPrefer && validSalary(salaryPrefer);
    }

    private boolean startingHourMatchWithPreferences(DataSnapshot job){
        String jobStartingHour = job.child("hour").getValue().toString() + ":" + job.child("minute").getValue().toString();
        String startingHourPrefer = findViewById(R.id.startingHour).toString();
        String[] startingHourPreferArray = startingHourPrefer.split(":");
        String[] jobstartingHourArray = jobStartingHour.split(":");

        double jobStartHour;
        double jobStartMinute;
        double startHourPrefer;
        double startMinutePrefer;

        try{
            jobStartHour = Double.parseDouble(startingHourPreferArray[0]);
        }
        catch (Exception e){
            jobStartHour = 0.00;
        }

        try{
            jobStartMinute += String.valueOf(Double.parseDouble(startingHourPreferArray[1]));
        }
        catch (Exception e){
            startingHourPrefer = startingHourPreferArray[1];
        }

        try{
            startingHourPrefer = String.valueOf(Double.parseDouble(startingHourPreferArray[0]));
        }
        catch (Exception e){
            startingHourPrefer = startingHourPreferArray[0];
        }

        startingHourPrefer += ":";

        try{
            startingHourPrefer += String.valueOf(Double.parseDouble(startingHourPreferArray[1]));
        }
        catch (Exception e){
            startingHourPrefer = startingHourPreferArray[1];
        }

        return


    }

    public boolean validJob(String job) {
        return job.length() <= 250;
    }

    public boolean validSalary(double salary) {
        //Minimum NS wage 13.35$ per hour
        return salary >= 13.35 && salary <= 10000;
    }

    public boolean validStartingTime(String time) {
        return !time.isEmpty() && time.matches("^([0-9]|1[0-9]|2[0-3]):([0-9]|[1-5][0-9])$");
    }

    public boolean validMaxDistance(int distance) {
        return distance > 0 && distance < 1000;
    }

    public boolean validDuration(int duration) {
        return duration > 0 && duration <= 168;
    }

}
