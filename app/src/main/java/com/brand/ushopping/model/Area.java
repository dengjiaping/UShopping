package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/10.
 */
public class Area extends BaseModel {
    private Long pid;
    private ArrayList<AreaItem> areaItems;
    private String msg;
    private boolean success;
    private Integer type;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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

    public ArrayList<AreaItem> getAreaItems() {
        return areaItems;
    }

    public void setAreaItems(ArrayList<AreaItem> areaItems) {
        this.areaItems = areaItems;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
