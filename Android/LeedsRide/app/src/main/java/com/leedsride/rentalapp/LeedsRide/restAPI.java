package com.leedsride.rentalapp.LeedsRide;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import com.leedsride.rentalapp.LeedsRide.models.Login;

public interface restAPI {
    @Headers("Content-Type: application/json")
    @POST("Live") ////////////Remainder of url from shortened base url in main activity
    Call<Login> getReply(@Body Login login); //When POST request is made, the body should be an instance of Login class called login
}
