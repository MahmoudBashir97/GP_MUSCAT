package com.example.loginpage.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.loginpage.Chat;
import com.example.loginpage.DownloadQuiz;
import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.room.DbRepository;
import com.example.loginpage.room.MessageSchema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentDashboard extends AppCompatActivity {
    private CardView student_to_chat_btn,student_to_files_btn,student_to_attendance_btn,student_to_quiz_btn,logout_btn,student_to_profile_btn;
    RelativeLayout rel_count;
    TextView txt_count;
    String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        student_to_chat_btn = findViewById(R.id.student_to_chat_btn);
        student_to_files_btn = findViewById(R.id.student_to_files_btn);
        student_to_attendance_btn = findViewById(R.id.student_to_attendance_btn);
        student_to_quiz_btn = findViewById(R.id.student_to_quiz_btn);
        student_to_profile_btn = findViewById(R.id.student_to_profile_btn);
        logout_btn = findViewById(R.id.logout_btn);
        rel_count = findViewById(R.id.rel_count);
        txt_count = findViewById(R.id.txt_count);

        myId = SharedPrefranceManager.getInastance(this).getUser_ID();


        navTo_AttendanceActivity();
        navToChatActivity();
        navToFilesActivity();
        navToQuizActivity();
        navToProfileActivity();
        doLogout();
        getRecentMessagesIdCount();

    }

    private void getRecentMessagesIdCount(){
        DbRepository repository = new DbRepository(getApplication());
        repository.getAllRecentMessages().observe(this, new Observer<List<MessageSchema>>() {
            @Override
            public void onChanged(List<MessageSchema> messageSchemas) {
                if (messageSchemas.size() > 0){
                  for (MessageSchema schema : messageSchemas){
                if (schema.getId() != Integer.parseInt(myId)){
                        Log.d("idcomparer : ","myId : "+myId +" "+"schemaId : "+schema.getId());
                            rel_count.setVisibility(View.VISIBLE);
                            int count = messageSchemas.size();
                            txt_count.setText(count+"");
                        }
                  }
                }

            }
        });
    }

    private void navToProfileActivity(){
        student_to_profile_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Student_Profile_Activity2.class));
        });
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

        student_to_chat_btn.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), Chat.class));
        });
    }

    private void navToFilesActivity(){
        student_to_files_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Files.class));
        });
    }

    private void navTo_AttendanceActivity(){
            student_to_attendance_btn.setOnClickListener(v -> {
                startActivity(new Intent(getApplicationContext(), StudentAttendance_Activity.class));
            });
    }
    private void navToQuizActivity(){
                student_to_quiz_btn.setOnClickListener(v -> {
                    startActivity(new Intent(getApplicationContext(), DownloadQuiz.class));
                });
    }

}