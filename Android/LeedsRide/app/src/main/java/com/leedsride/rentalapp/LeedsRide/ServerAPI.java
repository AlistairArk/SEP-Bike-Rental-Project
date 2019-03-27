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
    private static ServerAPI instance = null;
    private static Retrofit retrofit = null;

    public ServerAPI() throws IOException{

    }

    public static ServerAPI getInstance() throws IOException, MalformedURLException {
        if (instance == null) {
            instance = new ServerAPI();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://sc17gs.pythonanywhere.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}
