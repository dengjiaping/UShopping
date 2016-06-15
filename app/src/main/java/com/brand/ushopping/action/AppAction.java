package com.brand.ushopping.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.Feedback;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.Version;
import com.brand.ushopping.utils.HttpClientUtil;
import com.brand.ushopping.utils.StaticValues;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
//            resultString = OkHttpUtil.post("GetMaxVersionAction.action", jsonParam);

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
        Main mMain = new Main();
        if(user != null)
        {
            mMain.setUserId(user.getUserId());
            mMain.setSessionid(user.getSessionid());

        }
        Main result = null;
        try
        {
            result = new MainpageAction().home(context, mMain);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        appContext.setMain(result);

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

        new AppAction().downloadSplash(context, "http://static.oschina.net/uploads/img/201208/13122559_L8G0.png");
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
//            resultString = OkHttpUtil.post("FeedbackSaveAction.action", jsonParam);

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

    public void downloadSplash(Context context, String url)
    {
        try
        {
            FileOutputStream fileOutputStream = context.openFileOutput("splash.png", Context.MODE_PRIVATE);

            byte[] byteArray = HttpClientUtil.getImageFromWeb(url);
            fileOutputStream.write(byteArray);

            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public Bitmap loadSplash(Context context)
    {
        String url = Environment.getExternalStorageDirectory() + "/splash.png";
        try
        {
            File file = new File(url);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(url);
                return bitmap;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }

}
