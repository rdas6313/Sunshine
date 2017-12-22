package com.example.rdas6313.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by rdas6313 on 4/8/17.
 */

public class WeatherProvider extends ContentProvider {
    private WeatherDbHelper helper;
    private UriMatcher matcher;
    private  final int WEATHER = 100;
    private  final int WEATHER_WITH_DATE = 101;
    @Override
    public boolean onCreate() {

        helper = new WeatherDbHelper(getContext());
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(WeatherContract.AUTHORITY,WeatherContract.PATH_WEATHER,WEATHER);
        matcher.addURI(WeatherContract.AUTHORITY,WeatherContract.PATH_WEATHER+"/#",WEATHER_WITH_DATE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int match = matcher.match(uri);
        Cursor cursor = null;
        switch (match){
            case WEATHER:
                cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WEATHER_WITH_DATE:
                selection = WeatherContract.WeatherEntry.COLUMN_DATE + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+uri);
        }
        if(cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = matcher.match(uri);
        switch (match){
            case WEATHER:
                return WeatherContract.WeatherEntry.WEATHER_DIR_MIME;
            case WEATHER_WITH_DATE:
                return WeatherContract.WeatherEntry.WEATHER_ITEM_MIME;
            default:
                throw new UnsupportedOperationException("unknown uri "+uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int match = matcher.match(uri);
        switch (match){
            case WEATHER:
                int row = 0;
                db.beginTransaction();
                try {
                    row = 0;
                    for(ContentValues value:values){
                        long id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,null,value);
                        if(id != -1)
                            row++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if(row > 0)
                    getContext().getContentResolver().notifyChange(uri,null);
                return row;

            default:
                return super.bulkInsert(uri, values);
        }



    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int match = matcher.match(uri);
        int row = 0;
        if(selection == null)
            selection = "1";
        switch (match){
            case WEATHER:
                row = db.delete(WeatherContract.WeatherEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case WEATHER_WITH_DATE:
                selection = WeatherContract.WeatherEntry.COLUMN_DATE+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                row = db.delete(WeatherContract.WeatherEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unknown uri "+uri);
        }
        if(row > 0)
            getContext().getContentResolver().notifyChange(uri,null);
        return row;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
