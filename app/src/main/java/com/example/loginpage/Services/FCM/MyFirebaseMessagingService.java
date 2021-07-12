package com.example.loginpage.Services.FCM;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.Services.MyReceiver;
import com.example.loginpage.room.DbRepository;
import com.example.loginpage.room.MessageSchema;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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


            addLocalRecentMessagesId(messageFrom,senderName,messageReceiverToken);


            Intent reciveMessage = new Intent(getApplicationContext(), MyReceiver.class)
                    .setAction("message")
                    .putExtra("_id",messageFrom)
                    .putExtra("_name",senderName)
                    .putExtra("userToken",messageReceiverToken);

            PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 2, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
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

    private void addLocalRecentMessagesId(String messageFrom, String senderName, String messageReceiverToken) {
        MessageSchema schema = new MessageSchema(Integer.parseInt(messageFrom),
                senderName,
                messageReceiverToken);
        DbRepository repository = new DbRepository(getApplication());
        repository.insert(schema).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d("insertId : ","completedSuccess");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.getMessage();
                    }
                });
    }
}