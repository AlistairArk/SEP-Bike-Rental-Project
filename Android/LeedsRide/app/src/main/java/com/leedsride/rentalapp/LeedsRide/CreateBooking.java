package com.leedsride.rentalapp.LeedsRide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.leedsride.rentalapp.LeedsRide.Data.Booking;
import com.leedsride.rentalapp.LeedsRide.models.Book;
import com.leedsride.rentalapp.LeedsRide.models.Register;
import com.xw.repo.BubbleSeekBar;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import com.braintreepayments.api.dropin.DropInRequest;

public class CreateBooking extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateDataSetListener;
    private TimePickerDialog.OnTimeSetListener timeDataSetListener;

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

    private String startDateTime;
    private String endDateTime;
    private String apiDate;
    private String apiTime;

    private Boolean is24HourView = false;

    private static final int REQUEST_CODE = 4949;

    private Booking booking;
    private static final String BASE_URL = "https://sc17gs.pythonanywhere.com/api/";
    private static final String TAG = CreateBooking.class.getSimpleName();

    Book book = new Book();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        Intent intent = getIntent();
        booking = intent.getParcelableExtra("booking");

        /**
         * Initialising UI objects
         */

        Button completeBooking = (Button) findViewById(R.id.completeBooking);

        TextView bookingLocation = (TextView)findViewById(R.id.bookingLocation);
        bookingLocation.setText(booking.getBookingLocation());

        textViewDayOfMonth = (TextView)findViewById(R.id.textViewDayOfMonth);
        textViewMonth = (TextView)findViewById(R.id.textViewMonth);
        textViewDayOfWeek = (TextView)findViewById(R.id.textViewDayOfWeek);

        textViewTimeOfDay = (TextView)findViewById(R.id.textViewTimeOfDay);
        textViewTimeId = (TextView)findViewById(R.id.textViewTimeId);

        rentalReturnDate = (TextView)findViewById(R.id.rentalReturnDate);

        LinearLayout dateViewContainer = (LinearLayout)findViewById(R.id.dateViewContainer);
        LinearLayout timeViewContainer = (LinearLayout)findViewById(R.id.timeViewContainer);

        ElegantNumberButton bikeNumberWidget = (ElegantNumberButton)findViewById(R.id.bikeNumber);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar)findViewById(R.id.bubbleSeekBar);

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
        int month = bookingDateTime.get(Calendar.MONTH);
        String apiDay = convertDate(day);
        String apiMonth = convertDate(month+1);

        apiDate = year + "-" + apiMonth + "-" + apiDay;
        apiTime = "T" + convertDate(bookingHour) + ":" + convertDate(bookingMinute);

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


        completeBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  onBraintreeSubmit(null);
            }
        });


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

                apiDate = year + "-" + convertDate(month+1) + "-" + convertDate(day);
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

                apiTime = "T" + convertDate(hour) + ":" + convertDate(minute);

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
        int month = temp.get(Calendar.MONTH);
        int year = temp.get(Calendar.YEAR);

        endDateTime = year + "-" + convertDate(month+1) + "-" + convertDate(day)+ "T" + convertDate(hour) + ":" + convertDate(min);

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

    private void sendNetworkRequest(final Book request) {

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptRegister method from restAPI interface
        Call<Book> call = sampleAPI.makeBooking(request);

        //add call to queue (in this case nothing in queue)
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                String reply = response.body().getBookingStatus();
                //Log.d(TAG, reply);

                if(reply.equals("Accepted")){ ////////////Update once server has been changed
                    Toast.makeText(getApplicationContext(), startDateTime+endDateTime, Toast.LENGTH_LONG).show();
                    Intent startMainMenu = new Intent(getApplicationContext(), MyOrders.class);
                    startActivity(startMainMenu);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("error", "Could not connect to external API");
                Toast.makeText(getApplicationContext(), "Network Error: please check connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                book.setUsername(SaveSharedPreference.getPrefUsername(getApplicationContext()));//from shared prer
                book.setPassword(SaveSharedPreference.getPrefPassword(getApplicationContext()));
                book.setBikeNumber(numberOfBikes);

                startDateTime = apiDate + apiTime;

                book.setStartTime(startDateTime);
                book.setEndTime(endDateTime);
                book.setStartLocation(booking.getBookingLocation());
                book.setEndLocation(booking.getBookingLocation());

                Log.d(TAG, book.toString());

                sendNetworkRequest(book);
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
//                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }

    public void onBraintreeSubmit(View v) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJiYjljNTYyZGZmMGY2MDE0ZDRmMTliYTE3OTVlNTIwZWVjMjA0OGMzMzVkMGQ1OGE0NzRiNjE3ZmM1MWZhNWFkfGNyZWF0ZWRfYXQ9MjAxOS0wNS0wMlQyMzoyNTo0My44MzIyMDYwMTcrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJncmFwaFFMIjp7InVybCI6Imh0dHBzOi8vcGF5bWVudHMuc2FuZGJveC5icmFpbnRyZWUtYXBpLmNvbS9ncmFwaHFsIiwiZGF0ZSI6IjIwMTgtMDUtMDgifSwiY2hhbGxlbmdlcyI6W10sImVudmlyb25tZW50Ijoic2FuZGJveCIsImNsaWVudEFwaVVybCI6Imh0dHBzOi8vYXBpLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb206NDQzL21lcmNoYW50cy8zNDhwazljZ2YzYmd5dzJiL2NsaWVudF9hcGkiLCJhc3NldHNVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImF1dGhVcmwiOiJodHRwczovL2F1dGgudmVubW8uc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFuYWx5dGljcyI6eyJ1cmwiOiJodHRwczovL29yaWdpbi1hbmFseXRpY3Mtc2FuZC5zYW5kYm94LmJyYWludHJlZS1hcGkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }
}
