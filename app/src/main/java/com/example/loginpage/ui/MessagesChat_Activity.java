package com.example.loginpage.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.loginpage.Inter_faces.Api_Interface;
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
import com.example.loginpage.models.send;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessagesChat_Activity extends AppCompatActivity {

    String messageSenderID,messageRecieverID;
    String saveCurrentDate,saveCurrentTime;
    DatabaseReference rootRef,messageRefReceiver,RecentMessagesPath;
    private String myName,pharma_name,destination_name;
    ActivityMessagesChatBinding chatBinding;
    private MessageAdapter adapter;
    private final List<Messages> messagesList=new ArrayList<>();

    String BaseURL="https://fcm.googleapis.com/";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAgI6vvSI:APA91bHGvogeUgWQ97SkStw6arGtDecp-uxTrhIqP5Z0N0zFUASCLcsg_0SJ-R1U_pCl2jdGwgFjL4VW0vHckrVNWVSWI3AGKmZ79lc5HRWVCDuclbR-mF7Iq3o7Zutnsw12G_hQpfwP";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private String CurrentUserID;
    private String userToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = DataBindingUtil.setContentView(this
                ,R.layout.activity_messages_chat_);

        messageSenderID = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_ID();
        messageRecieverID = getIntent().getStringExtra("_id");
        myName= SharedPrefranceManager.getInastance(getApplicationContext()).getUser_Name();
        destination_name = getIntent().getStringExtra("_name");
        userToken = getIntent().getStringExtra("userToken");

        rootRef = FirebaseDatabase.getInstance().getReference("Messages");
        messageRefReceiver = FirebaseDatabase.getInstance().getReference("Messages");
        RecentMessagesPath = FirebaseDatabase.getInstance().getReference("RecentMessagesIds");

        chatBinding.txtDestinationName.setText(destination_name);
        createNotificationChannel();

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

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MahmoudChannel";
            String description = "Channel for Mahmoud Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("message", name, importance);
            Log.d("checkingMessage : ","created"+channel.getId());
            channel.setDescription(description);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI,audioAttributes);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
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

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            Api_Interface api_interface =retrofit.create(Api_Interface.class);





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



            Messages data = new Messages();
            data.setTo(messageRecieverID);
            data.setFrom(messageSenderID);
            data.setDate(saveCurrentDate);
            data.setTime(saveCurrentTime);
            data.setType("message");
            data.setName(SharedPrefranceManager.getInastance(getApplicationContext()).getUser_Name());
            data.setMessage(messageText);
            data.setMessageID(messagePushID);
            data.setReceiverToken(SharedPrefranceManager.getInastance(getApplicationContext()).getDeviceToken());

            send stored_data = new send(userToken,data);
            Call<send> sendCall = api_interface.storedata(stored_data);
            sendCall.enqueue(new Callback<send>() {
                @Override
                public void onResponse(Call<send> call, Response<send> response) {
                    send sendResponse = response.body();
                    Log.e("send", "sendResponse --> " + sendResponse);
                }

                @Override
                public void onFailure(Call<send> call, Throwable t) {
                    Toast.makeText(MessagesChat_Activity.this, "error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



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
                                                    recentMessagesIdPath();
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
    private void recentMessagesIdPath(){
        long datTime = System.currentTimeMillis();
        HashMap<String,Object> map = new HashMap<>();
        map.put("senderId",messageSenderID);
        map.put("date",""+datTime);

        RecentMessagesPath.child(messageRecieverID)
                .child(messageSenderID)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("recentMessagesId : ","success ");
                        }
                    }
                });

    }
}