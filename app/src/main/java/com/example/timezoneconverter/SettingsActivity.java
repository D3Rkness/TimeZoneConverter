package com.example.timezoneconverter;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {
    TextInputEditText homeCityEdit;
    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creates page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        // Find home city text component of page
        homeCityEdit = findViewById(R.id.editCityText);

        // Create shared preferences
        Context context = getApplicationContext();
        myPrefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Set home city text
        homeCityEdit.setText(myPrefs.getString("HomeCityName", "Baltimore"));
    }

    public void returnToMain(View view) {
        // Obtain the text from spinner and home city
        Spinner settingsSpinner = findViewById(R.id.settingsTimeZoneSpinner);
        String selectedTimeZone = settingsSpinner.getSelectedItem().toString();
        String cityName = "";
        if (homeCityEdit.getText() != null) {
            cityName = homeCityEdit.getText().toString();
        }

        // Set shared preferences
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("SelectedTimeZone", selectedTimeZone);
        editor.putString("HomeCityName", cityName);
        editor.apply();

        // Go back to main activity
        finish();
    }

}