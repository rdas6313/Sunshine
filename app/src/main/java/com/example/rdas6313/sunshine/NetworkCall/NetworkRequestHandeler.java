package com.example.rdas6313.sunshine.NetworkCall;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.rdas6313.sunshine.data.SunshinePreference;

import java.net.URL;

/**
 * Created by rdas6313 on 13/7/17.
 */

public class NetworkRequestHandeler extends android.content.AsyncTaskLoader<String> {
    private String location = "";
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Boolean indicator = false;
    private String jdata = null;
    public NetworkRequestHandeler(Context context){
        super(context);
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        location = SunshinePreference.getPreferedLocation(getContext());
        indicator = false;
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        URL url = null;
        String jsonData = "";
        if(indicator)
            url = NetworkUtils.buildUrl(latitude,longitude);
        else
            url = NetworkUtils.buildUrl(location);
        if(url == null)
            return jsonData;
        jsonData = NetworkUtils.getHttpresponse(url);
        return jsonData;
    }

}
