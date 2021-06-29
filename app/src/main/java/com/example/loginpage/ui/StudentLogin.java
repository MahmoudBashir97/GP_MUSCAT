package com.example.loginpage.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class StudentLogin extends AppCompatActivity {
    Button student_login_btn,student_signup_btn;
    EditText edt_email,edt_pass;
    TextView txt_forgot_btn;
    FirebaseAuth auth;
    DatabaseReference reference;
    String randomKey="";
    ProgressBar pro_bar;
    String devicetoken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        student_signup_btn = findViewById(R.id.student_signup_btn);
        student_login_btn = findViewById(R.id.student_login_btn);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        pro_bar = findViewById(R.id.pro_bar);
        txt_forgot_btn = findViewById(R.id.txt_forgot_btn);


        FirebaseApp.initializeApp(getApplicationContext());
        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Registeration");

        student_signup_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),StudentSignup.class));
        });

        student_login_btn.setOnClickListener(v -> {
            if (isValidate()){
               doLogin(edt_email.getText().toString(),edt_pass.getText().toString());
            }
        });

        txt_forgot_btn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class).putExtra("path","student"));
        });

    }
    private boolean isValidate(){
        if (TextUtils.isEmpty(edt_email.getText().toString())){
            edt_email.setError("Please enter your email!");
            edt_email.requestFocus();
        }else  if (TextUtils.isEmpty(edt_pass.getText().toString()) || edt_pass.getText().toString().length()<6){
            edt_pass.setError("Please enter a valid password with more than 6 digits!");
            edt_pass.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    private void doLogin(String email,String pass){
        pro_bar.setVisibility(View.VISIBLE);
        reference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn : snapshot.getChildren()){
                        if (sn.hasChild("email")){
                            String _email = sn.child("email").getValue().toString();
                            String _pass = sn.child("password").getValue().toString();
                            String _name = sn.child("name").getValue().toString();
                            String _id = sn.child("id").getValue().toString();

                            if (email.equals(_email) && pass.equals(_pass)){
                                pro_bar.setVisibility(View.GONE);
                                navigateToDashboard();
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
                                        new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (task.isSuccessful()){
                                                    devicetoken=task.getResult();
                                                    SharedPrefranceManager.getInastance(getApplicationContext()).saveUser("doctor",_name,_email,_id,devicetoken);

                                                }
                                            }
                                        }
                                );
                            }else {
                                edt_email.setError("Invalid Inputs,Please enter a registered email!");
                                edt_pass.setError("Invalid Inputs,Please enter a registered password!");
                               pro_bar.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                e.getMessage();
            }
        });
    }
    private void navigateToDashboard(){
        startActivity(new Intent(getApplicationContext(), StudentDashboard.class)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}