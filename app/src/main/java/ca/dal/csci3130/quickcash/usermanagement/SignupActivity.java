package ca.dal.csci3130.quickcash.usermanagement;

import android.graphics.Color;
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

        verifyUserData();
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

    private boolean verifyUserData() {

        boolean allDataCorrect = true;

        if (!verifyName(user.getFirstName())) {
            firstNameField.setTextColor(Color.RED);
            allDataCorrect = false;
        }
        else firstNameField.setTextColor(Color.GRAY);

        if (!verifyName(user.getLastName())) {
            lastNameField.setTextColor(Color.RED);

            allDataCorrect = false;
        }
        else lastNameField.setTextColor(Color.GRAY);

        if (!verifyEmail(user.getEmail())) {
            emailField.setTextColor(Color.RED);

            allDataCorrect = false;
        }
        else emailField.setTextColor(Color.GRAY);

        if (!verifyPassword(user.getPassword())) {
            passwordField.setTextColor(Color.RED);

            allDataCorrect = false;
        }
        else passwordField.setTextColor(Color.GRAY);

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            confirmPasswordField.setTextColor(Color.RED);

            allDataCorrect = false;
        }
        else confirmPasswordField.setTextColor(Color.GRAY);

        if (!verifyPhone(user.getPhone())) {
            phoneField.setTextColor(Color.RED);

            allDataCorrect = false;
        }
        else phoneField.setTextColor(Color.GRAY);

        return allDataCorrect;
    }

    public boolean verifyName(String name) {

        //Only allow letters on the name
        return name.matches("^[A-Za-z]+$");
    }

    protected boolean verifyEmail(String email) {

        //Check that the email provided has the correct format

        //Regex expression obtained from FreeFormatter.com
        //URL: https://www.freeformatter.com/java-regex-tester.html
        //Date accessed: February 4 - 2022
        return email.toLowerCase().matches("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$");
    }

    protected boolean verifyPassword(String password) {

        //Check that the password provided has the correct format
        //Password must contains minimum 8 chars, 1 Uppercase, 1 Lowercase, 1 Digit, and 1 Special character

        //Regex expression obtained from Mkyong.com
        //URL: https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
        //Date accessed: February 4 - 2022
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
    }

    protected boolean verifyPhone(String phone) {

        return phone.matches("^[0-9]{10}$");
    }
}