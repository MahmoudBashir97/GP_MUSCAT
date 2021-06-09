package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.adapters.StudentAdapter;
import com.example.loginpage.adapters.StudentAttendee_Adapter;
import com.example.loginpage.models.StudentItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String courseName;
    private Integer position;
    private RecyclerView recyclerView,rec_studentsAttendee;
    private StudentAdapter adapter;
    private StudentAttendee_Adapter st_attendeeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private ArrayList<StudentItem> student_attendees = new ArrayList<>();

    DatabaseReference doctorAtt_Ref,studentAtt_Ref,lecturersRef;
    String myId;
    HashMap<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


       Intent intent =getIntent();

       className = intent.getStringExtra("className");
       courseName = intent.getStringExtra("courseName");
       position = intent.getIntExtra("position",-1);

        doctorAtt_Ref = FirebaseDatabase.getInstance().getReference("Attendance");
        studentAtt_Ref = FirebaseDatabase.getInstance().getReference("Attendance");
        lecturersRef = FirebaseDatabase.getInstance().getReference("Attendance");

       setToolBar();
       doInitialization();
        getSudentAttendeeList();
    }

    private void getSudentAttendeeList(){
        student_attendees.clear();
        lecturersRef.child("Lecturers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot sn : snapshot.getChildren()){
                                if (sn.child("doctor_id").getValue().toString().equals(myId)){
                                   lecturersRef.child("Lecturers")
                                           .child(className)
                                           .child("Students")
                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   if (snapshot.exists()){
                                                       for (DataSnapshot snp:snapshot.getChildren()){
                                                           StudentItem item = snp.getValue(StudentItem.class);
                                                           student_attendees.add(item);
                                                       }
                                                       st_attendeeAdapter = new StudentAttendee_Adapter(StudentActivity.this,student_attendees);
                                                       rec_studentsAttendee.setAdapter(st_attendeeAdapter);
                                                   }
                                               }
                                               @Override
                                               public void onCancelled(@NonNull DatabaseError error) {
                                                   error.getMessage();
                                               }
                                           });
                               }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.getMessage();
                    }
                });
    }
    private void doInitialization(){
        recyclerView = findViewById(R.id.Student_recycler);
        rec_studentsAttendee = findViewById(R.id.rec_studentsAttendee);
        recyclerView.setHasFixedSize(true);
        rec_studentsAttendee.setHasFixedSize(true);

        adapter= new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position->changeStatus(position));

        myId = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_ID();

        map = new HashMap<>();
        map.put("courseName",courseName);
        map.put("className",className);
        map.put("doctor_id",myId);
    }

    private void changeStatus(int position) {
      String status = studentItems.get(position).getStatus();

      if (status.equals("P")) status = "A";
      else status ="P";

      studentItems.get(position).setStatus(status);
      adapter.notifyItemChanged(position);
    }


    private void setToolBar() {

        toolbar =findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);


        title.setText(className);
        subtitle.setText(courseName);

        back.setOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMEnuItemClick(menuItem));
        save.setOnClickListener(v -> {
            if (studentItems.size() > 0) {
                saveDataAttendance();
                getSudentAttendeeList();
            }
        });
    }

    private void saveDataAttendance(){
        if (className != null){
            sendDoctorAttPath();
        }
    }
    private void sendDoctorAttPath(){
        doctorAtt_Ref.child("Doctors").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    for (StudentItem ele : studentItems) {
                        doctorAtt_Ref.child("Doctors")
                                .child(myId)
                                .child("Lecturers")
                                .child(className)
                                .setValue(ele).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d("saved data status ","saved successfully!");
                                    sendStudentAttPath();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    private void sendStudentAttPath(){
        studentAtt_Ref.child("Students").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    for (StudentItem ele : studentItems) {
                        studentAtt_Ref.child("Students")
                                .child(ele.getStudent_id())
                                .child("Lecturers")
                                .child(className)
                                .setValue(ele)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            sendLecturers();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
    private void sendLecturers(){
        lecturersRef.child("Lecturers")
                .child(className)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            for (StudentItem ele : studentItems) {
                                lecturersRef.child("Lecturers")
                                        .child(className)
                                        .child("Students")
                                        .child(ele.getStudent_id())
                                        .setValue(ele).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                Toast.makeText(StudentActivity.this, "Attendance Data sent Successfully!!", Toast.LENGTH_SHORT).show();
                                            }

                                    }
                                });
                            }
                        }
                    }
                });

    }

    private boolean onMEnuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        return true;
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListenerSt((roll,name,id)->addStudent(roll,name,id));
    }

    private void addStudent(String roll, String name,String id){
        String myId = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_ID();
        StudentItem item = new StudentItem(roll,name);
        item.setDoctor_id(myId);
        item.setStudent_id(id);
        studentItems.add(item);
        item.setClassName(className);
        item.setCourseName(courseName);
        item.setDate(getCurrentDate());
        adapter.notifyDataSetChanged();
    }
    private String getCurrentDate(){
        String dateStr = "04/05/2010";

        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        String newDateStr = curFormater.format(new Date());
        return newDateStr;
    }
}