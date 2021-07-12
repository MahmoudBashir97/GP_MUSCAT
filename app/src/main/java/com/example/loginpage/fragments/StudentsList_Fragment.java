package com.example.loginpage.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.loginpage.LocalStorage.SharedPrefranceManager;
import com.example.loginpage.R;
import com.example.loginpage.adapters.ChatList_adapter;
import com.example.loginpage.models.RecentMessagesIds_Model;
import com.example.loginpage.models.User_Data_Model;
import com.example.loginpage.room.DbRepository;
import com.example.loginpage.room.MessageSchema;
import com.example.loginpage.ui.MessagesChat_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class StudentsList_Fragment extends Fragment {

    DatabaseReference reference,RecentMessagesId;
    ArrayList<User_Data_Model> mlist = new ArrayList<>();
    ArrayList<RecentMessagesIds_Model> recentsList = new ArrayList<>();
    ArrayList<MessageSchema> recentsIds = new ArrayList<>();
    RecyclerView rec_chats;
    ChatList_adapter adapter;
    String myId;
    String n;

    public StudentsList_Fragment( ArrayList<MessageSchema> recentsList) {
        // Required empty public constructor
        this.recentsIds = recentsList;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_students_list_, container, false);
        rec_chats = v.findViewById(R.id.rec_chats);
        rec_chats.setHasFixedSize(true);


        myId = SharedPrefranceManager.getInastance(getContext()).getUser_ID();

        reference = FirebaseDatabase.getInstance().getReference("Registeration");
        RecentMessagesId = FirebaseDatabase.getInstance().getReference("RecentMessagesIds");


        adapter = new ChatList_adapter(getContext(),mlist,recentsIds,index -> {
            getContext().startActivity(new Intent(getContext(), MessagesChat_Activity.class)
                    .putExtra("_id",mlist.get(index).getId())
            .putExtra("_name",mlist.get(index).getName())
            .putExtra("userToken",mlist.get(index).getDeviceToken()));

            removeReadRecentIdsMessages(Integer.parseInt(mlist.get(index).getId()),
                    mlist.get(index).getName(),
                    mlist.get(index).getDeviceToken());

        });


        getStudentsList();

        return v;
    }

    private void removeReadRecentIdsMessages(int id, String name, String deviceToken) {
        MessageSchema schema = new MessageSchema(id,name,deviceToken);
        DbRepository repository = new DbRepository(getActivity().getApplication());
        repository.delete(schema).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d("messageStatus : ","read!");
                        Log.d("messageStatus : ","deletedSuccess");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.getMessage();
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter.notifyDataSetChanged();
    }

    private void getStudentsList(){
        mlist.clear();
        String myId = SharedPrefranceManager.getInastance(getContext()).getUser_ID();
        reference.child("Students").addValueEventListener(new ValueEventListener() {
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
                   // getRecentMessagesIds();
                    rec_chats.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("resumedNow : ","success");
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("pausedNow : ","success");
    }
}

