package com.example.loginpage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginpage.R;
import com.example.loginpage.models.StudentItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_doctorAttendance extends RecyclerView.Adapter<Adapter_doctorAttendance.ViewHolder> {

    ArrayList<StudentItem> mlist = new ArrayList<>();
    Context context;
    OnItemClickListenerAttendanceDoctor onItemClickListenerAttendanceDoctor;

    public Adapter_doctorAttendance(Context context, ArrayList<StudentItem> mlist,OnItemClickListenerAttendanceDoctor onItemClickListenerAttendanceDoctor){
        this.context=context;
        this.mlist=mlist;
        this.onItemClickListenerAttendanceDoctor=onItemClickListenerAttendanceDoctor;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ClassName.setText(mlist.get(position).getClassName());
        holder.CourseName.setText(mlist.get(position).getCourseName());
        holder.itemView.setOnClickListener(v -> {
            onItemClickListenerAttendanceDoctor.onClickItemAttendance(position,
                    mlist.get(position).getClassName(),
                    mlist.get(position).getCourseName());
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ClassName;
        TextView CourseName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ClassName = itemView.findViewById(R.id.class_tv);
            CourseName =itemView.findViewById(R.id.course_tv);
        }
    }
    public interface OnItemClickListenerAttendanceDoctor{
        void onClickItemAttendance(int position,String className,String courseName);

    }
}


