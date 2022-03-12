package ca.dal.csci3130.quickcash.preferencesmanager;

public class Preferences implements PreferencesInterface{

    private String employeeID = "";
    private String job = "";
    private double salary;
    private int startingHour;
    private int startingMinute;
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

    public void setSalary(double salary){
        this.salary = salary;
    }

    public double getSalary(){
        return salary;
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

    public void setStartingHour(int hour){ this.startingHour = hour; }

    public int getStartingHour(){
        return this.startingHour;
    }

    public void setStartingMinute(int minute){
        this.startingMinute = minute;
    }

    public int getStartingMinute(){
        return this.startingMinute;
    }

    public String getStartingTime(){ return startingHour + ":" + startingMinute; }
}
