package com.example.asus.trendhimapp.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.asus.trendhimapp.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load preferences from the XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}

