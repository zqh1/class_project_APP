package ca.dal.csci3130.quickcash.usermanagement;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

public class Signup {

    private final SignupActivity activity;
    private boolean userDataCorrect;
    private UserInterface user;

    protected Signup() {
        this.activity = null;
    }

    protected Signup(SignupActivity activity) {
        this.activity = activity;
    }

    protected void setUser(UserInterface user) {
        this.user = user;
    }

    protected boolean[] verifyUserData() {

        boolean[] fieldsStatus = new boolean[6];
        userDataCorrect = true;

        if (!verifyName(user.getFirstName())) {
            fieldsStatus[0] = false;
            userDataCorrect = false;
        }
        else fieldsStatus[0] = true;

        if (!verifyName(user.getLastName())) {
            fieldsStatus[1] = false;
            userDataCorrect = false;
        }
        else fieldsStatus[1] = true;

        if (!verifyEmail(user.getEmail())) {
            fieldsStatus[2] = false;
            userDataCorrect = false;
        }
        else {
            fieldsStatus[2] = true;
            verifyUniqueEmail(user.getEmail());
        }

        if (!verifyPassword(user.getPassword())) {
            fieldsStatus[3] = false;
            userDataCorrect = false;
        }
        else fieldsStatus[3] = true;

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            fieldsStatus[4] = false;
            userDataCorrect = false;
        }
        else fieldsStatus[4] = true;

        if (!verifyPhone(user.getPhone())) {
            fieldsStatus[5] = false;
            userDataCorrect = false;
        }
        else fieldsStatus[5] = true;

        if (!userDataCorrect && activity != null) activity.enableSignUpButton();

        return fieldsStatus;
    }

    protected boolean verifyName(String name) {

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

    protected void verifyUniqueEmail(String email) {

        //Check if email already on database
        DatabaseReference db = new UserDAO().getDatabaseReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if (Objects.equals(data.child("email").getValue(), email)) {
                        if (activity != null) activity.emailUsedSetScreen();
                        userDataCorrect = false;
                    }
                }

                pushUserAndChangeScreenToLogin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (activity != null) activity.errorVerifyEmail();
            }
        });
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

    private static String getSalt() {

        byte[] salt = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Arrays.toString(salt);
    }
    /*
    End cited code
    */

    protected void pushUserToFirebase(){
        new UserDAO().add(user);
    }

    protected void pushUserAndChangeScreenToLogin() {

        if (!userDataCorrect) return;

        encryptUserPasswords();

        pushUserToFirebase();

        if (activity != null) {
            activity.throwUserCreatedToast();
            activity.changeScreenToLogin();
        }
    }

    protected void encryptUserPasswords() {
        String salt = getSalt();
        user.setPassword(getSHA256SecurePassword(user.getPassword(), salt));
        user.setConfirmPassword(salt);
    }
}
