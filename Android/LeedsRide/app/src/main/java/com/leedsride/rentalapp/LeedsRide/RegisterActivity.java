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

import com.leedsride.rentalapp.LeedsRide.models.Register;

public class RegisterActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://733y6weqb0.execute-api.eu-west-2.amazonaws.com/"; ////base url does not include exact path ///should make this available to all activities
    private static final String TAG = LoginActivity.class.getSimpleName();

    Register register = new Register();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = (Button)findViewById(R.id.completeRegistration);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.usernameRegisterEntry);
                EditText password = (EditText) findViewById(R.id.passwordRegisterEntry);
                EditText repeatPassword = (EditText) findViewById(R.id.passwordCheckerRegisterEntry);
                EditText email = (EditText) findViewById(R.id.emailRegisterEntry);
                EditText phone = (EditText) findViewById(R.id.phoneRegisterEntry);

                register.setUsername(username.getText().toString());
                register.setPassword(password.getText().toString());
                register.setRepeatPassword(repeatPassword.getText().toString());
                register.setEmail(email.getText().toString());
                register.setPhone(phone.getText().toString());

                sendNetworkRequest(register);
            }
        });
    }
    private void sendNetworkRequest(final Register request) {

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptRegister method from restAPI interface
        Call<Register> call = sampleAPI.attemptRegister(request);

        //add call to queue (in this case nothing in queue)
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {

                String reply = response.body().getRegistrationStatus();
                Log.d(TAG, reply);

                if(reply.equals("Login Accepted")){
                    Intent startMainMenu = new Intent(getApplicationContext(), MapsActivity.class);
                    startMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(startMainMenu);
                    finish();
                }
                if(reply.equals("Incorrect Login Information")){
                    Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Log.e("error", "Could not connect to external API");
            }
        });
    }
}
