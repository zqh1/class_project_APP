package ca.dal.csci3130.quickcash.feedback;

public class Feedback implements FeedbackInterface {

    private String id = "";
    private int rating = -1;
    private int count = 0;

    public Feedback(){ /* Require for firebase */ }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}
