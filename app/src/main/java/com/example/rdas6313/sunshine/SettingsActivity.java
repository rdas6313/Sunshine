package com.example.rdas6313.sunshine;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.MenuItem;

import com.example.rdas6313.sunshine.sync.SunshineReciver;
import com.example.rdas6313.sunshine.sync.SunshineSyncUtils;

public class SettingsActivity extends AppCompatActivity {
    private String FRAGTAG = "settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment(),FRAGTAG).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public final static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref);
            android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
            EditTextPreference editTextPreference = (EditTextPreference) findPreference(getResources().getString(R.string.pref_location_key));
            bindToSummary(editTextPreference);
            ListPreference listPreference = (ListPreference)findPreference(getString(R.string.pref_units_key));
            bindToSummary(listPreference);
        }
        private void bindToSummary(Preference preference){

            if(preference instanceof ListPreference){
                int index = ((ListPreference) preference).findIndexOfValue(((ListPreference) preference).getValue());
                CharSequence[] entries = ((ListPreference) preference).getEntries();
                if(index >= 0 && index < entries.length){
                    preference.setSummary(entries[index]);
                    return;
                }
            }else if(preference instanceof EditTextPreference){
                preference.setSummary(((EditTextPreference) preference).getText());
                return;
            }

        }
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference = findPreference(key);
            bindToSummary(preference);
            SunshineSyncUtils.startImmediateSync(getContext());
        }

        @Override
        public void onDestroyView() {
            android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
            super.onDestroyView();
        }
    }
}
