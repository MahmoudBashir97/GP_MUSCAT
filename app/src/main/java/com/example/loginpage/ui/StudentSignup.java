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

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.models.User_Data_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Random;

public class StudentSignup extends AppCompatActivity {
    Button student_signup_btn;
    EditText edt_fname,edt_lname,edt_email,edt_pass;
    FirebaseAuth auth;
    DatabaseReference reference;
    String randomKey="";
    ProgressBar pro_bar;
    String deviceToken= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        student_signup_btn = findViewById(R.id.student_signup_btn);
        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        pro_bar = findViewById(R.id.pro_bar);

        FirebaseApp.initializeApp(getApplicationContext());
        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Registeration");

        student_signup_btn.setOnClickListener(v -> {
            if (isValidate()){
                String name = edt_fname.getText() +" "+edt_lname.getText();
                String id = ""+getGenerateRandomKey(500,10000);
                User_Data_Model model = new User_Data_Model(
                        id,
                        name,
                       edt_email.getText().toString(),
                        edt_pass.getText().toString(),
                        ""
                );
                doSignup(model);
            }
        });


    }

    private void doSignup(User_Data_Model model) {
        pro_bar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(model.getEmail(),model.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                                studentRef(model);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pro_bar.setVisibility(View.GONE);
                e.getStackTrace();
            }
        });
    }

    private void studentRef(User_Data_Model model) {
        reference.child("Students").child(model.getId()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    pro_bar.setVisibility(View.GONE);
                    navigateToDashboard();
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
                            new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()){
                                        deviceToken=task.getResult();
                                        HashMap<String,Object> map = new HashMap<>();
                                        map.put("deviceToken",deviceToken);
                                        reference.child("Students").child(model.getId()).updateChildren(map);
                                        SharedPrefranceManager.getInastance(getApplicationContext()).saveUser("student",model.getName(),model.getEmail(),model.getId(),deviceToken);
                                    }
                                }
                            }
                    );
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getStackTrace();
                e.getMessage();
                pro_bar.setVisibility(View.GONE);
            }
        });
    }

    private void navigateToDashboard() {
            startActivity(new Intent(getApplicationContext(), StudentDashboard.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
    }

    private boolean isValidate(){
        if (TextUtils.isEmpty(edt_fname.getText().toString())){
            edt_fname.setError("Please enter your first name!");
            edt_fname.requestFocus();
        }else if (TextUtils.isEmpty(edt_lname.getText().toString())){
            edt_lname.setError("Please enter your last name!");
            edt_lname.requestFocus();
        }else if (TextUtils.isEmpty(edt_email.getText().toString())){
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

    public static int getGenerateRandomKey(int max,int min){

        Random rn = new Random();
        int n = max - min + 1;
        int i = rn.nextInt() % n;
        int randomNum =  min + i;

        return randomNum;
    }

}