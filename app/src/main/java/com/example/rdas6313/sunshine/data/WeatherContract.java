package com.example.rdas6313.sunshine.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.StringBuilderPrinter;

/**
 * Created by rdas6313 on 31/7/17.
 */

public class WeatherContract {

    public static final String AUTHORITY = "com.example.rdas6313.sunshine";

    public static final String PATH_WEATHER = "weather";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final class WeatherEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_MIN_TEMP = "min_temp";
        public static final String COLUMN_MAX_TEMP = "max_temp";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure_inchis";
        public static final String COLUMN_ICON = "icon_and_Daynight";
        public static final String COLUMN_CURRENT_TEMP = "current_temp";
        public static final String COLUMN_WIND_SPEED = "wind";
        //public static final String COLUMN_DEGREE = "degrees";
        public static final String COLUMN_CONDITION = "condition";

        public static final String WEATHER_DIR_MIME = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"
                +AUTHORITY+"/"+PATH_WEATHER;
        public static final String WEATHER_ITEM_MIME = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"
                +AUTHORITY+"/"+PATH_WEATHER;
    }
}
