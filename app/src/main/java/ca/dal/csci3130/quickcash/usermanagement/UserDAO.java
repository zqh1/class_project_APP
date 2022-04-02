package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.common.DAO;

public class UserDAO {

    //Firebase database reference
    private final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private final DatabaseReference USER_DATABASE = FIREBASE_DATABASE.getReference(User.class.getSimpleName());

    public DatabaseReference getUserDatabaseReference(){
        return USER_DATABASE;
    }

    public Task<Void> addUser(UserInterface user) {
        return USER_DATABASE.push().setValue(user);
    }

}
