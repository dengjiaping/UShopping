package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppStoresList;
import com.brand.ushopping.model.AppStoresListItem;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.BrandGoodsList;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/23.
 */
public class StoreAction extends BaseAction
{
    public StoreAction(Context context) {
        super(context);
    }
//    private ACache mCache;

    public AppStoresList gettAppStoresList(AppStoresList appStoresList)
    {
        appStoresList.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appStoresList);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GettAppStoresList.action"), CommonUtils.generateParams(jsonParam));
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
        brandGoodsList.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(brandGoodsList);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppStoresIdAll.action"), CommonUtils.generateParams(jsonParam));
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
