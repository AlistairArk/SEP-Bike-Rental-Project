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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

public class CreateBooking extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateDataSetListener;
    private TimePickerDialog.OnTimeSetListener timeDataSetListener;

    private LinearLayout dateViewContainer;
    private LinearLayout timeViewContainer;
    private TextView textViewDayOfMonth;
    private TextView textViewMonth;
    private TextView textViewDayOfWeek;
    private TextView textViewTimeOfDay;
    private TextView textViewTimeId;
    private ElegantNumberButton bikeNumberWidget;

    private Boolean is24HourView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        textViewDayOfMonth = (TextView) findViewById(R.id.textViewDayOfMonth);
        textViewMonth = (TextView) findViewById(R.id.textViewMonth);
        textViewDayOfWeek = (TextView) findViewById(R.id.textViewDayOfWeek);

        textViewTimeOfDay = (TextView) findViewById(R.id.textViewTimeOfDay);
        textViewTimeId = (TextView) findViewById(R.id.textViewTimeId);

        dateViewContainer = (LinearLayout) findViewById(R.id.dateViewContainer);
        timeViewContainer = (LinearLayout) findViewById(R.id.timeViewContainer);

        bikeNumberWidget = (ElegantNumberButton) findViewById(R.id.bikeNumber);

        dateViewContainer.setOnClickListener(new View.OnClickListener() {
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
                Date date = new Date(year, month, day-1);
                Calendar cal = Calendar.getInstance();
                Locale locale = Locale.getDefault();

                cal.setTime(date);
                String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);

                date = new Date(year, month, day);
                cal.setTime(date);
                String monthOfYear = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale);



                String stringDate = monthOfYear + " " + year;

                textViewDayOfWeek.setText(dayOfWeek);
                textViewMonth.setText(stringDate);
                textViewDayOfMonth.setText(convertDate(day));
            }
        };

        timeViewContainer.setOnClickListener(new View.OnClickListener() {
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

                String date = hour + ":" + convertDate(minute);
                textViewTimeOfDay.setText(date);
                textViewTimeId.setText(timeOfDay);
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
