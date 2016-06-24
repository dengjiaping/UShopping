package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/1/17.
 */
public class SaveAppBrandCollect extends BaseModel
{
    private long userId;
    private String sessionid;
    private AppuserId appuserId;
    private AppbrandId appbrandId;
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

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    public AppbrandId getAppbrandId() {
        return appbrandId;
    }

    public void setAppbrandId(AppbrandId appbrandId) {
        this.appbrandId = appbrandId;
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
