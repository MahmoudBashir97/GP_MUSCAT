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

import com.example.loginpage.Attendance;
import com.example.loginpage.Chat;
import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.room.DbRepository;
import com.example.loginpage.room.MessageSchema;

import java.util.List;

public class TeacherDashBoard extends AppCompatActivity {

    private CardView teacher_to_chat_btn,teacher_to_files_btn,teacher_to_attendance_btn,teacher_to_quiz_btn,logout_btn;
    RelativeLayout rel_count;
    TextView txt_count;
    String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dash_borad);
        teacher_to_chat_btn = findViewById(R.id.teacher_to_chat_btn);
        teacher_to_files_btn = findViewById(R.id.teacher_to_files_btn);
        teacher_to_attendance_btn = findViewById(R.id.teacher_to_attendance_btn);
        teacher_to_quiz_btn = findViewById(R.id.teacher_to_quiz_btn);
        logout_btn = findViewById(R.id.logout_btn);
        rel_count = findViewById(R.id.rel_count);
        txt_count = findViewById(R.id.txt_count);

        myId = SharedPrefranceManager.getInastance(this).getUser_ID();


        navTo_AttendanceActivity();
        navToChatActivity();
        navToFilesActivity();
        navToQuizActivity();
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
            startActivity(new Intent(getApplicationContext(), Chat.class));
        });
    }

    private void navToFilesActivity(){
        teacher_to_files_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),Files.class));
        });
    }

    private void navTo_AttendanceActivity(){
        teacher_to_attendance_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Attendance.class));
        });
    }
    private void navToQuizActivity(){
        teacher_to_quiz_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AddAssignment.class));
        });
    }
}