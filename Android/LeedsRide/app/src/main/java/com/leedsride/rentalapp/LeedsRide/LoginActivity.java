package com.leedsride.rentalapp.LeedsRide;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button forgotPasswordBtn = (Button)findViewById(R.id.forgotPasswordBtn);
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startFPIntent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(startFPIntent);
            }
        });

        Button loginSubmitBtn = (Button)findViewById(R.id.loginSubmitBtn);
        loginSubmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startMainMenuCHANGE = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(startMainMenuCHANGE);
            }
        });
    }
}
