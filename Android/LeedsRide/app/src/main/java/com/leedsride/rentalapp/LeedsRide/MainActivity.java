package com.leedsride.rentalapp.LeedsRide;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.leedsride.rentalapp.LeedsRide.models.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private static final String BASE_URL = "https://733y6weqb0.execute-api.eu-west-2.amazonaws.com/"; ////base url does not include exact path ///should make this available to all activities
    private static final String TAG = LoginActivity.class.getSimpleName();

    Login login = new Login();

    String storedUsername;
    String storedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = SaveSharedPreference.getPrefUsername(MainActivity.this);
        String password = SaveSharedPreference.getPrefPassword(MainActivity.this);


        if(SaveSharedPreference.getPrefUsername(MainActivity.this).length() != 0)
        {

            login.setUsername(username);
            login.setPassword(password);

            ////Create retrofit object for network call
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ///implement instance of restAPI interface
            restAPI sampleAPI = retrofit.create(restAPI.class);

            //create call which uses attemptLogin method from restAPI interface
            Call<Login> call = sampleAPI.attemptLogin(login);

            //add call to queue (in this case nothing in queue)
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {

                    String reply = response.body().getLoginStatus();
                    Log.d(TAG, reply);

                    if(reply.equals("Login Accepted")){
                        SaveSharedPreference.setLoginDetails(getApplicationContext(), storedUsername, storedPassword);
                        Intent startMainMenu = new Intent(getApplicationContext(), MapsActivity.class);
                        startMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startMainMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startMainMenu.putExtra("EXIT", true);
                        startActivity(startMainMenu);
                        finish();
                    }
                    if(reply.equals("Incorrect Login Information")){
                        Toast.makeText(getApplicationContext(), "Login authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.e("error", "Could not connect to external API");
                }
            });

        }

        Intent StartMenuIntent = new Intent(getApplicationContext(), StartMenu.class);
        startActivity(StartMenuIntent);
        finish();


    }
}
