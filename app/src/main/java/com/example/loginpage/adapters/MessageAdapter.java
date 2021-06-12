package com.example.loginpage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loginpage.R;
import com.example.loginpage.models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private String userName;
    private String destinationName;
    private Context context;
    private List<Messages> usermessageList;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private String SenderID;

    public MessageAdapter(String userName, String destinationName, Context context, List<Messages> usermessageList, String senderID) {
        this.userName = userName;
        this.destinationName = destinationName;
        this.context = context;
        this.usermessageList = usermessageList;
        SenderID = senderID;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_request_chat,parent,false);
        auth= FirebaseAuth.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Messages messages=usermessageList.get(position);

        String fromUserID=messages.getFrom();
        String fromMessageType=messages.getType();


        holder.lin_sender.setVisibility(View.GONE);
        holder.lin_receiver.setVisibility(View.GONE);
        holder.recieverMessagetext.setVisibility(View.GONE);
        holder.senderMessagetext.setVisibility(View.GONE);
        holder.prof_message_sender.setVisibility(View.GONE);
        holder.prof_message_receiver.setVisibility(View.GONE);
        holder.sender_message_img.setVisibility(View.GONE);
        holder.receiver_message_img.setVisibility(View.GONE);


        if (fromMessageType.equals("text")) {

            if (fromUserID.equals(SenderID)){

                holder.senderMessagetext.setVisibility(View.VISIBLE);
                holder.lin_sender.setVisibility(View.VISIBLE);
                holder.prof_message_sender.setVisibility(View.VISIBLE);

                holder.senderMessagetext.setBackgroundResource(R.drawable.sender_message);
                holder.senderMessagetext.setTextColor(Color.WHITE);
                holder.senderMessagetext.setText(messages.getMessage());
                holder.txt_sender_name.setText(userName);

            }else{
                holder.recieverMessagetext.setVisibility(View.VISIBLE);
                holder.lin_receiver.setVisibility(View.VISIBLE);
                holder.prof_message_receiver.setVisibility(View.VISIBLE);

                holder.recieverMessagetext.setBackgroundResource(R.drawable.ic_reciever_message);
                holder.recieverMessagetext.setTextColor(Color.BLACK);
                holder.recieverMessagetext.setText(messages.getMessage());
                holder.txt_receiver_name.setText(destinationName);

                //holder.text_message_time_reciever.setText(messages.getTime());

            }

        }

    }

    @Override
    public int getItemCount() {
        return usermessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView senderMessagetext ,recieverMessagetext,text_message_time_reciever,text_message_time_sender,txt_sender_name,txt_receiver_name;
        LinearLayout lin_sender,lin_receiver;
        ImageView prof_message_sender,prof_message_receiver,receiver_message_img,sender_message_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prof_message_sender = itemView.findViewById(R.id.prof_message_sender);
            prof_message_receiver = itemView.findViewById(R.id.prof_message_receiver);
            lin_receiver = itemView.findViewById(R.id.lin_receiver);
            lin_sender = itemView.findViewById(R.id.lin_sender);
            senderMessagetext=itemView.findViewById(R.id.txt_sender_message);
            recieverMessagetext=itemView.findViewById(R.id.txt_receiver_message);
            txt_sender_name=itemView.findViewById(R.id.txt_sender_name);
            txt_receiver_name=itemView.findViewById(R.id.txt_receiver_name);
            receiver_message_img=itemView.findViewById(R.id.receiver_message_img);
            sender_message_img=itemView.findViewById(R.id.sender_message_img);
        }
    }
}
