package com.example.rdas6313.sunshine.sync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.rdas6313.sunshine.DetailsActivity;
import com.example.rdas6313.sunshine.MainActivity;
import com.example.rdas6313.sunshine.R;
import com.example.rdas6313.sunshine.data.WeatherContract;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

/**
 * Created by rdas6313 on 14/8/17.
 */

public class NotificationUtils {
    public final static int PENDING_INTENT_CODE = 10021;

    public final static int NOTIFICATION_ID = 201;

    public static void setNotification(Context context,Double currentTemp,long weather_date){
        Uri.Builder Ubuilder = ContentUris.appendId(WeatherContract.WeatherEntry.CONTENT_URI.buildUpon(),weather_date);
        Uri uri = Ubuilder.build();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,uri.toString());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DetailsActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(PENDING_INTENT_CODE,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Current Temparature "+currentTemp+" degree")
                .setContentText("Sunshine Weather data is now Updated")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE | DEFAULT_SOUND);

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID,builder.build());
    }

    public static void cancelNotification(Context context){
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
}
