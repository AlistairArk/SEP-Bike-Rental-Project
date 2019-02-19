package com.leedsride.rentalapp.LeedsRide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Button goToLoginActivity = (Button)findViewById(R.id.goToLoginActivity);
        goToLoginActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(startLoginIntent);
            }
        });

        Button goToRegisterActivity = (Button)findViewById(R.id.goToRegisterActivity);
        goToRegisterActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startRegisterIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(startRegisterIntent);
            }
        });
    }
}
