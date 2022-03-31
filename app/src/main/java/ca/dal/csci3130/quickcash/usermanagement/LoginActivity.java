package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;

import java.util.Objects;

/**
 * LoginActivity, this class implement login logic by check if user entered email and password
 * exist and match in database or not.
 */
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;

    /**
     * OnCreate method, Initialize activity call getButtonReference, and set button behavior
     *
     * @param savedInstanceState: Instances status, required to start activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getButtonReferences();
        setButtonClickListeners();
    }

    /**
     * OnBackPressed method, This method will block user from pressing back button, trying to access homepage(When user logout)
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "[Back Button] is disable", Toast.LENGTH_SHORT).show();
    }

    /**
     * GetButtonReferences method, will assign 2 buttons from interface to global private
     * variable called loginButton and signUpButton
     */
    private void getButtonReferences() {
        loginButton = findViewById(R.id.loginBtn);
        signUpButton = findViewById(R.id.signupBtn);
    }

    /**
     * setButtonClickListeners method, set behavior of behavior to once the button is clicked
     * Then do
     * If login is pressed - Call checkInfo() method
     * If sign up pressed - Will call changeScreenToSignup() method
     */
    private void setButtonClickListeners() {
        loginButton.setOnClickListener(view -> checkInfo());
        signUpButton.setOnClickListener(view -> changeScreenToSignup());
    }

    /**
     * changeScreenToSignup method, will change the current activity to signup activity.
     */
    private void changeScreenToSignup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    /**
     * checkInfo method, will disable loginButton once called. retrieved inputted information
     * by calling getAccount() and getPassword().  Then check if entered information is valid.
     * Then pass to isCorrectInformation to check. if information not valid tell user by using toast
     */
    protected void checkInfo() {
        loginButton.setEnabled(false);

        String extractedEmail = getAccount();
        String extractedPassword = getPassword();

        if (Signup.verifyEmail(extractedEmail) && Signup.verifyPassword(extractedPassword)) {
            isCorrectInformation(extractedEmail, extractedPassword);
        } else {
            Toast.makeText(LoginActivity.this, "Wrong Email/Password", Toast.LENGTH_SHORT).show();
            loginButton.setEnabled(true);
        }
    }

    /**
     * getAccount method, this method will received input email from Login_Layout
     * and return as String
     *
     * @return String from editText email
     */
    protected String getAccount() {
        EditText email = findViewById(R.id.name);
        return email.getText().toString().trim();
    }

    /**
     * getPassword method, this method will received input from password from Login_Layout
     * and return as String.
     *
     * @return String from editText password
     */
    protected String getPassword() {
        EditText password = findViewById(R.id.password);
        return password.getText().toString().trim();
    }

    /**
     * isCorrectInformation method, this method will called once after input from users are verified.
     * This method will check user input with Firebase database to see if email exist,
     * and the password match with child of key.
     * -If email exist,, password's matched. Create new session and createLoginSession
     * -Else tell user that email/password is incorrect
     *
     * @param email:    String email will be received to check if the email enter exist in database
     * @param password: String password that required to be checked
     */
    protected void isCorrectInformation(String email, String password) {

        DAO.getUserReference().orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.hasChildren()) {

                    for (DataSnapshot data : snapshot.getChildren()) {
                        String salt = Objects.requireNonNull(data.child("confirmPassword").getValue()).toString();
                        String hashPassword = Signup.getSHA256SecurePassword(password, salt);

                        if (hashPassword.equals(Objects.requireNonNull(data.child("password").getValue()).toString())) {
                            if(Objects.equals(data.child("isEmployee").getValue(), "y")) {
                                FirebaseMessaging.getInstance().subscribeToTopic("Employees");
                            }
                            SessionManagerInterface session = new SessionManager(LoginActivity.this);
                            session.createLoginSession(data.getKey());

                            return;
                        }
                    }
                }
                Toast.makeText(LoginActivity.this, "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
                loginButton.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database connection error", Toast.LENGTH_LONG).show();
                loginButton.setEnabled(true);
            }
        });
    }
}
