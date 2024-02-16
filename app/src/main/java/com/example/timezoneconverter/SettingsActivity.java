package com.example.timezoneconverter;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
    }

    public void returnToMain(View view) {
        Spinner settingsSpinner = findViewById(R.id.settingsTimeZoneSpinner); // Assuming this is your Spinner ID
        String selectedTimeZone = settingsSpinner.getSelectedItem().toString();

        Context context = getApplicationContext();
        SharedPreferences myPrefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("SelectedTimeZone", selectedTimeZone);
        editor.apply();
        finish();
    }

}