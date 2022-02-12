package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.common.UserDAOInterface;

/**
 * Manager class that manage the push and pull of users from the firebase
 */
public class UserDAO implements UserDAOInterface {

    private final DatabaseReference databaseReference;

    /**
     * Manager constructor, it will initialize the database connection and set the reference
     * to the user table
     */
    public UserDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    /**
     * Method that return the reference of the user table
     *
     * @return DatabaseReference: User table in firebase
     */
    @Override
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /**
     * Method that push the user into firebase
     *
     * @param user: User class containing all data
     * @return Task<Void>: Firebase task
     */
    @Override
    public Task<Void> add(UserInterface user) {
        return databaseReference.push().setValue(user);
    }
}
