package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class EmployerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);

        ((TextView)findViewById(R.id.NameLabel)).setText(SessionManager.getUser().getFirstName());

        findViewById(R.id.logoutBtn).setOnClickListener(view -> logoutUser());
    }

    private void logoutUser() {
        new SessionManager(this).logoutUser();
        startActivity(new Intent(this, LoginActivity.class));
    }
}