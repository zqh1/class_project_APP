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

/**
 * DAO class contains and manage all firebase references
 */
public class DAO {

    //Disable instantiation of class, all methods are static
    private DAO() {
    }

    //Firebase database reference
    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    //User, Job, and Preferences tables references
    private static final DatabaseReference USER_DATABASE = FIREBASE_DATABASE.getReference(User.class.getSimpleName());
    private static final DatabaseReference JOB_DATABASE = FIREBASE_DATABASE.getReference(Job.class.getSimpleName());
    private static final DatabaseReference PREFERENCE_DATABASE = FIREBASE_DATABASE.getReference(Preferences.class.getSimpleName());

    /**
     * Method return the reference to the user table
     *
     * @return DatabaseReference: User table in Firebase
     */
    public static DatabaseReference getUserReference() {
        return USER_DATABASE;
    }

    /**
     * Method return the reference to the job table
     *
     * @return DatabaseReference: Job table in Firebase
     */
    public static DatabaseReference getJobReference() {
        return JOB_DATABASE;
    }

    /**
     * Method return the reference to the preference table
     *
     * @return DatabaseReference: Preference table in Firebase
     */
    public static DatabaseReference getPreferenceReference() {
        return PREFERENCE_DATABASE;
    }

    /**
     * Method add an User into the user table in Firebase
     *
     * @param user: User to be added into firebase
     * @return task: For firebase
     */
    public static Task<Void> add(UserInterface user) {
        return USER_DATABASE.push().setValue(user);
    }

    /**
     * Method add an Job into the job table in Firebase
     *
     * @param job: Job to be added into firebase
     * @return task: For firebase
     */
    public static Task<Void> add(JobInterface job) {
        return JOB_DATABASE.push().setValue(job);
    }

    /**
     * Method add an Preferences into the preferences table in Firebase
     *
     * @param preferences: Preferences to be added into firebase
     * @return task: For firebase
     */
    public static Task<Void> add(PreferencesInterface preferences) {
        return PREFERENCE_DATABASE.push().setValue(preferences);
    }
}
