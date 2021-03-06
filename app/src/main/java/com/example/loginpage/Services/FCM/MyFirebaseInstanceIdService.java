package com.example.loginpage.Services.FCM;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseInstanceId";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(LOG_TAG, "onTokenRefresh: ");
       // String refreshedToken = FirebaseInstanceIdInternal.NewTokenListener(onNewToken();).getInstance().getToken();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return;
        }

    }
}
