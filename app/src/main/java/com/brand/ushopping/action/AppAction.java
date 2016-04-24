package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.Feedback;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.Version;
import com.brand.ushopping.utils.HttpClientUtil;
import com.brand.ushopping.utils.StaticValues;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class AppAction {
    //获取版本号
    public Version getMaxVersionAction(Version version)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(version);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));
        Log.v("version jsonParam", jsonParam);
        try
        {
            resultString = HttpClientUtil.post("GetMaxVersionAction.action", params);
            Log.v("version", resultString);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                version.setSuccess(jsonObject.getBoolean("success"));
                if(version.isSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    version = JSON.parseObject(data, Version.class);
                    version.setSuccess(true);
                }
                else
                {
                    version.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return version;

    }

    //启动初始化
    public void appDataInit(Context context, User user)
    {
        AppContext appContext = (AppContext) context.getApplicationContext();

        if(user != null)
        {
            //地址列表
            Address address = new Address();
            address.setUserId(user.getUserId());
            address.setSessionid(user.getSessionid());

            ArrayList<Address> addresses = new AddressAction().GetAppAddressAllAction(address);
            if(addresses != null && !addresses.isEmpty())
            {
                appContext.setAddressList(addresses);

            }

            //获取默认地址
            Address addressDefault = new RefAction().getDefaultAddress(context);
            appContext.setDefaultAddress(addressDefault.getDeaddress());
            appContext.setDefaultAddressId(addressDefault.getAddressId());

        }

        //首页
        appContext.setMain(new MainpageAction().home(context, user));
        //首页下拉
        HomeRe homeRe = new HomeRe();
        if(user != null)
        {
            homeRe.setUserId(user.getUserId());
            homeRe.setSessionid(user.getSessionid());
        }
        homeRe.setMin(0);
        homeRe.setMax(StaticValues.RECOMMEND_PAGE_COUNT);
        appContext.setHomeRe(new MainpageAction().homeRe(context, homeRe));

    }

    //反馈
    public Feedback feedbackSaveAction(Feedback feedback)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(feedback);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("FeedbackSaveAction.action", params);

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                feedback.setSuccess(jsonObject.getBoolean("success"));
                if(feedback.isSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    feedback = JSON.parseObject(data, Feedback.class);
                    feedback.setSuccess(true);
                }
                else
                {
                    feedback.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return feedback;
    }

}
