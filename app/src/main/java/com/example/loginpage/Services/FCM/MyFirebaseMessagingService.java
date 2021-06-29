package com.example.loginpage.Services.FCM;

import android.app.Notification;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.example.loginpage.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseMessaging";
    public static final String MESSAGE = "message";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().get("Type").equals("message")) {
            Log.d("checkingMessage :","success");
            String senderName = remoteMessage.getData().get("name").toString();

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification builder = new NotificationCompat.Builder(this,MESSAGE)
                    .setSmallIcon(R.drawable.chat_icon2)
                    .setPriority(PRIORITY_MAX)
                    .setContentTitle(String.format("You have new message from ", senderName))
                    .setVibrate(new long[3000])
                    .setChannelId(MESSAGE)
                    .setSound(alarmSound)
                    .build();

            NotificationManagerCompat managerCompat =NotificationManagerCompat.from(getApplicationContext());
            managerCompat.notify(200,builder);
        }else {
            Log.d("checkingMessage :","failed");
        }
    }
}