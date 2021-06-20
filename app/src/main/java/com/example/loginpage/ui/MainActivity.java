package com.example.loginpage.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;

public class MainActivity extends AppCompatActivity {
private Button teacher_btn,student_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefranceManager.getInastance(getApplicationContext()).isLoggedIn()){
            String path = SharedPrefranceManager.getInastance(getApplicationContext()).getRegist_Type();
            if (path.equals("student")){
                navigateToDashboard("student");
            }else {
                navigateToDashboard("doctor");
            }

        }
        setContentView(R.layout.activity_main);

        teacher_btn = (Button) findViewById(R.id.teacher_btn);
        student_btn = (Button) findViewById(R.id.student_btn);
        teacher_btn.setOnClickListener(v -> {
                openTeacherLogin();

        });

        student_btn.setOnClickListener(v -> {
            openStudentLogin();
        });
    }
            public void openTeacherLogin() {

                Intent intent = new Intent(this,TeacherLogin.class );
                startActivity(intent);
            }

           public void openStudentLogin() {

                Intent intent = new Intent(this,StudentLogin.class );
                startActivity(intent);
           }

    private void navigateToDashboard(String path) {
        if (path.equals("student")){
        startActivity(new Intent(getApplicationContext(), StudentDashboard.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
        }else {
            startActivity(new Intent(getApplicationContext(), TeacherDashBoard.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }
}