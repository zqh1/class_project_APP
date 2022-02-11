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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private String extractedEmail;
    private String extractedPassword;

    private Button loginButton;
    private Button signUpButton;

    private boolean isEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getButtonReferences();
        setButtonClickListeners();
    }

    private void getButtonReferences() {
        loginButton = findViewById(R.id.loginBtn);
        signUpButton = findViewById(R.id.signupBtn);
    }

    private void setButtonClickListeners() {
        loginButton.setOnClickListener(view -> checkInfo());
        signUpButton.setOnClickListener(view -> changeScreenToSignup());
    }

    private void changeScreenToSignup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    //check if either account or password are missing
    protected void checkInfo(){
        loginButton.setEnabled(false);

        extractedEmail = getAccount();
        extractedPassword = getPassword();

        if (Signup.verifyEmail(extractedEmail) && Signup.verifyPassword(extractedPassword)) {
            isCorrectInformation(extractedEmail);
        }
        else {
            Toast.makeText(LoginActivity.this, "Wrong Email/Password", Toast.LENGTH_SHORT).show();
            loginButton.setEnabled(true);
        }
    }

    //get user account
    protected String getAccount() {
        EditText email = findViewById(R.id.name);
        return email.getText().toString().trim();
    }

    //get user password
    protected String getPassword() {
        EditText password = findViewById(R.id.password);
        return password.getText().toString().trim();
    }

    //check if account and password are correct
    protected void isCorrectInformation(String email){

        DatabaseReference db = new UserDAO().getDatabaseReference();    //link to database
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if (Objects.equals(data.child("email").getValue(), email)) {           //find the corresponding user email account

                        String salt = Objects.requireNonNull(data.child("confirmPassword").getValue()).toString();
                        String hashPassword = Signup.getSHA256SecurePassword(extractedPassword, salt);

                        if (hashPassword.equals(Objects.requireNonNull(data.child("password").getValue()).toString())) {

                            isEmployee = Objects.requireNonNull(data.child("isEmployee").getValue()).toString().equals("y");

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
