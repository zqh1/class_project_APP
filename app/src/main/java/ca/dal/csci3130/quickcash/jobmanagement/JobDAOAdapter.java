package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;


public class JobDAOAdapter extends DAO {

    private JobDAO jobDAO = null;

    public JobDAOAdapter(JobDAO jobDAO){
        this.jobDAO = jobDAO;
    }

    @Override
    public DatabaseReference getDatabaseReference(){
        return jobDAO.getJobDatabaseReference();
    }

    @Override
    public Task<Void> add(Object object){
        if(object instanceof Job){
            Job job = (Job)object;
            return jobDAO.addJob(job);
        }

        return null;



    }

}
