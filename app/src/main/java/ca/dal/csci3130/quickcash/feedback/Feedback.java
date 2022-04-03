package ca.dal.csci3130.quickcash.feedback;

public class Feedback implements FeedbackInterface {

    private String id = "";
    private int rating = -1;
    private int count = 0;

    public Feedback(){ /* Require for firebase */ }

    /**
     * This method will return user ID
     * @return user id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set parameter to User ID
     * @param id: user ID
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method will return rating once its called
     * @return rating of employer
     */
    @Override
    public int getRating() {
        return rating;
    }

    /**
     * This method will set rating to parm
     * @param rating: Rating to employer given by employee
     */
    @Override
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * This method will return the number that this employer has been rated
     * @return general rating of the employer
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * This method will set the times that this employer was rated
     * @param count: time the employer being given a feedback
     */
    @Override
    public void setCount(int count) {
        this.count = count;
    }
}
