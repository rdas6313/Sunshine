package com.example.rdas6313.sunshine.sync;

import android.content.ContentValues;
import android.content.Context;

import com.example.rdas6313.sunshine.NetworkCall.JsonUtils;
import com.example.rdas6313.sunshine.NetworkCall.NetworkUtils;
import com.example.rdas6313.sunshine.data.SunshinePreference;
import com.example.rdas6313.sunshine.data.WeatherContract;

import java.net.URL;

/**
 * Created by rdas6313 on 12/8/17.
 */

public final class SunshineSyncTask {
    public static void syncWeather(Context context){
        String location = SunshinePreference.getPreferedLocation(context);

        URL url = null;
        String jsonData = null;
        try {

            url = NetworkUtils.buildUrl(location);
            jsonData = NetworkUtils.getHttpresponse(url);
            ContentValues[] contentValues = JsonUtils.parseJson(jsonData,context);
            if(contentValues == null || contentValues.length == 0)
                return;
            context.getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI,null,null);
            context.getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI,contentValues);

            Double currentTemp = (Double) contentValues[0].get(WeatherContract.WeatherEntry.COLUMN_CURRENT_TEMP);
            long weather_date = (long) contentValues[0].get(WeatherContract.WeatherEntry.COLUMN_DATE);
            NotificationUtils.setNotification(context,currentTemp,weather_date);

        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
