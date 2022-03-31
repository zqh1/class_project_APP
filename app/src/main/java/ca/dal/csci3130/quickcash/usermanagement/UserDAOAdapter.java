package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

public class UserDAOAdapter extends DAO {

    private UserDAO userDAO = null;

    public UserDAOAdapter(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public DatabaseReference getDatabaseReference(){
        return userDAO.getUserDatabaseReference();
    }

    @Override
    public Task<Void> add(Object object){
        if(object instanceof User){
            User user = (User)object;
            return userDAO.addUser(user);
        }

        return null;

    }

}
