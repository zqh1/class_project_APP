package ca.dal.csci3130.quickcash.feedback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import ca.dal.csci3130.quickcash.common.DAO;

/**
 * FeedbackDAOAdapter class which adapts getDatabaseReference and add methods based on instance
 * of Feedback
 */

public class FeedbackDAOAdapter extends DAO {

    private FeedbackDAO feedbackDAO = null;

    /**
     * FeedbackDAOAdapter constructor
     *
     * @param feedbackDAO: DAO for feedback
     */
    public FeedbackDAOAdapter(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }


    /**
     * getDatabaseReference method that adapts to return feedback database reference
     *
     * @return
     */
    @Override
    public DatabaseReference getDatabaseReference() {
        return feedbackDAO.getFeedbackDatabaseReference();
    }

    /**
     * add method that adapts to adds feedback to database when given a Feedback object
     *
     * @param object: object to be added to database : User, Feedback, Preference, Job
     * @return
     */
    @Override
    public Task<Void> add(Object object) {
        if (object instanceof Feedback) {
            Feedback feedback = (Feedback) object;
            return feedbackDAO.addFeedback(feedback);
        }

        return null;

    }

}
