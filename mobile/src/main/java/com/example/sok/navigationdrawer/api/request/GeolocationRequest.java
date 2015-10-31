package com.example.sok.navigationdrawer.api.request;

import com.example.sok.navigationdrawer.api.response.Response;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.POST;

public interface GeolocationRequest {

    @POST("/location/bla-bla")
    Call<Response> sendLocation(@Field("lattitude") String lattitude, @Field("longitude") String longitude);
}
