package com.example.sok.navigationdrawer.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "group")
public class Group{
    public final static String GROUP_FIELD_NAME = "name";

    @DatabaseField(generatedId = true)
    private int Id;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;

    public Group() {
        // ORMLite needs a no-arg constructor
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
