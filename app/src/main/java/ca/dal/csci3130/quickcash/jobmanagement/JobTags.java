package ca.dal.csci3130.quickcash.jobmanagement;

public class JobTags {

    //Initial tags
    public static final JobTags MANUAL = new JobTags("MANUAL");
    public static final JobTags FULL_TIME = new JobTags("FULL_TIME");
    public static final JobTags HALF_TIME = new JobTags("HALF_TIME");
    public static final JobTags TEMPORAL = new JobTags("TEMPORAL");
    public static final JobTags URGENT = new JobTags("URGENT");
    public static final JobTags CS = new JobTags("CS");

    //Add more tags as required


    private final String tag;

    public JobTags(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
