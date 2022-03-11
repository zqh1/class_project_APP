package ca.dal.csci3130.quickcash.joblisting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.applicationslisting.ViewApplicationAdapter;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.jobmanagement.JobInterface;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.preferencesmanager.Preferences;
import ca.dal.csci3130.quickcash.preferencesmanager.PreferencesInterface;

public class ViewJobSearchWithPreferences extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewApplicationAdapter viewApplicationAdapter;
    private static final Integer MAP_REQUEST_CODE = 4254;
    private LatLng latlng;
    PreferencesInterface preference;
    Button cancelButton;
    Button loadButton;
    Button proceedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perferences_filling);

        getCurrentLocation();

        init();
        setButtonListener();
    }

    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        proceedButton = findViewById(R.id.proceedPreferBtn);
        cancelButton = findViewById(R.id.cancelPreferBtn);
        loadButton = findViewById(R.id.loadButton);
    }

    private void setButtonListener(){
        proceedButton.setOnClickListener(view -> connectToFBDB());
        cancelButton.setOnClickListener(view -> gotoHomepage());
        loadButton.setOnClickListener(view -> loadPreferences());
    }

    private void gotoHomepage(){
        startActivity(new Intent(this, EmployeeHomeActivity.class));
    }

    private void loadPreferences(){

    }

    private void connectToFBDB() {
        this.preference = readPreference();

        DAO.getJobReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> jobList = new ArrayList<>();

                for (DataSnapshot job: snapshot.getChildren()) {
                    JobInterface thisJob = job.getValue(Job.class);
                    if (checkWithPreference(thisJob)) {
                        jobList.add(job.getKey());
                    }
                }

                viewApplicationAdapter = new ViewApplicationAdapter(jobList, false);
                recyclerView.setAdapter(viewApplicationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewJobSearchWithPreferences.this, "Error reading jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkWithPreference(JobInterface job){
        boolean allField = true;

        if(!jobMatchWithPreference(job)){ allField = false; }
        if(!salaryMatchWithPreference(job)){ allField = false; }
        if(!startingHourMatchWithPreferences(job)){ allField = false; }
        if(!durationMatchWithPreference(job)){ allField = false; }
        if(!distanceMatchWithPreference(job)){ allField = false; }

        return allField;
    }

    private boolean jobMatchWithPreference(JobInterface job){
        String jobTitle = job.getTitle();
        String jobPrefer = preference.getJob();

        return jobTitle.contains(jobPrefer) && validJob(jobPrefer);
    }

    private boolean salaryMatchWithPreference(JobInterface job){
        double jobSalary = job.getSalary();
        double salaryPrefer = preference.getSalary();

        return jobSalary >= salaryPrefer && validSalary(salaryPrefer);
    }

    private boolean startingHourMatchWithPreferences(JobInterface job){
        double jobStartHour = job.getHour();
        double jobStartMinute = job.getMinute();
        double startHourPrefer = preference.getStartingHour();
        double startMinutePrefer = preference.getStartingMinute();

        String preferHourToCheck = startHourPrefer + ":" + startMinutePrefer;
        boolean validPreferHourAndMinute = startHourPrefer >= jobStartHour && startMinutePrefer >= jobStartMinute;

        return validStartingTime(preferHourToCheck) && validPreferHourAndMinute;
    }

    private boolean durationMatchWithPreference(JobInterface job){
        int jobDuration = job.getDuration();
        int durationPrefer = preference.getDuration();

        return jobDuration >= durationPrefer && validDuration(durationPrefer);
    }

    private boolean distanceMatchWithPreference(JobInterface job){
        double jobLatitude = job.getLatitude();
        double jobLongitude = job.getLongitude();
        int maxDistancePrefer = preference.getMaxDistance();

        float[] distanceToJob = new float[1];
        Location.distanceBetween(jobLatitude, jobLongitude, latlng.latitude, latlng.longitude, distanceToJob);

        return maxDistancePrefer*1000 <= distanceToJob[0] && validMaxDistance(maxDistancePrefer);
    }

    private void getCurrentLocation(){
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        /*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MAP_REQUEST_CODE);
        }
        else getCurrentLocation();


        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if(location != null) { latlng = new LatLng(location.getLatitude(),location.getLongitude()); }
        });
         */
    }

    private boolean validJob(String job) {
        return job.length() <= 250;
    }

    private boolean validSalary(double salary) {
        //Minimum NS wage 13.35$ per hour
        return salary >= 13.35 && salary <= 10000;
    }

    private boolean validStartingTime(String time) {
        return !time.isEmpty() && time.matches("^([0-9]|1[0-9]|2[0-3]):([0-9]|[1-5][0-9])$");
    }

    private boolean validMaxDistance(int distance) {
        return distance > 0 && distance < 1000;
    }

    private boolean validDuration(int duration) {
        return duration > 0 && duration <= 168;
    }

    private PreferencesInterface readPreference(){
        PreferencesInterface preference = new Preferences();
        String[] startingHourArray = findViewById(R.id.startingHour).toString().split(":");

        preference.setJob(findViewById(R.id.preferencesFilling).toString());
        try {
            preference.setSalary(Double.parseDouble(findViewById(R.id.salaryMinimal).toString()));
        }
        catch (Exception e){
            preference.setSalary(0);
        }
        try{
            preference.setMaxDistance(Integer.parseInt(findViewById(R.id.maxDistanceFill).toString()));
        }
        catch (Exception e){
            preference.setMaxDistance(1000);
        }
        try{
            preference.setDuration(Integer.parseInt(findViewById(R.id.durationFill).toString()));
        }
        catch (Exception e){
            preference.setDuration(168);
        }
        try{
            preference.setStartingHour(Integer.parseInt(startingHourArray[0]));
        }
        catch (Exception e){
            preference.setStartingHour(0);
        }
        try{
            preference.setStartingMinute(Integer.parseInt(startingHourArray[1]));
        }
        catch (Exception e){
            preference.setStartingMinute(0);
        }

        return preference;
    }

}
