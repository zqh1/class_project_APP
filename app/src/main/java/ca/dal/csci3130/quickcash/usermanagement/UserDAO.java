package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

public class UserDAO {

    //Firebase database reference
    private static final FirebaseDatabase FIREBASEDATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private final DatabaseReference USERDATABASE = FIREBASE_DATABASE.getReference(User.class.getSimpleName());


    /**
     * getUserDatabaseReference retrieves user database reference
     *
     * @return: returns user database reference
     */
    public DatabaseReference getUserDatabaseReference() {
        return USERDATABASE;
    }


    /**
     * addUser method adds user to the database
     *
     * @param user: user to be added to the database
     * @return
     */
    public Task<Void> addUser(UserInterface user) {
        return USERDATABASE.push().setValue(user);
    }

}
