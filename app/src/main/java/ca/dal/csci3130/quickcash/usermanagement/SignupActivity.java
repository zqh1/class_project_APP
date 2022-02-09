package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
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

    private Signup signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Instantiate signup class that contains implementation
        signup = new Signup(this);

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
        signInBtn.setOnClickListener(view -> signInBtnAction());
    }

    private void signUpBtnAction() {

        disableSignUpButton();
        signup.setUser(createUserFromData());
        verifyDataFields();
    }

    private void signInBtnAction() {
        changeScreenToLogin();
    }

    public void disableSignUpButton() {
        signUpBtn.setEnabled(false);
    }

    public void enableSignUpButton() {
        signUpBtn.setEnabled(true);
    }

    public void changeScreenToLogin() {
        final Intent changeSignIn = new Intent(this, LoginActivity.class);
        startActivity(changeSignIn);
    }

    private void verifyDataFields() {
        boolean[] fieldsStatus = signup.verifyUserData();

        if (fieldsStatus[0]) firstNameField.setTextColor(getResources().getColor(R.color.grey, null));
        else firstNameField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[1]) lastNameField.setTextColor(getResources().getColor(R.color.grey, null));
        else lastNameField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[2]) emailField.setTextColor(getResources().getColor(R.color.grey, null));
        else emailField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[3]) passwordField.setTextColor(getResources().getColor(R.color.grey, null));
        else passwordField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[4]) confirmPasswordField.setTextColor(getResources().getColor(R.color.grey, null));
        else confirmPasswordField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[5]) phoneField.setTextColor(getResources().getColor(R.color.grey, null));
        else phoneField.setTextColor(getResources().getColor(R.color.red, null));
    }

    public void errorVerifyEmail() {
        Toast.makeText(SignupActivity.this, "Unable to verify email", Toast.LENGTH_LONG).show();
        enableSignUpButton();
    }

    public void emailUsedSetScreen() {
        emailField.setTextColor(getResources().getColor(R.color.red, null));
        Toast.makeText(SignupActivity.this, "Email already in use", Toast.LENGTH_LONG).show();
        enableSignUpButton();
    }

    public void throwUserCreatedToast() {
        Toast.makeText(this, "User successfully created", Toast.LENGTH_LONG).show();
    }

    private UserInterface createUserFromData() {

        User user = new User();
        user.setFirstName(firstNameField.getText().toString().trim());
        user.setLastName(lastNameField.getText().toString().trim());
        user.setEmail(emailField.getText().toString().trim());
        user.setPassword(passwordField.getText().toString().trim());
        user.setConfirmPassword(confirmPasswordField.getText().toString().trim());
        user.setPhone(phoneField.getText().toString().trim());
        if (userTypeSpinner.getSelectedItem().toString().equals("Employee")) user.setIsEmployee("y");
        else user.setIsEmployee("n");

        return user;
    }
}