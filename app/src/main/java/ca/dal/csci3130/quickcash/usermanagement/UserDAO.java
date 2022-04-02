package ca.dal.csci3130.quickcash.usermanagement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

public class UserDAO {

    //Firebase database reference
    private static final FirebaseDatabase FIREBASEDATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private static final DatabaseReference USERDATABASE = FIREBASEDATABASE.getReference(User.class.getSimpleName());

    public DatabaseReference getUserDatabaseReference(){
        return USERDATABASE;
    }

    public Task<Void> addUser(UserInterface user) {
        return USERDATABASE.push().setValue(user);
    }

}
