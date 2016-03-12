package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/2.
 */
public class AppvoucherId implements Parcelable
{
    private long id;
    private String name;
    private long days;
    private int flag;
    private double money01;
    private double money02;
    private long validity;
    private AppbrandId appbrandId;

    protected AppvoucherId(Parcel in) {
        id = in.readLong();
        name = in.readString();
        days = in.readLong();
        flag = in.readInt();
        money01 = in.readDouble();
        money02 = in.readDouble();
        validity = in.readLong();
        appbrandId = in.readParcelable(AppbrandId.class.getClassLoader());

    }

    public AppvoucherId()
    {

    }

    public static final Creator<AppvoucherId> CREATOR = new Creator<AppvoucherId>() {
        @Override
        public AppvoucherId createFromParcel(Parcel in) {
            return new AppvoucherId(in);
        }

        @Override
        public AppvoucherId[] newArray(int size) {
            return new AppvoucherId[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getMoney01() {
        return money01;
    }

    public void setMoney01(double money01) {
        this.money01 = money01;
    }

    public double getMoney02() {
        return money02;
    }

    public void setMoney02(double money02) {
        this.money02 = money02;
    }

    public long getValidity() {
        return validity;
    }

    public void setValidity(long validity) {
        this.validity = validity;
    }

    public AppbrandId getAppbrandId() {
        return appbrandId;
    }

    public void setAppbrandId(AppbrandId appbrandId) {
        this.appbrandId = appbrandId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeLong(days);
        dest.writeInt(flag);
        dest.writeDouble(money01);
        dest.writeDouble(money02);
        dest.writeLong(validity);
        dest.writeParcelable(appbrandId, flags);
    }
}
