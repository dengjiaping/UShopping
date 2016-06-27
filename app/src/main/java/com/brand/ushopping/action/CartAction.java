package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AddAppShopcart;
import com.brand.ushopping.model.AppShopcart;
import com.brand.ushopping.model.AppShopcartBrand;
import com.brand.ushopping.model.AppShopcartIdList;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppsmShopcart;
import com.brand.ushopping.model.AppyyShopcart;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/5.
 */
public class CartAction extends BaseAction{
    public CartAction(Context context) {
        super(context);
    }

    public AddAppShopcart addAppShopcartAction(AddAppShopcart addAppShopcart)
    {
        addAppShopcart.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(addAppShopcart);

        try
        {
//            resultString = HttpClientUtil.post("AddAppShopcartAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("AddAppShopcartAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    addAppShopcart = JSON.parseObject(data, AddAppShopcart.class);
                    addAppShopcart.setSuccess(true);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return addAppShopcart;
    }

    //查看购物车内容..
    public AppShopcartIdList getAppShopcartIdListAction(AppShopcartIdList appShopcartIdList)
    {
        appShopcartIdList.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);

        try
        {
//            resultString = HttpClientUtil.post("GetAppShopcartIdListAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppShopcartIdListAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                Log.v("cart", resultString);
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<AppShopcartBrand> appShopcartBrandList = new ArrayList<AppShopcartBrand>();

                    for(int a=0; a<dataArray.length(); a++)
                    {
                        AppShopcartBrand appShopcartBrand = new AppShopcartBrand();

                        JSONArray brandArray = dataArray.getJSONArray(a);

                        ArrayList<AppShopcart> appShopcarts = new ArrayList<AppShopcart>();
                        for(int b=0; b<brandArray.length(); b++)
                        {
                            JSONObject appShopcartObject = brandArray.getJSONObject(b);


                            String appShopcartData = appShopcartObject.toString();
                            AppShopcart appShopcart = JSON.parseObject(appShopcartData, AppShopcart.class);

                            if(appShopcart != null)
                            {
                                AppbrandId appbrandId = appShopcart.getAppgoodsId().getAppbrandId();
                                if(appbrandId != null)
                                {
                                    appShopcartBrand.setId(appbrandId.getId());
                                    appShopcartBrand.setBrandName(appbrandId.getBrandName());
                                    appShopcartBrand.setLogopic(appbrandId.getLogopic());

                                }

                                appShopcarts.add(appShopcart);
                            }
                        }
                        appShopcartBrand.setAppShopcarts(appShopcarts);

                        appShopcartBrandList.add(appShopcartBrand);
                    }

                    appShopcartIdList.setAppShopcartBrands(appShopcartBrandList);

                    appShopcartIdList.setSuccess(true);
                }
                else
                {
                    appShopcartIdList.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appShopcartIdList;

    }

    //删除购物车商品
    public AppShopcartIdList deleteShopcartId(AppShopcartIdList appShopcartIdList)
    {
        appShopcartIdList.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);
        Log.v("delete_cart", jsonParam);

        try
        {
//            resultString = HttpClientUtil.post("DeleteShopcartIdAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("DeleteShopcartIdAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    appShopcartIdList = JSON.parseObject(data, AppShopcartIdList.class);
                    appShopcartIdList.setSuccess(true);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appShopcartIdList;
    }

    //加入预定购物车
    public AddAppShopcart saveInsertAppyyShopcart(AddAppShopcart addAppShopcart)
    {
        addAppShopcart.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(addAppShopcart);

        try
        {
//            resultString = HttpClientUtil.post("SaveInsertAppyyShopcart.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveInsertAppyyShopcart.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    addAppShopcart = JSON.parseObject(data, AddAppShopcart.class);
                    addAppShopcart.setSuccess(true);
                }
                else
                {
                    addAppShopcart.setSuccess(false);
                    addAppShopcart.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return addAppShopcart;
    }

    //删除预约购物车
    public AppyyShopcart updateAppyyShopcart(AppyyShopcart appyyShopcart)
    {
        appyyShopcart.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appyyShopcart);
        Log.v("delete_yy_cart", jsonParam);

        try
        {
//            resultString = HttpClientUtil.post("UpdateAppyyShopcart.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("UpdateAppyyShopcart.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    appyyShopcart = JSON.parseObject(data, AppyyShopcart.class);
                    appyyShopcart.setSuccess(true);
                }
                else
                {
                    appyyShopcart.setSuccess(false);
                    appyyShopcart.setMsg(jsonObject.getString("msg"));
                }
            }
            else
            {
                appyyShopcart.setSuccess(false);
                appyyShopcart.setMsg("预约购物车删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appyyShopcart;
    }

    //删除上门购物车
    public AppsmShopcart updateAppsmShopcart(AppsmShopcart appsmShopcart)
    {
        appsmShopcart.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(appsmShopcart);
        Log.v("delete_sm_cart", jsonParam);

        try
        {
//            resultString = HttpClientUtil.post("UpdateAppsmShopcartAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("UpdateAppsmShopcartAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    appsmShopcart = JSON.parseObject(data, AppsmShopcart.class);
                    appsmShopcart.setSuccess(true);
                }
                else
                {
                    appsmShopcart.setSuccess(false);
                    appsmShopcart.setMsg(jsonObject.getString("msg"));

                }

            }
            else
            {
                appsmShopcart.setSuccess(false);
                appsmShopcart.setMsg("上门购物车删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appsmShopcart;
    }

    // --  根据用户ID查询预约购物车  --
    public AppShopcartIdList getAppyyShopcartIdList(AppShopcartIdList appShopcartIdList)
    {
        appShopcartIdList.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);

        try
        {
//            resultString = HttpClientUtil.post("GetAppyyShopcartIdList.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppyyShopcartIdList.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                Log.v("yy_cart", resultString);
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<AppShopcartBrand> appShopcartBrandList = new ArrayList<AppShopcartBrand>();

                    for(int a=0; a<dataArray.length(); a++)
                    {
                        AppShopcartBrand appShopcartBrand = new AppShopcartBrand();

                        JSONArray brandArray = dataArray.getJSONArray(a);

                        ArrayList<AppShopcart> appShopcarts = new ArrayList<AppShopcart>();
                        for(int b=0; b<brandArray.length(); b++)
                        {
                            JSONObject appShopcartObject = brandArray.getJSONObject(b);

                            String appShopcartData = appShopcartObject.toString();
                            AppShopcart appShopcart = JSON.parseObject(appShopcartData, AppShopcart.class);

                            if(appShopcart != null)
                            {
                                AppbrandId appbrandId = appShopcart.getAppgoodsId().getAppbrandId();
                                if(appbrandId != null)
                                {
                                    appShopcartBrand.setId(appbrandId.getId());
                                    appShopcartBrand.setBrandName(appbrandId.getBrandName());
                                    appShopcartBrand.setLogopic(appbrandId.getLogopic());

                                }

                                appShopcarts.add(appShopcart);
                            }
                        }
                        appShopcartBrand.setAppShopcarts(appShopcarts);

                        appShopcartBrandList.add(appShopcartBrand);
                    }

                    appShopcartIdList.setAppShopcartBrands(appShopcartBrandList);

                    appShopcartIdList.setSuccess(true);
                }
                else
                {
                    appShopcartIdList.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appShopcartIdList;
    }

    // 加入上门购物车
    public AddAppShopcart SaveInsertAppsmShopcart(AddAppShopcart addAppShopcart)
    {
        addAppShopcart.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(addAppShopcart);

        try
        {
//            resultString = HttpClientUtil.post("SaveInsertAppsmShopcart.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveInsertAppsmShopcart.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                Log.v("sm_cart", resultString);
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    addAppShopcart = JSON.parseObject(data, AddAppShopcart.class);
                    addAppShopcart.setSuccess(true);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return addAppShopcart;
    }

    // --  根据用户ID查询上门购物车  --
    public AppShopcartIdList getAppsmShopcartIdList(AppShopcartIdList appShopcartIdList)
    {
        appShopcartIdList.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetAppsmShopcartIdList.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppsmShopcartIdList.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<AppShopcartBrand> appShopcartBrandList = new ArrayList<AppShopcartBrand>();

                    for(int a=0; a<dataArray.length(); a++)
                    {
                        AppShopcartBrand appShopcartBrand = new AppShopcartBrand();

                        JSONArray brandArray = dataArray.getJSONArray(a);

                        ArrayList<AppShopcart> appShopcarts = new ArrayList<AppShopcart>();
                        for(int b=0; b<brandArray.length(); b++)
                        {
                            JSONObject appShopcartObject = brandArray.getJSONObject(b);

                            String appShopcartData = appShopcartObject.toString();
                            AppShopcart appShopcart = JSON.parseObject(appShopcartData, AppShopcart.class);

                            if(appShopcart != null)
                            {
                                AppbrandId appbrandId = appShopcart.getAppgoodsId().getAppbrandId();
                                if(appbrandId != null)
                                {
                                    appShopcartBrand.setId(appbrandId.getId());
                                    appShopcartBrand.setBrandName(appbrandId.getBrandName());
                                    appShopcartBrand.setLogopic(appbrandId.getLogopic());

                                }

                                appShopcarts.add(appShopcart);
                            }
                        }
                        appShopcartBrand.setAppShopcarts(appShopcarts);

                        appShopcartBrandList.add(appShopcartBrand);
                    }

                    appShopcartIdList.setAppShopcartBrands(appShopcartBrandList);

                    appShopcartIdList.setSuccess(true);
                }
                else
                {
                    appShopcartIdList.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appShopcartIdList;
    }

}
