package com.example.rdas6313.sunshine.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by rdas6313 on 13/8/17.
 */

public class SunshineSyncIntentService extends IntentService {
    public SunshineSyncIntentService() {
        super("WeatherIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }
}
