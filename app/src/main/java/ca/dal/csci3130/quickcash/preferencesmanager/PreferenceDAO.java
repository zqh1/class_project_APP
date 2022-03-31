package ca.dal.csci3130.quickcash.preferencesmanager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;

public class PreferenceDAO {

    //Firebase database reference
    private final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private final DatabaseReference Preference_DATABASE = FIREBASE_DATABASE.getReference(Preferences.class.getSimpleName());

    public DatabaseReference getPreferenceDatabaseReference(){
        return Preference_DATABASE;
    }

    public Task<Void> addPreference(PreferencesInterface preference) {
        return Preference_DATABASE.push().setValue(preference);
    }

}
