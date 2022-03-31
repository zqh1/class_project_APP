package ca.dal.csci3130.quickcash.feedback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;


public class FeedbackDAOAdapter extends DAO {

    private FeedbackDAO feedbackDAO = null;

    public FeedbackDAOAdapter(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    @Override
    public DatabaseReference getDatabaseReference() {
        return feedbackDAO.getFeedbackDatabaseReference();
    }

    @Override
    public Task<Void> add(Object object) {
        if (object instanceof Feedback) {
            Feedback feedback = (Feedback) object;
            return feedbackDAO.addFeedback(feedback);
        }

        return null;

    }

}
