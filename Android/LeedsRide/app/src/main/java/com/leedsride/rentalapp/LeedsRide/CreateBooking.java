package com.leedsride.rentalapp.LeedsRide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.xw.repo.BubbleSeekBar;

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
    private TextView rentalReturnDate;
    private Calendar bookingDateTime;
    private int numberOfBikes;
    private int rentalDuration;
    private int bookingHour, bookingMinute;
    private float price;
    private BubbleSeekBar bubbleSeekBar;
    private ElegantNumberButton bikeNumberWidget;

    private Boolean is24HourView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        /**
         * Initialising UI objects
         */

        textViewDayOfMonth = (TextView)findViewById(R.id.textViewDayOfMonth);
        textViewMonth = (TextView)findViewById(R.id.textViewMonth);
        textViewDayOfWeek = (TextView)findViewById(R.id.textViewDayOfWeek);

        textViewTimeOfDay = (TextView)findViewById(R.id.textViewTimeOfDay);
        textViewTimeId = (TextView)findViewById(R.id.textViewTimeId);

        rentalReturnDate = (TextView)findViewById(R.id.rentalReturnDate);

        dateViewContainer = (LinearLayout)findViewById(R.id.dateViewContainer);
        timeViewContainer = (LinearLayout)findViewById(R.id.timeViewContainer);

        bikeNumberWidget = (ElegantNumberButton)findViewById(R.id.bikeNumber);
        bubbleSeekBar = (BubbleSeekBar)findViewById(R.id.bubbleSeekBar);

        TextView elegantCounter = (TextView)findViewById(R.id.number_counter);
        Button elegantAdd = (Button)findViewById(R.id.add_btn);
        Button elegantSubtract = (Button)findViewById(R.id.subtract_btn);
        ImageView bookingHeaderBullet = (ImageView)findViewById(R.id.bookingHeaderBullet);

        int colour = this.getResources().getColor(R.color.primaryBlue);
        bookingHeaderBullet.getDrawable().setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);
        numberOfBikes = 1;
        rentalDuration = 1;

        calculatePrice();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.open_sans_light);
        elegantCounter.setTypeface(typeface);

        elegantAdd.setTypeface(typeface);
        elegantSubtract.setTypeface(typeface);

        /**
         * Handle initial calender instances
         * Set the UI text views to the current date/time values
         * reset the return date
         */

        bookingDateTime = Calendar.getInstance();
        Locale locale = Locale.getDefault();

        bookingHour = bookingDateTime.get(Calendar.HOUR_OF_DAY);
        bookingMinute = bookingDateTime.get(Calendar.MINUTE);
        int year = bookingDateTime.get(Calendar.YEAR);
        int day = bookingDateTime.get(Calendar.DAY_OF_MONTH);
        String dayOfWeek = bookingDateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
        String monthOfYear = bookingDateTime.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale);

        String stringDate = monthOfYear + " " + year;

        textViewDayOfWeek.setText(dayOfWeek);
        textViewMonth.setText(stringDate);
        textViewDayOfMonth.setText(convertDate(day));

        String timeOfDay;
        if (bookingHour < 12) {
            timeOfDay = "(AM)";
        }
        else {
            timeOfDay = "(PM)";
        }

        String date = bookingHour + ":" + convertDate(bookingMinute);
        textViewTimeOfDay.setText(date);
        textViewTimeId.setText(timeOfDay);

        setReturnDate();


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
                bookingDateTime.set(year, month, day, bookingHour, bookingMinute);

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

                setReturnDate();
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
                bookingDateTime.set(Calendar.HOUR_OF_DAY, hour);
                bookingDateTime.set(Calendar.MINUTE, minute);
                bookingDateTime.getTime();
                bookingHour = hour;
                bookingMinute = minute;

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

                setReturnDate();
            }
        };

        bikeNumberWidget.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                numberOfBikes = newValue;
                calculatePrice();
            }
        });

        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                if (progress != rentalDuration){
                    rentalDuration = progress;
                    setReturnDate();
                    calculatePrice();
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });


    }

    private String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    private void calculatePrice() {
        float time = (float) rentalDuration;
        float bikes = (float) numberOfBikes;

        price = (bikes*3.5f)+(time/2*bikes*0.1f);
        TextView priceText = (TextView)findViewById(R.id.showRentalCost);
        //String priceString = String.valueOf(price);
        String priceString = String.format("%.2f", price);
        priceText.setText("Total: £"+priceString);
    }

    private void setReturnDate() {

        Calendar temp = (Calendar)bookingDateTime.clone();
        temp.add(Calendar.HOUR_OF_DAY, rentalDuration);
        temp.getTime();
        Locale locale = Locale.getDefault();

        int hour = temp.get(Calendar.HOUR_OF_DAY);
        int min = temp.get(Calendar.MINUTE);
        int day = temp.get(Calendar.DAY_OF_MONTH);
        int year = temp.get(Calendar.YEAR);

        String timeOfDay;
        if (hour < 12) {
            timeOfDay = "AM";
        }
        else {
            timeOfDay = "PM";
        }


        String dayOfWeek = temp.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
        String monthOfYear = temp.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale);

        String returnDate = hour + ":" + convertDate(min) + " " + timeOfDay + ", " + dayOfWeek + " " + day + " " + monthOfYear + " " + year;
        rentalReturnDate.setText(returnDate);
    }
}
