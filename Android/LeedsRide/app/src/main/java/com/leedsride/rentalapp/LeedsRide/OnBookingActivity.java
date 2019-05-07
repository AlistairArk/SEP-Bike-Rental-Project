package com.leedsride.rentalapp.LeedsRide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OnBookingActivity extends AppCompatActivity {

    private long START_TIME_IN_MILIS;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private TextView rentalTimer;
    private TextView warning;
    private ProgressBar rentalProgressBar;
    private Button returnBikes;
    private int bikeNumber;
    private int bookingID;
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bikeNumber = extras.getInt("BIKE_NUMBER");
            bookingID = extras.getInt("BOOKING_ID");
            endDate = extras.getString("END_DATE");
        }


        Calendar endDateTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        Locale locale = Locale.getDefault();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", locale);

        try {
            endDateTime.setTime(sdf.parse(endDate));
        }
        catch (ParseException e) {
            Log.e("Parse", "Error");
        }

        int compare = now.compareTo(endDateTime);

        setContentView(R.layout.activity_on_booking);
        rentalTimer = (TextView)findViewById(R.id.rentalTimer);
        rentalProgressBar = (ProgressBar)findViewById(R.id.rentalProgressBar);
        returnBikes = (Button)findViewById(R.id.returnBikes);

        if (compare == 1) {
            warning = (TextView)findViewById(R.id.warning);
            warning.setVisibility(View.VISIBLE);
        }
        else {

            START_TIME_IN_MILIS = endDateTime.getTimeInMillis() - now.getTimeInMillis();
            timeLeftInMillis = START_TIME_IN_MILIS;

            System.out.println("##############################################"+START_TIME_IN_MILIS);

            startTimer();

        }


        returnBikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startBarcode = new Intent(getApplicationContext(), BarcodeScanner.class);
                startBarcode.putExtra("BIKE_NUMBER", bikeNumber);
                startBarcode.putExtra("BOOKING_ID", bookingID);
                startBarcode.putExtra("COMMIT", "return");
                startActivity(startBarcode);
            }
        });


    }

    private  void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int seconds = (int) (timeLeftInMillis / 1000) % 60 ;
                int minutes = (int) ((timeLeftInMillis / (1000*60)) % 60);
                int hours   = (int) ((timeLeftInMillis / (1000*60*60)));

                int percentage = (int) (0.5d + ((double)timeLeftInMillis/(double)START_TIME_IN_MILIS) * 100);

                rentalProgressBar.setProgress(percentage);

                String timeLeft = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
                rentalTimer.setText(timeLeft);

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


}
