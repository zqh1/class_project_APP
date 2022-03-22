package ca.dal.csci3130.quickcash.feedback;

public interface FeedbackInterface {

    void setID(String id);

    String getID();

    void setRating(int rating);

    int getRating();

    void setNumberOfSubmit(int count);

    int getNumberOfSubmit();

}
