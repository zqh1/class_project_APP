package ca.dal.csci3130.quickcash.pushnotification;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.BuildConfig;
import ca.dal.csci3130.quickcash.R;

public class pushMessageActivity extends AppCompatActivity {
    private static final String PUSH_NOTIFICATION_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
    private Button submit;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        init();
        setListeners();
    }

    private void init() {
        submit = findViewById(R.id.postBtn);
        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("Job");
    }

    private void setListeners() {
        submit.setOnClickListener(view -> sendNotification());
    }

    private void sendNotification() {
        try {
            final JSONObject notificationJSONBody = new JSONObject();
            notificationJSONBody.put("title", "New Job Created!");
            notificationJSONBody.put("body", "A new job is created in your city.");

            final JSONObject dataJSONBody = new JSONObject();
            dataJSONBody.put("detail", "A new job is created, come and check it.");

            final JSONObject pushNotificationJSONBody = new JSONObject();
            pushNotificationJSONBody.put("to", "/topics/User");
            pushNotificationJSONBody.put("notification", notificationJSONBody);
            pushNotificationJSONBody.put("data", dataJSONBody);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    PUSH_NOTIFICATION_ENDPOINT,
                    pushNotificationJSONBody,
                    response ->
                            Toast.makeText(pushMessageActivity.this,
                                    "Push notification sent.",
                                    Toast.LENGTH_SHORT).show(),
                    Throwable::printStackTrace) {
                @Override
                public Map<String, String> getHeaders() {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("content-type", "application/json");


                    //TODO
                    headers.put("authorization", "key=" + BuildConfig.);
                    return headers;
                }
            };

                    requestQueue.add(request);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}