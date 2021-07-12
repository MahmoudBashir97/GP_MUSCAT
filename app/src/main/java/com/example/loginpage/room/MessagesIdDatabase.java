package com.example.loginpage.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {MessageSchema.class},exportSchema = false,version = 1)
public abstract class MessagesIdDatabase extends RoomDatabase {

    private static MessagesIdDatabase INSTANCE;
    public abstract MessageIdDao dao();


    public static synchronized MessagesIdDatabase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                    ,MessagesIdDatabase.class,"messages")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
