package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.HttpClientUtil;
import com.brand.ushopping.utils.StaticValues;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import simplecache.ACache;

/**
 * Created by Administrator on 2015/11/9.
 */
public class MainpageAction
{
    private ACache mCache;

    //首页信息
    public Main home(Context context, User user)
    {
        mCache = ACache.get(context);

        Main result = new Main();
        String resultString = null;
        String jsonParam = JSON.toJSONString(user);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = mCache.getAsString("HomeAction");
            if(resultString == null)
            {
//                resultString = URLConnectionUtil.post("HomeAction.action", jsonParam);
                resultString = HttpClientUtil.post("HomeAction.action", params);
                Log.v("home action", resultString);
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
                    mCache.put("HomeAction", resultString, StaticValues.CACHE_LIFE);

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
        mCache = ACache.get(context);

        String resultString = null;
        String jsonParam = JSON.toJSONString(homeRe);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("HomeReAction.action", params);

            if(resultString != null)
            {
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

}
