package ca.dal.csci3130.quickcash.feedback;

public class Feedback implements FeedbackInterface {

    private String id;
    private int rating;
    private int count;

    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return id;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public int getRating(){
        return rating;
    }

    public void setNumberOfSubmit(int count){
        this.count = count;
    }

    public int getNumberOfSubmit(){
        return count;
    }
}
