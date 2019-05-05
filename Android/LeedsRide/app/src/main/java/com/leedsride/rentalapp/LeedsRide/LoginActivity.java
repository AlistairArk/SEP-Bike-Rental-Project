package com.leedsride.rentalapp.LeedsRide;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.leedsride.rentalapp.LeedsRide.models.Login;

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://demo7156093.mockable.io/"; ////base url does not include exact path ///should make this available to all activities
    private static final String TAG = LoginActivity.class.getSimpleName();

    Login login = new Login();

    String storedUsername;
    String storedPassword;

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
                EditText username = (EditText) findViewById(R.id.usernameLoginEntry);
                EditText password = (EditText) findViewById(R.id.passwordLoginEntry);

                storedUsername = username.getText().toString();
                storedPassword = password.getText().toString();

                login.setUsername(username.getText().toString());
                login.setPassword(password.getText().toString());

                sendNetworkRequest(login);
            }
        });
    }

    private void sendNetworkRequest(final Login request) {

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptLogin method from restAPI interface
        Call<Login> call = sampleAPI.attemptLogin(request);

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
                    Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("error", "Could not connect to external API");
                Toast.makeText(getApplicationContext(), "Could not connect to external API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
