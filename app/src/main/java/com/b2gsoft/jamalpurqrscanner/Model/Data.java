package com.b2gsoft.jamalpurqrscanner.Model;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("access_token")
    private String token;

    @SerializedName("token_type")
    private String type;

    @SerializedName("expires_in")
    private String validity;

    @SerializedName("user")
    private User user;

    public Data(String token, String type, String validity, User user) {
        this.token = token;
        this.type = type;
        this.validity = validity;
        this.user = user;
    }

    public Data() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}