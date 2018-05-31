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

    private String mName;
    private long mBarcode;
    private String mGroup;

    public Good(String name, long barcode, String group) {
        mId = System.currentTimeMillis();
        mName = name;
        mBarcode = barcode;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getBarcode() {
        return mBarcode;
    }

    public void setBarcode(long barcode) {
        mBarcode = barcode;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }
}
