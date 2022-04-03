package ca.dal.csci3130.quickcash.preferencesmanager;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * This class will verify if the preference that user enter is valid
 */
public class PreferencesVerification {
    private PreferencesInterface preferences;

    /**
     * Set param to local variable to check inside this class
     *
     * @param preferences: PreferencesInterface
     */
    public void setPreferences(PreferencesInterface preferences) {
        this.preferences = preferences;
    }

    /**
     * This method will call each valid method to check each element of PreferencesInterface
     * If all index of array or allfield is true, then it will be upload/update to internet
     *
     * @return boolean array. Each index come from checking each element of PreferencesInterface
     */
    public boolean[] verifyFields() {
        boolean[] fieldStatus = new boolean[]{true, true, true, true, true};
        boolean allFieldCorrect = true;
        if (!validJob(preferences.getJob())) {
            fieldStatus[0] = false;
            allFieldCorrect = false;
        }
        if (!validSalary(preferences.getSalary())) {
            fieldStatus[1] = false;
            allFieldCorrect = false;
        }
        if (!validStartingTime(preferences.getStartingTime())) {
            fieldStatus[2] = false;
            allFieldCorrect = false;
        }
        if (!validMaxDistance(preferences.getMaxDistance())) {
            fieldStatus[3] = false;
            allFieldCorrect = false;
        }
        if (!validDuration(preferences.getDuration())) {
            fieldStatus[4] = false;
            allFieldCorrect = false;
        }

        if (allFieldCorrect) updateOrCreatePreferences();

        return fieldStatus;
    }

    /**
     * This method will check if the title of the job is valid
     * For it to return true, the field need to be not empty and number of letters is less than 251
     *
     * @param job: a String from job.getTitle()
     * @return true if requirement is fulfil, false otherwise.
     */
    public boolean validJob(String job) {
        return !job.isEmpty() && job.length() <= 250;
    }

    /**
     * This method will check if salary is valid
     * Minimal required salary is 13.35, and max is 10,000
     *
     * @param salary: a double from job.getSalary()
     * @return true if requirement is fulfil, false otherwise.
     */
    public boolean validSalary(double salary) {
        //Minimum NS wage 13.35$ per hour
        return salary >= 13.35 && salary <= 10000.0;
    }

    /**
     * If the Starting time for the job is not empty and is match in form of XX:XX
     * NOTE: 24:00 does not valid since 24:00 is 00:00 of next day
     *
     * @param time: time String to check
     * @return true if it fits qualification, false otherwise
     */
    public boolean validStartingTime(String time) {
        return !time.isEmpty() && time.matches("^([0-9]|1[0-9]|2[0-3]):([0-9]|[1-5][0-9])$");
    }

    /**
     * distance has to be int, and greater than 0 but less than 1001
     *
     * @param distance: distance to check
     * @return true if it fits qualification, false otherwise
     */
    public boolean validMaxDistance(int distance) {
        return distance > 0 && distance < 1000;
    }

    /**
     * duration to be check. Has to be high than 0 but less than 100
     *
     * @param duration: duration as int to check
     * @return true if it fits qualification, false otherwise
     */
    public boolean validDuration(int duration) {
        return duration > 0 && duration <= 99;
    }

    /**
     * When this method is call it will connect to DB.
     * Once it connect to DB, it will check if employeeID of this user exists in DB, if it exist
     * it will update instead of creating new query
     * if the ID does not exist in DB, it will create new query inside DB.
     */
    private void updateOrCreatePreferences() {
        DatabaseReference db = new PreferenceDAOAdapter(new PreferenceDAO()).getDatabaseReference();
        db.orderByChild("employeeID").equalTo(preferences.getEmployeeID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() == 1) {
                    //delete existing query
                    for (DataSnapshot data : snapshot.getChildren()) {
                        db.child(Objects.requireNonNull(data.getKey())).getRef().setValue(preferences);
                    }
                } else pushPreferencesToFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error: preferences", error.getMessage());
            }
        });
    }

    //Private method that push job into job manager to being push onto the database
    private void pushPreferencesToFirebase() {
        new PreferenceDAOAdapter(new PreferenceDAO()).add(preferences);
    }

}
