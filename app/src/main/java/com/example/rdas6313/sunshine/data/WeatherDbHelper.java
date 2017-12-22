package com.example.rdas6313.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rdas6313 on 31/7/17.
 */

public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 3;

    public WeatherDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         final String CREATE_TABLE = "CREATE TABLE "+ WeatherContract.WeatherEntry.TABLE_NAME+"("
                + WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WeatherContract.WeatherEntry.COLUMN_DATE + " TIMESTAMP NOT NULL UNIQUE,"
                + WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL,"
                 + WeatherContract.WeatherEntry.COLUMN_PRESSURE + " REAL DEFAULT NULL,"
                 + WeatherContract.WeatherEntry.COLUMN_CURRENT_TEMP + " REAL DEFAULT NULL,"
                 + WeatherContract.WeatherEntry.COLUMN_ICON + " TEXT NOT NULL,"
                + WeatherContract.WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL,"
                + WeatherContract.WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL,"
                + WeatherContract.WeatherEntry.COLUMN_CONDITION + " TEXT NOT NULL,"
                + WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL,"
                + WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ WeatherContract.WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
