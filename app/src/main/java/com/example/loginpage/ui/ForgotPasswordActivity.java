package com.example.loginpage.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.loginpage.R;
import com.example.loginpage.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding forgotBinding;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference reference,updateRef;
    private String path="";
    boolean check=false;
    boolean ch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotBinding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        getDataFromIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Registeration");
        updateRef = FirebaseDatabase.getInstance().getReference("Registeration");


        forgotBinding.setIsSigned(false);

        forgotBinding.saveBtn.setOnClickListener(v -> {

            if (TextUtils.isEmpty(forgotBinding.edtEmail.getText().toString())){
                forgotBinding.edtEmail.setError("Please Enter Your Email!");
                forgotBinding.edtEmail.requestFocus();
            }else {
                forgotBinding.setIsLoading(true);
                String email = forgotBinding.edtEmail.getText().toString();
                String btnNamed = forgotBinding.saveBtn.getText().toString();
                if (btnNamed.equals("Next")){
                    checkPath(path,email);
                }else {
                    String newPass = forgotBinding.edtPass.getText().toString();
                    saveNewPassword(path,newPass,email);
                }
            }
        });
    }

    private void getDataFromIntent(){
        path = getIntent().getStringExtra("path");
    }

    private void checkPath(String p,String email){
        if (p.equals("student")){
            studentOrDoctorRef("Students",email);
        }else {
            studentOrDoctorRef("Doctors",email);
        }
    }

    private void saveNewPassword( String path, String newPass, String email){
        forgotBinding.setIsLoading(false);
        if (path.equals("student")){
            addNewPassForThePath("Students",newPass,email);
        }else {
            addNewPassForThePath("Doctors",newPass,email);
        }
    }

    private void addNewPassForThePath(String _child,String newPass,String email){
        reference.child(_child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn:snapshot.getChildren()) {
                        String _email = sn.child("email").getValue().toString();
                        if (email.equals(_email)){
                            String id = sn.child("id").getValue().toString();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("password",newPass);
                            updateRef.child(_child).child(id).updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                forgotBinding.setIsLoading(false);
                                                onBackPressed();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
                Toast.makeText(getApplicationContext(), "some error occurred while update new password!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void studentOrDoctorRef(String _child,String email)
    {
        reference.child(_child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn:snapshot.getChildren()) {
                        String _email = sn.child("email").getValue().toString();
                        if (email.equals(_email)){
                            forgotBinding.setIsSigned(true);
                            forgotBinding.saveBtn.setText("Save");
                            forgotBinding.setIsLoading(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
                forgotBinding.setIsSigned(false);
                forgotBinding.setIsLoading(false);
            }
        });
    }
}