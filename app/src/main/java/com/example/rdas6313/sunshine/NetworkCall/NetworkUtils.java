package com.example.rdas6313.sunshine.NetworkCall;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by rdas6313 on 13/7/17.
 */

public final class NetworkUtils {
    private final static String FORECAST_BASE_URL = "http://api.apixu.com/v1//forecast.json";
    private final static String KEY = "key";
    private final static String QUERY = "q";
    private final static String DAY = "days";
    /*
    Here's come the Key and query parameter
     */
    private final static String key = "7e2458b016774e62a5d62840171307";
    private final static String day = "10";

    public static URL buildUrl(String location){
        Uri uri = Uri.parse(FORECAST_BASE_URL);
        Uri.Builder builder = uri.buildUpon();
        builder = builder.appendQueryParameter(KEY,key).appendQueryParameter(QUERY,location)
        .appendQueryParameter(DAY,day);
        URL url = null;
        try{
            url = new URL(builder.toString());
        }catch (MalformedURLException e){
            url = null;
            e.printStackTrace();
        }finally {
            return url;
        }
    }

    public static URL buildUrl(Double lat,Double lon){
        //Todo:We will do this later
        URL url = null;
        return url;
    }

    public static String getHttpresponse(URL url){
        HttpURLConnection conn = null;
        String jsonData = "";
        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            jsonData = scanner.hasNext()?scanner.next():"";
        }catch (IOException e){
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
            return null;
        }finally {
            conn.disconnect();
            if(url != null)
                url = null;
        }
        return jsonData;
    }

}
