package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppBrandCollect;
import com.brand.ushopping.model.AppBrandCollectItem;
import com.brand.ushopping.model.BrandRecommend;
import com.brand.ushopping.model.SaveAppBrandCollect;
import com.brand.ushopping.utils.HttpClientUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public class BrandAction
{
    public BrandRecommend getRecommendAppBrandAction(BrandRecommend brand)
    {
        BrandRecommend result = null;
        String resultString = null;
        String jsonParam = JSON.toJSONString(brand);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetRecommendAppBrandAction.action", params);
            Log.v("ushopping brands", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    result = JSON.parseObject(data, BrandRecommend.class);
                    result.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    // --  收藏品牌  --
    public SaveAppBrandCollect saveAppBrandCollectAction(SaveAppBrandCollect saveAppBrandCollect)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(saveAppBrandCollect);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("SaveAppBrandCollectAction.action", params);
            Log.v("brand favourite", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    saveAppBrandCollect = JSON.parseObject(data, SaveAppBrandCollect.class);
                    saveAppBrandCollect.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return saveAppBrandCollect;

    }

    // --  查询收藏列表  --
    public AppBrandCollect getListAppBrandCollectUserIdAction(AppBrandCollect appBrandCollect)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appBrandCollect);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetListAppBrandCollectUserIdAction.action", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<AppBrandCollectItem> appBrandCollectItems = new ArrayList<AppBrandCollectItem>();
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject dataObject = dataArray.getJSONObject(a);
                        String data = dataObject.toString();
                        appBrandCollectItems.add(JSON.parseObject(data, AppBrandCollectItem.class));

                    }
                    appBrandCollect.setAppBrandCollectItems(appBrandCollectItems);

                    appBrandCollect.setSuccess(true);
                }
                else
                {
                    appBrandCollect.setSuccess(false);
                    appBrandCollect.setMsg(jsonObject.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appBrandCollect;

    }


}
