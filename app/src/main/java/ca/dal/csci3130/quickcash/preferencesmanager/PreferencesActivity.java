package ca.dal.csci3130.quickcash.preferencesmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.joblisting.ViewJobSearchWithPreferences;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobInterface;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class PreferencesActivity extends AppCompatActivity {

    private EditText extractedJob;
    private EditText extractedSalary;
    private EditText extractedStartingTime;
    private EditText extractedMaxDistance;
    private EditText extractedDuration;

    private PreferencesVerification verification;

    private Button cancelButton;
    private Button proceedButton;
    private Button loadButton;

    private boolean search;
    private static final Integer MAP_REQUEST_CODE = 4254;
    private LatLng latlng;
    private PreferencesInterface preference;

    /**
     * OnCreate method, Initialize activity call, and determine whether this activity will be use
     * for search or set preference by using Extra. Also call methods to get and set reference
     * if search is true will call getCurrentLocation() method, and change title of activity to
     * search
     *
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_filling);

        search = getIntent().getBooleanExtra("SEARCH", false);

        getScreenReferences();
        setButtonClickListeners();

        verification = new PreferencesVerification();

        if (search) {
            getCurrentLocation();
            String title = "Search Parameters";
            ((TextView) findViewById(R.id.preferencesFillingTitle)).setText(title);
            findViewById(R.id.distanceDefaultLabel).setVisibility(View.VISIBLE);
        }
    }

    //get References from UI activity, assign them to variable
    private void getScreenReferences() {
        extractedJob = findViewById(R.id.preferencesFilling);
        extractedSalary = findViewById(R.id.salaryMinimal);
        extractedStartingTime = findViewById(R.id.startingHour);
        extractedMaxDistance = findViewById(R.id.maxDistanceFill);
        extractedDuration = findViewById(R.id.durationFill);

        proceedButton = findViewById(R.id.proceedPreferBtn);
        cancelButton = findViewById(R.id.cancelPreferBtn);
        loadButton = findViewById(R.id.loadButton);
    }

    //set each method will be assign to each button once pressed
    private void setButtonClickListeners() {
        proceedButton.setOnClickListener(view -> proceedButtonListener());
        cancelButton.setOnClickListener(view -> changeScreenToHome());
        loadButton.setOnClickListener(view -> loadPreferences());
    }

    //Redirect user to mainactity (employee homepage)
    private void changeScreenToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Once the button is pressed, the button will first be disable.
     * If this activity is not for search,
     * then all this will do is pass it to check input and verify
     * Else,
     * Will ask permission from user, then call getCurrentLocation(), get information from UI, and
     * lastly call retrieveJobs()
     */
    private void proceedButtonListener() {
        proceedButton.setEnabled(false);

        if (!search) {
            verification.setPreferences(readRetrievedData());
            verify();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_REQUEST_CODE);
            } else {
                getCurrentLocation();
                preference = readRetrievedData();
                retrieveJobs();
            }
        }
    }

    /**
     * This method will be call once the load button is pressed,
     * It will check if employeeID exist in database,
     * If ID exist in DB then it will pull information and display on UI activity
     * Else Toast message will appear.
     */
    private void loadPreferences() {

        DAO.getPreferenceReference().orderByChild("employeeID").equalTo(SessionManager.getUserID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.getChildrenCount() == 1) {
                            for (DataSnapshot data : snapshot.getChildren()) {

                                PreferencesInterface preferences = data.getValue(Preferences.class);

                                if (preferences == null) {
                                    Toast.makeText(PreferencesActivity.this, "Unable to read preferences", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String jobName = preferences.getJob();
                                String salary = preferences.getSalary() + "";
                                String time = preferences.getStartingTime();
                                String distance = preferences.getMaxDistance() + "";
                                String duration = preferences.getDuration() + "";

                                extractedJob.setText(jobName);
                                extractedSalary.setText(salary);
                                extractedStartingTime.setText(time);
                                extractedMaxDistance.setText(distance);
                                extractedDuration.setText(duration);
                            }
                        } else {
                            Toast.makeText(PreferencesActivity.this, "No preferences available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PreferencesActivity.this, "Unable to read preferences", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * This method will read data from activity UI, and return as PreferencesInterface object
     * During reading it will check whether data can be parse to int or double
     * if not then the application will automatically apply data
     *
     * @return PreferencesInterface object
     */
    private PreferencesInterface readRetrievedData() {
        PreferencesInterface preferences = new Preferences();

        preferences.setEmployeeID(SessionManager.getUserID());
        preferences.setJob(getJob());
        String startingTimeString = getStartingTime();
        String[] extractStartingTimeArray = startingTimeString.split(":");

        try {
            preferences.setStartingHour(Integer.parseInt(extractStartingTimeArray[0]));
        } catch (NumberFormatException e) {
            preferences.setStartingHour(0);
        }

        try{
            preferences.setStartingMinute(Integer.parseInt(extractStartingTimeArray[0]));
        }
        catch (Exception e){
            preferences.setStartingMinute(0);
        }

        try {
            preferences.setSalary(Double.parseDouble(getSalary()));
        } catch (NumberFormatException e) {
            preferences.setSalary(13.35);
        }

        try {
            preferences.setDuration(Integer.parseInt(getDuration()));
        } catch (NumberFormatException e) {
            preferences.setDuration(99);
        }

        try {
            preferences.setMaxDistance(Integer.parseInt(getMaxDistance()));
        } catch (NumberFormatException e) {
            preferences.setMaxDistance(15);
        }

        return preferences;
    }

    /**
     * This method will interact with fields in UI activity, for each false in the array
     * it will turn information of the text in the field into red color indicate the location
     * Once verification is done, it will change to homepage
     */
    private void verify() {
        boolean[] fieldsStatus = verification.verifyFields();
        if (fieldsStatus[0]) extractedJob.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedJob.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[1])
            extractedSalary.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedSalary.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[2])
            extractedStartingTime.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedStartingTime.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[3])
            extractedMaxDistance.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedMaxDistance.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[4])
            extractedDuration.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedDuration.setTextColor(getResources().getColor(R.color.red, null));

        for (boolean status : fieldsStatus) {
            if (!status) {
                proceedButton.setEnabled(true);
                Toast.makeText(this, "Incorrect Information", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Toast.makeText(this, "Preferences has been added to database", Toast.LENGTH_LONG).show();
        changeScreenToHome();
    }

    /**
     * This method get title from UI activity from local variable and turn it into String
     *
     * @return title as a String
     */
    private String getJob() {
        return extractedJob.getText().toString().trim();
    }

    /**
     * This method get salary from UI activity from local variable and turn it into String
     *
     * @return salary as a String
     */
    private String getSalary() {
        return extractedSalary.getText().toString().trim();
    }

    /**
     * This method get starting time from UI activity from local variable and turn it into String
     *
     * @return starting time as a String
     */
    private String getStartingTime() {
        return extractedStartingTime.getText().toString().trim();
    }

    /**
     * This method get max distance from UI activity from local variable and turn it into String
     *
     * @return max distance as a String
     */
    private String getMaxDistance() {
        return extractedMaxDistance.getText().toString().trim();
    }

    /**
     * This method get max distance from UI activity from local variable and turn it into String
     *
     * @return max distance as a String
     */
    private String getDuration() {
        return extractedDuration.getText().toString().trim();
    }

    /**
     * If user is in search mode, this method will be call.
     * it will query though DB get all the job and call checkWithPreference(thisJob) to check
     * if the job should be show to user depends on the search preferences.
     * If it pass it will be add to Arraylist.
     * If the arraylist is empty(no job to show) then toast message will appear.
     * Else start new activity to show jobs on the phone
     */
    private void retrieveJobs() {
        DAO.getJobReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> jobList = new ArrayList<>();

                for (DataSnapshot job : snapshot.getChildren()) {

                    JobInterface thisJob = job.getValue(Job.class);

                    if (checkWithPreference(thisJob)) {
                        jobList.add(job.getKey());
                    }
                }

                proceedButton.setEnabled(true);

                if (jobList.isEmpty()) {
                    Toast.makeText(PreferencesActivity.this, "No job fit the search parameters", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(PreferencesActivity.this, ViewJobSearchWithPreferences.class).putStringArrayListExtra("JOBLIST", jobList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PreferencesActivity.this, "Error reading jobs", Toast.LENGTH_SHORT).show();
                proceedButton.setEnabled(true);
            }
        });
    }

    //Main method to call each verification method to check with each element of JobInterface
    private boolean checkWithPreference(JobInterface job) {
        boolean allField = true;

        if (!jobMatchWithPreference(job)) {
            allField = false;
        }
        if (!salaryMatchWithPreference(job)) {
            allField = false;
        }
        if (!startingHourMatchWithPreferences(job)) {
            allField = false;
        }
        if (!durationMatchWithPreference(job)) {
            allField = false;
        }
        if (!distanceMatchWithPreference(job)) {
            allField = false;
        }

        return allField;
    }

    /**
     * This method will check if the job user enter is in title of job from DB,
     * and check if is valid.
     * If the job title from DB contain job that user want and job that user want is valid,
     * return true.
     * Else return false
     *
     * @param job: JobInterface to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean jobMatchWithPreference(JobInterface job) {
        String jobTitle = job.getTitle();
        String jobPrefer = preference.getJob();

        return jobTitle.contains(jobPrefer) && validJob(jobPrefer);
    }

    /**
     * This method will check between salary user want and job's salary from DB,
     * and check if is valid.
     * If the job salary from DB equal or higher than salary that user want
     * and salary that user want is valid, return true.
     * Else return false
     *
     * @param job: JobInterface to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean salaryMatchWithPreference(JobInterface job) {
        double jobSalary = job.getSalary();
        double salaryPrefer = preference.getSalary();

        return jobSalary >= salaryPrefer && validSalary(salaryPrefer);
    }

    /**
     * This method will check between startingHour user want and job's startingHour from DB,
     * and check if is valid.
     * If the job startingHour from DB is after startingHour that user's want and
     * startingHour that user want is valid, return true.
     * Else return false
     *
     * @param job: JobInterface to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean startingHourMatchWithPreferences(JobInterface job) {
        int jobStartHour = job.getHour();
        int jobStartMinute = job.getMinute();
        int startHourPrefer = preference.getStartingHour();
        int startMinutePrefer = preference.getStartingMinute();

        String preferHourToCheck = startHourPrefer + ":" + startMinutePrefer;

        boolean validPreferHourAndMinute;

        if (startHourPrefer < jobStartHour) validPreferHourAndMinute = true;
        else
            validPreferHourAndMinute = (startHourPrefer == jobStartHour && startMinutePrefer <= jobStartMinute);

        return validStartingTime(preferHourToCheck) && validPreferHourAndMinute;
    }

    /**
     * This method will check between duration user want and job's duration from DB,
     * and check if is valid.
     * If the job duration from DB contain lower than what user's want and
     * duration that user want is valid, return true.
     * Else return false
     *
     * @param job: JobInterface to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean durationMatchWithPreference(JobInterface job) {
        int jobDuration = job.getDuration();
        int durationPrefer = preference.getDuration();

        return jobDuration <= durationPrefer && validDuration(durationPrefer);
    }

    /**
     * This method will check between user's location and job's location and compare to max distance
     * and check if is valid.
     * If the distance between user and job is lower than what user wants, then return true.
     * Else return false
     * NOTE: user enter as KM but the Location.distanceBetween() will return meters.
     *
     * @param job: JobInterface to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean distanceMatchWithPreference(JobInterface job) {
        double jobLatitude = job.getLatitude();
        double jobLongitude = job.getLongitude();
        int maxDistancePrefer = preference.getMaxDistance(); //In meters

        float[] distanceToJob = new float[1];
        Location.distanceBetween(jobLatitude, jobLongitude, latlng.latitude, latlng.longitude, distanceToJob);

        return maxDistancePrefer * 1000 >= distanceToJob[0] && validMaxDistance(maxDistancePrefer);
    }

    /**
     * This method will ask for permission for location.
     * once the permission is allowed, it will assign location to latLng latlng variable
     */
    private void getCurrentLocation() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_REQUEST_CODE);
        }

        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if (location != null) {
                latlng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
    }

    /**
     * If string to check has number of letters lower than 251 return true.
     *
     * @param job: job title to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean validJob(String job) {
        return job.length() <= 250;
    }

    /**
     * If the salary passed to check is higher than minimal wage, and lower than $10000
     *
     * @param salary: salary to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean validSalary(double salary) {
        //Minimum NS wage 13.35$ per hour
        return salary >= 13.35 && salary <= 10000;
    }

    /**
     * If the Starting time for the job is not empty and is match in form of XX:XX
     * NOTE: 24:00 does not valid since 24:00 is 00:00 of next day
     *
     * @param time: time String to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean validStartingTime(String time) {
        return !time.isEmpty() && time.matches("^([0-9]|1[0-9]|2[0-3]):([0-9]|[1-5][0-9])$");
    }

    /**
     * distance has to be int, and greater than 0 but less than 1001
     *
     * @param distance: distance to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean validMaxDistance(int distance) {
        return distance > 0 && distance <= 1000;
    }

    /**
     * duration to be check. Has to be high than 0 but less than 100
     *
     * @param duration: duration as int to check
     * @return true if it fits qualification, false otherwise
     */
    private boolean validDuration(int duration) {
        return duration > 0 && duration <= 99;
    }
}
