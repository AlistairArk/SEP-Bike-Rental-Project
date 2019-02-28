package com.leedsride.rentalapp.LeedsRide;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerAPI {

    private Retrofit retrofit;

    public ServerAPI() throws IOException, MalformedURLException{
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/todos/1")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

}
