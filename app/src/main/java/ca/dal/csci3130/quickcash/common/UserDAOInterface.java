package ca.dal.csci3130.quickcash.common;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

/**
 * UserDAO interface, created in case of creation of multiple firebase or database managers
 */
public interface UserDAOInterface {

    /**
     * Method that return the reference of the user table
     *
     * @return DatabaseReference: User table in firebase
     */
    DatabaseReference getDatabaseReference();

    /**
     * Method that push the user into firebase
     *
     * @param user: User class containing all data
     * @return Task<Void>: Firebase task
     */
    Task<Void> add(UserInterface user);
}
