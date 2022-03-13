package ca.dal.csci3130.quickcash.preferencesmanager;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.dal.csci3130.quickcash.common.DAO;

public class PreferencesVerification {
    private PreferencesInterface preferences;

    public void setPreferences(PreferencesInterface preferences){
        this.preferences = preferences;
    }

    public boolean[] verifyFields(){
        boolean[] fieldStatus = new boolean[] {true, true, true, true, true};
        boolean allFieldCorrect = true;
        if(!validJob(preferences.getJob())){
            fieldStatus[0] = false;
            allFieldCorrect = false;
        }
        if(!validSalary(preferences.getSalary())){
            fieldStatus[1] = false;
            allFieldCorrect = false;
        }
        if(!validStartingTime(preferences.getStartingTime())){
            fieldStatus[2] = false;
            allFieldCorrect = false;
        }
        if(!validMaxDistance(preferences.getMaxDistance())){
            fieldStatus[3] = false;
            allFieldCorrect = false;
        }
        if(!validDuration(preferences.getDuration())){
            fieldStatus[4] = false;
            allFieldCorrect = false;
        }

        if(allFieldCorrect) updateOrCreatePreferences();

        return fieldStatus;
    }

    public boolean validJob(String job) {
        return !job.isEmpty() && job.length() <= 250;
    }

    public boolean validSalary(double salary) {
        //Minimum NS wage 13.35$ per hour
        return salary >= 13.35 && salary <= 10000.0;
    }

    public boolean validStartingTime(String time) {
        return !time.isEmpty() && time.matches("^([0-9]|1[0-9]|2[0-3]):([0-9]|[1-5][0-9])$");
    }

    public boolean validMaxDistance(int distance) {
        return distance > 0 && distance < 1000;
    }

    public boolean validDuration(int duration) {
        return duration > 0 && duration <= 99;
    }

    private void updateOrCreatePreferences(){
        DatabaseReference db = DAO.getPreferenceReference();
        db.orderByChild("employeeID").equalTo(preferences.getEmployeeID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() == 1){
                    //delete existing query
                    for(DataSnapshot data : snapshot.getChildren()){
                        db.child(Objects.requireNonNull(data.getKey())).getRef().setValue(preferences);
                    }
                }
                else pushPreferencesToFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error: preferences", error.getMessage());
            }
        });
    }

    //Private method that push job into job manager to being push onto the database
    private void pushPreferencesToFirebase() {
        DAO.add(preferences);
    }

}
