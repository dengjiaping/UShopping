package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/3.
 */
public class User extends BaseModel implements Parcelable
{
    private long userId;
    private String userName;
    private String mobile;
    private String pass;
    private String msg;
    private String sessionid;
    private String headImg;
    private String defaultAddress;
    private long defaultAddressId;
    private String address;
    private String birthday;
    private Boolean success;
    private String token;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public long getDefaultAddressId() {
        return defaultAddressId;
    }

    public void setDefaultAddressId(long defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in){
        readFromParcel(in);
    }

    public User()
    {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in){
        userId = in.readLong();
        userName = in.readString();
        mobile = in.readString();
        pass = in.readString();
        msg = in.readString();
        sessionid = in.readString();
        headImg = in.readString();
        defaultAddress = in.readString();
        defaultAddressId = in.readLong();
        address = in.readString();
        birthday = in.readString();
        token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(userName);
        dest.writeString(mobile);
        dest.writeString(pass);
        dest.writeString(msg);
        dest.writeString(sessionid);
        dest.writeString(headImg);
        dest.writeString(defaultAddress);
        dest.writeLong(defaultAddressId);
        dest.writeString(address);
        dest.writeString(birthday);
        dest.writeString(token);
    }

    public boolean updateValidation()
    {
        boolean result = true;
        if(userName == null || userName.isEmpty())
        {
            msg = "昵称不能为空";
            result = false;
            return result;
        }
        if(address == null || address.isEmpty())
        {
            msg = "地址不能为空";
            result = false;
            return result;
        }
        if(birthday == null || birthday.isEmpty())
        {
            msg = "生日不能为空";
            result = false;
            return result;
        }

        return result;
    }

}
