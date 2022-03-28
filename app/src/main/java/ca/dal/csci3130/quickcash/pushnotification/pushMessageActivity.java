package ca.dal.csci3130.quickcash.pushnotification;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ca.dal.csci3130.quickcash.BuildConfig;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.common.DAO;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

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
            DAO.getUserReference().orderByChild("isEmployee").equalTo("y").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                            headers.put("authorization", "key=" + BuildConfig.FIREBASE_SERVER_KEY);
                            return headers;
                        }
                    };
                    requestQueue.add(request);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(pushMessageActivity.this, "Database connection error", Toast.LENGTH_LONG).show();
                }
            });


        }
        catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}