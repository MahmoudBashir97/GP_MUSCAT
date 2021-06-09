package com.example.loginpage.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.models.StudentItem;
import com.example.loginpage.adapters.Adapter_StudentAttendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentAttendance_Activity extends AppCompatActivity {
    private Adapter_StudentAttendance adapter_st;
    private RecyclerView rec_attendance;
    private ProgressBar prog_bar;
    DatabaseReference reference;
    ArrayList<StudentItem> mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_);
        mlist = new ArrayList<>();
        rec_attendance = findViewById(R.id.rec_attendance);
        prog_bar = findViewById(R.id.prog_bar);
        adapter_st = new Adapter_StudentAttendance(this,mlist);
        reference = FirebaseDatabase.getInstance().getReference("Attendance");


        getRefData();
    }
    private void getRefData(){
        prog_bar.setVisibility(View.VISIBLE);
        String myId = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_ID();
        mlist.clear();
        reference.child("Students")
                .child(myId)
                .child("Lecturers")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn : snapshot.getChildren()) {
                       StudentItem item = sn.getValue(StudentItem.class);
                       mlist.add(item);
                    }
                    if (mlist.size()>0){
                        prog_bar.setVisibility(View.GONE);
                        rec_attendance.setVisibility(View.VISIBLE);
                        rec_attendance.setAdapter(adapter_st);
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