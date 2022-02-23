package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

/**
 * Manager class that manage the push and pull of users from the firebase
 */
public class JobDAO implements JobDAOInterface {

    private final DatabaseReference databaseReference;

    /**
     * Manager constructor, it will initialize the database connection and set the reference
     * to the job table
     */
    public JobDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        databaseReference = db.getReference(Job.class.getSimpleName());
    }

    /**
     * Method that return the reference of the job table
     * @return DatabaseReference: User table in firebase
     */
    @Override
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /**
     * Method that push the job posting into firebase
     * @param job: Job class containing all data
     * @return Task<Void>: Firebase task
     */
    @Override
    public Task<Void> add(JobInterface job) {
        return databaseReference.push().setValue(job);
    }
}
