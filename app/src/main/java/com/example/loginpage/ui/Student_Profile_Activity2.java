package com.example.loginpage.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Profile_Activity2 extends AppCompatActivity {
    String id ="";
    DatabaseReference reference;
    TextView txt_id,txt_name,txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__profile_2);

        txt_id = findViewById(R.id.txt_id);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);

        reference = FirebaseDatabase.getInstance().getReference("Registeration");
        id = SharedPrefranceManager.getInastance(this).getUser_ID();

        getStudentInfo();

    }

    private void getStudentInfo(){
        reference.child("Students").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                String id =snapshot.child("id").getValue().toString();
                String name =snapshot.child("name").getValue().toString();
                String email =snapshot.child("email").getValue().toString();
                txt_id.setText(id);
                txt_name.setText(name);
                txt_email.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }
}