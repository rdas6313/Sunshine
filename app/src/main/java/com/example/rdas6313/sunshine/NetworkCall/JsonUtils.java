package com.example.rdas6313.sunshine.NetworkCall;

import android.content.ContentValues;
import android.content.Context;

import com.example.rdas6313.sunshine.data.SunshinePreference;
import com.example.rdas6313.sunshine.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rdas6313 on 13/7/17.
 */

public final class JsonUtils {
        public static ContentValues[] parseJson(String jsonData, Context context){
           // ArrayList<String> data = new ArrayList<String>();
            ContentValues contentValues[];
            try{
                Boolean isCelcius = SunshinePreference.isMetric(context);
                String maxTemp = "";
                String minTemp = "";
                JSONObject root = new JSONObject(jsonData);
                JSONObject current = root.getJSONObject("current");
                String current_TempC = current.getString("temp_c");
                String current_TempF = current.getString("temp_f");
                String current_pressure = current.getString("pressure_in");
                JSONObject forecast = root.getJSONObject("forecast");
                JSONArray forecastlist = forecast.getJSONArray("forecastday");
                JSONObject rootData = null;

                contentValues = new ContentValues[forecastlist.length()];

                for(int i=0;i<forecastlist.length();i++){
                    rootData = forecastlist.getJSONObject(i);
                    String date = rootData.getString("date_epoch");
                    JSONObject days = rootData.getJSONObject("day");
                    if(isCelcius) {
                        maxTemp = days.getString("maxtemp_c");
                        minTemp = days.getString("mintemp_c");
                    }else{
                        maxTemp = days.getString("maxtemp_f");
                        minTemp = days.getString("mintemp_f");
                    }
                    String condition = days.getJSONObject("condition").getString("text");
                    String iconAndDaynight = days.getJSONObject("condition").getString("icon");
                    String wind = days.getString("maxwind_kph");
                    String humidity = days.getString("avghumidity");

                    //data.add(date+"-"+maxTemp+"-"+minTemp+"-"+condition);
                    contentValues[i] = new ContentValues();
                    if(i == 0){
                        if(isCelcius)
                            contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_CURRENT_TEMP,Double.valueOf(current_TempC));
                        else
                            contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_CURRENT_TEMP,Double.valueOf(current_TempF));
                        contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_PRESSURE,Double.valueOf(current_pressure));
                    }
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_ICON,iconAndDaynight);
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_DATE,Long.valueOf(date));
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,Double.valueOf(maxTemp));
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,Double.valueOf(minTemp));
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,Double.valueOf(wind));
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY,Double.valueOf(humidity));
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_CONDITION,condition);
                    contentValues[i].put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,i);
                }
            }catch(JSONException e){
                e.printStackTrace();
                return null;
            }
            return contentValues;
        }
}
