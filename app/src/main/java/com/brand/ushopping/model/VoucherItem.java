package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/1.
 */
public class VoucherItem implements Parcelable
{
    private long id;
    private String name;
    private double money02;
    private double money01;
    private int flag;   //1为已领取 0为未领取
    private long days;
    private long validity;
    private int come;
    private AppbrandId appbrandId;

    protected VoucherItem(Parcel in) {
        id = in.readLong();
        name = in.readString();
        money02 = in.readDouble();
        money01 = in.readDouble();
        flag = in.readInt();
        days = in.readLong();
        validity = in.readLong();
        come = in.readInt();
        appbrandId = in.readParcelable(AppbrandId.class.getClassLoader());

    }

    public VoucherItem()
    {

    }

    public static final Creator<VoucherItem> CREATOR = new Creator<VoucherItem>() {
        @Override
        public VoucherItem createFromParcel(Parcel in) {
            return new VoucherItem(in);
        }

        @Override
        public VoucherItem[] newArray(int size) {
            return new VoucherItem[size];
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

    public double getMoney02() {
        return money02;
    }

    public void setMoney02(double money02) {
        this.money02 = money02;
    }

    public double getMoney01() {
        return money01;
    }

    public void setMoney01(double money01) {
        this.money01 = money01;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getValidity() {
        return validity;
    }

    public void setValidity(long validity) {
        this.validity = validity;
    }

    public int getCome() {
        return come;
    }

    public void setCome(int come) {
        this.come = come;
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
        dest.writeDouble(money02);
        dest.writeDouble(money01);
        dest.writeInt(flag);
        dest.writeLong(days);
        dest.writeLong(validity);
        dest.writeInt(come);
        dest.writeParcelable(appbrandId, flags);
    }
}
