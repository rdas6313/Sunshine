package com.example.rdas6313.sunshine.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rdas6313.sunshine.data.SunshinePreference;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by rdas6313 on 13/8/17.
 */

public class SunshineFirebaseJobService extends JobService {
    private AsyncTask<Void,Void,Void> task;
    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.e("JobService","Onstart");
        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.e("Async","Onbackground");
                Context context = getApplicationContext();
                SunshineSyncTask.syncWeather(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.e("Async","OnPOstExec");
                Long currentSystemTimeInSec = System.currentTimeMillis()/1000;
                SunshinePreference.setLastUpdate(getApplicationContext(),currentSystemTimeInSec);
                jobFinished(job, false);

            }
        };

        task.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(task != null)
            task.cancel(true);
        Log.e("JobService","Onstop");
        return true;
    }
}