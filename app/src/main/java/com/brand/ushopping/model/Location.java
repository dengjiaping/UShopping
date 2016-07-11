package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/7/8.
 * 保存位置信息
 */
public class Location
{
    private String city;
    private String longitude;
    private String latitude;
    private Long time;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
