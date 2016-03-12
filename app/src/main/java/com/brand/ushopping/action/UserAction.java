package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.WeiboUser;
import com.brand.ushopping.utils.HttpClientUtil;

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
            resultString = HttpClientUtil.post("GetLoginAction.action", params);
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
            resultString = HttpClientUtil.post("RegisteredAction.action", params);

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

    //微博登录
    public User sinaRegistered(WeiboUser weiboUser)
    {
        User user = null;
        String resultString = null;
        String jsonParam = JSON.toJSONString(weiboUser);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("SinaRegistered.action", params);
            //{"access_token":"2.00xgZWoB0CRvcw0fff1f85ecpxfsWC","followMe":false,"gender":"m","headImg":"http://tp4.sinaimg.cn/1662713331/50/5721488565/1","uid":"1662713331","url":"","userName":"Viator42X","verified":false}
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
            resultString = HttpClientUtil.post("UpdateAppUser.action", params);

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
