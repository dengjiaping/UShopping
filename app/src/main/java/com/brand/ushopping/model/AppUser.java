package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/2.
 */
public class AppUser
{
    private long userId;
    private String sessionid;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
