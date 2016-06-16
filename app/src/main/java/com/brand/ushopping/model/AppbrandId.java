package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/20.
 */
public class AppbrandId implements Parcelable
{
    private long id;
    private String logopic;
    private String brandName;
    private String detail;
    private String showpic;
    private int flag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogopic() {
        return logopic;
    }

    public void setLogopic(String logopic) {
        this.logopic = logopic;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getShowpic() {
        return showpic;
    }

    public void setShowpic(String showpic) {
        this.showpic = showpic;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AppbrandId createFromParcel(Parcel in) {
            return new AppbrandId(in);
        }

        public AppbrandId[] newArray(int size) {
            return new AppbrandId[size];
        }
    };

    public AppbrandId(Parcel in){
        readFromParcel(in);
    }

    public AppbrandId()
    {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(logopic);
        dest.writeString(brandName);
        dest.writeString(detail);
        dest.writeString(showpic);
        dest.writeInt(flag);

    }

    public void readFromParcel(Parcel in)
    {
        id = in.readLong();
        logopic = in.readString();
        brandName = in.readString();
        detail = in.readString();
        showpic = in.readString();
        flag = in.readInt();

    }

}
