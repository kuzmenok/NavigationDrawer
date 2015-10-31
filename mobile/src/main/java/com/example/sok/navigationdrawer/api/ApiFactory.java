package com.example.sok.navigationdrawer.api;

import android.support.annotation.NonNull;

import com.example.sok.navigationdrawer.api.request.GroupsRequest;
import com.example.sok.navigationdrawer.api.request.GeolocationRequest;
import com.example.sok.navigationdrawer.app.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiFactory {
    private static final int TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 120;
    private static final int CONNECT_TIMEOUT = 10;

    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        CLIENT.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
    }

    private static final Gson GSON = new GsonBuilder()
//            .registerTypeAdapter(Group.class, new RestDeserializer<>(Group.class, "group"))
//            чтобы достать данные из поля group:
//            {
//                "status":"OK",
//                    "reason":"some reason",
//                    "group" :
//                {
//                    "foo": 123,
//                        "bar": "some value"
//                }
//            }
            .create();

    @NonNull
    public static GroupsRequest getGroupsRequest() {
        return getRetrofit().create(GroupsRequest.class);
    }

    @NonNull
    public static GeolocationRequest getGeolocationRequest() {
        return getRetrofit().create(GeolocationRequest.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .client(CLIENT)
                .build();
    }

}
