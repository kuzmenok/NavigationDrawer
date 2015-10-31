package com.example.sok.navigationdrawer.api.response;

import android.content.Context;

import com.example.sok.navigationdrawer.data.Group;

import java.sql.SQLException;
import java.util.List;

public class GetGroupsResponse extends Response {

    @Override
    public void save(Context context) throws SQLException {
        List<Group> groups = getTypedAnswer();
        if (groups != null) {
            // TODO: 31.10.2015 delete old data, save new
        }
    }
}
