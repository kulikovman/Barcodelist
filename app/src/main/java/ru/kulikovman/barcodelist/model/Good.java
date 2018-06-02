package ru.kulikovman.barcodelist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Good extends RealmObject {
    public static final String ID = "mId";
    public static final String NAME = "mName";
    public static final String BARCODE = "mBarcode";
    public static final String GROUP = "mGroup";

    @PrimaryKey
    private long mId;

    private String mBarcode, mName, mGroup;

    public Good(String barcode, String name, String group) {
        mId = System.currentTimeMillis();
        mBarcode = barcode;
        mName = name;
        mGroup = group;
    }

    public Good() {
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }
}
