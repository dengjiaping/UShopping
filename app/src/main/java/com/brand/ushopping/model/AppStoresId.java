package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AppStoresId implements Parcelable{
    private Long id;

    protected AppStoresId(Parcel in) {
    }

    public AppStoresId()
    {
    }

    public static final Creator<AppStoresId> CREATOR = new Creator<AppStoresId>() {
        @Override
        public AppStoresId createFromParcel(Parcel in) {
            return new AppStoresId(in);
        }

        @Override
        public AppStoresId[] newArray(int size) {
            return new AppStoresId[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
