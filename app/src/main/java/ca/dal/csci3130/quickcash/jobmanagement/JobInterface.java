package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

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

    Calendar getDate();

    void setDate(Calendar date);

    LatLng getLocation();

    void setLocation(LatLng location);
}
