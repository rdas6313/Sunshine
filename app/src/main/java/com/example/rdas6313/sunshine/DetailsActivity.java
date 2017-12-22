package com.example.rdas6313.sunshine;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rdas6313.sunshine.data.WeatherContract;
import com.example.rdas6313.sunshine.databinding.ActivityDetailsBinding;
import com.example.rdas6313.sunshine.sync.IconAndDaynightUtils;
import com.example.rdas6313.sunshine.sync.SunshineReciver;

import java.net.NetPermission;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private String weatherData;
    private Uri weatherUri;
    private TextView dateView,minTempView,maxTempView,humidityView,pressureView,windView,currentTempView;
    private final int LOADER_ID = 2;
    private ActivityDetailsBinding detailsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_details);
        weatherUri = null;
        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            weatherUri = Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);


    }

    private void shareWeatherData(){
        String type = "text/plain";
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
        Intent intent = intentBuilder.setType(type).setText(weatherData).getIntent();
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sharedetails,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                if(weatherData.length() > 0) {
                    shareWeatherData();
                    return true;
                }
                return false;
            case R.id.settings_button:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(weatherUri == null)
            return null;
        String projection[] = null;
        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = null;
        return new CursorLoader(this,weatherUri,projection,selection,selectionArgs,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null || !data.moveToFirst())
            return;
        int weather_id = data.getInt(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));
        long timeStamp = data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE));
        double min_temp = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
        double max_temp = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
        double humidity = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY));
        String condition = data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_CONDITION));
        double wind = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED));
        weatherData = DateUtils.convertTo(timeStamp)+" \n "+min_temp+" \n "+max_temp+" \n "+humidity+" \n "+condition+" \n "+wind;

        String iconAnddaynight = data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_ICON));
        String icon = IconAndDaynightUtils.extractIcon(this,iconAnddaynight);
        int resId = getResources().getIdentifier(icon,"drawable",getPackageName());

        detailsBinding.detailsUpperLayout.detailsWeatherDate.setText(DateUtils.convertTo(timeStamp));
        detailsBinding.detailsUpperLayout.detailsWeatherIcon.setImageResource(resId);
        detailsBinding.detailsUpperLayout.detailsWeatherCondition.setText(condition);
        detailsBinding.detailsUpperLayout.detailsWeatherTempMax.setText(getString(R.string.temp,max_temp));
        detailsBinding.detailsUpperLayout.detailsWeatherTempMin.setText(getString(R.string.temp,min_temp));

        detailsBinding.detailsLowerLayout.avgHumidityValue.setText(getString(R.string.avghumidity,humidity));
        detailsBinding.detailsLowerLayout.windValue.setText(getString(R.string.windvalue,wind));
        if(weather_id == 0){
            double pressure = data.getDouble(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE));
            detailsBinding.detailsLowerLayout.pressureValue.setText(getString(R.string.pressurevalue,pressure));
            detailsBinding.detailsLowerLayout.pressureLabel.setVisibility(View.VISIBLE);
            detailsBinding.detailsLowerLayout.pressureValue.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        weatherUri = null;
        weatherData = "";
    }
}
