package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.JobActivity;
import ca.dal.csci3130.quickcash.search.ViewJobActivity;
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
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        UserInterface user = SessionManager.getUser();
        String nameOfUser = user.getFirstName() + " " + user.getLastName();
        ((TextView)findViewById(R.id.NameLabel)).setText(nameOfUser);

        findViewById(R.id.logoutBtn).setOnClickListener(view -> logoutUser());

        findViewById(R.id.viewJobsBtn).setOnClickListener(view -> redirectViewJobs());
    }

    /**
     * OnBackPressed method, This method will block user from pressing back button, trying to access homepage(When user logout)
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "[Back Button] is disabled", Toast.LENGTH_SHORT).show();
    }

    /**
     * logoutUser method, This method will be call once user pressed logout. Will call logoutUser from sessionManager
     * And send user back to login activity.
     **/
    private void logoutUser() {
        new SessionManager(this).logoutUser();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void redirectViewJobs(){
        startActivity(new Intent(this, ViewJobActivity.class));
    }
}