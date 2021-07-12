package com.example.loginpage.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "identify_message_table")
public class MessageSchema {
    @PrimaryKey
    int id;
    String name;
    String token;

    public MessageSchema(int id, String name, String token) {
        this.id = id;
        this.name = name;
        this.token = token;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
