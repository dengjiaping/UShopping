package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/29.
 */
public class OrderItem
{
    private long id;
    private ArrayList<OrderGoodsItem> orderGoodsItems;
    private int flag;    //0 未付款, 1 已付款, 2 已发货
    private String orderNo;
    private double money;
    private int quantity;
    private long reTime;

    public ArrayList<OrderGoodsItem> getOrderGoodsItems() {
        return orderGoodsItems;
    }

    public void setOrderGoodsItems(ArrayList<OrderGoodsItem> orderGoodsItems) {
        this.orderGoodsItems = orderGoodsItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getReTime() {
        return reTime;
    }

    public void setReTime(long reTime) {
        this.reTime = reTime;
    }
}
