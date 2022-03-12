package ca.dal.csci3130.quickcash.jobmanagement;

public class Job implements JobInterface{

    private String employerID = "";
    private String title = "";
    private String description = "";
    private String tags = "";
    private int duration;
    private double salary;
    private boolean urgent;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private double latitude;
    private double longitude;

    private String applicantsID = "";
    private String acceptedID = "";

    //Constructor for firebase UI
    public Job(){ /*Empty required by firebase*/}

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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getApplicantsID() {
        return applicantsID;
    }

    public void setApplicantsID(String applicantsID) {
        this.applicantsID = applicantsID;
    }

    public String getAcceptedID() {
        return acceptedID;
    }

    public void setAcceptedID(String acceptedID) {
        this.acceptedID = acceptedID;
    }
}
