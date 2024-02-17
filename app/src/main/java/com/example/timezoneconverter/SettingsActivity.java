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
    Spinner settingsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creates page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        // Find all components of page
        homeCityEdit = findViewById(R.id.editCityText);
        settingsSpinner = findViewById(R.id.settingsTimeZoneSpinner);

        // Create shared preferences
        Context context = getApplicationContext();
        myPrefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Set home time zone and city text
        homeCityEdit.setText(myPrefs.getString("HomeCityName", "Baltimore"));
        settingsSpinner.setSelection(myPrefs.getInt("TimeZoneSelectionNumber", 1));
    }

    public void returnToMain(View view) {
        // Obtain the text from spinner and home city
        String selectedTimeZone = settingsSpinner.getSelectedItem().toString();
        String cityName = "";
        if (homeCityEdit.getText() != null) {
            cityName = homeCityEdit.getText().toString();
        }

        // Find corresponding selection number in spinner for the selected time zone
        int selectionNumber;
        switch (selectedTimeZone) {
            case ("America/Los_Angeles: GMT -08:00"):
                selectionNumber = 1;
                break;
            case ("Europe/Berlin: GMT +01:00"):
                selectionNumber = 2;
                break;
            case ("Europe/Istanbul: GMT +02:00"):
                selectionNumber = 3;
                break;
            case ("Asia/Bangkok: GMT +07:00"):
                selectionNumber = 4;
                break;
            case ("Asia/Singapore: GMT +08:00"):
                selectionNumber = 5;
                break;
            case ("Asia/Tokyo: GMT +09:00"):
                selectionNumber = 6;
                break;
            case ("Australia/Canberra: GMT +10:00"):
                selectionNumber = 7;
                break;
            default:
                selectionNumber = 0;
                break;
        }

        // Set shared preferences
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("SelectedTimeZone", selectedTimeZone);
        editor.putInt("TimeZoneSelectionNumber", selectionNumber);
        editor.putString("HomeCityName", cityName);
        editor.apply();

        // Go back to main activity
        finish();
    }



}