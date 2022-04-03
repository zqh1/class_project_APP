package ca.dal.csci3130.quickcash.preferencesmanager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

/**
 * DAO class contains and manage preference references
 */

public class PreferenceDAO {

    //Firebase database reference
    private static final FirebaseDatabase FIREBASEDATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private final DatabaseReference PREFERENCEDATABASE = FIREBASEDATABASE.getReference(Preferences.class.getSimpleName());

    /**
     * getPreferenceDatabaseReference retrieves preference database reference
     *
     * @return: returns preference database reference
     */

    public DatabaseReference getPreferenceDatabaseReference() {
        return PREFERENCEDATABASE;
    }

    /**
     * addPreference method adds preference to the database
     *
     * @param preference: preference to be added to the database
     * @return
     */

    public Task<Void> addPreference(PreferencesInterface preference) {
        return PREFERENCEDATABASE.push().setValue(preference);
    }

}
