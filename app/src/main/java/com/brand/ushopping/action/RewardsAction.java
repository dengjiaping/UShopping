package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.RewardGoodsItem;
import com.brand.ushopping.model.UserReward;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public class RewardsAction
{
    //查询用户U币余额
    public UserReward getAppUserRewards(UserReward userReward)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(userReward);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetAppUserRewards.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppUserRewards.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    userReward = JSON.parseObject(data, UserReward.class);
                    userReward.setSuccess(true);
                }
                else
                {
                    userReward.setMsg(jsonObject.getString("msg"));
                    userReward.setSuccess(false);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return userReward;
    }

    //查询U购积分商城
    public UserReward getAppUshopAll(UserReward userReward)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(userReward);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetAppUshopAll.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppUshopAll.action"), CommonUtils.generateParams(jsonParam));
            Log.v("reward", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    String data = dataArray.toString();
                    userReward.setRewardGoodsItems((ArrayList<RewardGoodsItem>) JSON.parseArray(data, RewardGoodsItem.class));
                    userReward.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return userReward;
    }

    //兑换商品
    public UserReward appUshopBuy(UserReward userReward)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(userReward);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("AppUshopBuy.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("AppUshopBuy.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    userReward = JSON.parseObject(data, UserReward.class);
                    userReward.setSuccess(true);
                }
                else
                {
                    userReward.setSuccess(false);
                    userReward.setMsg(jsonObject.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return userReward;
    }

}
