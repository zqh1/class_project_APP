package ca.dal.csci3130.quickcash.preferencesmanager;

public class Preferences implements PreferencesInterface{

    private String employeeID;
    private String job;
    private int salary;
    private String startingTime;
    private int maxDistance;
    private int duration;


    public void setEmployeeID(String employeeID){
        this.employeeID = employeeID;
    }

    public String getEmployeeID(){
        return employeeID;
    }

    public void setJob(String job){
        this.job = job;
    }

    public String getJob(){
        return job;
    }

    public void setSalary(int salary){
        this.salary = salary;
    }

    public int getSalary(){
        return salary;
    }

    public void setStartingTime(String startingTime){
        this.startingTime = startingTime;
    }

    public String getStartingTime(){
        return startingTime;
    }

    public void setMaxDistance(int distance){
        this.maxDistance = distance;
    }

    public int getMaxDistance(){
        return maxDistance;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public int getDuration(){
        return this.duration;
    }
}
