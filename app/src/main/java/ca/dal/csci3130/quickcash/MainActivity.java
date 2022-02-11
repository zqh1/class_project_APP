package ca.dal.csci3130.quickcash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SessionManager(this).checkLogin();
    }
}
