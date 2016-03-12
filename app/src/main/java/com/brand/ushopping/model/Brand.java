package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/16.
 */
public class Brand implements Parcelable
{
    private long id;
    private String brandName;
    private String logopic;
    private String intro;
    private String detail;
    private String showpic;
    private int flag;

    protected Brand(Parcel in) {
        id = in.readLong();
        brandName = in.readString();
        logopic = in.readString();
        intro = in.readString();
        detail = in.readString();
        showpic = in.readString();
        flag = in.readInt();

    }

    public Brand()
    {

    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel in) {
            return new Brand(in);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLogopic() {
        return logopic;
    }

    public void setLogopic(String logopic) {
        this.logopic = logopic;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(brandName);
        dest.writeString(logopic);
        dest.writeString(intro);
        dest.writeString(detail);
        dest.writeString(showpic);
        dest.writeInt(flag);

    }
}
