package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.loginpage.Inter_faces.downloadQuiz_Interface;
import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.adapters.assignment_Adapter;
import com.example.loginpage.databinding.ActivityAddAssignmentBinding;
import com.example.loginpage.models.AssignmentDataModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddAssignment extends AppCompatActivity implements downloadQuiz_Interface {
    private ActivityAddAssignmentBinding assignmentBinding;
    private ProgressDialog dialog;
    private assignment_Adapter adapter;
    String myId;
    List<AssignmentDataModel> mlist = new ArrayList<>();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignmentBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_assignment);

        myId = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_ID();
        reference = FirebaseDatabase.getInstance().getReference("Assignments");


        assignmentBinding.uploadAssignment.setOnClickListener(v -> {
            getPdfFile();
        });
        adapter = new assignment_Adapter(this,mlist,this);
        assignmentBinding.recUploadedFiles.setHasFixedSize(true);
        assignmentBinding.recUploadedFiles.setAdapter(adapter);
        getAssignmentsList();
    }

    private void getAssignmentsList() {
        mlist.clear();
        reference.child("Doctors").child(myId).addValueEventListener(new ValueEventListener() {
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


    private void getPdfFile() {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select PDF"), 1);
    }

    private void addClass(String className,String courseName,Uri fileUri) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading");
        dialog.setCancelable(false);
        dialog.show();
        final String timestamp = "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final String messagePushID = timestamp;
        String namePath = "Assignments /"+myId+"/";

        // Here we are uploading the pdf in firebase storage with the name of current time
        final StorageReference filepath = storageReference.child(namePath+messagePushID + "." + "pdf");

        filepath.putFile(fileUri).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    Uri uri = task.getResult();
                    String myurl;
                    myurl = uri.toString();
                    uploadDataInfoToRealTimeDatabase(className,courseName,myurl);
                } else {
                    dialog.dismiss();
                    Toast.makeText(AddAssignment.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadDataInfoToRealTimeDatabase(String className,String courseName,String url){
        DatabaseReference stRef,doctRef;
        stRef= FirebaseDatabase.getInstance().getReference("Assignments");
        doctRef= FirebaseDatabase.getInstance().getReference("Assignments");

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MM-dd-yyyy");
        String currentDatTime=currentDate.format(calendar.getTime());

        HashMap<String,Object> map = new HashMap<>();
        map.put("className",className);
        map.put("courseName",courseName);
        map.put("pdfUrl",url);
        map.put("date",currentDatTime);


        doctRef.child("Doctors").child(myId).child(currentDatTime).updateChildren(map).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            stRef.child("Students")
                                    .child(currentDatTime)
                                    .updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText(AddAssignment.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    }else {
                                        dialog.dismiss();
                                        Toast.makeText(AddAssignment.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri dat = data.getData();
                String pathe = dat.getPath();
                // path = getpath(pathe);
                    MyDialog dia= new MyDialog();
                    dia.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_ASSIGNMENT);
                    dia.setListener((className,courseName)->addClass(className,courseName,dat));
            }
        }
    }

    @Override
    public void onClickFile(int index) {

    }
}