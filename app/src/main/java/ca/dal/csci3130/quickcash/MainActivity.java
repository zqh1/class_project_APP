package ca.dal.csci3130.quickcash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

/**
 * Main activity of the program, it only call the session manager to start application logic
 */
public class MainActivity extends AppCompatActivity {

    /**
     * OnCreate method that initialize the activity. It only call the session manager to check
     * for a valid account
     *
     * @param savedInstanceState: Activities states, required for activity creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManager.getInstance(this).checkLogin();
    }
}
