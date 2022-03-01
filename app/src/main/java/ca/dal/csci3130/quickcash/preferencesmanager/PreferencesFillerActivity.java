package ca.dal.csci3130.quickcash.preferencesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SignupActivity;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;

public class PreferencesFillerActivity extends AppCompatActivity{

    private String extractedJob;
    private String extractedSalary;
    private String extractedStartingTime;
    private String getExtractedMaxDistance;
    private String extractedDuration;

    private Button cancelButton;
    private Button proceedButton;

    /**
     * OnCreate method, Initialize activity get userInterface from sessionManager.
     * Then set textView to user first name and last name to show on screen.
     * Also apply algorithm for logging out user once log out user button pressed.
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        getButtonReferences();
        setButtonClickListeners();
    }

    private void getButtonReferences() {
        proceedButton = findViewById(R.id.proceedPreferBtn);
        cancelButton = findViewById(R.id.canclePreferBtn);
    }

    private void setButtonClickListeners() {
        proceedButton.setOnClickListener(view -> RetrivedData());
        cancelButton.setOnClickListener(view -> changeScreenToHome());
    }

    private void changeScreenToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void RetrivedData(){
        extractedJob = getJob();
        extractedSalary = getSalary();
        extractedStartingTime = getStartingTime();
        getExtractedMaxDistance = getMaxDistance();
        extractedDuration = getDuration();

        pushDataToFirebase();
    }

    private void pushDataToFirebase(){
        // have not implement yet
    }

    protected String getJob() {
        EditText job = findViewById(R.id.preferencesFilling);
        return job.getText().toString().trim();
    }

    protected String getSalary(){
        EditText salary = findViewById(R.id.salaryMinimal);
        return salary.getText().toString().trim();
    }

    protected String getStartingTime(){
        EditText time = findViewById(R.id.startingHour);
        return time.getText().toString().trim();
    }

    protected String getMaxDistance(){
        EditText distance = findViewById(R.id.maxDistanceFill);
        return distance.getText().toString().trim();
    }

    protected String getDuration(){
        EditText length = findViewById(R.id.durationFill);
        return length.getText().toString().trim();
    }
}
