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

public class GiveFeedbackActivity extends AppCompatActivity {

    private String id;
    private TextView name;
    private Spinner ratingNumSpinner;
    private int rating;

    private Button submit;

    private FeedbackInterface feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");

        feedback = new Feedback();

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

        feedback.setID(id);
        feedback.setRating(rating);
        feedback.setNumberOfSubmit(1);

        updateOrCreateFeedback();
    }

    private void updateOrCreateFeedback() {
        DatabaseReference db = DAO.getPreferenceReference();
        db.orderByChild("id").equalTo(feedback.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() == 1) {

                    try {
                        int getRatingFromDB = Integer.parseInt(Objects.requireNonNull(snapshot.child("rating").getValue()).toString());
                        feedback.setRating(rating + getRatingFromDB);
                    }
                    catch (Exception e){
                        feedback.setRating(feedback.getRating());
                    }

                    try {
                        int getCountFromDB = Integer.parseInt(Objects.requireNonNull(snapshot.child("count").getValue()).toString());
                        feedback.setRating(1 + getCountFromDB);
                    }
                    catch (Exception e){
                        feedback.setNumberOfSubmit(feedback.getNumberOfSubmit());
                    }

                    for (DataSnapshot data : snapshot.getChildren()) {
                        db.child(Objects.requireNonNull(data.getKey())).getRef().setValue(feedback);
                    }
                } else pushFeedbackToFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error: feedback", error.getMessage());
            }
        });
    }

    private void pushFeedbackToFirebase(){
        DAO.add(feedback);
    }

}
