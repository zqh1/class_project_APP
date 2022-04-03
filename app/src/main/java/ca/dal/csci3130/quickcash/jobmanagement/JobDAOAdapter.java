package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;


public class JobDAOAdapter extends DAO {

    private JobDAO jobDAO = null;

    /**
     * JobDAOAdapter constructor
     *
     * @param jobDAO: DAO for job
     */

    public JobDAOAdapter(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

    /**
     * getDatabaseReference method that adapts to return feedback database reference
     *
     * @return
     */

    @Override
    public DatabaseReference getDatabaseReference() {
        return jobDAO.getJobDatabaseReference();
    }


    /**
     * add method that adapts to adds job to database when given a Job object
     *
     * @param object: object to be added to database : User, Feedback, Preference, Job
     * @return
     */

    @Override
    public Task<Void> add(Object object) {
        if (object instanceof Job) {
            Job job = (Job) object;
            return jobDAO.addJob(job);
        }

        return null;

    }

}
