package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.adapters.ViewpageAdapter;
import com.example.loginpage.fragments.DoctorsList_Fragment;
import com.example.loginpage.fragments.StudentsList_Fragment;
import com.example.loginpage.models.RecentMessagesIds_Model;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Chat extends AppCompatActivity {

    private TabLayout tablay;
    private ViewPager viewPager;
    ArrayList<RecentMessagesIds_Model> recentsList = new ArrayList<>();
    DatabaseReference RecentMessagesId;
    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tablay = findViewById(R.id.tablay);
        viewPager = findViewById(R.id.viewpage);

        myId = SharedPrefranceManager.getInastance(this).getUser_ID();

        RecentMessagesId = FirebaseDatabase.getInstance().getReference("RecentMessagesIds");
        getRecentMessagesIds();

        ViewpageAdapter adapter=new ViewpageAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentsList_Fragment(recentsList),"Students");
        adapter.addFragment(new DoctorsList_Fragment(recentsList),"Doctors");


        viewPager.setAdapter(adapter);
        tablay.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("actStatus : ","resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("actStatus : ","pause");
        getRecentMessagesIds();
    }
    private void getRecentMessagesIds(){

        recentsList.clear();
        RecentMessagesId.child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot sn :
                                snapshot.getChildren()) {
                            RecentMessagesIds_Model model = sn.getValue(RecentMessagesIds_Model.class);
                            recentsList.add(model);
                            Log.d("recentIds : ","gg "+recentsList.size());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }
}