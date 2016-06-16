package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/22.
 */
public class UserReward
{
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private int rewards;
    private ArrayList<RewardGoodsItem> rewardGoodsItems;
    private AppuserId appuserId;
    private AppushopId appushopId;
    private AppaddressId appaddressId;
    private long ushopId;

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

    public int getRewards() {
        return rewards;
    }

    public void setRewards(int rewards) {
        this.rewards = rewards;
    }

    public ArrayList<RewardGoodsItem> getRewardGoodsItems() {
        return rewardGoodsItems;
    }

    public void setRewardGoodsItems(ArrayList<RewardGoodsItem> rewardGoodsItems) {
        this.rewardGoodsItems = rewardGoodsItems;
    }

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    public AppushopId getAppushopId() {
        return appushopId;
    }

    public void setAppushopId(AppushopId appushopId) {
        this.appushopId = appushopId;
    }

    public AppaddressId getAppaddressId() {
        return appaddressId;
    }

    public void setAppaddressId(AppaddressId appaddressId) {
        this.appaddressId = appaddressId;
    }

    public long getUshopId() {
        return ushopId;
    }

    public void setUshopId(long ushopId) {
        this.ushopId = ushopId;
    }
}
