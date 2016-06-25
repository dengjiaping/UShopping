package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/1/19.
 */
public class AppCustomer extends BaseModel {
    private long userId;
    private String sessionid;
    private AppuserId appuserId;
    private ApporderId apporderId;
    private boolean success;
    private String msg;
    private String content;

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

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    public ApporderId getApporderId() {
        return apporderId;
    }

    public void setApporderId(ApporderId apporderId) {
        this.apporderId = apporderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
