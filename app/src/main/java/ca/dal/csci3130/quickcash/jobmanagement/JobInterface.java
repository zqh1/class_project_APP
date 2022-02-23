package ca.dal.csci3130.quickcash.jobmanagement;

import java.util.ArrayList;

public interface JobInterface {

    enum JOB_TAGS {MANUAL, FULL_TIME, HALF_TIME, TEMPORAL, URGENT, CS}

    void setDate(int year, int month, int day, int hour, int minute);

    void addTag(JobInterface.JOB_TAGS tag);

    String getEmployerID();

    void setEmployerID(String employerID);

    String getTitle();

    void setTitle(String title);

    int getYear();

    void setYear(int year);

    int getMonth();

    void setMonth(int month);

    int getDay();

    void setDay(int day);

    int getHour();

    void setHour(int hour);

    int getMinute();

    void setMinute(int minute);

    int getDuration();

    void setDuration(int duration);

    int getSalary();

    void setSalary(int salary);

    boolean isUrgent();

    void setUrgent(boolean urgent);

    ArrayList<JobInterface.JOB_TAGS> getTags();

    void setTags(ArrayList<JobInterface.JOB_TAGS> tags);
}
