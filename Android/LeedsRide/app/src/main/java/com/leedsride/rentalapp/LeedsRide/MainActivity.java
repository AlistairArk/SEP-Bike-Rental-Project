package com.leedsride.rentalapp.LeedsRide;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SaveSharedPreference.getPrefUsername(MainActivity.this).length() != 0)
        {
            Intent startMainMenu = new Intent(getApplicationContext(), MapsActivity.class);
            startMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(startMainMenu);
            finish();
        }
        else {
            Intent StartMenuIntent = new Intent(getApplicationContext(), StartMenu.class);
            startActivity(StartMenuIntent);
            finish();
        }

    }
}
