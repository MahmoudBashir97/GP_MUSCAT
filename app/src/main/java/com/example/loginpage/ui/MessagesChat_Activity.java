package com.example.loginpage.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.adapters.MessageAdapter;
import com.example.loginpage.databinding.ActivityMessagesChatBinding;
import com.example.loginpage.models.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesChat_Activity extends AppCompatActivity {

    String messageSenderID,messageRecieverID;
    String saveCurrentDate,saveCurrentTime;
    DatabaseReference rootRef,messageRefReceiver;
    private String myName,pharma_name,destination_name;
    ActivityMessagesChatBinding chatBinding;
    private MessageAdapter adapter;
    private final List<Messages> messagesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = DataBindingUtil.setContentView(this
                ,R.layout.activity_messages_chat_);

        messageSenderID = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_ID();
        messageRecieverID = getIntent().getStringExtra("_id");
        myName= SharedPrefranceManager.getInastance(getApplicationContext()).getUser_Name();
        destination_name = getIntent().getStringExtra("_name");

        rootRef = FirebaseDatabase.getInstance().getReference("Messages");
        messageRefReceiver = FirebaseDatabase.getInstance().getReference("Messages");


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMMM dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");

        saveCurrentTime=currentTime.format(calendar.getTime());

        RetreiverMessages();
        adapter = new MessageAdapter(myName,destination_name,this,messagesList,messageSenderID);
        chatBinding.recRequestChat.setAdapter(adapter);


        chatBinding.btnSend.setOnClickListener(v -> {
            sendMessage();
        });


        chatBinding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void RetreiverMessages(){
        messagesList.clear();
        rootRef.child(messageSenderID).child(messageRecieverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Messages messages = snapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        adapter.notifyDataSetChanged();
                        chatBinding.recRequestChat.smoothScrollToPosition(chatBinding.recRequestChat.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendMessage(){

        String messageText = chatBinding.inputMessage.getText().toString();
        if (TextUtils.isEmpty(messageText)){
            chatBinding.inputMessage.setError("Please type a message!!");
            chatBinding.inputMessage.requestFocus();
        }else {

            DatabaseReference random ;
            random = FirebaseDatabase.getInstance().getReference("Registeration");
            random.child("Students").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            String messageSendRef = "Messages/" + messageSenderID + "/" + messageRecieverID;
            String messageRecieveRef = "Messages/" + messageRecieverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = rootRef.child("Messages").child(messageSenderID)
                    .child(messageRecieverID).push();

            String messagePushID =random.push().getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);

            messageTextBody.put("to", messageRecieverID);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("name", SharedPrefranceManager.getInastance(getApplicationContext()).getUser_Name());


            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSendRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageRecieveRef + "/" + messagePushID, messageTextBody);


            rootRef.child(messageSenderID)
                    .child(messageRecieverID)
                    .child(messagePushID)
                    .setValue(messageTextBody)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                messageRefReceiver.child(messageRecieverID)
                                        .child(messageSenderID)
                                        .child(messagePushID)
                                        .setValue(messageTextBody)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.e("Send message", "Message Sent Successfully...");
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(MessagesChat_Activity.this, "Error in sending message!!", Toast.LENGTH_SHORT).show();
                            }
                            chatBinding.inputMessage.setText("");
                        }
                    });


        }
    }
}