package ca.dal.csci3130.quickcash.jobmanagement;

public interface JobInterface {

    void addTag(String tag);

    String getEmployerID();

    void setEmployerID(String employerID);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    String getTags();

    void setTags(String tags);

    int getDuration();

    void setDuration(int duration);

    int getSalary();

    void setSalary(int salary);

    boolean isUrgent();

    void setUrgent(boolean urgent);

    public int getYear();

    void setYear(int year);

    int getMonth();

    void setMonth(int month);

    int getDay();

    void setDay(int day);

    int getHour();

    void setHour(int hour);

    int getMinute();

    void setMinute(int minute);

    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);
}
