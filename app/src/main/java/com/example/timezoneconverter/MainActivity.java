package com.example.timezoneconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Button timeButton;
    int hour, minute;
    String period;
    TextView homeTimeZoneText;
    Spinner currentTimeZoneText;
    ImageView warningSign;
    TextView convertedTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creates page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find all components of page
        timeButton = findViewById(R.id.timeButton);
        homeTimeZoneText = findViewById(R.id.homeTimeText);
        currentTimeZoneText = (Spinner)findViewById(R.id.timeZoneSpinner);
        warningSign = findViewById(R.id.warningSign);
        convertedTimeText = findViewById(R.id.convertedTimeText);


        // Obtain current time using calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        // Obtain the current hour and minute
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Obtain the period (am/pm) of the current time
        int periodInt = calendar.get(Calendar.AM_PM);
        if (periodInt == 0) {
            period = "AM";
        } else {
            period = "PM";
        }

        // Adjust hour to fit 12hr time format
        int adjustedHour = hour;
        if (hour > 12) {
            adjustedHour -= 12;
        }
        if (hour == 0) {
            adjustedHour = 12;
        }

        // Set the original time default as the current time and set converted default time
        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",adjustedHour, minute, period));
        convertTime(convertedTimeText);

        // Set default Home Time Zone
        String defaultHomeTime = "America/Los_Angeles: GMT -08:00";
        homeTimeZoneText.setText(defaultHomeTime);
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            // Obtain the selected hour and minute, initialize variables
            hour = selectedHour;
            minute = selectedMinute;
            int adjustedHour = hour;

            // Obtain the period and adjust hour to fit 12hr clock
            if (hour >= 12) {
                period = "PM";
                if (hour > 12) {
                    adjustedHour -= 12;
                }
            } else {
                period = "AM";
                if (hour == 0) {
                    adjustedHour = 12;
                }
            }

            // Set the original time as the new time picked by the user
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",adjustedHour, minute, period));
        };

        // Modify time picker dialog settings
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void convertTime(View view) {
        // Obtain information about the home time zone
        String homeTimeZone = homeTimeZoneText.getText().toString();
        char homeTimeSign = homeTimeZone.charAt(homeTimeZone.length() - 6);
        int homeTimeDigit = (homeTimeZone.charAt(homeTimeZone.length() - 4)) - '0';

        // Obtain information about the current time zone
        String currTimeZone = currentTimeZoneText.getSelectedItem().toString();
        char currTimeSign = currTimeZone.charAt(currTimeZone.length() - 6);
        int currTimeDigit = (currTimeZone.charAt(currTimeZone.length() - 4)) - '0';

        // Error handling if same time zones for conversion
        if (homeTimeZone.equals(currTimeZone)) {
            String text = "Current & Home Time Zone Are Same";
            int duration = Toast.LENGTH_LONG;

            // Creation and display of toast
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        }

        int convertedHour = hour;

        // Get the time difference between current and home time to modify hour
        if (homeTimeSign == currTimeSign) {
            convertedHour += currTimeDigit - homeTimeDigit;
        } else if (homeTimeSign == '-') {
            convertedHour -= homeTimeDigit + currTimeDigit;
        } else {
            convertedHour += homeTimeDigit + currTimeDigit;
        }

        // Ensure hours are within a 24 hour range, adjust accordingly
        while (convertedHour >= 24) {
            convertedHour -= 24;
        }
        while (convertedHour < 0) {
            convertedHour += 24;
        }

        // Obtain the period of the time
        if (convertedHour >= 12) {
            // Convert the time back to the 12hr range
            if (convertedHour > 12) {
                convertedHour -= 12;
            }
            // Set period to PM since hour is past 12
            if (period.equals("AM")) {
                period = "PM";
            }
            else if (convertedHour == 12) {
                period = "PM";
            }

            // Do not disturb warning symbol logic
            if (convertedHour == 11) {
                warningSign.setVisibility(View.VISIBLE);
            } else {
                warningSign.setVisibility(View.GONE);
            }
        } else {
            // Do not disturb warning symbol logic
            if (convertedHour <= 7) {
                warningSign.setVisibility(View.VISIBLE);
            } else {
                warningSign.setVisibility(View.GONE);
            }

            if (convertedHour == 0) {
                // Ensure hour of 0 will set the period as AM
                convertedHour = 12;
                period = "AM";
            } else if (period.equals("PM")) {
                // Set period to PM since hour is before 12
                period = "AM";
            }
        }

        // Display the converted time inside the textview
        convertedTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d %s", convertedHour, minute, period));
    }

    public void viewSettings(View view) {
        // Go to the settings page
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the selected timezone from SharedPreferences
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        String selectedTimeZone = sharedPref.getString("SelectedTimeZone", "America/Los_Angeles: GMT -08:00");
        homeTimeZoneText.setText(selectedTimeZone);

        // Convert time from the new home town, same time zone will output toast and stop conversion
        convertTime(convertedTimeText);
    }
}