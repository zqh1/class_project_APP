package ca.dal.csci3130.quickcash.preferencesmanager;

public class Preferences implements PreferencesInterface{

    private String employeeID = "";
    private String job = "";
    private double salary;
    private int startingHour;
    private int startingMinute;
    private int maxDistance;
    private int duration;

    /**
     * setEmployeeID will set ID of the user
     * @param employeeID: Employee ID of the user
     */
    public void setEmployeeID(String employeeID){
        this.employeeID = employeeID;
    }

    /**
     * return ID of the employee
     * @return ID of the employee
     */
    public String getEmployeeID(){
        return employeeID;
    }

    /**
     * set preferences job according to String entered
     * @param job: preferred job
     */
    public void setJob(String job){
        this.job = job;
    }

    /**
     * get preferred job from local variable
     * @return preferred job
     */
    public String getJob(){
        return job;
    }

    /**
     * set minimal salary to local variable
     * @param salary: minimal salary
     */
    public void setSalary(double salary){
        this.salary = salary;
    }

    /**
     * get minimal salary from local variable
     * @return minimal salary
     */
    public double getSalary(){
        return salary;
    }

    /**
     * set max distance to local variable
     * @param distance: max distance
     */
    public void setMaxDistance(int distance){
        this.maxDistance = distance;
    }

    /**
     * return max distance from local variable
     * @return max distance
     */
    public int getMaxDistance(){
        return maxDistance;
    }

    /**
     * set max duration to local variable
     * @param duration: max duration
     */
    public void setDuration(int duration){
        this.duration = duration;
    }

    /**
     * get max duration from local variable
     * @return max duration
     */
    public int getDuration(){
        return this.duration;
    }

    /**
     * set starting hour to local variable
     * @param hour: starting hour
     */
    public void setStartingHour(int hour){ this.startingHour = hour; }

    /**
     * get starting hour from local variable
     * @return starting hour
     */
    public int getStartingHour(){
        return this.startingHour;
    }

    /**
     * set starting minute to local variable
     * @param minute: starting minute
     */
    public void setStartingMinute(int minute){
        this.startingMinute = minute;
    }

    /**
     * get starting minute from local variable
     * @return starting minute
     */
    public int getStartingMinute(){
        return this.startingMinute;
    }

    /**
     * Combine starting hour and starting minute into a single String
     * by calling getStartingHour() and getStartingMinute()
     * @return a string combined between starting hour and starting minute
     */
    public String getStartingTime(){ return startingHour + ":" + startingMinute; }
}
