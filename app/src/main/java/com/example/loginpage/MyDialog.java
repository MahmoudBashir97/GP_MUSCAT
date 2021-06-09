package com.example.loginpage;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment{
    public static final String CLASS_ADD_DIALOG="addClass";
    public static final String STUDENT_ADD_DIALOG="addStudent";

    private  OnClickListener listener;
    private  OnClickListenerSt listenerSt;
    public interface OnClickListener{
        void onClick(String Text1, String Text2);
    }

    public interface OnClickListenerSt{
        void onClick(String Text1, String Text2,String id);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setListenerSt(OnClickListenerSt listener) {
        this.listenerSt = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog=null;
        if (getTag().equals(CLASS_ADD_DIALOG))dialog=getAddClassDialog();
        if (getTag().equals(STUDENT_ADD_DIALOG))dialog=getAddStudentDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);


        builder.setView(view);

        TextView title = view.findViewById(R.id.TitleDialog);
        title.setText("Add New Student");

        EditText roll_edt = view.findViewById(R.id.edt01);
        EditText name_edt = view.findViewById(R.id.ed02);
        EditText iD_edt = view.findViewById(R.id.ed03);

        roll_edt.setHint("Roll");
        name_edt.setHint("Name");
        iD_edt.setHint("ID");

        Button cancel = view.findViewById(R.id.Cancel_btn);
        Button add = view.findViewById(R.id.Add_btn);

        cancel.setOnClickListener(v -> dismiss() );
        add.setOnClickListener(v ->{
            String roll = roll_edt.getText().toString();
            String name = name_edt.getText().toString();
            String id = iD_edt.getText().toString();
            if (TextUtils.isEmpty(roll) || TextUtils.isEmpty(name) || TextUtils.isEmpty(id)){
                Snackbar.make(v,"Please fill all required Fields!",500).show();
            }
            roll_edt.setText(String.valueOf(Integer.parseInt(roll)+1));
            name_edt.setText("");
            iD_edt.setText("");
            listenerSt.onClick(roll,name,id);
        });
        return builder.create();

    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);


        builder.setView(view);

        TextView title = view.findViewById(R.id.TitleDialog);
        title.setText("Add New Class");

        EditText class_edt = view.findViewById(R.id.edt01);
        EditText course_edt = view.findViewById(R.id.ed02);
        EditText edt03 = view.findViewById(R.id.ed03);
        edt03.setVisibility(View.GONE);

        class_edt.setHint("Class Name");
        course_edt.setHint("Course Name");

        Button cancel = view.findViewById(R.id.Cancel_btn);
        Button add = view.findViewById(R.id.Add_btn);

        cancel.setOnClickListener(v -> dismiss() );
        add.setOnClickListener(v ->{
            String className = class_edt.getText().toString();
            String subName = course_edt.getText().toString();
            if (TextUtils.isEmpty(className) || TextUtils.isEmpty(subName)){
                Snackbar.make(v,"Please fill all required Fields!",500).show();
            }else {
           listener.onClick(className,subName);
            dismiss();
            }
        });
        return builder.create();
    }
}
