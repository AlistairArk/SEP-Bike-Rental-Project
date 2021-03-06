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

    private static final String BASE_URL = "https://sc17gs.pythonanywhere.com/api/";
    private static final String TAG = RegisterActivity.class.getSimpleName();

    Register register = new Register();

    String storedUsername;
    String storedPassword;

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

                storedUsername = username.getText().toString();
                storedPassword = password.getText().toString();

                String password1 = password.getText().toString();
                String password2 = repeatPassword.getText().toString();

                register.setUsername(username.getText().toString());
                register.setPassword(password1);
                register.setRepeatPassword(password2);
                register.setEmail(email.getText().toString());
                register.setPhone(phone.getText().toString());

                if(password1.equals(password2)){
                    sendNetworkRequest(register);
                } else {
                    Toast.makeText(getApplicationContext(), "Those passwords don't match!!!", Toast.LENGTH_SHORT).show();
                }

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
                System.out.println(response.message());
                String reply = response.body().getRegistrationStatus();
                Log.d(TAG, reply);

                if(reply.equals("User Registered")){ ////////////Update once server has been changed
                    SaveSharedPreference.setLoginDetails(getApplicationContext(), storedUsername, storedPassword);
                    Intent startMainMenu = new Intent(getApplicationContext(), MapsActivity.class);
                    startMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(startMainMenu);
                    finish();
                }
                else{//if(reply.equals("Registration could not be completed")){
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
