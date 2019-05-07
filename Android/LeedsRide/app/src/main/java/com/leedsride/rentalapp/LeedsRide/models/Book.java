package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {
    /*********************
     * Declared fields are only used internally,
     * what needs to match the API key names are the Serialized names
     */
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("bikeNumber")
    @Expose
    private int bikeNumber;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("startLocation")
    @Expose
    private String startLocation;
    @SerializedName("endLocation")
    @Expose
    private String endLocation;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBikeNumber(int bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    @Override
    public String toString() {
        return "Book{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bikeNumber=" + bikeNumber +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
