package com.example.loginpage.room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;

@Dao
public interface MessageIdDao {

    @Insert
    Completable Insert(MessageSchema schema);

    @Query("SELECT * FROM identify_message_table order by id ASC")
    LiveData<List<MessageSchema>> getListMessagesId();

    @Delete
    Completable Delete(MessageSchema schema);
}
