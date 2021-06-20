package com.example.loginpage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginpage.R;
import com.example.loginpage.ui.StudentActivity;
import com.example.loginpage.models.StudentItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAttendee_Adapter extends RecyclerView.Adapter<StudentAttendee_Adapter.StudentViewHolder> {
    ArrayList<StudentItem> studentItems;
    Context context;

    public StudentAttendee_Adapter(StudentActivity context, ArrayList<StudentItem> studentsItems) {

        this.studentItems = studentsItems;
        this.context = context;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder{
TextView roll;
TextView name;
TextView status;
CardView cardView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name =itemView.findViewById(R.id.name);
            status =itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.roll.setText(studentItems.get(position).getRoll());
        holder.name.setText(studentItems.get(position).getName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));

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

    @Override
    public int getItemCount() {
        return studentItems.size();
    }
}
