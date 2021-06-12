package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.loginpage.adapters.ViewpageAdapter;
import com.example.loginpage.fragments.DoctorsList_Fragment;
import com.example.loginpage.fragments.StudentsList_Fragment;
import com.google.android.material.tabs.TabLayout;


public class Chat extends AppCompatActivity {

    private TabLayout tablay;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tablay = findViewById(R.id.tablay);
        viewPager = findViewById(R.id.viewpage);


        ViewpageAdapter adapter=new ViewpageAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentsList_Fragment(),"Students");
        adapter.addFragment(new DoctorsList_Fragment(),"Doctors");


        viewPager.setAdapter(adapter);
        tablay.setupWithViewPager(viewPager);

    }
}