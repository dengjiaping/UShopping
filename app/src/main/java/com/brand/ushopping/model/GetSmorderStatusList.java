package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/30.
 */
public class GetSmorderStatusList extends BaseModel
{
    private Long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private String order_no;
    private ArrayList<OrderStatusListItem> orderStatusListItems;

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

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public ArrayList<OrderStatusListItem> getOrderStatusListItems() {
        return orderStatusListItems;
    }

    public void setOrderStatusListItems(ArrayList<OrderStatusListItem> orderStatusListItems) {
        this.orderStatusListItems = orderStatusListItems;
    }
}
