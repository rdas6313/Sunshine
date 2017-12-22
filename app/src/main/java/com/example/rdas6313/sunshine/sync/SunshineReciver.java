package com.example.rdas6313.sunshine.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rdas6313 on 13/9/17.
 */

public class SunshineReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       // Log.e("OnRecv","NETWORK STATE CHANGED");
        SunshineSyncUtils.initialize(context);
    }
}
