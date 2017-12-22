package com.example.rdas6313.sunshine;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by rdas6313 on 14/8/17.
 */

public class DateUtils {
    public static String convertTo(long timestamp/* timestamp comes in sec. */){
        // converting timestamp into millesec;
        timestamp = TimeUnit.SECONDS.toMillis(timestamp);
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM ");
        SimpleDateFormat anotherFormat = new SimpleDateFormat("EEEE");
        format.setTimeZone(TimeZone.getDefault());
        //Log.e("Date",android.text.format.DateUtils.isToday(timestamp)+"");
        if(android.text.format.DateUtils.isToday(timestamp))
            return "Today, "+format.format(date);
        else if(isTomorrow(timestamp))
            return "Tomorrow";
        else
            return anotherFormat.format(date);

    }
    public static boolean isTomorrow(long timestamp){
        Date newDate = new Date(timestamp);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM ");
        format.setTimeZone(TimeZone.getDefault());
        String tomorrow = format.format(date);
        String now_Date = format.format(newDate);
        if(tomorrow.equals(now_Date))
            return true;
        else
            return false;
    }

}
