package ca.dal.csci3130.quickcash.usermanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import ca.dal.csci3130.quickcash.common.Constants;

public class SessionManager implements SessionManagerInterface {

    Context context;
    SharedPreferences sharePref;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        this.sharePref = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
        this.editor = sharePref.edit();
    }

    @Override
    public void createLoginSession(String email, String password, String name) {
        editor.putString(Constants.EMAIL_KEY, email);
        editor.putString(Constants.PASSWORD_KEY, password);
        editor.putString(Constants.NAME_KEY, name);
        editor.apply();
    }

    @Override
    public void checkLogin() {
        if (!isLoggedIn()) context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }

    @Override
    public boolean isLoggedIn() {
        return sharePref.contains(Constants.EMAIL_KEY) && sharePref.contains(Constants.NAME_KEY) &&
                sharePref.contains(Constants.PASSWORD_KEY);
    }

    @Override
    public String getKeyName() {
        return sharePref.getString(Constants.NAME_KEY,null);
    }

    @Override
    public String getKeyEmail() {
        return sharePref.getString(Constants.EMAIL_KEY, null);
    }
}