package ca.dal.csci3130.quickcash.preferencesmanager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

/**
 * PreferenceDAOAdapter class which adapts getDatabaseReference and add methods based on instance
 * of a Preference
 */

public class PreferenceDAOAdapter extends DAO {

    private PreferenceDAO preferenceDAO = null;

    /**
     * PreferenceDAOAdapter constructor
     *
     * @param preferenceDAO: DAO for preference
     */

    public PreferenceDAOAdapter(PreferenceDAO preferenceDAO) {
        this.preferenceDAO = preferenceDAO;
    }

    /**
     * getDatabaseReference method that adapts to return preference database reference
     *
     * @return
     */

    @Override
    public DatabaseReference getDatabaseReference() {
        return preferenceDAO.getPreferenceDatabaseReference();
    }

    /**
     * add method that adapts to adds preference to database when given a Preference object
     *
     * @param object: object to be added to database : User, Feedback, Preference, Job
     * @return
     */

    @Override
    public Task<Void> add(Object object) {
        if (object instanceof Preferences) {
            Preferences preferences = (Preferences) object;
            return preferenceDAO.addPreference(preferences);
        }

        return null;


    }

}
