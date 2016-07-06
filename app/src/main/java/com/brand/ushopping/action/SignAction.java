package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.Sign;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/21.
 */
public class SignAction extends BaseAction
{
    public SignAction(Context context) {
        super(context);
    }

    // --  查询签到天数 --
    public Sign getSignAction(Sign sign)
    {
        sign.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(sign);

        try
        {
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
        sign.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(sign);

        try
        {
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
