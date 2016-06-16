package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/11.
 */
public class ClientCharge
{
    private long userId;
    private String sessionid;
    private String channelVal; //alipay 支付宝客户端, wx 微信支付, upacp 银联
    private long amountVal;
    private double summary;
    private String subjectVal;
    private String bodyVal;
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

    public String getChannelVal() {
        return channelVal;
    }

    public void setChannelVal(String channelVal) {
        this.channelVal = channelVal;
    }

    public long getAmountVal() {
        return amountVal;
    }

    public void setAmountVal(long amountVal) {
        this.amountVal = amountVal;
    }

    public String getSubjectVal() {
        return subjectVal;
    }

    public void setSubjectVal(String subjectVal) {
        this.subjectVal = subjectVal;
    }

    public String getBodyVal() {
        return bodyVal;
    }

    public void setBodyVal(String bodyVal) {
        this.bodyVal = bodyVal;
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

    public double getSummary() {
        return summary;
    }

    public void setSummary(double summary) {
        this.summary = summary;
    }
}
