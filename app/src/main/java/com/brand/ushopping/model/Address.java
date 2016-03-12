package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/11.
 */
public class Address implements Parcelable
{
    private long id;
    private long userId;
    private String sessionid;
    private String area;
    private String consignee;
    private String deaddress;
    private int flag;
    private long addressId;
    private double latitude;
    private double longitude;
    private String mobile;
    private String telephone;
    private String zipcode;
    private AppUser_id appuserId;

    private String msg;
    private boolean success;

    public boolean addValidation()
    {
        boolean result = true;

        if(consignee == null || consignee.isEmpty())
        {
            result = false;
            msg = "收货人不能为空";
            return result;
        }
        if(mobile == null || mobile.isEmpty())
        {
            result = false;
            msg = "手机号不能为空";
            return result;
        }
        if(area == null || area.isEmpty())
        {
            result = false;
            msg = "地区不能为空";
            return result;
        }
        if(zipcode == null || zipcode.isEmpty())
        {
            result = false;
            msg = "邮编不能为空";
            return result;
        }

        return result;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getDeaddress() {
        return deaddress;
    }

    public void setDeaddress(String deaddress) {
        this.deaddress = deaddress;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AppUser_id getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppUser_id appuserId) {
        this.appuserId = appuserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setId(long id) {
        this.id = id;
        this.addressId = id;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Address()
    {

    }

    public Address(Parcel in)
    {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(sessionid);
        dest.writeString(area);
        dest.writeString(consignee);
        dest.writeString(deaddress);
        dest.writeInt(flag);
        dest.writeLong(addressId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(mobile);
        dest.writeString(telephone);
        dest.writeString(zipcode);

    }

    public void readFromParcel(Parcel in){
        id = in.readLong();
        sessionid = in.readString();
        area = in.readString();
        consignee = in.readString();
        deaddress = in.readString();
        flag = in.readInt();
        addressId = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
        mobile = in.readString();
        telephone = in.readString();
        zipcode = in.readString();

    }

}
