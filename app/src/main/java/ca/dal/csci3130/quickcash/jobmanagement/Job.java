package ca.dal.csci3130.quickcash.jobmanagement;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class Job implements JobInterface{

    private String employerID;
    private String title;
    private String description;
    private String tags;
    private int duration;
    private int salary;
    private boolean urgent;

    private Calendar date;

    private LatLng location;

    public Job() {

    }

    public Job(String employerID, String title, String description, String tags, Calendar date, int duration, int salary, boolean urgent, LatLng location) {
        this.employerID = employerID;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.date = date;
        this.duration = duration;
        this.salary = salary;
        this.urgent = urgent;
        this.location = location;
    }

    public void addTag(String tag) {
        this.tags += ", " + tag;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
