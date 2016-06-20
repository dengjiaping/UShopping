package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.OkHttpUtil;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/11.
 */
public class AddressAction
{
    public ArrayList<Address> GetAppAddressAllAction(Address address)
    {
        ArrayList<Address> result = null;
        String resultString = null;
        String jsonParam = JSON.toJSONString(address);
//        List params = new ArrayList();
//        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetAppAddressAllAction.action", params);
//            resultString = OkHttpUtil.post("GetAppAddressAllAction.action", jsonParam);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppAddressAllAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    String data = dataArray.toString();
                    result = (ArrayList<Address>) JSON.parseArray(data, Address.class);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public Address addAddress(Address address)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(address);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("SaveAppAddressAction.action", params);
            resultString = OkHttpUtil.post("SaveAppAddressAction.action", jsonParam);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveAppAddressAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    String data = jsonObject.toString();
                    address = JSON.parseObject(data, Address.class);

                    address.setSuccess(true);
                }
                else
                {
                    address.setSuccess(false);
                    address.setMsg(jsonObject.getString("msg"));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return address;
    }

    public Address updateAddress(Address address)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(address);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("UpdateAddressAction.action", params);
//            resultString = OkHttpUtil.post("UpdateAddressAction.action", jsonParam);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("UpdateAddressAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    String data = jsonObject.toString();
                    JSON.parseObject(data, Address.class);

                    address.setSuccess(true);
                }
                else
                {
                    address.setSuccess(false);
                    address.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return address;
    }

    public Address deleteAddress(Address address)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(address);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("DeleteAddressAction.action", params);
//            resultString = OkHttpUtil.post("DeleteAddressAction.action", jsonParam);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("DeleteAddressAction.action"), CommonUtils.generateParams(jsonParam));

            Log.v("delete address", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    address.setSuccess(true);

                }
                else
                {
                    address.setSuccess(false);
                    address.setMsg(jsonObject.getString("msg"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return address;
    }

    public Address setDefaultAddress(Address address)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(address);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("DefaultAddressAction.action", params);
//            resultString = OkHttpUtil.post("DefaultAddressAction.action", jsonParam);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("DefaultAddressAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                address.setSuccess(jsonObject.getBoolean("success"));
                address.setMsg(jsonObject.getString("msg"));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return address;
    }

}
