package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/24.
 */
public class AppyyShopcart extends BaseModel
{
    private long userId;
    private String sessionid;
    private long appyyshopcartId;
    private boolean success;
    private String msg;

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

    public long getAppyyshopcartId() {
        return appyyshopcartId;
    }

    public void setAppyyshopcartId(long appyyshopcartId) {
        this.appyyshopcartId = appyyshopcartId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
