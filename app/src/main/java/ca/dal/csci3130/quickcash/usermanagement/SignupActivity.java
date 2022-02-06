package ca.dal.csci3130.quickcash.usermanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText phoneField;
    private Button signUpBtn;
    private Button signInBtn;
    private Spinner userTypeSpinner;

    private UserInterface user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Add logic for signup

        //Link screen items
        linkScreenItems();

        //Set buttons on click listeners
        setButtonsActions();
    }

    private void linkScreenItems() {

        firstNameField = findViewById(R.id.firstNameInput);
        lastNameField = findViewById(R.id.lastNameInput);
        emailField = findViewById(R.id.emailInput);
        passwordField = findViewById(R.id.passwordInput);
        confirmPasswordField = findViewById(R.id.confirmPasswordInput);
        phoneField = findViewById(R.id.phoneInput);
        signUpBtn = findViewById(R.id.signUpBtn);
        signInBtn = findViewById(R.id.signInBtn);
        userTypeSpinner = findViewById(R.id.userTypeInput);
    }

    private void setButtonsActions() {

        signUpBtn.setOnClickListener(view -> signUpBtnAction());
    }

    private void signUpBtnAction() {

        readUserData();
    }

    private void readUserData() {

        user = new User();
        user.setFirstName(firstNameField.getText().toString().trim());
        user.setLastName(lastNameField.getText().toString().trim());
        user.setEmail(emailField.getText().toString().trim());
        user.setPassword(passwordField.getText().toString().trim());
        user.setConfirmPassword(confirmPasswordField.getText().toString().trim());
        user.setPhone(phoneField.getText().toString().trim());
        if (userTypeSpinner.getSelectedItem().toString().equals("Employee")) user.setIsEmployee("y");
        else user.setIsEmployee("n");
    }
}