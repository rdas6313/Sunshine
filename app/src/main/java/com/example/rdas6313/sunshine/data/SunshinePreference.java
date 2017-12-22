package com.example.rdas6313.sunshine.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.rdas6313.sunshine.R;

/**
 * Created by rdas6313 on 28/7/17.
 */

public final class SunshinePreference {
    private static boolean needToLoad = false;
    private static final String DEFAULT_LOCATION = "katwa";
    private static final double DEFAULT_LATITUDE = 0.0;
    private static final double DEFAULT_LONGITUDE = 0.0;
    public static String getDefaultLocation(){
        return DEFAULT_LOCATION;
    }
    public static String getPreferedLocation(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_location_key),context.getString(R.string.pref_location_default));
    }
    public static boolean isMetric(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(context.getString(R.string.pref_units_key),context.getString(R.string.pref_units_metric));
        if(value.equals(context.getString(R.string.pref_units_metric)))
            return true;
        else
            return false;
    }
    public static void setLoad(Context context){
        needToLoad = true;
    /*
    *    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    *    SharedPreferences.Editor editor = sharedPreferences.edit();
    *   editor.putBoolean(context.getString(R.string.initLoad),true);
    *    editor.commit();
    */
    }
    public static Boolean isInitLoadNotRequired(Context context){

        return needToLoad;
        /*
        * SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        * return sharedPreferences.getBoolean(context.getString(R.string.initLoad),context.getResources().getBoolean(R.bool.defaultLoad));
       */
    }
    public static void setLastUpdate(Context context,Long value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(context.getString(R.string.lastupdate_key),value);
        editor.commit();
    }
    public static Long getLastUpdate(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(context.getString(R.string.lastupdate_key),Long.valueOf(context.getString(R.string.lastupdate_default)));
    }
}

