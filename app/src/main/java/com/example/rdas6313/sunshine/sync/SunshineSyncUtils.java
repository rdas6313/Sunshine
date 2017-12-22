package com.example.rdas6313.sunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

import com.example.rdas6313.sunshine.data.SunshinePreference;
import com.example.rdas6313.sunshine.data.WeatherContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.sql.Driver;
import java.util.concurrent.TimeUnit;

/**
 * Created by rdas6313 on 13/8/17.
 */

public class SunshineSyncUtils{
   // public static Boolean minit = false;

    public static final int INTERVAL_HOUR = 2;
    //public static final int INTERVAL_SECOND = (int)TimeUnit.HOURS.toSeconds(INTERVAL_HOUR);
    public static final int INTERVAL_SECOND = (int)TimeUnit.MINUTES.toSeconds(5);
    public static final int FLEX_HOUR = 3;
   // public static final int FLEX_SECOND = (int)TimeUnit.HOURS.toSeconds(FLEX_HOUR);
   public static final int FLEX_SECOND = (int)TimeUnit.MINUTES.toSeconds(5);

    public static final String JOB_TAG = "SCHEDULE_JOB_TAG";

    /* intialize uses these variables */

    private static int shouldUpdateAfterInHours = 1;

    private static Long shouldUpdateAfterInSec = TimeUnit.HOURS.toSeconds(shouldUpdateAfterInHours);

    public static void startImmediateSync(final Context context){
        Intent intent = new Intent(context,SunshineSyncIntentService.class);
        context.startService(intent);
    }

    public static void scheduleFirebaseDispatcher(Context context){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job myjob = dispatcher.newJobBuilder()
                .setTag(JOB_TAG)
                .setService(SunshineFirebaseJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(INTERVAL_SECOND,INTERVAL_SECOND+FLEX_SECOND))
                .setRecurring(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(myjob);
        Log.e("Job","YES");
    }

    public static void initialize(final Context context){
    /*    if(SunshinePreference.isInitLoadNotRequired(context) && !isInternetAvailable(context))
            return;
        SunshinePreference.setLoad(context);
       // Log.e("Init","Yes");
        scheduleFirebaseDispatcher(context);

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                Cursor cursor = context.getContentResolver().query(WeatherContract.WeatherEntry.CONTENT_URI,null,null,null,null);
                if(cursor == null || cursor.getCount() == 0)
                    startImmediateSync(context);
                if(cursor != null)
                    cursor.close();
                return null;
            }
        }.execute();

    */

        scheduleFirebaseDispatcher(context);
        if(!isInternetAvailable(context))
            return;

        Long currentSystemTimeInSec = System.currentTimeMillis()/1000;

        Long lastDataUpdateInSec = SunshinePreference.getLastUpdate(context);

        if((currentSystemTimeInSec-lastDataUpdateInSec) < shouldUpdateAfterInSec)
            return;

        startImmediateSync(context);

        SunshinePreference.setLastUpdate(context,currentSystemTimeInSec);

    }
    public static boolean isInternetAvailable(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
