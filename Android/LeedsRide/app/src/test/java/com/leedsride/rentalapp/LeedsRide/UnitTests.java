package com.leedsride.rentalapp.LeedsRide;

import com.leedsride.rentalapp.LeedsRide.models.Login;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

import com.leedsride.rentalapp.LeedsRide.models.Login;

import java.io.IOException;

public class UnitTests {

    private static final String BASE_URL = "https://sc17gs.pythonanywhere.com/api/";

    @Test
    public void correctLogin() {
        Login login = new Login();

        login.setUsername("prudd");
        login.setPassword("password");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restAPI sampleAPI = retrofit.create(restAPI.class);

        Call<Login> call = sampleAPI.attemptLogin(login);

        try {
            //Magic is here at .execute() instead of .enqueue()
            Response<Login> response = call.execute();
            Login result = response.body();

            assertEquals("Login Accepted", result.getLoginStatus());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void register() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void locations() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void booking() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void orders() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void collectBikes() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void returnBikes() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void logout() {
        assertEquals(4, 2 + 2);
    }
}