package com.leedsride.rentalapp.LeedsRide;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServerAPI {

    private URL url;
    private HttpsURLConnection connection;

    public ServerAPI() throws IOException, MalformedURLException{
        /* Establish http connection with the server */

        url = new URL("http://www.sc17gs.pythonanywhere.com");
        connection = (HttpsURLConnection)url.openConnection();
    }

    public void ServerLogin(String username, String password) {


    }

    public void ServerLogout() {

    }
}
