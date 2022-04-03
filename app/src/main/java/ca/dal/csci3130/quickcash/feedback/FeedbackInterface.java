package ca.dal.csci3130.quickcash.feedback;

/**
 * Interface for feedback function
 */
public interface FeedbackInterface {

    /**
     * set parameter to unique user id
     * @param id: User id
     */
    void setId(String id);

    /**
     * get user ID
     * @return user ID
     */
    String getId();

    /**
     * set feedback rating of the user
     * @param rating: rating for the user. Out of 5
     */
    void setRating(int rating);

    /**
     * get the rating for the user who post that job
     * @return rating of the user
     */
    int getRating();

    /**
     * get the number of times this user has been rated
     * @return : how many times this user has been rated
     */
    int getCount();

    /**
     * set how many times the user being review
     * @param count: how many times have someone give feedback to the person
     */
    void setCount(int count);
}
