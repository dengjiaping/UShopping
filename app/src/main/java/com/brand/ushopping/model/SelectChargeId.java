package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SelectChargeId
{
    private Long userId;
    private String sessionid;
    private String charge_id;
    private Charge charge;
    private Boolean success;
    private String msg;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getCharge_id() {
        return charge_id;
    }

    public void setCharge_id(String charge_id) {
        this.charge_id = charge_id;
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

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }
}
