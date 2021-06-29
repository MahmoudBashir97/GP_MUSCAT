package com.example.loginpage.models;

import com.google.gson.annotations.SerializedName;

public class send {

    @SerializedName("to")
    private String to;
    @SerializedName("data")
    private Messages data;

    public send(){}
    public send(String to, Messages data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Messages getData() {
        return data;
    }

    public void setData(Messages data) {
        this.data = data;
    }
}
