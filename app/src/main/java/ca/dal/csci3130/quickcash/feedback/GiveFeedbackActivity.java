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

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

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

    private void linkScreenItems(){
        name = findViewById(R.id.personNameForGivingRating);
        ratingNumSpinner = findViewById(R.id.ratingInput);

        submit = findViewById(R.id.submitButton);
    }

    private void setNameOnScreen(){
        DAO.getUserReference().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void setButtonListeners(){
        submit.setOnClickListener(view -> collectInformation());
    }

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

    private void updateOrCreateFeedback(int score) {
        DatabaseReference db = DAO.getFeedbackDatabase();
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

    private void pushFeedbackToFirebase(int score){
        FeedbackInterface feedback = new Feedback();

        feedback.setId(id);
        feedback.setRating(score);
        feedback.setCount(1);

        DAO.add(feedback);
    }
}