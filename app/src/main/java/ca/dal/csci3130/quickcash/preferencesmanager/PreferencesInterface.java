package ca.dal.csci3130.quickcash.preferencesmanager;

/**
 * This call is contain all the methods the preference need to have, created in case
 * Getter and Setter method
 */
public interface PreferencesInterface {

    /**
     * setEmployeeID will set ID of the user
     *
     * @param id: Employee ID of the user
     */
    void setEmployeeID(String id);

    /**
     * return ID of the employee
     *
     * @return ID of the employee
     */
    String getEmployeeID();

    /**
     * set preferences job according to String entered
     *
     * @param job: preferred job
     */
    void setJob(String job);

    /**
     * get preferred job from local variable
     *
     * @return preferred job
     */
    String getJob();

    /**
     * set minimal salary to local variable
     *
     * @param salary: minimal salary
     */
    void setSalary(double salary);

    /**
     * get minimal salary from local variable
     *
     * @return minimal salary
     */
    double getSalary();

    /**
     * set max distance to local variable
     *
     * @param distance: max distance
     */
    void setMaxDistance(int distance);

    /**
     * return max distance from local variable
     *
     * @return max distance
     */
    int getMaxDistance();

    /**
     * set max duration to local variable
     *
     * @param duration: max duration
     */
    void setDuration(int duration);

    /**
     * get max duration from local variable
     *
     * @return max duration
     */
    int getDuration();

    /**
     * set starting hour to local variable
     *
     * @param hour: starting hour
     */
    void setStartingHour(int hour);

    /**
     * get starting hour from local variable
     *
     * @return starting hour
     */
    int getStartingHour();

    /**
     * set starting minute to local variable
     *
     * @param minute: starting minute
     */
    void setStartingMinute(int minute);

    /**
     * get starting minute from local variable
     *
     * @return starting minute
     */
    int getStartingMinute();

    /**
     * Combine starting hour and starting minute into a single String
     * by calling getStartingHour() and getStartingMinute()
     *
     * @return a string combined between starting hour and starting minute
     */
    String getStartingTime();
}
