package com.example.loginpage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginpage.Inter_faces.chatListOnCLickInterface;
import com.example.loginpage.R;
import com.example.loginpage.models.User_Data_Model;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatList_adapter extends RecyclerView.Adapter<ChatList_adapter.ViewHolder> {
    ArrayList<User_Data_Model> mlist;
    Context context;
    chatListOnCLickInterface cLickInterface;
    public ChatList_adapter(Context context, ArrayList<User_Data_Model> mlist,chatListOnCLickInterface cLickInterface) {
        this.mlist=mlist;
        this.context=context;
        this.cLickInterface=cLickInterface;
    }

    @NonNull
    @Override
    public ChatList_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_chats_,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatList_adapter.ViewHolder holder, int position) {
        holder.txt_user_name.setText(mlist.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
            cLickInterface.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_user_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
        }
    }
}