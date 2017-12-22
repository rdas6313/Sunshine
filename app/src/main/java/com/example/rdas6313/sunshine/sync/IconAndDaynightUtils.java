package com.example.rdas6313.sunshine.sync;

import android.content.Context;
import android.util.Log;

/**
 * Created by rdas6313 on 26/8/17.
 */

public class IconAndDaynightUtils {
    //cdn.apixu.com/weather/64x64/day/113.png

    public static String extractIcon(Context context,String url){
        String urlData[] = url.split("/");
        String icon = "w"+urlData[urlData.length-1];
        //Log.e("ICON",icon.replaceAll(".png",""));
        return icon.replaceAll(".png","");
    }
    public static String extractDaynight(Context context,String url){
        String urlData[] = url.split("/");
        String dayOrnight = urlData[urlData.length-2];
        return dayOrnight;
    }
}
