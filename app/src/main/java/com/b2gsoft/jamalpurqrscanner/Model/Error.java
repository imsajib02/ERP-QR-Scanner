package com.b2gsoft.jamalpurqrscanner.Model;


import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("login")
    private String loginError;

    public Error(String loginError) {
        this.loginError = loginError;
    }

    public Error() {
    }

    public String getLoginError() {
        return loginError == null ? "" : loginError;
    }

    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }
}