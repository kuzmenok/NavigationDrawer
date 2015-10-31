package com.example.sok.navigationdrawer.api.request;

import com.example.sok.navigationdrawer.data.Group;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GroupsRequest {

    @GET("/groups/bla-bla") //TODO proper url
    Call<List<Group>> getAllGroups(@Query("id") String id);

}
