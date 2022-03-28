package ca.dal.csci3130.quickcash.jobmanagement;

public interface JobInterface {

    /**
     * If there happened to be more than 1 tag, the method will add another tag
     * to tag variable with "," in front
     *
     * @param tag: String tag to be add after existing tag
     */
    void addTag(String tag);

    /**
     * The method will return employerID once called
     *
     * @return ID of the employer who created the job
     */
    String getEmployerID();

    /**
     * The method will be be called once the employer created the job to collect ID of employer
     *
     * @param employerID: ID of an employer who created the job
     */
    void setEmployerID(String employerID);

    /**
     * This method will return title of the job
     *
     * @return title of the job
     */
    String getTitle();

    /**
     * Will set the title variable to String param
     *
     * @param title: title that employer input during job creation
     */
    void setTitle(String title);

    /**
     * This method will return description of the job
     *
     * @return description of the job
     */
    String getDescription();

    /**
     * This method will set the description according to what the employer enter
     * in description section during job creation
     *
     * @param description: description of the job that employer entered
     */
    void setDescription(String description);

    /**
     * This method will return tag that employer entered during job creation
     *
     * @return tags that employer set to the job
     */
    String getTags();

    /**
     * This method will called once the user entered the tag, after it is call addTag() will be call
     *
     * @param tags: tags that employer will set for the job according to their judgement
     */
    void setTags(String tags);

    /**
     * This method will return estimate duration it will take to complete the job
     *
     * @return duration of the job will take to be complete
     */
    int getDuration();

    /**
     * This method will set duration of the job according to what employer entered for duration
     * in job creation
     *
     * @param duration: estimate time for the job to be complete
     */
    void setDuration(int duration);

    /**
     * This job will return salary once the job is complete
     *
     * @return salary of job as double
     */
    double getSalary();

    /**
     * This method will set  local variable salary to amount of salary employer entered
     *
     * @param salary: amount of salary employer set (as double)
     */
    void setSalary(double salary);

    /**
     * This method will return true/false depends on employer interaction with urgent tick box
     *
     * @return return true if employer trigger urgent button during job creation, false otherwise
     */
    boolean isUrgent();

    /**
     * This method will set urgent to true or false depends on employer interaction with tick box
     *
     * @param urgent: true if employer trigger urgent button during job creation, false otherwise
     */
    void setUrgent(boolean urgent);

    /**
     * This method will return what year the job will start
     *
     * @return What year the job will start
     */
    int getYear();

    /**
     * This method will set year according to employer interaction with calender
     *
     * @param year: Year of the job will start according to the calender
     */
    void setYear(int year);

    /**
     * This method will return what month the job will start
     *
     * @return What month the job will start
     */
    int getMonth();

    /**
     * This method will set month according to employer interaction with calender
     *
     * @param month :month of the job will start according to the calender
     */
    void setMonth(int month);

    /**
     * This method will return what date the job will start
     *
     * @return What date the job will start
     */
    int getDay();

    /**
     * This method will set date according to employer interaction with calender
     *
     * @param day: date of the job will start according to the calender
     */
    void setDay(int day);

    /**
     * This method will return what hour the job will start
     *
     * @return Starting hour of job
     */
    int getHour();

    /**
     * This method will set hour according to employer interaction with clock
     *
     * @param hour: Starting hour depends on when did the employer set time to on clock UI
     */
    void setHour(int hour);

    /**
     * This method will return what minute the job will start
     *
     * @return Starting minute of job
     */
    int getMinute();

    /**
     * This method will set minute according to employer interaction with clock
     *
     * @param minute: Starting minute depends on when did the employer set time to on clock UI
     */
    void setMinute(int minute);

    /**
     * Return latitude of location which is suppose to be where employee will be working on.
     *
     * @return latitude of job location
     */
    double getLatitude();

    /**
     * Set latitude according to where is the job will be located
     *
     * @param latitude: come from LatLng object, but only Latitude will be set.
     */
    void setLatitude(double latitude);

    /**
     * Return longitude of location which is suppose to be where employee will be working on.
     *
     * @return longitude of the job location
     */
    double getLongitude();

    /**
     * Set longitude according to where is the job will be located
     *
     * @param longitude: come from LatLng object, but only Longitude will be set.
     */
    void setLongitude(double longitude);

    /**
     * Return ID of the employee(s) who apply for the job
     *
     * @return employee(s) ID who applied to the job separated with ", "
     */
    String getApplicantsID();

    /**
     * Set String applicantsID to a param String
     *
     * @param applicantsID: a String that have all of the applicants ID
     */
    void setApplicantsID(String applicantsID);

    /**
     * Return ID of the employee who is accepted for the position
     *
     * @return Accepted employee ID
     */
    String getAcceptedID();

    /**
     * This method will be call only once. Once the employer select which applicant
     * will be apply to the position this method will be called
     *
     * @param acceptedID: an employee ID who got accepted.
     */
    void setAcceptedID(String acceptedID);

    boolean isPaid();

    void setPaid(boolean paid);

    boolean isEmployeeFeedback();

    void setEmployeeFeedback(boolean employeeFeedback);

    boolean isEmployerFeedback();

    void setEmployerFeedback(boolean employerFeedback);


}
