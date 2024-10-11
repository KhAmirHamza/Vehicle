package com.tripbd.driver.view;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tripbd.driver.R;

import java.util.HashMap;
import java.util.Map;

public class MyPushNotificationHandler {
    private static final String TAG = "MyPushNotificationHandl";

    static Activity activity;


    public MyPushNotificationHandler(Activity activity) {
        this.activity = activity;
        handle();
    }


    public static void handle(){
        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                Object value = activity.getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }


    }





    void createChannel(String channelId, String channelName){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri soundUri = Uri.parse(
                    "android.resource://" +
                            activity.getPackageName() +
                            "/" +
                            R.raw.notification);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();


            // Create channel to show notifications.
            NotificationManager notificationManager =
                    activity.getSystemService(NotificationManager.class);
            NotificationChannel nc = new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH);
            nc.setSound(soundUri, audioAttributes);
            notificationManager.createNotificationChannel(nc);
        }
    }

    void subscribeTopic(){
        Log.d(TAG, "Subscribing to weather topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //String msg = getString(R.string.msg_subscribed);
                        String msg = "Successfully Subscribed";
                        if (!task.isSuccessful()) {
                            //msg = getString(R.string.msg_subscribe_failed);
                            msg = "Failed to Subscribe";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END subscribe_topics]
    }

    public void getAndSaveToken(String driverID){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String,Object> fcmTokenMap = new HashMap<>();
                        Log.d(TAG, "onComplete: token: "+token);
                        fcmTokenMap.put("fcmToken", token);
                        db.collection("driver").document(driverID).update(fcmTokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //activity.startActivity(new Intent(activity, MainActivity.class));
                               // activity.finish();
                               // Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();

                            }
                        });


                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //String msg = "FCM Token: "+token;
                       // Log.d(TAG, msg);
                        // Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END log_reg_token]

    }

}
