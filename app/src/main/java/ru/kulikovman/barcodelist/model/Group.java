package ru.kulikovman.barcodelist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Group extends RealmObject {
    public static final String ID = "mId";
    public static final String NAME = "mName";

    @PrimaryKey
    private long mId;

    private String mName;

    public Group(String name) {
        mId = System.currentTimeMillis();
        mName = name;
    }

    public Group() {
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
