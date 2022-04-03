package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

/**
 * UserDAOAdapter class which adapts getDatabaseReference and add methods based on instance
 * of User
 */


public class UserDAOAdapter extends DAO {

    private UserDAO userDAO = null;

    /**
     * userDAOAdapter constructor
     *
     * @param userDAO: DAO for user
     */

    public UserDAOAdapter(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * getDatabaseReference method that adapts to return preference database reference
     *
     * @return
     */

    @Override
    public DatabaseReference getDatabaseReference() {
        return userDAO.getUserDatabaseReference();
    }

    /**
     * add method that adapts to adds user to database when given a User object
     *
     * @param object: object to be added to database : User, Feedback, Preference, Job
     * @return
     */

    @Override
    public Task<Void> add(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            return userDAO.addUser(user);
        }

        return null;

    }

}
