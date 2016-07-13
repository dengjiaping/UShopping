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
import com.brand.ushopping.model.GetSelectAppStartpicture;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.Version;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/30.
 */
public class AppAction extends BaseAction{
    public AppAction(Context context) {
        super(context);
    }

    //获取版本号
    public Version getMaxVersionAction(Version version)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(version);

        Log.v("version jsonParam", jsonParam);
        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetMaxVersionAction.action"), CommonUtils.generateParams(jsonParam));

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
    public void appDataInit(User user)
    {
        AppContext appContext = (AppContext) context.getApplicationContext();

        if(user != null)
        {
            //地址列表
            Address address = new Address();
            address.setUserId(user.getUserId());
            address.setSessionid(user.getSessionid());

            ArrayList<Address> addresses = new AddressAction(context).GetAppAddressAllAction(address);
            if(addresses != null && !addresses.isEmpty())
            {
                appContext.setAddressList(addresses);

            }

            //获取默认地址
            Address addressDefault = new RefAction(context).getDefaultAddress(context);
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
        mMain.setUseCache(true);
        Main result = null;
        try
        {
            result = new MainpageAction(context).home(context, mMain);
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
        homeRe.setUseCache(true);
        appContext.setHomeRe(new MainpageAction(context).homeRe(homeRe));


//        new AppAction().downloadSplash(context, "http://static.oschina.net/uploads/img/201208/13122559_L8G0.png");
    }

    //反馈
    public Feedback feedbackSaveAction(Feedback feedback)
    {
        feedback.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(feedback);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("FeedbackSaveAction.action"), CommonUtils.generateParams(jsonParam));

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

//    public void downloadSplash(Context context, String url)
//    {
//        try
//        {
//            FileOutputStream fileOutputStream = context.openFileOutput("splash.png", Context.MODE_PRIVATE);
//
//            byte[] byteArray = HttpClientUtil.getImageFromWeb(url);
//            fileOutputStream.write(byteArray);
//
//            fileOutputStream.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//    }

    public Bitmap loadSplash(Context context)
    {
        String url = Environment.getExternalStorageDirectory() + "/com.brand.ushopping/splash.png";
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

    //-- 启动页
    public GetSelectAppStartpicture getSelectAppStartpictureAction(GetSelectAppStartpicture getSelectAppStartpicture)
    {
        getSelectAppStartpicture.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(getSelectAppStartpicture);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetSelectAppStartpicture.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                getSelectAppStartpicture.setSuccess(jsonObject.getBoolean("success"));
                if(getSelectAppStartpicture.isSuccess())
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    getSelectAppStartpicture = JSON.parseObject(data, GetSelectAppStartpicture.class);
                    getSelectAppStartpicture.setSuccess(true);
                }
                else
                {
                    getSelectAppStartpicture.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return getSelectAppStartpicture;
    }

}
