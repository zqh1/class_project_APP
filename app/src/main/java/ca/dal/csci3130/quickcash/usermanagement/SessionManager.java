package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;

/**
 * Session Manager, this class is the one that store and manage the user information.
 * The user ID key is store in the shared preferences of the application.
 * While the user is logged in and inside the app, this class store the general user data
 */
public class SessionManager implements SessionManagerInterface {

    private final Context context;
    private final SharedPreferences sharePref;
    private final SharedPreferences.Editor editor;

    private static UserInterface user;

    /**
     * Session Manager constructor, it will set the context (screen that initialize manager)
     * and initialize the shared preferences with the user key string and editor
     *
     * @param context: activity that is calling the manager and the one used for manager interaction
     */
    public SessionManager(Context context) {
        this.context = context;
        this.sharePref = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
        this.editor = sharePref.edit();
    }

    /**
     * Method return reference to user
     *
     * @return UserInterface: User class containing user data
     */
    public static UserInterface getUser() {
        return user;
    }

    /**
     * Method will save user id into the shared preferences of the application to keep
     * user logged in. Also, it will read the user information from the database.
     *
     * @param userID: User ID given on sign up
     */
    @Override
    public void createLoginSession(String userID) {
        editor.putString(Constants.USER_KEY, userID);
        editor.apply();

        this.getUserInformation(userID);
    }

    /**
     * Method will check if the user is logged in and sent to user home screen,
     * otherwise, sent to login screen
     */
    @Override
    public void checkLogin() {
        if (!isLoggedIn()) context.startActivity(new Intent(context, LoginActivity.class));
        else getUserInformation(getUserID());
    }

    /**
     * Logout method, user ID and information is deleted from application
     */
    @Override
    public void logoutUser() {
        editor.clear();
        editor.apply();
        user = null;
    }

    /**
     * Method will check if the User is logged in in the app (User ID KEY exists)
     *
     * @return boolean: true if user logged in, otherwise, false
     */
    @Override
    public boolean isLoggedIn() {
        return sharePref.contains(Constants.USER_KEY);
    }

    //Private method that return the user ID stored on shared preferences
    public String getUserID() {
        return sharePref.getString(Constants.USER_KEY, null);
    }

    //Private method used by manager to logout user and change screen to login
    private void goHomeScreen() {
        if (user.getIsEmployee().equals("y"))
            context.startActivity(new Intent(context, EmployeeHomeActivity.class));
        else context.startActivity(new Intent(context, EmployerHomeActivity.class));
    }

    //Private method used by manager to query user data from database
    private void getUserInformation(String userID) {
        DatabaseReference db = new UserDAO().getDatabaseReference();    //link to database
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Get user from all database
                DataSnapshot data = snapshot.child(userID);

                //Retrieve user data
                user = new User();
                user.setFirstName(Objects.requireNonNull(data.child("firstName").getValue()).toString());
                user.setLastName(Objects.requireNonNull(data.child("lastName").getValue()).toString());
                user.setEmail(Objects.requireNonNull(data.child("email").getValue()).toString());
                user.setIsEmployee(Objects.requireNonNull(data.child("isEmployee").getValue()).toString());
                user.setPhone(Objects.requireNonNull(data.child("phone").getValue()).toString());

                //Change screen to user home screen
                goHomeScreen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Database connection error", Toast.LENGTH_LONG).show();
            }
        });
    }
}