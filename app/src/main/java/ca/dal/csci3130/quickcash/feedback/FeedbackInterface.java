package ca.dal.csci3130.quickcash.feedback;

public interface FeedbackInterface {

    void setId(String id);

    String getId();

    void setRating(int rating);

    int getRating();

    int getCount();

    void setCount(int count);
}
