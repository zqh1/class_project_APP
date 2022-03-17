package ca.dal.csci3130.quickcash.joblisting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.common.WrapLinearLayoutManager;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class ViewJobActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewJobAdapter viewJobAdapter;
    public GoogleMap mMap;
    ArrayList<Job> pins = new ArrayList<>();
    FusedLocationProviderClient client;
    static final Integer MAP_REQUEST_CODE = 4254;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        init();
        connectToFBDB();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recieveJobs();

    }

    private void connectToFBDB() {

        //Verify the user is valid, if user not loaded correctly, restart application from main
        if (SessionManager.getUser() == null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

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


    private void recieveJobs() {
        DAO.getJobReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot shot:snapshot.getChildren()){
                    pins.add(shot.getValue(Job.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if (location != null) {
                mapFragment.getMapAsync(googleMap -> {
                   LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("You are here");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
                    Objects.requireNonNull(googleMap.addMarker(markerOptions)).showInfoWindow();
                });
            }

        });
    }

    protected void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        for (Job j:pins) {
            LatLng jobPin = new LatLng(j.getLatitude(),j.getLongitude());
            mMap.addMarker(new MarkerOptions().position(jobPin).title(j.getTitle()));
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAP_REQUEST_CODE);
        }
        else getCurrentLocation();

    }



}

