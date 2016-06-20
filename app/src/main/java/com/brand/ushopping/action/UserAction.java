package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.ThirdPartyUser;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class UserAction
{
    //登录
    public User login(User user)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(user);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetLoginAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetLoginAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("ushopping login", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                user.setSuccess(jsonObject.getBoolean("success"));
                if(user.getSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    user = JSON.parseObject(data, User.class);
                    user.setSuccess(true);
                }
                else
                {
                    user.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    //注册
    public User register(User user)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(user);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("RegisteredAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("RegisteredAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                //{"data":{"flag":0,"mobile":"15165417820","sessionid":"2327FA2387FBD8FE3995F3CBC2E9D84A","userId":6,"userName":"用户名"},"sessionid":"2327FA2387FBD8FE3995F3CBC2E9D84A","success":true,"userId":6}
                user.setSuccess(jsonObject.getBoolean("success"));
                if(user.getSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    user = JSON.parseObject(data, User.class);
                    user.setSuccess(true);
                }
                else
                {
                    user.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return user;

    }

    //第三方登录 //2 weibo 3 qq 4 weixin
    public User thirdPartyLogin(ThirdPartyUser thirdPartyUser)
    {
        User user = null;
        String resultString = null;
        String jsonParam = JSON.toJSONString(thirdPartyUser);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("SinaRegistered.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SinaRegistered.action"), CommonUtils.generateParams(jsonParam));
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                //赋值
                JSONObject dataObject = jsonObject.getJSONObject("data");
                String data = dataObject.toString();
                user = JSON.parseObject(data, User.class);
                if(user.getUserId() != 0)
                {
                    user.setSuccess(true);
                }
                else
                {
                    user.setSuccess(false);
                    user.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    //更新用户信息
    public User updateAppUser(User user)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(user);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("UpdateAppUser.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("UpdateAppUser.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                //赋值
                JSONObject dataObject = jsonObject.getJSONObject("data");
                String data = dataObject.toString();
                user = JSON.parseObject(data, User.class);
                if(jsonObject.getBoolean("success"))
                {
                    user.setSuccess(true);
                }
                else
                {
                    return null;
                }

            }
            else
            {
                user.setSuccess(false);
                user.setMsg("网络连接错误");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return user;

    }

}
