package ca.dal.csci3130.quickcash.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class EmployeeHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        UserInterface user = SessionManager.getUser();
        String nameOfUser = user.getFirstName() + " " + user.getLastName();
        ((TextView)findViewById(R.id.NameLabel)).setText(nameOfUser);

        findViewById(R.id.logoutBtn).setOnClickListener(view -> logoutUser());

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "[Back Button] is disable", Toast.LENGTH_SHORT).show();
    }

    private void logoutUser() {
        new SessionManager(this).logoutUser();
        startActivity(new Intent(this, LoginActivity.class));
    }
}