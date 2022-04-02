package ca.dal.csci3130.quickcash.preferencesmanager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

public class PreferenceDAO {

    //Firebase database reference
    private static final FirebaseDatabase FIREBASEDATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private static final DatabaseReference PreferenceDATABASE = FIREBASEDATABASE.getReference(Preferences.class.getSimpleName());

    public DatabaseReference getPreferenceDatabaseReference(){
        return PreferenceDATABASE;
    }

    public Task<Void> addPreference(PreferencesInterface preference) {
        return PreferenceDATABASE.push().setValue(preference);
    }

}
