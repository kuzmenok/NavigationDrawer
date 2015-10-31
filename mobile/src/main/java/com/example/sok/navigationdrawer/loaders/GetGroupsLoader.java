package com.example.sok.navigationdrawer.loaders;

import android.content.Context;

import com.example.sok.navigationdrawer.api.ApiFactory;
import com.example.sok.navigationdrawer.api.request.GroupsRequest;
import com.example.sok.navigationdrawer.api.response.GetGroupsResponse;
import com.example.sok.navigationdrawer.api.response.RequestResult;
import com.example.sok.navigationdrawer.api.response.Response;
import com.example.sok.navigationdrawer.data.Group;

import java.io.IOException;
import java.util.List;

import retrofit.Call;

public class GetGroupsLoader extends BaseLoader {

    private final String id;

    public GetGroupsLoader(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected Response apiCall() throws IOException {
        GroupsRequest request = ApiFactory.getGroupsRequest();
        Call<List<Group>> call = request.getAllGroups(id);
        List<Group> groups = call.execute().body();
        return new GetGroupsResponse()
                .setRequestResult(RequestResult.SUCCESS)
                .setAnswer(groups);
    }
}
