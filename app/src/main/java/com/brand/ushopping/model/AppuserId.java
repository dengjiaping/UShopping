package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/5.
 */
public class AppuserId implements Parcelable {
    private long userId;
    private String userName;

    protected AppuserId(Parcel in) {
        userId = in.readLong();
        userName = in.readString();
    }

    public AppuserId()
    {

    }

    public static final Creator<AppuserId> CREATOR = new Creator<AppuserId>() {
        @Override
        public AppuserId createFromParcel(Parcel in) {
            return new AppuserId(in);
        }

        @Override
        public AppuserId[] newArray(int size) {
            return new AppuserId[size];
        }
    };

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(userName);
    }
}
