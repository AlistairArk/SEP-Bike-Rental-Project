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

import org.w3c.dom.Text;

import java.util.Locale;

public class OnBookingActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILIS = 6000000;
    private long timeLeftInMillis = START_TIME_IN_MILIS;
    private CountDownTimer countDownTimer;
    private TextView rentalTimer;
    private ProgressBar rentalProgressBar;
    private Button returnBikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_booking);

        rentalTimer = (TextView)findViewById(R.id.rentalTimer);
        rentalProgressBar = (ProgressBar)findViewById(R.id.rentalProgressBar);
        returnBikes = (Button)findViewById(R.id.returnBikes);

        startTimer();

        returnBikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startBarcode = new Intent(getApplicationContext(), BarcodeScanner.class);
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
                int hours   = (int) ((timeLeftInMillis / (1000*60*60)) % 24);

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
