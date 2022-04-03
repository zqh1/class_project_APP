package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.applicationslisting.ViewApplicationActivity;
import ca.dal.csci3130.quickcash.joblisting.ViewJobActivity;
import ca.dal.csci3130.quickcash.joblisting.ViewJobsMapActivity;
import ca.dal.csci3130.quickcash.preferencesmanager.PreferencesActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

/**
 * EmployeeHomeActivity, this class will launch activity once Employee user Log In .
 */
public class EmployeeHomeActivity extends AppCompatActivity {

    /**
     * OnCreate method, Initialize activity get userInterface from sessionManager.
     * Then set textView to user first name and last name to show on screen.
     * Also apply algorithm for logging out user once log out user button pressed.
     *
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        FirebaseMessaging.getInstance().subscribeToTopic("Employees");

        //Load current user, if user not loaded correctly, restart application from main
        UserInterface user = SessionManager.getUser();
        if (user == null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        //Load name of user and set buttons listeners
        String nameOfUser = user.getFirstName() + " " + user.getLastName();
        ((TextView) findViewById(R.id.NameLabel)).setText(nameOfUser);

        setButtonListeners();
    }

    private void setButtonListeners() {
        findViewById(R.id.logoutBtn).setOnClickListener(view -> logoutUser());
        findViewById(R.id.preferenceButton).setOnClickListener(view -> gotoSetPreferencePage());
        findViewById(R.id.viewJobsBtn).setOnClickListener(view -> redirectViewJobs());
        findViewById(R.id.applicationsBtn).setOnClickListener(view -> viewJobApplications());
        findViewById(R.id.searchJobButton).setOnClickListener(view -> searchJobApplications());
        findViewById(R.id.viewJobsMapBtn).setOnClickListener(view -> redirectViewJobsMap());
    }

    /**
     * OnBackPressed method, This method will block user from pressing back button,
     * Avoid the user to access homepage once they already logout
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "[Back Button] is disabled", Toast.LENGTH_SHORT).show();
    }

    private void logoutUser() {
        SessionManager.getInstance(this).logoutUser();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void redirectViewJobs() {
        startActivity(new Intent(this, ViewJobActivity.class));
    }

    private void gotoSetPreferencePage() {
        startActivity(new Intent(this, PreferencesActivity.class).putExtra("SEARCH", false));
    }
    private void viewJobApplications() {
        startActivity(new Intent(this, ViewApplicationActivity.class));
    }
    private void redirectViewJobsMap() {
        startActivity(new Intent(this, ViewJobsMapActivity.class));}

    private void searchJobApplications() {
        startActivity(new Intent(this, PreferencesActivity.class).putExtra("SEARCH", true));
    }
}