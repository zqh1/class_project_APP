package ca.dal.csci3130.quickcash.preferencesmanager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

public class PreferenceDAOAdapter extends DAO {

    private PreferenceDAO preferenceDAO = null;

    public PreferenceDAOAdapter(PreferenceDAO preferenceDAO){
        this.preferenceDAO = preferenceDAO;
    }

    @Override
    public DatabaseReference getDatabaseReference(){
        return preferenceDAO.getPreferenceDatabaseReference();
    }

    @Override
    public Task<Void> add(Object object){
        if(object instanceof Preferences){
            Preferences preferences = (Preferences)object;
            return preferenceDAO.addPreference(preferences);
        }

        return null;


    }

}
