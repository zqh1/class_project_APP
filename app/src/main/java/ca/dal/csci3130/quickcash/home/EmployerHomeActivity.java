package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.JobActivity;
import ca.dal.csci3130.quickcash.joblisting.ViewJobActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

/**
 * EmployerHomeActivity, this class will launch activity once Employer user Log In .
 */
public class EmployerHomeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_employer_home);

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
        findViewById(R.id.createJobBtn).setOnClickListener(view -> redirectJobCreation());
        findViewById(R.id.postingsBtn).setOnClickListener(view -> viewApplicants());
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

    private void redirectJobCreation() {
        startActivity(new Intent(this, JobActivity.class));
    }

    private void viewApplicants() {
        startActivity(new Intent(this, ViewJobActivity.class));
    }
}