package ca.dal.csci3130.quickcash.usermanagement;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

import ca.dal.csci3130.quickcash.common.DAO;

/**
 * Signup class, this class contains implementation of signup activity
 */
public class Signup {

    private final SignupActivity activity;
    private boolean userDataCorrect;
    private UserInterface user;

    /**
     * Constructor of signup class without a connected activity
     */
    protected Signup() {
        this.activity = null;
    }

    /**
     * Constructor of signup class with a connected signup activity
     *
     * @param activity: optional activity that will manage the UI part of signup
     */
    protected Signup(SignupActivity activity) {
        this.activity = activity;
    }

    /**
     * Method to set the user that will be used to verify data
     *
     * @param user: user that will be verified and registered
     */
    protected void setUser(UserInterface user) {
        this.user = user;
    }

    /**
     * Method that will check each data from the user and return an boolean array with result.
     * NOTE: if all the information is correct, this method will trigger the registration and
     * return to login once completed
     *
     * @return boolean[]: array that contain true if field is correct or false otherwise
     */
    protected boolean[] verifyUserData() {

        boolean[] fieldsStatus = new boolean[6];
        userDataCorrect = true;

        if (!verifyName(user.getFirstName())) {
            fieldsStatus[0] = false;
            userDataCorrect = false;
        } else fieldsStatus[0] = true;

        if (!verifyName(user.getLastName())) {
            fieldsStatus[1] = false;
            userDataCorrect = false;
        } else fieldsStatus[1] = true;

        if (!verifyEmail(user.getEmail())) {
            fieldsStatus[2] = false;
            userDataCorrect = false;
        } else {
            fieldsStatus[2] = true;
            verifyUniqueEmail(user.getEmail());
        }

        if (!verifyPassword(user.getPassword())) {
            fieldsStatus[3] = false;
            userDataCorrect = false;
        } else fieldsStatus[3] = true;

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            fieldsStatus[4] = false;
            userDataCorrect = false;
        } else fieldsStatus[4] = true;

        if (!verifyPhone(user.getPhone())) {
            fieldsStatus[5] = false;
            userDataCorrect = false;
        } else fieldsStatus[5] = true;

        if (!userDataCorrect && activity != null) activity.enableSignUpButton();

        return fieldsStatus;
    }

    /**
     * Method that check if the provided name is valid (only contains letters)
     *
     * @param name: name that will be checked
     * @return boolean: true if name is valid, otherwise false
     */
    protected boolean verifyName(String name) {

        //Only allow letters on the name
        return name.matches("^[A-Za-z]+$");
    }

    /**
     * Method that check if the provided email has the correct format
     *
     * @param email: email that will be checked
     * @return boolean: true if email is valid, otherwise false
     */
    protected static boolean verifyEmail(String email) {

        //Check that the email provided has the correct format
        //Regex expression obtained from FreeFormatter.com
        //URL: https://www.freeformatter.com/java-regex-tester.html
        //Date accessed: February 4 - 2022
        return email.toLowerCase().matches("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$");
    }

    /**
     * Method that check if the provided password has the minimum requirements
     * Password must contains minimum 8 chars, 1 Uppercase, 1 Lowercase, 1 Digit, and 1 Special character
     *
     * @param password: password that will be checked
     * @return boolean: true if password is valid, otherwise false
     */
    protected static boolean verifyPassword(String password) {

        //Check that the password provided has the correct format
        //Password must contains minimum 8 chars, 1 Uppercase, 1 Lowercase, 1 Digit, and 1 Special character

        //Regex expression obtained from Mkyong.com
        //URL: https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
        //Date accessed: February 4 - 2022
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
    }

    /**
     * Method that check if the provided phone has the correct format
     *
     * @param phone: email that will be checked
     * @return boolean: true if phone is valid, otherwise false
     */
    protected boolean verifyPhone(String phone) {

        return phone.matches("^[0-9]{10}$");
    }

    //Private method that check the database to see if the email is unique and trigger user registration
    private void verifyUniqueEmail(String email) {

        //Check if email already on database
        DAO.getUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
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

    ----- START OF CITED CODE:
    */

    /**
     * Method will encrypt the user password in SHA-256 format using provided salt
     *
     * @param passwordToHash: password to encrypt
     * @param salt:           salt that will be used to encrypt the password
     * @return String: encrypted password is returned
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

    //Private method that generate a salt and return it
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
    ----- End cited code
    */

    //Private method that in case all the user information is correct, trigger user registration
    // and call the UI actions and messages
    private void pushUserAndChangeScreenToLogin() {

        if (!userDataCorrect) return;

        encryptUserPasswords();

        pushUserToFirebase();

        if (activity != null) {
            activity.throwUserCreatedToast();
            activity.changeScreenToLogin();
        }
    }

    //Private method that push user into user manager to being push onto the database
    private void pushUserToFirebase() {
        DAO.add(user);
    }

    //Private method that will encrypt current user password and save the salt used
    private void encryptUserPasswords() {
        String salt = getSalt();
        user.setPassword(getSHA256SecurePassword(user.getPassword(), salt));
        user.setConfirmPassword(salt);
    }
}
