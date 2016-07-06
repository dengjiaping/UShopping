package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/5.
 * 筛选条件
 */
public class FilterParams implements Parcelable{
    private String year;
    private String season;
    private String priceMin;
    private String priceMax;
    private Long appCategoryId;

    protected FilterParams(Parcel in) {
        year = in.readString();
        season = in.readString();
        priceMin = in.readString();
        priceMax = in.readString();
        appCategoryId = in.readLong();
    }

    public FilterParams()
    {

    }

    public static final Creator<FilterParams> CREATOR = new Creator<FilterParams>() {
        @Override
        public FilterParams createFromParcel(Parcel in) {
            return new FilterParams(in);
        }

        @Override
        public FilterParams[] newArray(int size) {
            return new FilterParams[size];
        }
    };

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public Long getAppCategoryId() {
        return appCategoryId;
    }

    public void setAppCategoryId(Long appCategoryId) {
        this.appCategoryId = appCategoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(year);
        dest.writeString(season);
        dest.writeString(priceMin);
        dest.writeString(priceMax);
        dest.writeLong(appCategoryId);
    }
}
