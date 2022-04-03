package ca.dal.csci3130.quickcash.jobmanagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
import java.util.Objects;

import ca.dal.csci3130.quickcash.R;

/**
 * This class will is to set the map on create job page
 */
public class JobMap extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    static final Integer MAP_REQUEST_CODE = 4254;

    String jobName;
    LatLng jobLatLng;
    LatLng userLatLng;

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
        setContentView(R.layout.activity_job_map);

        loadJobInfo();
        startMap();
        supportMapFragment.getMapAsync(this::onMapReady);

        ((Button) findViewById(R.id.returnBtn)).setOnClickListener(view -> onBackPressed());
    }

    /**
     * loadJobInfo method. This method will get job latitude and longitude
     * Then set one field on the screen to the location of the job
     */
    private void loadJobInfo() {

        jobName = getIntent().getStringExtra("JOBNAME");
        jobLatLng = new LatLng(getIntent().getDoubleExtra("LATITUDE", 0), getIntent().getDoubleExtra("LONGITUDE", 0));

        String mapTitle = jobName + " Location:";
        ((TextView) findViewById(R.id.jobMapLabel)).setText(mapTitle);
    }

    /**
     * This method will ask permission from user to get location of user.
     * If agree: call getCurrentLocation, else ask for permission again
     */
    private void startMap() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_REQUEST_CODE);
        }

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * onMapReady method. This method will set up the google map in create job page
     * Then display it on the screen
     *
     * @param map: set google map
     */
    private void onMapReady(GoogleMap map) {

        setJobMarker(map);
        getCurrentLocation(map);
    }

    /**
     * After permission is allowed, it will show map with marker on current location of the user
     *
     * @param map : request to get google map
     */
    private void getCurrentLocation(GoogleMap map) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if (location != null) {
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("You are here");
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14));
                Objects.requireNonNull(map.addMarker(markerOptions)).showInfoWindow();

                setMapLabels();

                zoomPins(map);
            }
        });
    }

    /**
     * setJobMarker method. This method will add a pin market on the location where the user is
     *
     * @param map : request to get google map
     */
    private void setJobMarker(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(jobLatLng).title(jobName));
    }

    /**
     * setMapLabels method. This method will allow the application to compare user location and job location
     * Then it will set one of the field on screen to the distance between user and job
     */
    private void setMapLabels() {

        float[] distanceToJob = new float[1];
        Location.distanceBetween(userLatLng.latitude, userLatLng.longitude, jobLatLng.latitude, jobLatLng.longitude, distanceToJob);

        String jobDistance = "Distance: " + String.format(Locale.CANADA, "%.2f", distanceToJob[0] / 1000.0) + " KM";
        ((TextView) findViewById(R.id.distanceLocationLabel)).setText(jobDistance);
    }

    /**
     * zoomPins method. This method will make us see the animation of zooming map to user location
     *
     * @param map: request google map
     */
    private void zoomPins(GoogleMap map) {

        LatLngBounds.Builder zoomBuilder = new LatLngBounds.Builder();
        zoomBuilder.include(userLatLng);
        zoomBuilder.include(jobLatLng);

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(zoomBuilder.build(), 75));
    }
}
