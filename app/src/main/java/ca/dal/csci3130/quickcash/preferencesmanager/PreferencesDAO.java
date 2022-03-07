package ca.dal.csci3130.quickcash.preferencesmanager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;

public class PreferencesDAO implements PreferencesDAOInterface{

    private final DatabaseReference databaseReference;

    public PreferencesDAO(){
        FirebaseDatabase db = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);
        databaseReference = db.getReference(Preferences.class.getSimpleName());
    }

    @Override
    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    @Override
    public Task<Void> add(PreferencesInterface preferences){
        return databaseReference.push().setValue(preferences);
    }
}
