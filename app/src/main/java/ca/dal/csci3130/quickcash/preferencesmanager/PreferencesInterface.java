package ca.dal.csci3130.quickcash.preferencesmanager;

public interface PreferencesInterface {

    void setEmployeeID(String id);

    String getEmployeeID();

    void setJob(String job);

    String getJob();

    void setSalary(int salary);

    int getSalary();

    void setMaxDistance(int distance);

    int getMaxDistance();

    void setDuration(int duration);

    int getDuration();

    void setStartingHour(int hour);

    int getStartingHour();

    void setStartingMinute(int minute);

    int getStartingMinute();

    String getStartingTime();

}
