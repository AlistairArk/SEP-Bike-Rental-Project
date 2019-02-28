package com.leedsride.rentalapp.LeedsRide;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent StartMenuIntent = new Intent(getApplicationContext(), StartMenu.class);
                startActivity(StartMenuIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
