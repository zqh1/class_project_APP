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

public class SessionManager implements SessionManagerInterface {

    private final Context context;
    private final SharedPreferences sharePref;
    private final SharedPreferences.Editor editor;

    private static UserInterface user;

    public SessionManager(Context context) {
        this.context = context;
        this.sharePref = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
        this.editor = sharePref.edit();
    }

    public static UserInterface getUser() {
        return user;
    }

    @Override
    public void createLoginSession(String userID) {
        editor.putString(Constants.USER_KEY, userID);
        editor.apply();

        this.getUserInformation(userID);
    }

    @Override
    public void checkLogin() {
        if (!isLoggedIn()) context.startActivity(new Intent(context, LoginActivity.class));
        else getUserInformation(getUserID());
    }

    @Override
    public void logoutUser() {
        editor.clear();
        editor.apply();
        user = null;
    }

    @Override
    public boolean isLoggedIn() {
        return sharePref.contains(Constants.USER_KEY);
    }

    private String getUserID() {
        return sharePref.getString(Constants.USER_KEY, null);
    }

    private void goHomeScreen() {
        if (user.getIsEmployee().equals("y")) context.startActivity(new Intent(context, EmployeeHomeActivity.class));
        else context.startActivity(new Intent(context, EmployerHomeActivity.class));
    }

    private void getUserInformation(String userID) {
        DatabaseReference db = new UserDAO().getDatabaseReference();    //link to database
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot data = snapshot.child(userID);

                user = new User();
                user.setFirstName(Objects.requireNonNull(data.child("firstName").getValue()).toString());
                user.setLastName(Objects.requireNonNull(data.child("lastName").getValue()).toString());
                user.setEmail(Objects.requireNonNull(data.child("email").getValue()).toString());
                user.setIsEmployee(Objects.requireNonNull(data.child("isEmployee").getValue()).toString());
                user.setPhone(Objects.requireNonNull(data.child("phone").getValue()).toString());

                goHomeScreen();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Database connection error", Toast.LENGTH_LONG).show();
            }
        });
    }
}