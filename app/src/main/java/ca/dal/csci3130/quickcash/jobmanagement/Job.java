package ca.dal.csci3130.quickcash.jobmanagement;

import java.util.ArrayList;

public class Job {

    private String employerID;
    private String title;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int duration;
    private int salary;
    private boolean urgent;
    private ArrayList<JobInterface.JOB_TAGS> tags;

    public Job() {
        this.tags = new ArrayList<>();
    }

    public Job(String employerID, String title, int year, int month, int day, int hour, int minute,
               int duration, int salary, boolean urgent, ArrayList<JobInterface.JOB_TAGS> tags) {
        this.employerID = employerID;
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.duration = duration;
        this.salary = salary;
        this.urgent = urgent;
        this.tags = tags;
    }

    public void setDate(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public void addTag(JobInterface.JOB_TAGS tag) {
        this.tags.add(tag);
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
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

    public ArrayList<JobInterface.JOB_TAGS> getTags() {
        return tags;
    }

    public void setTags(ArrayList<JobInterface.JOB_TAGS> tags) {
        this.tags = tags;
    }
}
