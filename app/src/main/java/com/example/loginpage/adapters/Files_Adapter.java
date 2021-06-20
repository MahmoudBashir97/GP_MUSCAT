package com.example.loginpage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginpage.Inter_faces.downloadQuiz_Interface;
import com.example.loginpage.R;
import com.example.loginpage.models.AssignmentDataModel;
import com.example.loginpage.models.FilesDataModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Files_Adapter extends RecyclerView.Adapter<Files_Adapter.ViewHolder> {
    Context context;
    List<FilesDataModel> mlist;
    downloadQuiz_Interface downloadQuizInterface;

    public Files_Adapter(Context context, List<FilesDataModel> mlist, downloadQuiz_Interface downloadQuizInterface) {
        this.context = context;
        this.mlist = mlist;
        this.downloadQuizInterface=downloadQuizInterface;
    }

    @NonNull
    @Override
    public Files_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_assignment,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Files_Adapter.ViewHolder holder, int position) {
        holder.txt_date.setText(mlist.get(position).getDate());
        holder.txt_className.setText(mlist.get(position).getClassName());
        holder.txt_courseName.setText(mlist.get(position).getCourseName()+" - "+mlist.get(position).getType());
        holder.itemView.setOnClickListener(v -> {
            downloadQuizInterface.onClickFile(position);
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_className,txt_courseName,txt_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_className = itemView.findViewById(R.id.txt_className);
            txt_courseName = itemView.findViewById(R.id.txt_courseName);
            txt_date = itemView.findViewById(R.id.txt_date);
        }
    }
}
