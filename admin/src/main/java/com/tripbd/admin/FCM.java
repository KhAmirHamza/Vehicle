package com.tripbd.admin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FCM {
    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("body")
    @Expose
    String body;

    @SerializedName("tokens")
    @Expose
    List<String> tokens;


    public FCM(String title, String body, List<String> tokens) {
        this.title = title;
        this.body = body;
        this.tokens = tokens;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}

