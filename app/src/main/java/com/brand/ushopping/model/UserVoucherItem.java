package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/8.
 */
public class UserVoucherItem extends BaseModel implements Parcelable {
    private AppvoucherId appvoucherId;
    private long days;
    private int flag;
    private long id;
    private AppuserId appuserId;

    protected UserVoucherItem(Parcel in) {
        appvoucherId = in.readParcelable(AppvoucherId.class.getClassLoader());
        days = in.readLong();
        flag = in.readInt();
        id = in.readLong();
        appuserId = in.readParcelable(AppuserId.class.getClassLoader());
    }

    public UserVoucherItem()
    {

    }

    public static final Creator<UserVoucherItem> CREATOR = new Creator<UserVoucherItem>() {
        @Override
        public UserVoucherItem createFromParcel(Parcel in) {
            return new UserVoucherItem(in);
        }

        @Override
        public UserVoucherItem[] newArray(int size) {
            return new UserVoucherItem[size];
        }
    };

    public AppvoucherId getAppvoucherId() {
        return appvoucherId;
    }

    public void setAppvoucherId(AppvoucherId appvoucherId) {
        this.appvoucherId = appvoucherId;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(appvoucherId, flags);
        dest.writeLong(days);
        dest.writeInt(flag);
        dest.writeLong(id);
        dest.writeParcelable(appuserId, flags);
    }
}
