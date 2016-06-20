package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.Sign;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class SignAction
{
    // --  查询签到天数 --
    public Sign getSignAction(Sign sign)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(sign);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetSignAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetSignAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("last sign", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    sign = JSON.parseObject(data, Sign.class);
                    sign.setSuccess(true);
                }
                else
                {
                    sign.setSuccess(false);
                    sign.setMsg(jsonObject.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return sign;
    }

    //--  签到接口 --
    public Sign signAction(Sign sign)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(sign);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("SignAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SignAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    sign = JSON.parseObject(data, Sign.class);
                    sign.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return sign;
    }

}
