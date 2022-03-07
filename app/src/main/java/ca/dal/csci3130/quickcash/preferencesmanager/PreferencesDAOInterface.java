package ca.dal.csci3130.quickcash.preferencesmanager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public interface PreferencesDAOInterface {

    DatabaseReference getDatabaseReference();

    Task<Void> add(PreferencesInterface preferences);
}
