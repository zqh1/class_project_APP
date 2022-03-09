package ca.dal.csci3130.quickcash.common;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.jobmanagement.JobInterface;
import ca.dal.csci3130.quickcash.preferencesmanager.Preferences;
import ca.dal.csci3130.quickcash.preferencesmanager.PreferencesInterface;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class DAO {

    //Disable instantiation of class, all methods are static
    private DAO() {}

    private static final FirebaseDatabase db = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private static final DatabaseReference userDatabase = db.getReference(User.class.getSimpleName());
    private static final DatabaseReference jobDatabase = db.getReference(Job.class.getSimpleName());
    private static final DatabaseReference preferenceDatabase = db.getReference(Preferences.class.getSimpleName());

    public static DatabaseReference getUserReference() {
        return userDatabase;
    }

    public static DatabaseReference getJobReference() {
        return jobDatabase;
    }

    public static DatabaseReference getPreferenceReference() {
        return preferenceDatabase;
    }

    public static Task<Void> add(UserInterface user) {
        return userDatabase.push().setValue(user);
    }

    public static Task<Void> add(JobInterface job) {
        return jobDatabase.push().setValue(job);
    }

    public static Task<Void> add(PreferencesInterface preferences){
        return preferenceDatabase.push().setValue(preferences);
    }
}
