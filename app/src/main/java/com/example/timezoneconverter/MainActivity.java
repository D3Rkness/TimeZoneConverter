package com.example.timezoneconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button timeButton;
    int hour, minute;
    String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeButton = findViewById(R.id.timeButton);
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;

            if (hour >= 12) {
                period = "PM";
                if (hour > 12) {
                    hour -= 12;
                }
            } else {
                period = "AM";
                if (hour == 0) {
                    hour = 12;
                }
            }

            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",hour, minute, period));
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}