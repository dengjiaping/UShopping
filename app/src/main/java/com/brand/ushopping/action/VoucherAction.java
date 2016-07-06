package com.brand.ushopping.action;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/1.
 */
public class VoucherAction extends BaseAction
{
    public VoucherAction(Context context) {
        super(context);
    }

    // -- 查询优惠券 --
    public AppVoucherSelect appVoucherSelectGeneralAction(AppVoucherSelect appVoucherSelect)
    {
        appVoucherSelect.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appVoucherSelect);

        try
        {
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
        saveUserVoucher.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(saveUserVoucher);

        try
        {
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
        getUserVoucher.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(getUserVoucher);

        try
        {
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
        manJainVoucher.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(manJainVoucher);

        try
        {
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
        batchSaveUserVoucher.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(batchSaveUserVoucher);

        try
        {
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
