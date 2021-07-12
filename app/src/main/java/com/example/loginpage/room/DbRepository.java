package com.example.loginpage.room;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Completable;

public class DbRepository {
    private MessageIdDao dao;
    public LiveData<List<MessageSchema>> allRecentMessages;

    public DbRepository(Application application) {
       MessagesIdDatabase messagesIdDatabase = MessagesIdDatabase.getInstance(application);
       dao = messagesIdDatabase.dao();
       allRecentMessages = dao.getListMessagesId();
    }
    public Completable insert(MessageSchema schema){
        return dao.Insert(schema);
    }
    public LiveData<List<MessageSchema>> getAllRecentMessages(){
        return allRecentMessages;
    }

    public Completable delete(MessageSchema schema){
        return dao.Delete(schema);
    }

}
