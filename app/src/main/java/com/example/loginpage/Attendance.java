package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.adapters.Adapter_doctorAttendance;
import com.example.loginpage.models.StudentItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView,rec_lecturers;
    ClassAdapter classAdapter;
    Adapter_doctorAttendance doctorAtt_adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems =new ArrayList<>();
    Toolbar toolbar;
    ArrayList<StudentItem> mlist;
    private String myId;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        mlist = new ArrayList<>();
        myId = SharedPrefranceManager.getInastance(this).getUser_ID();
        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog() );
        reference = FirebaseDatabase.getInstance().getReference("Attendance");

        recyclerView =  findViewById(R.id.recyclerview);
        rec_lecturers =  findViewById(R.id.rec_lecturers);
        recyclerView.setHasFixedSize(true);
        rec_lecturers.setHasFixedSize(true);

        doctorAtt_adapter = new Adapter_doctorAttendance(this,mlist,(position, className, courseName) ->gotoItemActivity(position,className,courseName));
        classAdapter = new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        setToolBar();

        getLecturersList();
    }

    private void getLecturersList(){
        mlist.clear();
        reference.child("Doctors")
                .child(myId)
                .child("Lecturers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot sn:snapshot.getChildren()){
                                StudentItem item = sn.getValue(StudentItem.class);
                                mlist.add(item);
                            }
                            rec_lecturers.setAdapter(doctorAtt_adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.getMessage();
                    }
                });
    }
    private void setToolBar() {

        toolbar =findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);


        title.setText("Attendance");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);

        intent.putExtra("className",classItems.get(position).getClassName());
        intent.putExtra("courseName",classItems.get(position).getCourseName());
        intent.putExtra("position",position);
        startActivity(intent);
    }

    private void gotoItemActivity(int position,String class_Name,String course_Name) {
        Intent intent = new Intent(this,StudentActivity.class);

        intent.putExtra("className",class_Name);
        intent.putExtra("courseName",course_Name);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    private void showDialog(){
     MyDialog dialog = new MyDialog();
     dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
     dialog.setListener((className,courseName)->addClass(className,courseName));


    }

    private void addClass(String className,String courseName) {
        ClassItem classItem =new ClassItem(className,courseName);
         classItems.add(classItem);
         classAdapter.notifyDataSetChanged();


    }
}