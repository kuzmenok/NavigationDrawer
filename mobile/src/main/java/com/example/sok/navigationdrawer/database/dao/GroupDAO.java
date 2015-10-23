package com.example.sok.navigationdrawer.database.dao;

import com.example.sok.navigationdrawer.data.Group;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class GroupDAO extends BaseDaoImpl<Group, Integer> {

    public GroupDAO(ConnectionSource connectionSource, Class<Group> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Group> getAllGroups() throws SQLException{
        return this.queryForAll();
    }

    public List<Group> getGroupByName(String name)  throws SQLException{
        QueryBuilder<Group, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Group.GROUP_FIELD_NAME, name);
        PreparedQuery<Group> preparedQuery = queryBuilder.prepare();
        return query(preparedQuery);
    }
}
