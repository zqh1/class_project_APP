package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.dal.csci3130.quickcash.R;

/**
 * Sign up activity, this class focus on the UI of the sign up page, implementation and data
 * verification method come from Signup class
 */
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

    /**
     * OnCreate method, initialization of activity and the implementation. Also, link and set
     * buttons listeners
     *
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Instantiate signup class that contains implementation
        signup = new Signup(this);

        //Link screen items and set click listeners
        linkScreenItems();
        setButtonsActions();
    }

    //Private method to link all fields from screen into usable variables
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

    //Private method to set the click listeners of all the buttons on screen
    private void setButtonsActions() {

        signUpBtn.setOnClickListener(view -> signUpBtnAction());
        signInBtn.setOnClickListener(view -> signInBtnAction());
    }

    //Private method that contains the sign up functionality
    //Disable the button, retrieve user data and verify it
    private void signUpBtnAction() {

        disableSignUpButton();
        signup.setUser(createUserFromData());
        verifyDataFields();
    }

    //Private method that contains login button, change screen to login
    private void signInBtnAction() {
        changeScreenToLogin();
    }

    //Private method to disable sign up button
    private void disableSignUpButton() {
        signUpBtn.setEnabled(false);
    }

    /**
     * Method to enable the sign up button
     */
    protected void enableSignUpButton() {
        signUpBtn.setEnabled(true);
    }

    /**
     * Method that change the screen into the login screen (after logout)
     */
    protected void changeScreenToLogin() {
        final Intent changeSignIn = new Intent(this, LoginActivity.class);
        startActivity(changeSignIn);
    }

    //Private method that call for field verification and set the field color
    private void verifyDataFields() {
        boolean[] fieldsStatus = signup.verifyUserData();

        if (fieldsStatus[0])
            firstNameField.setTextColor(getResources().getColor(R.color.grey, null));
        else firstNameField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[1])
            lastNameField.setTextColor(getResources().getColor(R.color.grey, null));
        else lastNameField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[2]) emailField.setTextColor(getResources().getColor(R.color.grey, null));
        else emailField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[3])
            passwordField.setTextColor(getResources().getColor(R.color.grey, null));
        else passwordField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[4])
            confirmPasswordField.setTextColor(getResources().getColor(R.color.grey, null));
        else confirmPasswordField.setTextColor(getResources().getColor(R.color.red, null));

        if (fieldsStatus[5]) phoneField.setTextColor(getResources().getColor(R.color.grey, null));
        else phoneField.setTextColor(getResources().getColor(R.color.red, null));
    }

    /**
     * Method that set email error toast and enable the sign up button
     */
    protected void errorVerifyEmail() {
        Toast.makeText(SignupActivity.this, "Unable to verify email", Toast.LENGTH_LONG).show();
        enableSignUpButton();
    }

    /**
     * Method the set the email field as error, display toast, and enable the sign up button
     */
    protected void emailUsedSetScreen() {
        emailField.setTextColor(getResources().getColor(R.color.red, null));
        Toast.makeText(SignupActivity.this, "Email already in use", Toast.LENGTH_LONG).show();
        enableSignUpButton();
    }

    /**
     * Method to throw successful message from user registration
     */
    protected void throwUserCreatedToast() {
        Toast.makeText(this, "User successfully created", Toast.LENGTH_LONG).show();
    }

    //Private method to read data from fields and create and return a user
    private UserInterface createUserFromData() {

        User user = new User();
        user.setFirstName(firstNameField.getText().toString().trim());
        user.setLastName(lastNameField.getText().toString().trim());
        user.setEmail(emailField.getText().toString().trim());
        user.setPassword(passwordField.getText().toString().trim());
        user.setConfirmPassword(confirmPasswordField.getText().toString().trim());
        user.setPhone(phoneField.getText().toString().trim());
        if (userTypeSpinner.getSelectedItem().toString().equals("Employee"))
            user.setIsEmployee("y");
        else user.setIsEmployee("n");

        return user;
    }
}