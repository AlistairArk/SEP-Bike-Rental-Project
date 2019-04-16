package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

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
    @SerializedName("loginStatus")
    @Expose
    private String loginStatus;

    /// only basic constructor and a couple getters + setters need to be used

    public Login() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", login status='" + loginStatus + '\'' +
                '}';
    }
}