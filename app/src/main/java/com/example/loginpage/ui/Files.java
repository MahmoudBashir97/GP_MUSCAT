package com.example.loginpage.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.MyDialog;
import com.example.loginpage.R;
import com.example.loginpage.adapters.Files_Adapter;
import com.example.loginpage.databinding.ActivityFilesBinding;
import com.example.loginpage.models.FilesDataModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Files extends AppCompatActivity {

    private ActivityFilesBinding filesBinding;
    DatabaseReference getFilesRef;
    private ProgressDialog dialog;
    String myId;
    Files_Adapter adapter;
    List<FilesDataModel> mlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filesBinding = DataBindingUtil.setContentView(this, R.layout.activity_files);

        myId = SharedPrefranceManager.getInastance(this).getUser_ID();
        getFilesRef = FirebaseDatabase.getInstance().getReference("Files");

        String userType = SharedPrefranceManager.getInastance(this).getRegist_Type();
        if (userType.equals("student")){
            filesBinding.AddFile.setVisibility(View.GONE);
        }else {
            filesBinding.AddFile.setVisibility(View.VISIBLE);
            addFilesbtn();
        }

        setupRecView();
        String registType = SharedPrefranceManager.getInastance(this).getRegist_Type();
        if (registType.equals("student")){
            getStudentsFilesList();
        }else {
            getDoctorsFilesList();
        }
    }
    private void getStudentsFilesList(){
        mlist.clear();
        getFilesRef.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn: snapshot.getChildren()){
                        FilesDataModel model = sn.getValue(FilesDataModel.class);
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
    private void getDoctorsFilesList() {
        mlist.clear();
        getFilesRef.child("Doctors").child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn: snapshot.getChildren()){
                        FilesDataModel model = sn.getValue(FilesDataModel.class);
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

    private void setupRecView() {
        adapter = new Files_Adapter(Files.this,mlist, index -> {
            String registType = SharedPrefranceManager.getInastance(this).getRegist_Type();
            if (registType.equals("student")){
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
            }
        });
        filesBinding.recUploadedFiles.setHasFixedSize(true);
        filesBinding.recUploadedFiles.setAdapter(adapter);
    }

    private void addFilesbtn(){
        filesBinding.AddFile.setOnClickListener(v -> {
            getPdfFile();
        });
    }

    private void getPdfFile() {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select PDF"), 1);
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
                dia.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_Files);
                dia.setListener((className,courseName,type)->addFiles(className,courseName,type,dat));
            }
        }
    }

    private void addFiles(String className,String courseName,String type,Uri fileUri){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading");
        dialog.setCancelable(false);
        dialog.show();

        final String timestamp = "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final String messagePushID = timestamp;
        String namePath = "Files /"+myId+"/";

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
                    uploadDataInfoToRealTimeDatabase(className,courseName,myurl,type);
                } else {
                    dialog.dismiss();
                    Toast.makeText(Files.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadDataInfoToRealTimeDatabase(String className,String courseName,String url,String type){
        DatabaseReference stRef,doctRef;
        stRef= FirebaseDatabase.getInstance().getReference("Files");
        doctRef= FirebaseDatabase.getInstance().getReference("Files");

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MM-dd-yyyy");
        String currentDatTime=currentDate.format(calendar.getTime());

        HashMap<String,Object> map = new HashMap<>();
        map.put("className",className);
        map.put("courseName",courseName);
        map.put("pdfUrl",url);
        map.put("date",currentDatTime);
        map.put("type",type);


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
                                                Toast.makeText(Files.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            }else {
                                                dialog.dismiss();
                                                Toast.makeText(Files.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                }
        );
    }

}
