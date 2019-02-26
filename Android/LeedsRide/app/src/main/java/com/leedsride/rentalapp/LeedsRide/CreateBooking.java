package com.leedsride.rentalapp.LeedsRide;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

public class CreateBooking extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateDataSetListener;
    private TimePickerDialog.OnTimeSetListener timeDataSetListener;
    private TextView selectBookingDateBtn;
    private TextView selectBookingTimeBtn;
    private Boolean is24HourView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        selectBookingDateBtn = (TextView) findViewById(R.id.selectBookingDateBtn);
        selectBookingTimeBtn = (TextView) findViewById(R.id.selectBookingTimeBtn);

        selectBookingDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateBooking.this,
                        R.style.CalendarDatePickerDialog,
                        dateDataSetListener,
                        year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        dateDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Date date = new Date(year, month, day);
                Calendar cal = Calendar.getInstance();
                Locale locale = Locale.getDefault();

                cal.setTime(date);
                String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
                String monthOfYear = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);



                String stringDate = dayOfWeek + " " + convertDate(day) + " " + monthOfYear + ", " + year;
                selectBookingDateBtn.setText(stringDate);
            }
        };

        selectBookingTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minutes = currentTime.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(CreateBooking.this,
                        R.style.CalendarTimePickerDialog,
                        timeDataSetListener,
                        hour, minutes, is24HourView);

                dialog.show();
            }
        });

        timeDataSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String timeOfDay;
                if (hour < 12) {
                    timeOfDay = "(AM)";
                }
                else {
                    timeOfDay = "(PM)";
                }

                String date = hour + ":" + convertDate(minute) + " " + timeOfDay;
                selectBookingTimeBtn.setText(date);
            }
        };


    }

    private String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }
}
