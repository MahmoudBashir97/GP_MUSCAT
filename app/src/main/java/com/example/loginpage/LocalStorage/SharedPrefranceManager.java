package com.example.loginpage.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

public class SharedPrefranceManager {
    Context context;
    private static final String SHARED_PREF_USER = "GP_MUSCAT_app";

    private static SharedPrefranceManager sharedPrefranceManager;

    private SharedPrefranceManager(Context context) {
        this.context = context;
    }

    public synchronized static SharedPrefranceManager getInastance(Context context){
        if (sharedPrefranceManager == null){
            sharedPrefranceManager = new SharedPrefranceManager(context);
        }
        return sharedPrefranceManager;
    }


    //--------------- user -------------//
    public void saveUser(String regist_type,String name,String email,String id,String deviceToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

        editor.putString("RegistType", regist_type);
        editor.putString("UserName", name);
        editor.putString("UserEmail", email);
        editor.putString("UserId", id);
        editor.putString("DevToken", deviceToken);

        editor.putBoolean("userLogged", true);

        editor.apply();
    }

    public void saveListRecentIdsMessages(Set<String> recentIdsMessages) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String n:recentIdsMessages){
           Log.d("savedVal :","  "+ n);
        }
        editor.putStringSet("recentIds",recentIdsMessages);
        editor.apply();
        editor.commit();
    }

    public Set<String> getRecentIdsMessages(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet("recentIds",null);
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("DevToken", "");
    }

    public String getRegist_Type(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("RegistType", "");
    }
    public String getUser_ID(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserId", "");
    }

    public String getUser_Name(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserName", "");
    }

    public String getUser_Email(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserEmail", "");
    }


    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("userLogged", false);
    }

    public void clearUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("userLogged", false);
        editor.clear();
        editor.apply();
        editor.commit();
    }
}