package ca.dal.csci3130.quickcash.preferencesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class PreferencesActivity extends AppCompatActivity{

    private EditText extractedJob;
    private EditText extractedSalary;
    private EditText extractedStartingTime;
    private EditText extractedMaxDistance;
    private EditText extractedDuration;

    private PreferencesVerification verification;

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
        setContentView(R.layout.activity_perferences_filling);

        getScreenReferences();
        setButtonClickListeners();

        verification = new PreferencesVerification();
    }

    private void getScreenReferences() {
        extractedJob = findViewById(R.id.preferencesFilling);
        extractedSalary = findViewById(R.id.salaryMinimal);
        extractedStartingTime = findViewById(R.id.startingHour);
        extractedMaxDistance = findViewById(R.id.maxDistanceFill);
        extractedDuration = findViewById(R.id.durationFill);

        proceedButton = findViewById(R.id.proceedPreferBtn);
        cancelButton = findViewById(R.id.canclePreferBtn);
    }

    private void setButtonClickListeners() {
        proceedButton.setOnClickListener(view -> proceedButtonListener());
        cancelButton.setOnClickListener(view -> changeScreenToHome());
    }

    private void changeScreenToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void proceedButtonListener(){
        proceedButton.setEnabled(false);
        verification.setPreferences(readRetrievedData());
        verify();
    }

    private PreferencesInterface readRetrievedData(){
        PreferencesInterface preferences = new Preferences();

        preferences.setEmployeeID(new SessionManager(this).getUserID());
        preferences.setJob(extractedJob.toString().trim());
        preferences.setStartingTime(extractedStartingTime.toString().trim());

        try{
            preferences.setSalary(Integer.parseInt(extractedSalary.toString().trim()));
        }
        catch (NumberFormatException e){
            preferences.setSalary(0);
        }

        try{
            preferences.setDuration(Integer.parseInt(extractedDuration.toString().trim()));
        }
        catch (NumberFormatException e){
            preferences.setDuration(168);
        }

        try{
            preferences.setMaxDistance(Integer.parseInt(extractedMaxDistance.toString().trim()));
        }
        catch (NumberFormatException e){
            preferences.setMaxDistance(1000);
        }

        return preferences;
    }

    private void verify(){
        boolean[] fieldsStatus = verification.verifyFields();
        if (fieldsStatus[0]) extractedJob.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedJob.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[1]) extractedSalary.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedSalary.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[2]) extractedStartingTime.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedStartingTime.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[3]) extractedMaxDistance.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedMaxDistance.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[4]) extractedDuration.setTextColor(getResources().getColor(R.color.grey, null));
        else extractedDuration.setTextColor(getResources().getColor(R.color.red, null));

        for (boolean status : fieldsStatus) {
            if (!status) {
                proceedButton.setEnabled(true);
                Toast.makeText(this, "Incorrect Information", Toast.LENGTH_LONG).show();
                return;
            }
        }
        changeScreenToHome();
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
