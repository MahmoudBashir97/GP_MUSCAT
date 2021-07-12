package com.example.loginpage.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.loginpage.Chat;
import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.adapters.ChatList_adapter;
import com.example.loginpage.models.RecentMessagesIds_Model;
import com.example.loginpage.models.User_Data_Model;
import com.example.loginpage.room.MessageSchema;
import com.example.loginpage.ui.MessagesChat_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DoctorsList_Fragment extends Fragment {

    DatabaseReference reference,RecentMessagesId;
    ArrayList<User_Data_Model> mlist = new ArrayList<>();
    ArrayList<MessageSchema> recentIdsList = new ArrayList<>();
    RecyclerView rec_chats;
    ChatList_adapter adapter;
    String myId ;

    public DoctorsList_Fragment(ArrayList<MessageSchema> recentIds) {
        this.recentIdsList = recentIds;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctors_list_, container, false);
        rec_chats = v.findViewById(R.id.rec_chats);
        rec_chats.setHasFixedSize(true);

        myId = SharedPrefranceManager.getInastance(getContext()).getUser_ID();

        reference = FirebaseDatabase.getInstance().getReference("Registeration");
        RecentMessagesId = FirebaseDatabase.getInstance().getReference("RecentMessagesIds");


        adapter = new ChatList_adapter(getContext(),mlist,recentIdsList,index -> {
            getContext().startActivity(new Intent(getContext(), MessagesChat_Activity.class)
                    .putExtra("_id",mlist.get(index).getId())
                    .putExtra("_name",mlist.get(index).getName()));

           /* for (RecentMessagesIds_Model n :recentIds) {
                if (mlist.get(index).getId().equals(n.getSenderId())){
                    removeSenderIdFromRecentIds(n.getSenderId());
                }
            }*/
        });

        getDoctorsList();
        return v;
    }
    private void removeSenderIdFromRecentIds(String senderId){
        RecentMessagesId.child(myId).child(senderId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("recentIds","removed successfully");
                }
            }
        });
    }

    private void getDoctorsList(){
        mlist.clear();
        reference.child("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot sn : snapshot.getChildren()){
                        String _id = sn.child("id").getValue().toString();
                        if (!myId.equals(_id)){
                            String _email= sn.child("email").getValue().toString();
                            String _name = sn.child("name").getValue().toString();
                            String _pass = sn.child("password").getValue().toString();
                            String _deviceToken = sn.child("deviceToken").getValue().toString();
                            User_Data_Model model =new User_Data_Model();
                            model.setId(_id);
                            model.setEmail(_email);
                            model.setName(_name);
                            model.setPassword(_pass);
                            model.setDeviceToken(_deviceToken);
                            mlist.add(model);
                        }
                    }
                    rec_chats.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }
}