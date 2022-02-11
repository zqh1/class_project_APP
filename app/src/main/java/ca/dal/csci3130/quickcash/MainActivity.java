package ca.dal.csci3130.quickcash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new SessionManager(this).logoutUser();

        new SessionManager(this).checkLogin();

        //findViewById(R.id.logoutBtn).setOnClickListener(view -> logoutUser());
    }

    /*
    private void logoutUser() {
        new SessionManager(this).logoutUser();
        startActivity(new Intent(this, LoginActivity.class));
    }
    */
}
