package com.example.loginpage.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.loginpage.ui.MessagesChat_Activity;

import androidx.core.app.NotificationManagerCompat;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
        String destName = intent.getStringExtra("_name");
        String destToken = intent.getStringExtra("userToken");
        String destId = intent.getStringExtra("_id");

        if (intent.getAction().equals("message")) {
            context.startActivity(new Intent(context, MessagesChat_Activity.class)
                    .putExtra("_id",destId)
                    .putExtra("_name",destName)
                    .putExtra("userToken",destToken)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK));
            NotificationManagerCompat managerCompat =NotificationManagerCompat.from(context);
            managerCompat.cancel(200);

        }

    }
}
