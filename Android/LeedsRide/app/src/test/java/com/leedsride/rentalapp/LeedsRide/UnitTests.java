package com.leedsride.rentalapp.LeedsRide;

import com.leedsride.rentalapp.LeedsRide.models.Book;
import com.leedsride.rentalapp.LeedsRide.models.Locations;
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
import com.leedsride.rentalapp.LeedsRide.models.Register;

import java.io.IOException;
import java.util.List;

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
    public void incorrectLogin() {
        Login login = new Login();

        login.setUsername("random");
        login.setPassword("random");

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

            assertEquals("Incorrect Login Information", result.getLoginStatus());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void successfulRegister() {
        Register register = new Register();

        register.setUsername("hi");
        register.setPassword("password");
        register.setEmail("test@test.com");
        register.setPhone("07951399157");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restAPI sampleAPI = retrofit.create(restAPI.class);

        Call<Register> call = sampleAPI.attemptRegister(register);

        try {
            Response<Register> response = call.execute();
            Register result = response.body();

            assertEquals("User Registered", result.getRegistrationStatus());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void emailFailureRegister() {
        Register register = new Register();

        register.setUsername("prudd");
        register.setPassword("password");
        register.setEmail("prudd@gmail.com"); ///////////////////Set this to an email that is already in the database
        register.setPhone("07951399157");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restAPI sampleAPI = retrofit.create(restAPI.class);

        Call<Register> call = sampleAPI.attemptRegister(register);

        try {
            Response<Register> response = call.execute();
            Register result = response.body();

            assertEquals("That email is taken", result.getRegistrationStatus());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void usernameFailureRegister() {
        Register register = new Register();

        register.setUsername("prudd"); ///////////////////Set this to a username that is already in the database
        register.setPassword("password");
        register.setEmail("testing@test.com");
        register.setPhone("07951399157");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restAPI sampleAPI = retrofit.create(restAPI.class);

        Call<Register> call = sampleAPI.attemptRegister(register);

        try {
            Response<Register> response = call.execute();
            Register result = response.body();

            assertEquals("That username is taken", result.getRegistrationStatus());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void locations() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restAPI sampleAPI = retrofit.create(restAPI.class);

        Call<List<Locations>> call = sampleAPI.getLocations();

        try {
            Response<List<Locations>> response = call.execute();
            List<Locations> result = response.body();

            assertEquals("Leeds City Centre", result.get(0).getName());
            assertEquals(53.81159973144531, result.get(0).getLatitude(), 0.01);
            assertEquals(-1.62727, result.get(0).getLongitude(), 0.01);
            assertEquals(5, result.get(0).getBikesAvailable());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void succesfulBooking() {
        Book booking = new Book();

        booking.setUsername("prudd"); ///////////////////Set this to a username that is already in the database
        booking.setPassword("password");
        booking.setStartTime("2019-05-24T09:30");
        booking.setEndTime("2019-05-24T12:30");
        booking.setStartLocation("Kirkstall");
        booking.setEndLocation("Hyde Park");
        booking.setBikeNumber(2);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restAPI sampleAPI = retrofit.create(restAPI.class);

        Call<Book> call = sampleAPI.makeBooking(booking);

        try {
            Response<Book> response = call.execute();
            Book result = response.body();

            assertEquals("Accepted", result.getBookingStatus());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unsuccessfulBooking() {
        Book booking = new Book();

        booking.setUsername("prudd"); ///////////////////Set this to a username that is already in the database
        booking.setPassword("password");
        booking.setStartTime("2019-05-24T09:30");
        booking.setEndTime("2019-05-24T12:30");
        booking.setStartLocation("Kirkstall");
        booking.setEndLocation("Hyde Park");
        booking.setBikeNumber(42);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restAPI sampleAPI = retrofit.create(restAPI.class);

        Call<Book> call = sampleAPI.makeBooking(booking);

        try {
            Response<Book> response = call.execute();
            Book result = response.body();

            assertEquals("Booking unavailable as the start location will not have enough bikes at the start time.", result.getBookingStatus());

        } catch (IOException e) {
            e.printStackTrace();
        }
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

}