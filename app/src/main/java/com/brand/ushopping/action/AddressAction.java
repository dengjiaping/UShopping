package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.utils.HttpClientUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
            resultString = HttpClientUtil.post("GetAppAddressAllAction.action", params);
            //{"data":[{"area":"asdasd","consignee":"孙成","deaddress":"ssssss","flag":0,"id":1,"latitude":12,"longitude":12,"mobile":"23123123","zipcode":123213},{"area":"山东省滨州市","consignee":"张三","deaddress":"阳信县xxx乡xxx村","flag":1,"id":2,"latitude":36.686382,"longitude":117.092626,"mobile":"15634828386"}],"sessionid":"4BBF9AE348D080F1A065A0932DA089A9","success":true,"userId":6}

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
            resultString = HttpClientUtil.post("SaveAppAddressAction.action", params);

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
            resultString = HttpClientUtil.post("UpdateAddressAction.action", params);

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
            resultString = HttpClientUtil.post("DeleteAddressAction.action", params);
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
            resultString = HttpClientUtil.post("DefaultAddressAction.action", params);
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
