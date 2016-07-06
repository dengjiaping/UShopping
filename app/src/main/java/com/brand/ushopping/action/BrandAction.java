package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppBrandCollect;
import com.brand.ushopping.model.AppBrandCollectItem;
import com.brand.ushopping.model.BrandRecommend;
import com.brand.ushopping.model.SaveAppBrandCollect;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.DataCache;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/16.
 */
public class BrandAction extends BaseAction
{
    public BrandAction(Context context) {
        super(context);
    }

    public BrandRecommend getRecommendAppBrandAction(Context context, BrandRecommend brand)
    {
        brand.addVersion(context);   //添加App版本信息
        BrandRecommend result = null;
        String resultString = null;
        String jsonParam = JSON.toJSONString(brand);

        try
        {
            resultString = DataCache.getData(context, "GetRecommendAppBrandAction.action");
            if(resultString == null)
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetRecommendAppBrandAction.action"), CommonUtils.generateParams(jsonParam));
            }

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

                    //存入缓存
                    DataCache.putData(context, "GetRecommendAppBrandAction.action", resultString);
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
        saveAppBrandCollect.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(saveAppBrandCollect);

        try
        {
            if(CommonUtils.isValueEmpty(resultString))
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveAppBrandCollectAction.action"), CommonUtils.generateParams(jsonParam));

                Log.v("brand favourite", resultString);

            }

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
        appBrandCollect.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appBrandCollect);

        try
        {
            if(CommonUtils.isValueEmpty(resultString))
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetListAppBrandCollectUserIdAction.action"), CommonUtils.generateParams(jsonParam));

            }
            Log.v("brand collect", resultString);

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
