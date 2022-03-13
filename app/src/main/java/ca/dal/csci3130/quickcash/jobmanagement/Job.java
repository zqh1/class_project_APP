package ca.dal.csci3130.quickcash.jobmanagement;

public class Job implements JobInterface {

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
    public Job() { /*Empty required by firebase*/}

    /**
     * If there happened to be more than 1 tag, the method will add another tag
     * to tag variable with "," in front
     *
     * @param tag: String tag to be add after existing tag
     */
    public void addTag(String tag) {
        this.tags += ", " + tag;
    }

    /**
     * The method will return employerID once called
     *
     * @return ID of the employer who created the job
     */
    public String getEmployerID() {
        return employerID;
    }

    /**
     * The method will be be called once the employer created the job to collect ID of employer
     *
     * @param employerID: ID of an employer who created the job
     */
    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    /**
     * This method will return title of the job
     *
     * @return title of the job
     */
    public String getTitle() {
        return title;
    }

    /**
     * Will set the title variable to String param
     *
     * @param title: title that employer input during job creation
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method will return description of the job
     *
     * @return description of the job
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method will set the description according to what the employer enter
     * in description section during job creation
     *
     * @param description: description of the job that employer entered
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method will return tag that employer entered during job creation
     *
     * @return tags that employer set to the job
     */
    public String getTags() {
        return tags;
    }

    /**
     * This method will called once the user entered the tag, after it is call addTag() will be call
     *
     * @param tags: tags that employer will set for the job according to their judgement
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * This method will return estimate duration it will take to complete the job
     *
     * @return duration of the job will take to be complete
     */
    public int getDuration() {
        return duration;
    }

    /**
     * This method will set duration of the job according to what employer entered for duration
     * in job creation
     *
     * @param duration: estimate time for the job to be complete
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * This job will return salary once the job is complete
     *
     * @return salary of job as double
     */
    public double getSalary() {
        return salary;
    }

    /**
     * This method will set  local variable salary to amount of salary employer entered
     *
     * @param salary: amount of salary employer set (as double)
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * This method will return true/false depends on employer interaction with urgent tick box
     *
     * @return return true if employer trigger urgent button during job creation, false otherwise
     */
    public boolean isUrgent() {
        return urgent;
    }

    /**
     * This method will set urgent to true or false depends on employer interaction with tick box
     *
     * @param urgent: true if employer trigger urgent button during job creation, false otherwise
     */
    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    /**
     * This method will return what year the job will start
     *
     * @return What year the job will start
     */
    public int getYear() {
        return year;
    }

    /**
     * This method will set year according to employer interaction with calender
     *
     * @param year: Year of the job will start according to the calender
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * This method will return what month the job will start
     *
     * @return What month the job will start
     */
    public int getMonth() {
        return month;
    }

    /**
     * This method will set month according to employer interaction with calender
     *
     * @param month :month of the job will start according to the calender
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * This method will return what date the job will start
     *
     * @return What date the job will start
     */
    public int getDay() {
        return day;
    }

    /**
     * This method will set date according to employer interaction with calender
     *
     * @param day: date of the job will start according to the calender
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * This method will return what hour the job will start
     *
     * @return Starting hour of job
     */
    public int getHour() {
        return hour;
    }

    /**
     * This method will set hour according to employer interaction with clock
     *
     * @param hour: Starting hour depends on when did the employer set time to on clock UI
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * This method will return what minute the job will start
     *
     * @return Starting minute of job
     */
    public int getMinute() {
        return minute;
    }

    /**
     * This method will set minute according to employer interaction with clock
     *
     * @param minute: Starting minute depends on when did the employer set time to on clock UI
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Return latitude of location which is suppose to be where employee will be working on.
     *
     * @return latitude of job location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set latitude according to where is the job will be located
     *
     * @param latitude: come from LatLng object, but only Latitude will be set.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Return longitude of location which is suppose to be where employee will be working on.
     *
     * @return longitude of the job location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set longitude according to where is the job will be located
     *
     * @param longitude: come from LatLng object, but only Longitude will be set.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Return ID of the employee(s) who apply for the job
     *
     * @return employee(s) ID who applied to the job separated with ", "
     */
    @Override
    public String getApplicantsID() {
        return applicantsID;
    }

    /**
     * Set String applicantsID to a param String
     *
     * @param applicantsID: a String that have all of the applicants ID
     */
    public void setApplicantsID(String applicantsID) {
        this.applicantsID = applicantsID;
    }

    /**
     * Return ID of the employee who is accepted for the position
     *
     * @return Accepted employee ID
     */
    public String getAcceptedID() {
        return acceptedID;
    }

    /**
     * This method will be call only once. Once the employer select which applicant
     * will be apply to the position this method will be called
     *
     * @param acceptedID: an employee ID who got accepted.
     */
    public void setAcceptedID(String acceptedID) {
        this.acceptedID = acceptedID;
    }
}
