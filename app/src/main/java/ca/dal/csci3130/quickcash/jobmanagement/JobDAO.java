package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;


public class JobDAO {

    //Firebase database reference
    private final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private final DatabaseReference Job_DATABASE = FIREBASE_DATABASE.getReference(Job.class.getSimpleName());

    /**
     * getJobDatabaseReference retrieves job database reference
     *
     * @return: returns job database reference
     */

    public DatabaseReference getJobDatabaseReference() {
        return Job_DATABASE;
    }


    /**
     * addJob method adds job to the database
     *
     * @param job: job to be added to the database
     * @return
     */
    public Task<Void> addJob(JobInterface job) {
        return Job_DATABASE.push().setValue(job);
    }

}
