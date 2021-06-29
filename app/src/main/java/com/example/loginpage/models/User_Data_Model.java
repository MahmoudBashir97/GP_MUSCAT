package com.example.loginpage.models;

import java.io.Serializable;

public class User_Data_Model implements Serializable {
    String id;
    String name;
    String email;
    String password;
    String deviceToken;

    public User_Data_Model(String id, String name, String email, String password,String deviceToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deviceToken = deviceToken;
    }

    public User_Data_Model() {
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
