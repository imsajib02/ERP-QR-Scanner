package com.b2gsoft.jamalpurqrscanner.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.b2gsoft.jamalpurqrscanner.Utils.StaticValue.*;

public class ConnectivityStatus extends BroadcastReceiver {

    public ConnectivityStatus() {}

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null)
        {
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                isConnected = true;
                isConnectionActive();
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                isConnected = true;
                isConnectionActive();
            }
        }
        else
        {
            isConnected = false;
        }
    }

    public void isConnectionActive()
    {
        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {

                if(internet)
                {
                    isConnectionActive = true;
                }
                else
                {
                    isConnectionActive = false;
                }
            }
        });
    }
}
