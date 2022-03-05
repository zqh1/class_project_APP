package ca.dal.csci3130.quickcash.preferencesmanager;

public class PreferencesVerification {
    private PreferencesInterface preferences;

    public void setPreferences(PreferencesInterface preferences){
        this. preferences = preferences;
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
            fieldStatus[1] = false;
            allFieldCorrect = false;
        }
        if(!validMaxDistance(preferences.getMaxDistance())){
            fieldStatus[1] = false;
            allFieldCorrect = false;
        }
        if(!validDuration(preferences.getDuration())){
            fieldStatus[1] = false;
            allFieldCorrect = false;
        }
        if(allFieldCorrect) pushPreferencesToFirebase();

        return fieldStatus;
    }

    public boolean validJob(String title) {
        return !title.isEmpty() && title.length() <= 250;
    }

    public boolean validSalary(int salary) {
        //Minimum NS wage 13.35$ per hour
        return salary > 13.35 && salary <= 10000;
    }

    public boolean validStartingTime(String time) {
        return !time.isEmpty() && time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
    }

    public boolean validMaxDistance(int distance) {
        return distance > 0 && distance < 1000;
    }

    public boolean validDuration(int duration) {
        return duration > 0 && duration <= 168;
    }

    //Private method that push job into job manager to being push onto the database
    private void pushPreferencesToFirebase() {
        new PreferencesDAO().add(preferences);
    }

}
