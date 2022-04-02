package ca.dal.csci3130.quickcash.pushnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.jobmanagement.Job;
import ca.dal.csci3130.quickcash.preferencesmanager.PreferenceDAO;
import ca.dal.csci3130.quickcash.preferencesmanager.PreferenceDAOAdapter;
import ca.dal.csci3130.quickcash.preferencesmanager.Preferences;
import ca.dal.csci3130.quickcash.preferencesmanager.PreferencesInterface;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    /**
     * This method will control the the notification
     * when we use this message it will receive a message and then push to the phone
     *
     * @param message receive from firebase data base cloud message
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        // If the notification message received is null, return.
        if (message.getNotification() == null) {
            return;
        }

        // Extract fields from the notification message.
        final String title = message.getNotification().getTitle();
        final String body = message.getNotification().getBody();


        Gson gson = new GsonBuilder().create();
        Job job = gson.fromJson(body, Job.class);

        // Create an intent to start activity when the notification is clicked.
        Intent intent = new Intent(this, EmployeeHomeActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 10, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a notification that will be displayed in the notification tray.
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "JOBS")
                        .setContentTitle(title)
                        .setContentText("A new job is created in your city.")
                        .setSmallIcon(com.google.firebase.messaging.R.drawable.gcm_icon);

        // Add the intent to the notification.
        notificationBuilder.setContentIntent(pendingIntent);

        // Notification manager to display the notification.
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();

        // If the build version is greater than, put the notification in a channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("JOBS", "JOBS", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        //Connect to firebase so that it will compare and send notification to employee with same preference

        new PreferenceDAOAdapter(new PreferenceDAO()).getDatabaseReference().orderByChild("employeeID").equalTo(SessionManager.getUserID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            PreferencesInterface preferences = data.getValue(Preferences.class);
                            int duration = preferences.getDuration();
                            double salary = preferences.getSalary();
                            String title = preferences.getJob();
                            String startTime = preferences.getStartingTime();
                            String jobStartTime = job.getHour() + ":" + job.getMinute();
                            if (duration >= job.getDuration() || salary <= job.getSalary() || title.equalsIgnoreCase(job.getTitle()) || startTime.equals(jobStartTime)) {
                                notificationManager.notify(id, notificationBuilder.build());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Not possible to catch background errors
                    }
                });
    }
}
