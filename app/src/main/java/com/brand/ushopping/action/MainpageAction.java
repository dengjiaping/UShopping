package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppOnlineshopping;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.OnlineshoppingGoods;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.DataCache;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/9.
 */
public class MainpageAction extends BaseAction
{
    public MainpageAction(Context context) {
        super(context);
    }

    //首页信息
    public Main home(Context context, Main mMain)
    {
        mMain.addVersion(context);  //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(mMain);

        try
        {
            if(mMain.getUseCache())
            {
                resultString = DataCache.getData(context, "HomeAction.action");
            }

            if(resultString == null)
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("HomeAction.action"), CommonUtils.generateParams(jsonParam));

                Log.v("home", resultString);
            }

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                mMain.setSuccess(jsonObject.getBoolean("success"));
                if(mMain.isSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    mMain = JSON.parseObject(data, Main.class);
                    mMain.setSuccess(true);

                    //存入缓存
                    DataCache.putData(context, "HomeAction.action", resultString);

                }
                else
                {
                    mMain.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mMain;

    }

    //首页下拉
    public HomeRe homeRe(HomeRe homeRe)
    {
        homeRe.addVersion(context);  //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(homeRe);

        try
        {
            if(homeRe.getUseCache())
            {
                resultString = DataCache.getData(context, "HomeReAction.action");
            }

            if(CommonUtils.isValueEmpty(resultString))
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("HomeReAction.action"), CommonUtils.generateParams(jsonParam));
            }

            if(resultString != null)
            {
                Log.v("homeRe", resultString);

                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    ArrayList<AppgoodsId> appgoodsIds = new ArrayList<AppgoodsId>();
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject AppgoodsIdJSONObject = dataArray.getJSONObject(a).getJSONObject("appgoodsId");
                        String data = AppgoodsIdJSONObject.toString();

                        appgoodsIds.add(JSON.parseObject(data, AppgoodsId.class));
                    }
                    homeRe.setAppgoodsId(appgoodsIds);

//                    ArrayList<AppgoodsId> appgoodsIds = (ArrayList<AppgoodsId>) JSON.parseArray(data, AppgoodsId.class);
                    homeRe.setSuccess(true);

                    //存入缓存
                    DataCache.putData(context, "HomeReAction.action", resultString);
                }
                else
                {
                    homeRe.setSuccess(false);
                    homeRe.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return homeRe;

    }

    //首页主题活动
    public OnlineshoppingGoods onlineshoppingGoodsAction(Context context, OnlineshoppingGoods onlineshoppingGoods)
    {
        onlineshoppingGoods.addVersion(context);  //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(onlineshoppingGoods);

        try
        {
            //读取缓存
            resultString = DataCache.getData(context, "OnlineshoppingGoodsAction.action");

            if(resultString == null)
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("OnlineshoppingGoodsAction.action"), CommonUtils.generateParams(jsonParam));

            }

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                onlineshoppingGoods.setSuccess(jsonObject.getBoolean("success"));
                if(onlineshoppingGoods.isSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");

                    ArrayList<AppgoodsId> appGoodes = new ArrayList<AppgoodsId>();
                    JSONArray appGoodsJSONArray = dataObject.getJSONArray("appGoods");
                    for(int a=0; a<appGoodsJSONArray.length(); a++)
                    {
                        JSONObject appGoodsJSONObject = appGoodsJSONArray.getJSONObject(a);
                        String data = appGoodsJSONObject.toString();
                        appGoodes.add(JSON.parseObject(data, AppgoodsId.class));
                    }
                    onlineshoppingGoods.setAppgoodsIds(appGoodes);

                    JSONObject appOnlineshoppingJSONObject = dataObject.getJSONObject("appOnlineshopping");
                    String data = appOnlineshoppingJSONObject.toString();
                    AppOnlineshopping appOnlineshopping = JSON.parseObject(data, AppOnlineshopping.class);
                    onlineshoppingGoods.setAppOnlineshopping(appOnlineshopping);

                    onlineshoppingGoods.setSuccess(true);

                    //存入缓存
                    DataCache.putData(context, "OnlineshoppingGoodsAction.action", resultString);

                }
                else
                {
                    onlineshoppingGoods.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return onlineshoppingGoods;

    }



}
