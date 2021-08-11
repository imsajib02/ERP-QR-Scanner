package com.b2gsoft.jamalpurqrscanner.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.b2gsoft.jamalpurqrscanner.Interface.Session;
import com.b2gsoft.jamalpurqrscanner.Model.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class SessionValidation extends BroadcastReceiver {

    private Session session;
    private SharedPreference sharedPreference;
    private CurrentDateTime currentDateTime = new CurrentDateTime();

    public SessionValidation() {
    }

    public SessionValidation(Context context, Session session) {
        this.session = session;
        sharedPreference = new SharedPreference(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Data data = sharedPreference.getCurrentUser();

        if(data != null && data.getToken() != null) {

            String loginTime = sharedPreference.getLoginTime();
            String currentTime = currentDateTime.getDateTime();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            try {
                Date startDate = sdf.parse(currentTime);
                Date endDate = sdf.parse(loginTime);

                long millis = startDate.getTime() - endDate.getTime();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

                if(minutes > Long.parseLong(data.getValidity())) {

                    session.onSessionEnd();
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
