package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppStoresList;
import com.brand.ushopping.model.AppStoresListItem;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.BrandGoodsList;
import com.brand.ushopping.utils.HttpClientUtil;

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
    public AppStoresList gettAppStoresList(AppStoresList appStoresList)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appStoresList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GettAppStoresList.action", params);
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
    public BrandGoodsList getAppStoresIdAll(BrandGoodsList brandGoodsList)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(brandGoodsList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetAppStoresIdAll.action", params);
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
