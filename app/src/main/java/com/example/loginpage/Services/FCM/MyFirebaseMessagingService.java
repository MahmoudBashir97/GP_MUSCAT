package com.example.loginpage.Services.FCM;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.example.loginpage.R;
import com.example.loginpage.Services.MyReceiver;
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

            String senderName = remoteMessage.getData().get("name");
            String messageContent = remoteMessage.getData().get("Message");
            String messageFrom = remoteMessage.getData().get("From");
            String messageTo = remoteMessage.getData().get("to");
            String messageReceiverToken = remoteMessage.getData().get("receiverToken");
            Log.d("checkingMessage :","success"+messageReceiverToken);




            Intent reciveMessage = new Intent(getApplicationContext(), MyReceiver.class)
                    .setAction("message")
                    .putExtra("_id",messageFrom)
                    .putExtra("_name",senderName)
                    .putExtra("userToken",messageReceiverToken);

            PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 2, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification builder = new NotificationCompat.Builder(this,MESSAGE)
                    .setSmallIcon(R.drawable.chat_icon2)
                    .setPriority(PRIORITY_MAX)
                    .setContentTitle(senderName)
                    .setContentText(messageContent)
                    .setVibrate(new long[3000])
                    .setChannelId(MESSAGE)
                    .setSound(alarmSound)
                    .setContentIntent(pendingIntentAccept)
                    .build();

            NotificationManagerCompat managerCompat =NotificationManagerCompat.from(getApplicationContext());
            managerCompat.notify(200,builder);
        }else {
            Log.d("checkingMessage :","failed");
        }
    }
}