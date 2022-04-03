package ca.dal.csci3130.quickcash.jobmanagement;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.BuildConfig;

public class JobVerification {

    private JobInterface job;
    private static final String PUSH_NOTIFICATION_ENDPOINT = "https://fcm.googleapis.com/fcm/send";


    /**
     * Set job variable to param
     *
     * @param job: a JobInterface to be test for verification
     */
    public void setJob(JobInterface job) {
        this.job = job;
    }

    /**
     * This method will test each element of JobInterface to check if it's a valid element
     * Once all element has been tested and all field are still true, the job will be pushed to DB
     *
     * @return array of boolean for each field test
     */
    public boolean[] verifyFields(RequestQueue requestQueue) {

        boolean[] fieldStatus = new boolean[]{true, true, true, true, true};
        boolean allFieldCorrect = true;

        if (!validTitle(job.getTitle())) {
            fieldStatus[0] = false;
            allFieldCorrect = false;
        }

        if (!validDescription(job.getDescription())) {
            fieldStatus[1] = false;
            allFieldCorrect = false;
        }

        if (!validDuration(job.getDuration())) {
            fieldStatus[2] = false;
            allFieldCorrect = false;
        }

        if (!validSalary(job.getSalary())) {
            fieldStatus[3] = false;
            allFieldCorrect = false;
        }

        if (!validTags(job.getTags())) {
            fieldStatus[4] = false;
            allFieldCorrect = false;
        }

        if (allFieldCorrect) {
            pushJobToFirebase();
            sendNotification(requestQueue);
        }

        return fieldStatus;
    }

    /**
     * This method will check if the title of the job is valid
     * For it to return true, the field need to be not empty and number of letters is less than 251
     *
     * @param title: a String from job.getTitle()
     * @return true if requirement is fulfil, false otherwise.
     */
    public boolean validTitle(String title) {
        return !title.isEmpty() && title.length() <= 250;
    }

    /**
     * This method will check description of the job is valid
     * For it to return true,description need to be not empty and number of letters less than 501
     *
     * @param description: a String from job.getDescription()
     * @return true if requirement is fulfil, false otherwise.
     */
    public boolean validDescription(String description) {
        return !description.isEmpty() && description.length() <= 500;
    }

    /**
     * This method will check if the duration of the job is valid
     * For it to return true, duration has to be high than 99
     *
     * @param duration: a int from job.getDuration()
     * @return true if requirement is fulfil, false otherwise.
     */
    public boolean validDuration(int duration) {
        return duration > 0 && duration <= 99;
    }

    /**
     * This method will check if salary is valid
     * Minimal required salary is 13.35, and max is 10,000
     *
     * @param salary: a double from job.getSalary()
     * @return true if requirement is fulfil, false otherwise.
     */
    public boolean validSalary(double salary) {
        //Minimum NS wage 13.35$ per hour
        return salary > 13.35 && salary <= 10000.0;
    }

    /**
     * This method will check if tags are valid
     * Tags should not be empty, and number of letters less than 501
     *
     * @param tags: a String from job.getTags();
     * @return true if requirement is fulfil, false otherwise.
     */
    public boolean validTags(String tags) {
        return !tags.isEmpty() && tags.length() <= 500;
    }

    //Private method that push job into job manager to being push onto the database
    private void pushJobToFirebase() {
        new JobDAOAdapter(new JobDAO()).add(job);
    }

    /**
     * This method will allow send a notification to employees when there is a new job created.
     * it will push a toast to employer to mention notification send to employee
     * meanwhile employees will receive a notification on their phone
     */
    private void sendNotification(RequestQueue requestQueue) {
        try {

            final JSONObject notificationJSONBody = new JSONObject();
            notificationJSONBody.put("title", "New Job Created!");
            Gson gson = new Gson();
            String json = gson.toJson(job);
            notificationJSONBody.put("body", json);

            final JSONObject pushNotificationJSONBody = new JSONObject();
            pushNotificationJSONBody.put("to","/topics/Employees");
            pushNotificationJSONBody.put("notification", notificationJSONBody);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    PUSH_NOTIFICATION_ENDPOINT,
                    pushNotificationJSONBody,
                    response -> {},
                    Throwable::printStackTrace) {
                @Override
                public Map<String, String> getHeaders() {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("content-type", "application/json");
                    headers.put("authorization", "key=" + BuildConfig.FIREBASE_SERVER_KEY);
                    return headers;
                }
            };
            requestQueue.add(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
