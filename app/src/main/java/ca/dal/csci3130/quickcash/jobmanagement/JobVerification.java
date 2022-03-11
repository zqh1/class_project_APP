package ca.dal.csci3130.quickcash.jobmanagement;

import ca.dal.csci3130.quickcash.common.DAO;

public class JobVerification {

    private JobInterface job;

    public void setJob(JobInterface job) {
        this.job = job;
    }

    public boolean[] verifyFields() {

        boolean[] fieldStatus = new boolean[] {true, true, true, true, true};
        boolean allFieldCorrect = true;

        if (!validTitle(job.getTitle())){
            fieldStatus[0] = false;
            allFieldCorrect = false;
        }

        if (!validDescription(job.getDescription())){
            fieldStatus[1] = false;
            allFieldCorrect = false;
        }

        if (!validDuration(job.getDuration())){
            fieldStatus[2] = false;
            allFieldCorrect = false;
        }

        if (!validSalary(job.getSalary())){
            fieldStatus[3] = false;
            allFieldCorrect = false;
        }

        if (!validTags(job.getTags())){
            fieldStatus[4] = false;
            allFieldCorrect = false;
        }

        if (allFieldCorrect) {
            pushJobToFirebase();
        }

        return fieldStatus;
    }

    public boolean validTitle(String title) {
        return !title.isEmpty() && title.length() <= 250;
    }

    public boolean validDescription(String description) {
        return !description.isEmpty() && description.length() <= 500;
    }

    public boolean validDuration(int duration) {
        return duration > 0 && duration <= 99;
    }

    public boolean validSalary(double salary) {
        //Minimum NS wage 13.35$ per hour
        return salary > 13.35 && salary <= 10000;
    }

    public boolean validTags(String tags) {
        return !tags.isEmpty() && tags.length() <= 500;
    }

    //Private method that push job into job manager to being push onto the database
    private void pushJobToFirebase() {
        DAO.add(job);
    }
}
