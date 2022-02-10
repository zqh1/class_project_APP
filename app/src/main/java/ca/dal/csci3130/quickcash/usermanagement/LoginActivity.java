package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    static String extractedEmail;
    static String extractedPassword;
    private UserInterface user;
    Button login;
    private String userKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkInfo();
            }
        });

        Button signup = findViewById(R.id.signupBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    //check if either account or password are missing
    protected void checkInfo(){
        login.setEnabled(false);
        extractedEmail = getAccount();
        extractedPassword = getPassword();
        if(extractedEmail.isEmpty() || extractedPassword.isEmpty()){
            Toast.makeText(LoginActivity.this, "Missing Email/Password", Toast.LENGTH_SHORT).show();
            login.setEnabled(true);
        }
        else {
            isCorrectInformation(extractedEmail);
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
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if (Objects.equals(data.child("email").getValue(), email)) {           //find the corresponding user email account
                        userKey = data.getKey();

                        user.setConfirmPassword(Objects.requireNonNull(data.child("confirmPassword").getValue()).toString());
                        user.setPassword(Objects.requireNonNull(data.child("password").getValue()).toString());
                        user.setEmail(Objects.requireNonNull(data.child("email").getValue()).toString());
                        user.setFirstName(Objects.requireNonNull(data.child("firstName").getValue()).toString());
                        user.setLastName(Objects.requireNonNull(data.child("lastName").getValue()).toString());
                        user.setIsEmployee(Objects.requireNonNull(data.child("isEmployee").getValue()).toString());
                        user.setPhone(Objects.requireNonNull(data.child("phone").getValue()).toString());

                        if(verifyPassword()){
                            redirectScreen();
                            return;
                        }
                    }
                    Toast.makeText(LoginActivity.this, "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
                    user = new User();
                    login.setEnabled(true);
                }
            }
            //if cannot find user account
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
                login.setEnabled(true);
            }
        });
    }

    protected void redirectScreen(){
        if(isEmployee()){
            startActivity(new Intent(this, EmployeeHomeActivity.class));
        }
        else{
            startActivity(new Intent(this, EmployerHomeActivity.class));
        }
    }

    protected boolean verifyPassword(){
        return user.getPassword().equals(encrypt());
    }

    protected String encrypt(){
        return getSHA256SecurePassword(extractedPassword, user.getConfirmPassword());
    }
    protected boolean isEmployee(){
        return user.getIsEmployee().equals("y");
    }

    /*

    SHA-256 hash code obtained from HowToDoInJava website
    URL: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    Author: Lokesh Gupta
    Date accessed: February 2, 2022
    */
    protected static String getSHA256SecurePassword(String passwordToHash, String salt) {
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

    /*
    End cited code
    */

}
