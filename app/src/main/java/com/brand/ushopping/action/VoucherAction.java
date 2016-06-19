package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppVoucherSelect;
import com.brand.ushopping.model.BatchSaveUserVoucher;
import com.brand.ushopping.model.GetUserVoucher;
import com.brand.ushopping.model.ManJainVoucher;
import com.brand.ushopping.model.ManJianVoucherItem;
import com.brand.ushopping.model.SaveUserVoucher;
import com.brand.ushopping.model.UserVoucherItem;
import com.brand.ushopping.model.VoucherItem;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/1.
 */
public class VoucherAction
{
    // -- 查询优惠券 --
    public AppVoucherSelect appVoucherSelectGeneralAction(AppVoucherSelect appVoucherSelect)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(appVoucherSelect);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("AppVoucherSelectGeneralAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("AppVoucherSelectGeneralAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("voucher", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray vouderJSONArray = jsonObject.getJSONArray("data");
                    ArrayList<VoucherItem> voucherItems = new ArrayList<VoucherItem>();
                    for(int a=0; a<vouderJSONArray.length(); a++)
                    {
                        JSONObject dataObject = vouderJSONArray.getJSONObject(a);
                        String data = dataObject.toString();
                        voucherItems.add(JSON.parseObject(data, VoucherItem.class));

                    }
                    appVoucherSelect.setVoucherItems(voucherItems);
                    appVoucherSelect.setSuccess(true);

                }
                else
                {
                    appVoucherSelect.setSuccess(false);
                    appVoucherSelect.setMsg(jsonObject.getString("msg"));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appVoucherSelect;
    }

    // --  用户领取优惠券  --
    public SaveUserVoucher saveUserVoucherAction(SaveUserVoucher saveUserVoucher)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(saveUserVoucher);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("SaveUserVoucherAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveUserVoucherAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    saveUserVoucher = JSON.parseObject(data, SaveUserVoucher.class);
                    saveUserVoucher.setSuccess(true);

                }
                else
                {
                    saveUserVoucher.setSuccess(false);
                    saveUserVoucher.setMsg(jsonObject.getString("msg"));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return saveUserVoucher;
    }

    // --  查询用户优惠券  --
    public GetUserVoucher getUserVoucherIdAction(GetUserVoucher getUserVoucher)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(getUserVoucher);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetUserVoucherIdAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetUserVoucherIdAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("user voucher", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray vouderJSONArray = jsonObject.getJSONArray("data");
                    ArrayList<UserVoucherItem> userVoucherItems = new ArrayList<UserVoucherItem>();
                    for(int a=0; a<vouderJSONArray.length(); a++)
                    {
                        JSONObject dataObject = vouderJSONArray.getJSONObject(a);
                        String data = dataObject.toString();
                        userVoucherItems.add(JSON.parseObject(data, UserVoucherItem.class));

                    }
                    getUserVoucher.setUserVoucherItems(userVoucherItems);

                    getUserVoucher.setSuccess(true);

                }
                else
                {
                    getUserVoucher.setSuccess(false);
                    getUserVoucher.setMsg(jsonObject.getString("msg"));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return getUserVoucher;

    }

    //查询满减券
    public ManJainVoucher manJainAllAction(ManJainVoucher manJainVoucher)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(manJainVoucher);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("ManJainAllAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("ManJainAllAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("manjian", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONArray vouderJSONArray = jsonObject.getJSONArray("data");
                    ArrayList<ManJianVoucherItem> manJianVoucherItems = new ArrayList<ManJianVoucherItem>();

                    for(int a=0; a<vouderJSONArray.length(); a++)
                    {
                        JSONObject dataObject = vouderJSONArray.getJSONObject(a);
                        String data = dataObject.toString();
                        manJianVoucherItems.add(JSON.parseObject(data, ManJianVoucherItem.class));

                    }
                    manJainVoucher.setManJianVoucherItems(manJianVoucherItems);

                    manJainVoucher.setSuccess(true);

                }
                else
                {
                    manJainVoucher.setSuccess(false);
                    manJainVoucher.setMsg(jsonObject.getString("msg"));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return manJainVoucher;
    }

    // 一键领取大礼包
    public BatchSaveUserVoucher batchSaveUserVoucherAction(BatchSaveUserVoucher batchSaveUserVoucher)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(batchSaveUserVoucher);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("BatchSaveUserVoucherAction.action", params);
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("BatchSaveUserVoucherAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    batchSaveUserVoucher = JSON.parseObject(data, BatchSaveUserVoucher.class);
                    batchSaveUserVoucher.setSuccess(true);

                }
                else
                {
                    batchSaveUserVoucher.setSuccess(false);
                    batchSaveUserVoucher.setMsg(jsonObject.getString("msg"));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return batchSaveUserVoucher;

    }


}
