package com.leedsride.rentalapp.LeedsRide;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.leedsride.rentalapp.LeedsRide.models.Locations;
import com.leedsride.rentalapp.LeedsRide.models.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartMenu extends AppCompatActivity {

    private static final String BASE_URL = "https://733y6weqb0.execute-api.eu-west-2.amazonaws.com/"; ////base url does not include exact path ///should make this available to all activities
    private static final String TAG = StartMenu.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Button goToLoginActivity = (Button)findViewById(R.id.goToLoginActivity);
        goToLoginActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent startLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(startLoginIntent);
                //sendNetworkRequest();
                sendNetworkRequest();

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
