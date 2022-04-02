package ca.dal.csci3130.quickcash.feedback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.dal.csci3130.quickcash.common.Constants;


public class FeedbackDAO {

    //Firebase database reference
    private final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance(Constants.FIREBASE_URL);

    private final DatabaseReference FEEDBACK_DATABASE = FIREBASE_DATABASE.getReference(Feedback.class.getSimpleName());

    /**
     * getFeedbackDatabaseReference retrieves feedback database reference
     *
     * @return: returns feedback database reference
     */
    public DatabaseReference getFeedbackDatabaseReference() {
        return FEEDBACK_DATABASE;
    }

    /**
     * addFeedback method adds feedback to the database
     *
     * @param feedback: feedback to be added to the database
     * @return
     */
    public Task<Void> addFeedback(FeedbackInterface feedback) {
        return FEEDBACK_DATABASE.push().setValue(feedback);
    }
}
