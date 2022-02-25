package ca.dal.csci3130.quickcash.jobmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    static final Integer MAP_REQUEST_CODE = 4254;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        startMap();

        linkScreenItems();
        setButtonsListeners();

        calendar = Calendar.getInstance();
    }

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
    }

    private void setButtonsListeners() {

        dateBtn.setOnClickListener(view -> dateButtonListener());
        timeBtn.setOnClickListener(view -> timeButtonListener());
        cancelBtn.setOnClickListener(view -> cancelButtonListener());
        postBtn.setOnClickListener(view -> postButtonListener());
        mapBtn.setOnClickListener(view -> mapButtonListener());
    }

    private void dateButtonListener() {

        new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void timeButtonListener() {

        new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void cancelButtonListener() {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    private void postButtonListener() {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        String date = day + "/" + (month + 1) + "/" + year;
        dateLabel.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String time = hourOfDay + ":" + minute;
        timeLabel.setText(time);
    }

    private void startMap() {

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MAP_REQUEST_CODE);
        }
        else getCurrentLocation();
    }

    private void mapButtonListener() {
        getCurrentLocation();
    }

    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if(location != null) {
                supportMapFragment.getMapAsync(googleMap -> {
                    LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("You are here");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,14));
                    Objects.requireNonNull(googleMap.addMarker(markerOptions)).showInfoWindow();
                });
            }

        });
    }
}