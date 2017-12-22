package com.example.rdas6313.sunshine;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdas6313.sunshine.AdapterClasses.ListItemListener;
import com.example.rdas6313.sunshine.AdapterClasses.RecycleAdapter;
import com.example.rdas6313.sunshine.NetworkCall.JsonUtils;
import com.example.rdas6313.sunshine.NetworkCall.NetworkRequestHandeler;
import com.example.rdas6313.sunshine.data.SunshinePreference;
import com.example.rdas6313.sunshine.data.WeatherContract;
import com.example.rdas6313.sunshine.sync.SunshineReciver;
import com.example.rdas6313.sunshine.sync.SunshineSyncTask;
import com.example.rdas6313.sunshine.sync.SunshineSyncUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>,ListItemListener,SharedPreferences.OnSharedPreferenceChangeListener{
    private IntentFilter intentFilter;
    private SunshineReciver reciver;
    private String location = "katwa";
    private boolean reload = false;
    private final int LOADER_ID = 1;
    private RecycleAdapter adapter;
    private RecyclerView recyclerView = null;
    private TextView errorView = null;
    private ProgressBar progressBarView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.weatherrecycleView);
        adapter = new RecycleAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        errorView = (TextView)findViewById(R.id.errorView);
        errorView.setText(getString(R.string.error_msg)+". "+getString(R.string.error_connection));
        progressBarView = (ProgressBar)findViewById(R.id.progressbarView);

        //SunshineSyncUtils.initialize(this);

        loadWeatherData(true);

        reciver = new SunshineReciver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(reciver,intentFilter);
    }



    private void loadWeatherData(Boolean loaderSwitch){
        if(loaderSwitch)
            getLoaderManager().initLoader(LOADER_ID,null,this);
        else
            getLoaderManager().restartLoader(LOADER_ID,null,this);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        errorView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBarView.setVisibility(View.VISIBLE);
        String projection[] = null;
        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE;
        return new CursorLoader(this, WeatherContract.WeatherEntry.CONTENT_URI,projection,selection,selectionArgs,sortOrder);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if((data == null || data.getCount() == 0) && !SunshineSyncUtils.isInternetAvailable(this)) {
            progressBarView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }
        else if(data != null && data.getCount() > 0){
            progressBarView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast,menu);
        return true;
    }

    private void openMap(){
        String userLocation = SunshinePreference.getPreferedLocation(this);
        Uri uri = Uri.parse("geo:0,0?q="+userLocation);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh_button:
                SunshineSyncUtils.startImmediateSync(this);
                loadWeatherData(false);
                return true;
            case R.id.settings_button:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.map_button:
                openMap();
                return true;
            default:
                return false;
        }

    }


    @Override
    public void onItemClick(int itemPosition) {
        long dateId = adapter.getDateID(itemPosition);
        if(dateId == -1)
            return;
        Uri uri = WeatherContract.WeatherEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(dateId)).build();
        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,uri.toString());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(reload){
            getLoaderManager().restartLoader(LOADER_ID,null,this);
            reload = false;
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        reload = true;
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(reciver);
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }
}
