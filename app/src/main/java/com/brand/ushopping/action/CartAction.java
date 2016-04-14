package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AddAppShopcart;
import com.brand.ushopping.model.AppShopcart;
import com.brand.ushopping.model.AppShopcartBrand;
import com.brand.ushopping.model.AppShopcartIdList;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppyyShopcart;
import com.brand.ushopping.utils.HttpClientUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/5.
 */
public class CartAction {
    public AddAppShopcart addAppShopcartAction(AddAppShopcart addAppShopcart)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(addAppShopcart);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("AddAppShopcartAction.action", params);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetAppShopcartIdListAction.action", params);
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

    public AppShopcartIdList deleteShopcartId(AppShopcartIdList appShopcartIdList)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("DeleteShopcartIdAction.action", params);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(addAppShopcart);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("SaveInsertAppyyShopcart.action", params);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(appyyShopcart);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("UpdateAppyyShopcart.action", params);

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

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appyyShopcart;
    }

    // --  根据用户ID查询预约购物车  --
    public AppShopcartIdList getAppyyShopcartIdList(AppShopcartIdList appShopcartIdList)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetAppyyShopcartIdList.action", params);

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

    // 加入上门购物车
    public AddAppShopcart SaveInsertAppsmShopcart(AddAppShopcart addAppShopcart)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(addAppShopcart);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("SaveInsertAppsmShopcart.action", params);

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

    // --  根据用户ID查询上门购物车  --
    public AppShopcartIdList getAppsmShopcartIdList(AppShopcartIdList appShopcartIdList)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appShopcartIdList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetAppsmShopcartIdList.action", params);

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
