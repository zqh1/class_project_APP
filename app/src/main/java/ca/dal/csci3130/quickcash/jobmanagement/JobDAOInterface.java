package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

/**
 * JobDAO interface, created in case of creation of multiple firebase or database managers
 */
public interface JobDAOInterface {

    /**
     * Method that return the reference of the job table
     *
     * @return DatabaseReference: Job table in firebase
     */
    DatabaseReference getDatabaseReference();

    /**
     * Method that push the Job posting into firebase
     *
     * @param job: User class containing all data
     * @return Task<Void>: Firebase task
     */
    Task<Void> add(JobInterface job);
}
