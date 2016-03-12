package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppTheme;
import com.brand.ushopping.model.AppThemeItem;
import com.brand.ushopping.utils.HttpClientUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ThemeAction
{
    public AppTheme getAppThemeAllAction(AppTheme appTheme)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appTheme);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetAppThemeAllAction.action", params);
            Log.v("theme", resultString);
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
