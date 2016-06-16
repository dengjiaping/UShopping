package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/11/17.
 */
public class WeiboUser
{
    private String sina_id;
    private String access_token;
    private String userName;
    private String url;
    private String headImg;
    private String gender;
    private boolean verified;
    private boolean followMe;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isFollowMe() {
        return followMe;
    }

    public void setFollowMe(boolean followMe) {
        this.followMe = followMe;
    }

    public String getSina_id() {
        return sina_id;
    }

    public void setSina_id(String sina_id) {
        this.sina_id = sina_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
