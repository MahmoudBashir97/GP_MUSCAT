package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.loginpage.Inter_faces.downloadQuiz_Interface;
import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.adapters.assignment_Adapter;
import com.example.loginpage.databinding.ActivityDownloadQuizBinding;
import com.example.loginpage.models.AssignmentDataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadQuiz extends AppCompatActivity implements downloadQuiz_Interface {
    private ActivityDownloadQuizBinding quizBinding;
    assignment_Adapter adapter;
    String myId;
    List<AssignmentDataModel> mlist = new ArrayList<>();
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quizBinding = DataBindingUtil.setContentView(this,R.layout.activity_download_quiz);
        quizBinding.recDownloadFiles.setHasFixedSize(true);

        myId = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_ID();
        reference = FirebaseDatabase.getInstance().getReference("Assignments");

        adapter = new assignment_Adapter(this,mlist,this);
        quizBinding.recDownloadFiles.setAdapter(adapter);
        getAssignmentsList();
    }

    private void getAssignmentsList() {
        mlist.clear();
        reference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn: snapshot.getChildren()){
                        AssignmentDataModel model = sn.getValue(AssignmentDataModel.class);
                        mlist.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }

    @Override
    public void onClickFile(int index) {
        FirebaseApp.initializeApp(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        String httpUrl = mlist.get(index).getPdfUrl();
        String courseName = mlist.get(index).getCourseName();

        FirebaseUser user = auth.getCurrentUser();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Open and Download!");
        dialog.setPositiveButton("open",(dialog1, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mlist.get(index).getPdfUrl()));
            startActivity(intent);
        }).setNegativeButton("cancel",(dialog1, which) -> {
            dialog.setCancelable(true);
        });
        dialog.show();


        if (user != null) {
            //downloadFile(httpUrl,courseName);
        } else {
           // signInAnonymously(httpUrl,courseName);
        }
    }

    private void signInAnonymously(String url,String fileName) {
        auth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                downloadFile(url,fileName);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("mm", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    private void downloadFile(String url,String fileName) {
        Toast.makeText(this, "URL : "+url, Toast.LENGTH_SHORT).show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);

/*
        File rootPath = new File(Environment.getExternalStorageDirectory(), fileName);
*/
        try {
            File localFile =  File.createTempFile("AssignmentsFiles", ".pdf");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.e("firebase ",";local tem file created  created " +localFile.toString());
                    //  updateDb(timestamp,localFile.toString(),position);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ",";local tem file not created  created " +exception.toString());
                }
            });
        }catch (IOException e){

        }
        /*if(!rootPath.exists()) {
            rootPath.mkdirs();
        }*/

        //final File localFile = new File(rootPath,".pdf");

    }
}