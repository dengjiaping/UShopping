package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppTheme;
import com.brand.ushopping.model.AppThemeItem;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.DataCache;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ThemeAction extends BaseAction
{
    public ThemeAction(Context context) {
        super(context);
    }

    public AppTheme getAppThemeAllAction(Context context, AppTheme appTheme)
    {
        appTheme.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appTheme);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = DataCache.getData(context, "GetAppThemeAllAction.action");
            if(CommonUtils.isValueEmpty(resultString))
            {
//                resultString = HttpClientUtil.post("GetAppThemeAllAction.action", params);
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppThemeAllAction.action"), CommonUtils.generateParams(jsonParam));
                Log.v("theme", resultString);

            }

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<AppThemeItem> appThemeItems = new ArrayList<AppThemeItem>();
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject dataObject = dataArray.getJSONObject(a);
                        String data = dataObject.toString();
                        appThemeItems.add(JSON.parseObject(data, AppThemeItem.class));

                    }
                    appTheme.setAppThemeItems(appThemeItems);

                    appTheme.setSuccess(true);

                    //存入缓存
                    DataCache.putData(context, "GetAppThemeAllAction.action", resultString);
                }
                else
                {
                    appTheme.setMsg(jsonObject.getString("msg"));
                    appTheme.setSuccess(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appTheme;
    }

}
