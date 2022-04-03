package ca.dal.csci3130.quickcash.common;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

/**
 * DAO class contains and manage all firebase references
 */
public class DAO {
    /**
     * retrieves database reference
     * @return
     */
    public DatabaseReference getDatabaseReference(){
        return null;
    }

    /**
     * adds provided object to database
     * @param object: object to be added to database : User, Feedback, Preference, Job
     * @return
     */
    public Task<Void> add(Object object){
        return null;
    }
}
