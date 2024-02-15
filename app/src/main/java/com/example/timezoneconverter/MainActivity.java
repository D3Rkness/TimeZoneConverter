package com.example.timezoneconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;


import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Button timeButton;
    int hour, minute;
    String period;
    TextView homeTimeZoneText;
    Spinner currentTimeZoneText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeButton = findViewById(R.id.timeButton);
        homeTimeZoneText = findViewById(R.id.homeTimeText);
        currentTimeZoneText = (Spinner)findViewById(R.id.timeZoneSpinner);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        int periodInt = calendar.get(Calendar.AM_PM);
        if (periodInt == 0) {
            period = "AM";
        } else {
            period = "PM";
        }

        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",hour, minute, period));
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            int adjustedHour = hour;

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

            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",adjustedHour, minute, period));
        };

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

        int convertedHour = hour;

        if (homeTimeSign == currTimeSign) {
            convertedHour += currTimeDigit - homeTimeDigit;
        } else if (homeTimeSign == '-') {
            convertedHour -= homeTimeDigit + currTimeDigit;
        } else {
            convertedHour += homeTimeDigit + currTimeDigit;
        }

        while (convertedHour >= 24) {
            convertedHour -= 24;
        }
        while (convertedHour < 0) {
            convertedHour += 24;
        }

        if (convertedHour >= 12) {
            if (convertedHour > 12) {
                convertedHour -= 12;
            }
            if (period.equals("AM")) {
                period = "PM";
            }
            else if (convertedHour == 12) {
                period = "PM";
            }
        } else {
            if (convertedHour == 0) {
                convertedHour = 12;
                period = "AM";
            } else if (period.equals("PM")) {
                period = "AM";
            }
        }

        TextView convertedTimeText = findViewById(R.id.convertedTimeText);
        convertedTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d %s", convertedHour, minute, period));
    }

    public void viewSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}