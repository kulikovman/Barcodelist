package ru.kulikovman.barcodelist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Product extends RealmObject {
    public static final String ID = "mId";
    public static final String NAME = "mName";
    public static final String BARCODE = "mBarcode";
    public static final String BARCODE_TYPE = "mBarcodeType";
    public static final String GROUP = "mGroup";

    @PrimaryKey
    private long mId;

    private String mName;
    private long mBarcode;
    private String mBarcodeType;
    private String mGroup;

    public Product(String name, long barcode, String barcodeType, String group) {
        mId = System.currentTimeMillis();
        mName = name;
        mBarcode = barcode;
        mBarcodeType = barcodeType;
        mGroup = group;
    }

    public Product() {
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

    public String getBarcodeType() {
        return mBarcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        mBarcodeType = barcodeType;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }
}
