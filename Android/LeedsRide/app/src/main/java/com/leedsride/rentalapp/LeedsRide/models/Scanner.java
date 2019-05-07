package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scanner {

    @SerializedName("bookingId")
    @Expose
    final int bookingId;

    @SerializedName("username")
    @Expose
    final String username;

    @SerializedName("password")
    @Expose
    final String password;

    @SerializedName("bikeId")
    @Expose
    final int bikeId;

    @SerializedName("response")
    @Expose
    final String response;

    public Scanner(int bookingId, String username, String password, int bikeId, String response) {
        this.bookingId = bookingId;
        this.bikeId = bikeId;
        this.username = username;
        this.password = password;
        this.response = response;


    }

    public int getBookingId() {
        return bookingId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getBikeId() {
        return bikeId;
    }

    public String getResponse() {
        return response;
    }




    @Override
    public String toString() {
        return "Order{" +
                "bookingId='" + bookingId + '\'' +
                ", bikeId=" + bikeId +
                ", username=" + username +
                ", password=" + password +
                ", response=" + response +
                '}';
    }

}
