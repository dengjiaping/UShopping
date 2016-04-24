package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ManJianVoucherItem
{
    private long days;
    private int flag;
    private long id;
    private int money01;
    private int money02;
    private String name;
    private String regulation;
    private long validity;

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMoney01() {
        return money01;
    }

    public void setMoney01(int money01) {
        this.money01 = money01;
    }

    public int getMoney02() {
        return money02;
    }

    public void setMoney02(int money02) {
        this.money02 = money02;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public long getValidity() {
        return validity;
    }

    public void setValidity(long validity) {
        this.validity = validity;
    }
}
