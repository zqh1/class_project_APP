package ca.dal.csci3130.quickcash.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.UserDAO;
import ca.dal.csci3130.quickcash.usermanagement.UserDAOAdapter;

public class GiveFeedbackActivity extends AppCompatActivity {

    private String id;
    private TextView name;
    private Spinner ratingNumSpinner;

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        this.id = getIntent().getStringExtra("id");

        linkScreenItems();
        setNameOnScreen();
        setButtonListeners();
    }

    /**
     * This method will assign elements that present on the screen to each parameter
     */
    private void linkScreenItems(){
        name = findViewById(R.id.personNameForGivingRating);
        ratingNumSpinner = findViewById(R.id.ratingInput);

        submit = findViewById(R.id.submitButton);
    }

    /**
     * This method will set the name of the people who are being reviewed on the screen
     */
    private void setNameOnScreen(){
        DAO dao = new UserDAOAdapter(new UserDAO());
        dao.getDatabaseReference().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = Objects.requireNonNull(snapshot.child("firstName").getValue()).toString();
                String lastName = Objects.requireNonNull(snapshot.child("lastName").getValue()).toString();

                String statusText = firstName + " " + lastName;
                name.setText(statusText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GiveFeedbackActivity.this, "Error while reading Name from database", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Link the button from activity_give_feedback to local variable of this class
     */
    private void setButtonListeners(){
        submit.setOnClickListener(view -> collectInformation());
    }

    /**
     * this method will set the rating of the user 1 to 5
     */
    private void collectInformation(){
        int rating;
        if (ratingNumSpinner.getSelectedItem().toString().equals("1"))
            rating = 1;
        else if(ratingNumSpinner.getSelectedItem().toString().equals("2"))
            rating = 2;
        else if(ratingNumSpinner.getSelectedItem().toString().equals("3"))
            rating = 3;
        else if(ratingNumSpinner.getSelectedItem().toString().equals("4"))
            rating = 4;
        else
            rating = 5;

        updateOrCreateFeedback(rating);
    }

    /**
     * This method will create score for the user if they are first time to have a feedback
     * Or update existing user overall rating by adding score to the current and take average
     * Then back to the employer or employee home page
     * @param score : rating of the user
     */
    private void updateOrCreateFeedback(int score) {
        DatabaseReference db = new FeedbackDAOAdapter(new FeedbackDAO()).getDatabaseReference();
        db.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() == 1) {

                    DataSnapshot data = snapshot.getChildren().iterator().next();

                    FeedbackInterface feedbackToPush = Objects.requireNonNull(data.getValue(Feedback.class));

                    feedbackToPush.setRating(feedbackToPush.getRating() + score);
                    feedbackToPush.setCount(feedbackToPush.getCount() + 1);

                    db.child(Objects.requireNonNull(data.getKey())).getRef().setValue(feedbackToPush);
                } else pushFeedbackToFirebase(score);

                Toast.makeText(GiveFeedbackActivity.this, "Feedback has been updated", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error: feedback", error.getMessage());
            }
        });

        if(SessionManager.getUser().getIsEmployee().equals("y"))
            startActivity(new Intent(this, EmployeeHomeActivity.class));
        else
            startActivity(new Intent(this, EmployerHomeActivity.class));
    }

    /**
     * This method will allow application to push feedback result back to firebase database
     * @param score: general score of an employer
     */
    private void pushFeedbackToFirebase(int score){
        FeedbackInterface feedback = new Feedback();

        feedback.setId(id);
        feedback.setRating(score);
        feedback.setCount(1);

        new FeedbackDAOAdapter(new FeedbackDAO()).add(feedback);
    }
}
