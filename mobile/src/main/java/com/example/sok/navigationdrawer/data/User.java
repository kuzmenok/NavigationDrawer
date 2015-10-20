package com.example.sok.navigationdrawer.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User{

    public final static String USER_FIELD_NAME = "name";

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;

    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
