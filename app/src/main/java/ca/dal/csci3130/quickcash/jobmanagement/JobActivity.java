package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Objects;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class JobActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView dateLabel;
    TextView timeLabel;

    EditText jobTitle;
    EditText jobDescription;
    EditText jobHours;
    EditText jobSalary;
    EditText jobTags;

    Button dateBtn;
    Button timeBtn;
    Button cancelBtn;
    Button postBtn;
    Button mapBtn;

    SwitchCompat urgentSwitch;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    static final Integer MAP_REQUEST_CODE = 4254;

    Calendar calendar;
    Calendar userCalendar;

    LatLng latlng;

    JobVerification verification;

    /**
     * OnCreate method, Initialize activity call multiple method
     * and apply instances to local variable
     * Methods: startMap(), linkScreenItem(), setButtonListeners()
     * Instance: Calendar, JobVerification()
     *
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        startMap();

        linkScreenItems();
        setButtonsListeners();

        calendar = Calendar.getInstance();
        userCalendar = Calendar.getInstance();
        verification = new JobVerification();
    }

    //Link all of the field from activity_job to local variable of this class
    private void linkScreenItems() {

        dateLabel = findViewById(R.id.dateLabel);
        timeLabel = findViewById(R.id.timeLabel);

        jobTitle = findViewById(R.id.titleInput);
        jobDescription = findViewById(R.id.descriptionInput);
        jobHours = findViewById(R.id.durationInput);
        jobSalary = findViewById(R.id.salaryInput);
        jobTags = findViewById(R.id.tagsInput);

        dateBtn = findViewById(R.id.dateBtn);
        timeBtn = findViewById(R.id.timeBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        postBtn = findViewById(R.id.postBtn);
        mapBtn = findViewById(R.id.mapBtn);

        urgentSwitch = findViewById(R.id.urgentInput);
    }

    //Link all of the buttons from activity_job to local variable of this class
    private void setButtonsListeners() {

        dateBtn.setOnClickListener(view -> dateButtonListener());
        timeBtn.setOnClickListener(view -> timeButtonListener());
        cancelBtn.setOnClickListener(view -> cancelButtonListener());
        postBtn.setOnClickListener(view -> postButtonListener());
        mapBtn.setOnClickListener(view -> mapButtonListener());
    }

    //Listener once the user pressed pick date button, and will call onDateSet()
    private void dateButtonListener() {
        new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //Listener once the user pressed pick time button
    private void timeButtonListener() {
        new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    //Change back to employer homepage
    private void cancelButtonListener() {
        redirectEmployerHome();
    }

    /**
     * Once post button is pressed it will be disable, then the application will check if
     * user input correct data and meet requirement. Then will call local method verifyFields()
     */
    private void postButtonListener() {

        postBtn.setEnabled(false);
        verification.setJob(readJobInformation());
        verifyFields();
    }

    /**
     * Once the user picked what date the job will be on Calendar, this method will be called
     * This method will assign date of the job to local variable(DateLabel) once the date picked
     * by user is valid.
     * For it to be valid: Can't pick date in past, and can only create a job 3 month forward
     *
     * @param view:  DatePicker
     * @param year:  year of the job
     * @param month: month of the job
     * @param day:   day of the job
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar userDate = Calendar.getInstance();
        userDate.set(year, month, day);

        Calendar future = Calendar.getInstance();
        future.add(Calendar.MONTH, 3);

        if (calendar.after(userDate)) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_LONG).show();

            userCalendar.set(currentYear, currentMonth, currentDay);
        } else if (future.before(userDate)) {
            Toast.makeText(this, "Limit is 3 months in the future", Toast.LENGTH_LONG).show();

            userCalendar.set(currentYear, currentMonth, currentDay);
        } else {
            Toast.makeText(this, "Date Set", Toast.LENGTH_SHORT).show();
            userCalendar.set(year, month, day);
        }

        String date = userCalendar.get(Calendar.DAY_OF_MONTH) + "/" + userCalendar.get(Calendar.MONTH) + "/" + userCalendar.get(Calendar.YEAR);
        dateLabel.setText(date);
    }

    /**
     * Once the user picked what time the job will be on clock, this method will be called
     * This method will assign time of the job to local variable(timeLabel) once the time picked
     * by user is valid.
     * For it to be valid: Can't pick date in past, time must be at least 1 hour ahead
     *
     * @param view:      TimePicker
     * @param hourOfDay: starting hour of the job
     * @param minute:    starting minut of the job
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar userDate = Calendar.getInstance();
        userDate.set(userCalendar.get(Calendar.YEAR), userCalendar.get(Calendar.MONTH),
                userCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

        Calendar future = Calendar.getInstance();
        future.add(Calendar.HOUR_OF_DAY, 1);

        if (future.after(userDate)) {
            Toast.makeText(this, "Time must be at least 1 hour away", Toast.LENGTH_LONG).show();

            userCalendar.set(Calendar.HOUR_OF_DAY, future.get(Calendar.HOUR_OF_DAY));
            userCalendar.set(Calendar.MINUTE, future.get(Calendar.MINUTE));
        } else {
            Toast.makeText(this, "Time Set", Toast.LENGTH_SHORT).show();

            userCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            userCalendar.set(Calendar.MINUTE, minute);
        }

        String time = userCalendar.get(Calendar.HOUR_OF_DAY) + ":" + userCalendar.get(Calendar.MINUTE);
        timeLabel.setText(time);
    }

    /**
     * This method will ask permission from user to get location of user.
     * If agree: call getCurrentLocation, else ask for permission again
     */
    private void startMap() {

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_REQUEST_CODE);
        } else getCurrentLocation();
    }

    //Listener once the user interact with map
    private void mapButtonListener() {
        getCurrentLocation();
    }

    /**
     * After permission is allowed, it will show map with marker on current location of the user
     */
    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if (location != null) {
                supportMapFragment.getMapAsync(googleMap -> {
                    latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("You are here");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
                    Objects.requireNonNull(googleMap.addMarker(markerOptions)).showInfoWindow();
                });
            }

        });
    }

    /**
     * Will read information from activity and apply to Job variable
     *
     * @return JobInterface variable that has information from interface
     */
    private JobInterface readJobInformation() {

        JobInterface job = new Job();

        job.setEmployerID(SessionManager.getUserID());
        job.setTitle(jobTitle.getText().toString().trim());
        job.setDescription(jobDescription.getText().toString().trim());
        job.setYear(userCalendar.get(Calendar.YEAR));
        job.setMonth(userCalendar.get(Calendar.MONTH));
        job.setDay(userCalendar.get(Calendar.DAY_OF_MONTH));
        job.setHour(userCalendar.get(Calendar.HOUR_OF_DAY));
        job.setMinute(userCalendar.get(Calendar.MINUTE));
        job.setLatitude(latlng.latitude);
        job.setLongitude(latlng.longitude);
        job.setUrgent(urgentSwitch.isChecked());
        job.setTags(jobTags.getText().toString().trim());

        try {
            job.setDuration(Integer.parseInt(jobHours.getText().toString()));
        } catch (NumberFormatException e) {
            job.setDuration(0);
        }

        try {
            job.setSalary(Integer.parseInt(jobSalary.getText().toString()));
        } catch (NumberFormatException e) {
            job.setSalary(0);
        }

        return job;
    }

    /**
     * This method will interact with fields in UI activity, for each false in the array
     * it will turn information of the text in the field into red color indicate the location
     * Once verification is done, it will change to homepage
     */
    private void verifyFields() {

        boolean[] fieldsStatus = verification.verifyFields();

        if (fieldsStatus[0]) jobTitle.setTextColor(getResources().getColor(R.color.grey, null));
        else jobTitle.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[1])
            jobDescription.setTextColor(getResources().getColor(R.color.grey, null));
        else jobDescription.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[2]) jobHours.setTextColor(getResources().getColor(R.color.grey, null));
        else jobHours.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[3]) jobSalary.setTextColor(getResources().getColor(R.color.grey, null));
        else jobSalary.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[4]) jobTags.setTextColor(getResources().getColor(R.color.grey, null));
        else jobTags.setTextColor(getResources().getColor(R.color.red, null));

        for (boolean status : fieldsStatus) {
            if (!status) {
                postBtn.setEnabled(true);
                Toast.makeText(this, "Incomplete information", Toast.LENGTH_LONG).show();
                return;
            }
        }

        redirectEmployerHome();
    }

    //Return user UI to employer homepage
    private void redirectEmployerHome() {
        startActivity(new Intent(this, EmployerHomeActivity.class));
    }
}
