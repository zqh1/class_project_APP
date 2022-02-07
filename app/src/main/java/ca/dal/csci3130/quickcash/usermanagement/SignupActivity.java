package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

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

    private boolean dataVerified;

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
        signInBtn.setOnClickListener(view -> signInBtnAction());
    }

    private void signUpBtnAction() {

        //Disable button so user wait until verification is completed
        signUpBtn.setEnabled(false);

        readUserData();

        if (verifyUserData()) {

            //Encrypt User passwords
            encryptUserPassword();
        }
        else signUpBtn.setEnabled(true);
    }

    private void userRegisterComplete() {

        if (!dataVerified) return;

        new UserDAO().add(user);

        Toast.makeText(this, "User successfully created", Toast.LENGTH_LONG).show();

        signInBtnAction();
    }

    private void signInBtnAction() {

        final Intent changeSignIn = new Intent(this, LoginActivity.class);
        startActivity(changeSignIn);
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

    private void encryptUserPassword() {
        try {
            user.setPassword(get_SHA_256_SecurePassword(user.getPassword(), getSalt()));
            user.setConfirmPassword(get_SHA_256_SecurePassword(user.getConfirmPassword(), getSalt()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
        else {
            emailField.setTextColor(Color.GRAY);

            //Verify that the email is not being used
            verifyUniqueEmail(user.getEmail());
        }

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

        dataVerified = allDataCorrect;

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

    private void verifyUniqueEmail(String email) {

        //TODO: FIX THIS ASYNCHRONOUS SHIT

        //Check if email already on database
        DatabaseReference db = new UserDAO().getDatabaseReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if (Objects.equals(data.child("email").getValue(), email)) {

                        Toast.makeText(SignupActivity.this, "Email already in use", Toast.LENGTH_LONG).show();
                        emailField.setTextColor(Color.RED);
                        signUpBtn.setEnabled(true);
                        return;
                    }
                }

                //If email unique, save user and redirect to login
                userRegisterComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignupActivity.this, "Unable to verify email", Toast.LENGTH_LONG).show();
                signUpBtn.setEnabled(true);
            }
        });
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

    /*
    SHA-256 hash code obtained from HowToDoInJava website
    URL: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    Author: Lokesh Gupta
    Date accessed: February 2, 2022
    */
    private static String get_SHA_256_SecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Arrays.toString(salt);
    }
    /*
    End cited code
    */
}