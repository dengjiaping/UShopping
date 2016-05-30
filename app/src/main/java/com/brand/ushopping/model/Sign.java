package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/21.
 */
public class Sign
{
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private int fate;
    private int flag;       //1 积分 2 券 3 随机
    private String present; //礼品
    private long reTime;    //签到时间

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

    public int getFate() {
        return fate;
    }

    public void setFate(int fate) {
        this.fate = fate;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public long getReTime() {
        return reTime;
    }

    public void setReTime(long reTime) {
        this.reTime = reTime;
    }
}
