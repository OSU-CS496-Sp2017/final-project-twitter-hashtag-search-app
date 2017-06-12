package com.example.xiaoli.twitterhashtagsearcher;

/**
 * Created by Xiaoli on 2017/6/12.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.ListPreference;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
        EditTextPreference count_Pref = (EditTextPreference)findPreference(getString(R.string.pref_count_key));
        ListPreference result_type_Pref = (ListPreference)findPreference(getString(R.string.pref_result_type_key));
        ListPreference language_Pref = (ListPreference) findPreference(getString(R.string.pref_language_key));
        result_type_Pref.setSummary(result_type_Pref.getValue());
        language_Pref.setSummary((language_Pref.getValue()));
        count_Pref.setSummary(count_Pref.getText());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_count_key))) {
            EditTextPreference count_Pref = (EditTextPreference)findPreference(key);
            count_Pref.setSummary(count_Pref.getText());
        }

        if(key.equals(getString(R.string.pref_result_type_key))){
            ListPreference result_type_Pref = (ListPreference)findPreference(key);
            result_type_Pref.setSummary(result_type_Pref.getValue());
        }

        if(key.equals(getString(R.string.pref_language_key))){
            ListPreference language_Pref = (ListPreference) findPreference(key);
            language_Pref.setSummary(language_Pref.getValue());
        }
    }
}
