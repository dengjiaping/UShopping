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
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public class MainpageAction
{
//    private ACache mCache = null;

    //首页信息
    public Main home(Context context, Main mMain)
    {
        if(context != null)
        {
//            mCache = ACache.get(context);
        }

        Main result = new Main();
        String resultString = null;
        String jsonParam = JSON.toJSONString(mMain);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            if(mCache != null)
//            {
//                resultString = mCache.getAsString("HomeAction.action");
//            }

            if(resultString == null)
            {
//                resultString = URLConnectionUtil.post("HomeAction.action", jsonParam);
//                resultString = HttpClientUtil.post("HomeAction.action", params);
//                resultString = OkHttpUtil.post("HomeAction.action", jsonParam);
                resultString = URLConnectionUtil.post("HomeAction.action", jsonParam);

                Log.v("home", resultString);
            }

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                result.setSuccess(jsonObject.getBoolean("success"));
                if(result.isSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    result = JSON.parseObject(data, Main.class);
                    result.setSuccess(true);

                    //存入缓存
//                    mCache.put("HomeAction.action", resultString, StaticValues.CACHE_LIFE);

                }
                else
                {
                    result.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;

    }

    //首页下拉
    public HomeRe homeRe(Context context, HomeRe homeRe)
    {
//        if(context != null)
//        {
//            mCache = ACache.get(context);
//        }

        String resultString = null;
        String jsonParam = JSON.toJSONString(homeRe);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            if(mCache != null)
//            {
//                resultString = mCache.getAsString("HomeReAction.action" + homeRe.getMin());
//            }

            if(CommonUtils.isValueEmpty(resultString))
            {
//                resultString = HttpClientUtil.post("HomeReAction.action", params);
//                resultString = OkHttpUtil.post("HomeReAction.action", jsonParam);
                resultString = URLConnectionUtil.post("HomeReAction.action", jsonParam);
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
//                    mCache.put("HomeReAction.action" + homeRe.getMin(), resultString, StaticValues.CACHE_LIFE);

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
//        if(context != null)
//        {
//            mCache = ACache.get(context);
//        }

        OnlineshoppingGoods result = new OnlineshoppingGoods();
        String resultString = null;
        String jsonParam = JSON.toJSONString(onlineshoppingGoods);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            if(mCache != null)
//            {
//                resultString = mCache.getAsString("OnlineshoppingGoodsAction.action");
//            }

            if(resultString == null)
            {
//                resultString = HttpClientUtil.post("OnlineshoppingGoodsAction.action", params);
                resultString = URLConnectionUtil.post("OnlineshoppingGoodsAction.action", jsonParam);

            }

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                result.setSuccess(jsonObject.getBoolean("success"));
                if(result.isSuccess())
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
                    result.setAppgoodsIds(appGoodes);

                    JSONObject appOnlineshoppingJSONObject = dataObject.getJSONObject("appOnlineshopping");
                    String data = appOnlineshoppingJSONObject.toString();
                    AppOnlineshopping appOnlineshopping = JSON.parseObject(data, AppOnlineshopping.class);
                    result.setAppOnlineshopping(appOnlineshopping);

                    result.setSuccess(true);

                    //存入缓存
//                    mCache.put("OnlineshoppingGoodsAction.action", resultString, StaticValues.CACHE_LIFE);

                }
                else
                {
                    result.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;

    }



}
