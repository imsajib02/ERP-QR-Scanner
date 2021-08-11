package com.b2gsoft.jamalpurqrscanner.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentDateTime {

    public String getDateTime() {

        String dateTime = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date date = new Date();
        dateTime = dateFormat.format(date) + " " + timeFormat.format(date);

        return dateTime;
    }
}
