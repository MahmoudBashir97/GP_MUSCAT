package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.ui.MainActivity;

public class TeacherDashBoard extends AppCompatActivity {

    private CardView teacher_to_chat_btn,teacher_to_files_btn,teacher_to_attendance_btn,teacher_to_quiz_btn,logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dash_borad);
        teacher_to_chat_btn = findViewById(R.id.teacher_to_chat_btn);
        teacher_to_files_btn = findViewById(R.id.teacher_to_files_btn);
        teacher_to_attendance_btn = findViewById(R.id.teacher_to_attendance_btn);
        teacher_to_quiz_btn = findViewById(R.id.teacher_to_quiz_btn);
        logout_btn = findViewById(R.id.logout_btn);

        navTo_AttendanceActivity();
        navToChatActivity();
        navToFilesActivity();
        navToQuizActivity();

        doLogout();

    }
    private void doLogout(){
        logout_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            SharedPrefranceManager.getInastance(getApplicationContext()).clearUser();
        });
    }

    private void navToChatActivity(){
        teacher_to_chat_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),Chat.class));
        });
    }

    private void navToFilesActivity(){
        teacher_to_files_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),Files.class));
        });
    }

    private void navTo_AttendanceActivity(){
        teacher_to_attendance_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),Attendance.class));
        });
    }
    private void navToQuizActivity(){
        teacher_to_quiz_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),AddAssignment.class));
        });
    }
}