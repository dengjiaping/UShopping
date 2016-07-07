package com.brand.ushopping.action;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.AppCustomer;
import com.brand.ushopping.model.AppEvaluate;
import com.brand.ushopping.model.AppEvaluateItem;
import com.brand.ushopping.model.AppGoodsCollect;
import com.brand.ushopping.model.AppGoodsCollectItem;
import com.brand.ushopping.model.AppGoodsTypeId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.Charge;
import com.brand.ushopping.model.ClientCharge;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.GoodsInfo;
import com.brand.ushopping.model.SaveAppEvaluate;
import com.brand.ushopping.model.SaveAppGoodsCollect;
import com.brand.ushopping.model.SearchAppGoods;
import com.brand.ushopping.model.SelectChargeId;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.DataCache;
import com.brand.ushopping.utils.UDBHelper;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/19.
 */
public class GoodsAction extends BaseAction
{
    public GoodsAction(Context context) {
        super(context);
    }

    public GoodsInfo getAppGoodsIdAction(Context context, GoodsInfo goodsInfo)
    {
        goodsInfo.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(goodsInfo);

        try
        {
            resultString = DataCache.getData(context, "GetAppGoodsIdAction.action", goodsInfo.getGoodsId(), 0);
            if(resultString == null)
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppGoodsIdAction.action"), CommonUtils.generateParams(jsonParam));
            }

            Log.v("ushopping goods", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    goodsInfo = JSON.parseObject(data, GoodsInfo.class);
                    goodsInfo.setSuccess(true);

                    //存入缓存
                    DataCache.putData(context, "GetAppGoodsIdAction.action", resultString, goodsInfo.getGoodsId(), 0);
                }
                else
                {
                    goodsInfo.setSuccess(false);
                    goodsInfo.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return goodsInfo;
    }

    public String returnClientChargeAction(ClientCharge clientCharge)
    {
        clientCharge.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(clientCharge);
        String result = null;
        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("ReturnClientChargeAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    result = dataObject.toString();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public AppGoodsTypeId getAppGoodsTypeId(AppGoodsTypeId appGoodsTypeId)
    {
        appGoodsTypeId.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appGoodsTypeId);

        try
        {
            if(appGoodsTypeId.getUseCache())
            {
                resultString = DataCache.getData(context, "GetAppGoodsTypeId.action", appGoodsTypeId.getAppcategoryId(), appGoodsTypeId.getMin());
            }
            if(resultString == null)
            {
                resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppGoodsTypeId.action"), CommonUtils.generateParams(jsonParam));
            }

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray dataArray = jsonObject.getJSONArray("data");
//
                    ArrayList<Goods> goodses = new ArrayList<Goods>();
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject dataObject = dataArray.getJSONObject(a);
                        String data = dataObject.toString();
                        goodses.add(JSON.parseObject(data, Goods.class));

                    }
                    appGoodsTypeId.setGoods(goodses);

                    appGoodsTypeId.setSuccess(true);

                    //存入缓存
                    DataCache.putData(context, "GetAppGoodsTypeId.action", resultString, appGoodsTypeId.getAppcategoryId(), appGoodsTypeId.getMin());
                }
                else
                {
                    appGoodsTypeId.setSuccess(false);
                    appGoodsTypeId.setMsg(jsonObject.getString("msg"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appGoodsTypeId;
    }

    public SearchAppGoods searchAppGoodsAction(SearchAppGoods searchAppGoods)
    {
        searchAppGoods.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(searchAppGoods);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SearchAppGoodsAction.action"), CommonUtils.generateParams(jsonParam));
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<Goods> goodses = new ArrayList<Goods>();
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject dataObject = dataArray.getJSONObject(a);
                        String data = dataObject.toString();
                        goodses.add(JSON.parseObject(data, Goods.class));

                    }
                    searchAppGoods.setGoodses(goodses);

                    searchAppGoods.setSuccess(true);
                }
                else
                {
                    searchAppGoods.setSuccess(false);
                    searchAppGoods.setMsg(jsonObject.getString("msg"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return searchAppGoods;

    }

    // --  商品收藏  --
    public SaveAppGoodsCollect saveAppGoodsCollectAction(SaveAppGoodsCollect saveAppGoodsCollect)
    {
        saveAppGoodsCollect.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(saveAppGoodsCollect);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveAppGoodsCollectAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    saveAppGoodsCollect = JSON.parseObject(data, SaveAppGoodsCollect.class);
                    saveAppGoodsCollect.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return saveAppGoodsCollect;
    }

    // --  查询收藏列表  --
    public AppGoodsCollect getListAppGoodsCollectUserIdAction(AppGoodsCollect appGoodsCollect)
    {
        appGoodsCollect.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appGoodsCollect);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetListAppGoodsCollectUserIdAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<AppGoodsCollectItem> appGoodsCollectItems = new ArrayList<AppGoodsCollectItem>();
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject dataObject = dataArray.getJSONObject(a);
                        String data = dataObject.toString();
                        appGoodsCollectItems.add(JSON.parseObject(data, AppGoodsCollectItem.class));

                    }
                    appGoodsCollect.setAppGoodsCollectItems(appGoodsCollectItems);

                    appGoodsCollect.setSuccess(true);
                }
                else
                {
                    appGoodsCollect.setSuccess(false);
                    appGoodsCollect.setMsg(jsonObject.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appGoodsCollect;

    }

    public AppEvaluate getAppEvaluateAction(AppEvaluate appEvaluate)
    {
        appEvaluate.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appEvaluate);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetAppEvaluateAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    ArrayList<AppEvaluateItem> appEvaluateItems = new ArrayList<AppEvaluateItem>();
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject dataObject = dataArray.getJSONObject(a);
                        String data = dataObject.toString();
                        appEvaluateItems.add(JSON.parseObject(data, AppEvaluateItem.class));

                    }
                    appEvaluate.setAppEvaluateItems(appEvaluateItems);

                    appEvaluate.setSuccess(true);
                }
                else
                {
                    appEvaluate.setSuccess(false);
                    appEvaluate.setMsg(jsonObject.getString("msg"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appEvaluate;

    }

    // --  保存售后  --
    public AppCustomer saveAppCustomerAction(AppCustomer appCustomer)
    {
        appCustomer.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(appCustomer);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveAppCustomerAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    appCustomer = JSON.parseObject(data, AppCustomer.class);
                    appCustomer.setSuccess(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return appCustomer;
    }

    //发布商品评价
    public SaveAppEvaluate saveAppEvaluateAction(SaveAppEvaluate saveAppEvaluate)
    {
        saveAppEvaluate.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(saveAppEvaluate);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SaveAppEvaluateAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    saveAppEvaluate = JSON.parseObject(data, SaveAppEvaluate.class);
                    saveAppEvaluate.setSuccess(true);
                }
                else
                {
                    saveAppEvaluate.setSuccess(false);
                    saveAppEvaluate.setMsg(jsonObject.getString("msg"));
                }
            }
            else
            {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return saveAppEvaluate;
    }

    //添加到浏览历史
    public void addGoodsViewHistory(UDBHelper udbHelper, AppgoodsId appgoodsId)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("goods_id", appgoodsId.getId());
        contentValues.put("goods_name", appgoodsId.getGoodsName());
        contentValues.put("logopic", appgoodsId.getLogopicUrl());
        contentValues.put("price", appgoodsId.getPromotionPrice());

        SQLiteDatabase sqLiteDatabase = udbHelper.getWritableDatabase();
        long rowid = sqLiteDatabase.insert("goods_view_history", null, contentValues);

//        sqLiteDatabase.execSQL("insert into goods_view_history(goods_id, goods_name, logopic, price) values(?, ?, ?, ?)",
//                new Object[]{appgoodsId.getId(), appgoodsId.getGoodsName(), appgoodsId.getLogopicUrl(), appgoodsId.getPromotionPrice()});

        sqLiteDatabase.close();

    }

    //查询商品浏览历史
    public ArrayList<AppgoodsId> getGoodsViewHistory(UDBHelper udbHelper)
    {
        ArrayList<AppgoodsId> result = new ArrayList<AppgoodsId>();

        SQLiteDatabase sqLiteDatabase = udbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from goods_view_history", null);

        while(cursor.moveToNext()) {
            AppgoodsId appgoodsId = new AppgoodsId();
            appgoodsId.setId(cursor.getLong(cursor.getColumnIndex("goods_id")));
            appgoodsId.setGoodsName(cursor.getString(cursor.getColumnIndex("goods_name")));
            appgoodsId.setLogopicUrl(cursor.getString(cursor.getColumnIndex("logopic")));
            appgoodsId.setPromotionPrice(cursor.getDouble(cursor.getColumnIndex("price")));

            result.add(appgoodsId);
        }
        sqLiteDatabase.close();

        return result;
    }

    //商品是否存在
    public boolean isGoodsViewHistoryExists(UDBHelper udbHelper, long goodsId)
    {
        SQLiteDatabase sqLiteDatabase = udbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select goods_id from goods_view_history where goods_id="+Long.toString(goodsId), null);
        if(cursor.getCount() > 0)
        {
            sqLiteDatabase.close();
            return true;
        }
        else
        {
            sqLiteDatabase.close();
            return false;
        }

    }

    //清除历史足迹
    public void goodsViewHistoryClear(UDBHelper udbHelper)
    {
        SQLiteDatabase sqLiteDatabase = udbHelper.getWritableDatabase();

        sqLiteDatabase.execSQL("delete from goods_view_history");

        sqLiteDatabase.close();
    }


//    public Unionpay requestUnionpayAction(Unionpay unionpay)
//    {
//        String resultString = null;
//        String jsonParam = JSON.toJSONString(unionpay);
//        List params = new ArrayList();
//        params.add(new BasicNameValuePair("param", jsonParam));
//
//        try
//        {
//            resultString = HttpClientUtil.post("RequestUnionpayAction.action", params);
//            // {"data":{"appUser":{"sessionid":"94E0F1D135C3F809CDEB20DEDD5387BB","userId":6},"tn":"201512021431381452648"},"sessionid":"94E0F1D135C3F809CDEB20DEDD5387BB","success":true}
//            if(resultString != null)
//            {
//                JSONObject jsonObject = new JSONObject(resultString);
//
//                if(jsonObject.getBoolean("success"))
//                {
//                    unionpay.setSuccess(true);
//                    //赋值
//                    JSONObject dataObject = jsonObject.getJSONObject("data");
//                    String data = dataObject.toString();
//                    unionpay = JSON.parseObject(data, Unionpay.class);
//
//
//                } else
//                {
//                    unionpay.setSuccess(false);
//                    unionpay.setMsg(jsonObject.getString("msg"));
//
//                }
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return unionpay;
//    }

    //支付查询charge对象是否存在
    public SelectChargeId selectChargeIdPingAction(SelectChargeId selectChargeId)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(selectChargeId);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SelectChargeIdPingAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    selectChargeId.setCharge(JSON.parseObject(data, Charge.class));
                    selectChargeId.setSuccess(true);
                }
                else
                {
                    selectChargeId.setSuccess(false);
                    selectChargeId.setMsg(jsonObject.getString("msg"));
                }
            }
            else
            {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return selectChargeId;

    }

}
