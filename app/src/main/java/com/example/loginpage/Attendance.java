package com.example.loginpage;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems =new ArrayList<>();
    Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);


        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog() );



        recyclerView =  findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        classAdapter = new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        setToolBar();


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