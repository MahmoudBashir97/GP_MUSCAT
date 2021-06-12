package com.example.loginpage.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.loginpage.Chat;
import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.adapters.ChatList_adapter;
import com.example.loginpage.models.User_Data_Model;
import com.example.loginpage.ui.MessagesChat_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorsList_Fragment extends Fragment {

    DatabaseReference reference;
    ArrayList<User_Data_Model> mlist = new ArrayList<>();
    RecyclerView rec_chats;
    ChatList_adapter adapter;

    public DoctorsList_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctors_list_, container, false);
        rec_chats = v.findViewById(R.id.rec_chats);
        rec_chats.setHasFixedSize(true);

        reference = FirebaseDatabase.getInstance().getReference("Registeration");


        adapter = new ChatList_adapter(getContext(),mlist,index -> {
            getContext().startActivity(new Intent(getContext(), MessagesChat_Activity.class)
                    .putExtra("_id",mlist.get(index).getId())
                    .putExtra("_name",mlist.get(index).getName()));
        });

        getDoctorsList();
        return v;
    }

    private void getDoctorsList(){
        mlist.clear();
        String myId = SharedPrefranceManager.getInastance(getContext()).getUser_ID();
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
                            User_Data_Model model =new User_Data_Model();
                            model.setId(_id);
                            model.setEmail(_email);
                            model.setName(_name);
                            model.setPassword(_pass);
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