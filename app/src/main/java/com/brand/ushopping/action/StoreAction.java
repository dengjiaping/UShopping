package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppStoresList;
import com.brand.ushopping.model.AppStoresListItem;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.BrandGoodsList;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
public class StoreAction
{
//    private ACache mCache;

    public AppStoresList gettAppStoresList(AppStoresList appStoresList)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appStoresList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GettAppStoresList.action", params);
            resultString = URLConnectionUtil.post("GettAppStoresList.action", jsonParam);
            Log.v("ushopping around", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    String data = dataArray.toString();
                    appStoresList.setAppStoresListItems((ArrayList<AppStoresListItem>) JSON.parseArray(data, AppStoresListItem.class));
                    appStoresList.setSuccess(true);

                }
                else
                {
                    appStoresList.setSuccess(false);
                    appStoresList.setMsg(jsonObject.getString("msg"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appStoresList;
    }

    // 根据实体店铺查询店铺商品
    public BrandGoodsList getAppStoresIdAll(Context context, BrandGoodsList brandGoodsList)
    {
//        mCache = ACache.get(context);

        String resultString = null;
        String jsonParam = JSON.toJSONString(brandGoodsList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            /*
            resultString = mCache.getAsString("GetAppStoresIdAll.action" + brandGoodsList.getAppbrandId() + brandGoodsList.getMin());
            if(CommonUtils.isValueEmpty(resultString))
            {
                resultString = HttpClientUtil.post("GetAppStoresIdAll.action", params);
                Log.v("brand goods", resultString);

            }
            */

//            resultString = HttpClientUtil.post("GetAppStoresIdAll.action", params);
            resultString = URLConnectionUtil.post("GetAppStoresIdAll.action", jsonParam);
            Log.v("brand goods", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    String data = dataArray.toString();
                    brandGoodsList.setAppgoodsIds((ArrayList<AppgoodsId>) JSON.parseArray(data, AppgoodsId.class));

                    brandGoodsList.setSuccess(true);

                    //存入缓存
                    // mCache.put("GetAppStoresIdAll.action" + brandGoodsList.getAppbrandId() + brandGoodsList.getMin(), resultString, StaticValues.CACHE_LIFE);

                }
                else
                {
                    brandGoodsList.setSuccess(false);
                    brandGoodsList.setMsg(jsonObject.getString("msg"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return brandGoodsList;
    }
}
