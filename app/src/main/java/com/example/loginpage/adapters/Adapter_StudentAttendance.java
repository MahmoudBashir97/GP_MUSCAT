package com.example.loginpage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginpage.R;
import com.example.loginpage.models.StudentItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_StudentAttendance extends RecyclerView.Adapter<Adapter_StudentAttendance.ViewHolder> {

    ArrayList<StudentItem> studentItems;
    Context context;

    public Adapter_StudentAttendance(Context context, ArrayList<StudentItem> studentsItems) {

        this.studentItems = studentsItems;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_StudentAttendance.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_student_attendance,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_StudentAttendance.ViewHolder holder, int position) {
        holder.class_name.setText(studentItems.get(position).getClassName());
        holder.course_name.setText(studentItems.get(position).getCourseName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
        holder.date_att.setText(studentItems.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }

    private int getColor(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.Present)));
        else if (status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.Absent)));

        Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView class_name,course_name,date_att;
        TextView status;
        CardView cardView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            course_name =itemView.findViewById(R.id.course_name);
            class_name =itemView.findViewById(R.id.class_name);
            status =itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardview);
            date_att = itemView.findViewById(R.id.date_att);
        }
    }
}
