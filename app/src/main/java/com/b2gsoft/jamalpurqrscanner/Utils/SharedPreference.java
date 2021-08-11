package com.b2gsoft.jamalpurqrscanner.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.b2gsoft.jamalpurqrscanner.Model.Data;
import com.b2gsoft.jamalpurqrscanner.Model.LoginResponse;
import com.b2gsoft.jamalpurqrscanner.Model.User;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


public class SharedPreference {

    private Context context;
    private SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public SharedPreference(Context context) {
        this.context = context;
        editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }

    private final String MY_PREFS_NAME = "JamalpurQRScanner";
    private final String USER = "76VTyyfYF";
    private final String LoginTime = "vRVR6TIB";


    public void saveUser(Data data)
    {
        Gson gson = new Gson();
        String json = gson.toJson(data);

        editor.putString(USER, json);
        editor.apply();
    }

    public Data getCurrentUser()
    {
        Data data = new Data();

        if(prefs.contains(USER)) {

            Gson gson = new Gson();
            String json = prefs.getString(USER, "");

            data = gson.fromJson(json, Data.class);
        }

        return data;
    }

    public void clearUserData() {

        editor.remove(USER);
        editor.apply();
    }

    public void setLoginTime(String time) {

        editor.putString(LoginTime, time);
        editor.apply();
    }

    public String getLoginTime()
    {
        return prefs.getString(LoginTime, "");
    }
}
