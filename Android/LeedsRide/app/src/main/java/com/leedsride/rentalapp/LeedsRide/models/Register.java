package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Register {

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
    @SerializedName("repeatPassword")
    @Expose
    private String repeatPassword;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("registrationStatus")
    @Expose
    private String registrationStatus;

    /// only basic constructor and a couple getters + setters need to be used

    public Register() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
//    public void setRegistrationStatus(String loginStatus) {
//        this.registrationStatus = loginStatus;
//    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    @Override
    public String toString() {
        return "Register{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", registrationStatus='" + registrationStatus + '\'' +
                '}';
    }
}
