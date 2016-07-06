package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/18.
 */
public class AppaddressId extends BaseModel implements Parcelable
{
    private long id;

    protected AppaddressId(Parcel in) {
        id = in.readLong();
    }

    public AppaddressId()
    {

    }

    public static final Creator<AppaddressId> CREATOR = new Creator<AppaddressId>() {
        @Override
        public AppaddressId createFromParcel(Parcel in) {
            return new AppaddressId(in);
        }

        @Override
        public AppaddressId[] newArray(int size) {
            return new AppaddressId[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }
}
