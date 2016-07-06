package com.brand.ushopping.action;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.ConfirmOrder;
import com.brand.ushopping.model.GetSmorderStatusList;
import com.brand.ushopping.model.ManJianAll;
import com.brand.ushopping.model.OrderAll;
import com.brand.ushopping.model.OrderGoodsItem;
import com.brand.ushopping.model.OrderItem;
import com.brand.ushopping.model.OrderSaveList;
import com.brand.ushopping.model.OrderStatusListItem;
import com.brand.ushopping.model.OrderSuccess;
import com.brand.ushopping.model.OrderUpdate;
import com.brand.ushopping.model.SmOrderSaveList;
import com.brand.ushopping.model.YyOrderSaveList;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/27.
 */
public class OrderAction extends BaseAction
{
    public OrderAction(Context context) {
        super(context);
    }

    //提交订单
    public OrderSaveList orderSaveAction(OrderSaveList orderSaveList)
    {
        orderSaveList.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderSaveList);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("OrderSaveAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("OrderSaveAction", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    orderSaveList = JSON.parseObject(data, OrderSaveList.class);
                    orderSaveList.setSuccess(true);

                }
                else
                {
                    orderSaveList.setSuccess(false);
                    orderSaveList.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderSaveList;

    }

    public OrderAll getOrderAction(OrderAll orderAll)
    {
        orderAll.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderAll);

        String result = null;
        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetOrderAllAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("order", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);

                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray orderJSONArray = jsonObject.getJSONArray("data");

                    ArrayList<OrderItem> orderList = new ArrayList<OrderItem>();
                    for(int a=0; a<orderJSONArray.length(); a++)
                    {
                        double money = 0;
                        int quantity = 0;
                        JSONArray goodsJSONArray = orderJSONArray.getJSONArray(a);
                        OrderItem orderItem = new OrderItem();
                        ArrayList<OrderGoodsItem> orderGoodsItems = new ArrayList<OrderGoodsItem>();
                        for(int b=0; b<goodsJSONArray.length(); b++)
                        {
                            JSONObject goodsJSONObject = goodsJSONArray.getJSONObject(b);
                            String data = goodsJSONObject.toString();
                            OrderGoodsItem orderGoodsItem = JSON.parseObject(data, OrderGoodsItem.class);
                            if(orderGoodsItem != null)
                            {
                                orderGoodsItems.add(orderGoodsItem);

                                orderItem.setFlag(orderGoodsItem.getFlag());
                                orderItem.setOrderNo(orderGoodsItem.getOrderNo());

                                quantity += orderGoodsItem.getQuantity();
                                money += (orderGoodsItem.getMoney() * orderGoodsItem.getQuantity());

                            }

                        }
                        orderItem.setOrderGoodsItems(orderGoodsItems);
                        orderItem.setMoney(money);
                        orderItem.setQuantity(quantity);

                        orderList.add(orderItem);
                    }

                    orderAll.setOrderList(orderList);

                    orderAll.setSuccess(true);

                }
                else
                {
                    orderAll.setSuccess(false);
                    orderAll.setMsg(jsonObject.getString("msg"));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderAll;
    }

    // --  预约订单保存  --
    public YyOrderSaveList yyOrderSaveAction(YyOrderSaveList yyOrderSaveList)
    {
        yyOrderSaveList.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(yyOrderSaveList);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("YyOrderSaveAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    yyOrderSaveList = JSON.parseObject(data, YyOrderSaveList.class);
                    yyOrderSaveList.setSuccess(true);

                }
                else
                {
                    yyOrderSaveList.setSuccess(false);
                    yyOrderSaveList.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return yyOrderSaveList;

    }

    //    --  查询预约订单列表  --
    public OrderAll getYyOrderAllAction(OrderAll orderAll)
    {
        orderAll.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderAll);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetYyOrderAllAction.action"), CommonUtils.generateParams(jsonParam));
            Log.v("GetYyOrderAllAction", resultString);
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray orderJSONArray = jsonObject.getJSONArray("data");

                    ArrayList<OrderItem> orderList = new ArrayList<OrderItem>();
                    for(int a=0; a<orderJSONArray.length(); a++)
                    {
                        double money = 0;
                        int quantity = 0;
                        JSONArray goodsJSONArray = orderJSONArray.getJSONArray(a);
                        OrderItem orderItem = new OrderItem();
                        ArrayList<OrderGoodsItem> orderGoodsItems = new ArrayList<OrderGoodsItem>();
                        for(int b=0; b<goodsJSONArray.length(); b++)
                        {
                            JSONObject goodsJSONObject = goodsJSONArray.getJSONObject(b);
                            String data = goodsJSONObject.toString();
                            OrderGoodsItem orderGoodsItem = JSON.parseObject(data, OrderGoodsItem.class);
                            if(orderGoodsItem != null)
                            {
                                orderGoodsItems.add(orderGoodsItem);

                                orderItem.setFlag(orderGoodsItem.getFlag());
                                orderItem.setOrderNo(orderGoodsItem.getOrderNo());

                                quantity += orderGoodsItem.getQuantity();
                                money += (orderGoodsItem.getMoney() * orderGoodsItem.getQuantity());

                            }

                        }
                        orderItem.setOrderGoodsItems(orderGoodsItems);
                        orderItem.setMoney(money);
                        orderItem.setQuantity(quantity);

                        orderList.add(orderItem);
                    }

                    orderAll.setOrderList(orderList);

                    orderAll.setSuccess(true);


                }
                else
                {
                    orderAll.setSuccess(false);
                    orderAll.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderAll;

    }

    //    --  查询上门订单列表  --
    public OrderAll getSmOrderAllAction(OrderAll orderAll)
    {
        orderAll.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderAll);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetSmOrderAllAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray orderJSONArray = jsonObject.getJSONArray("data");

                    ArrayList<OrderItem> orderList = new ArrayList<OrderItem>();
                    for(int a=0; a<orderJSONArray.length(); a++)
                    {
                        double money = 0;
                        int quantity = 0;
                        JSONArray goodsJSONArray = orderJSONArray.getJSONArray(a);
                        OrderItem orderItem = new OrderItem();
                        ArrayList<OrderGoodsItem> orderGoodsItems = new ArrayList<OrderGoodsItem>();
                        for(int b=0; b<goodsJSONArray.length(); b++)
                        {
                            JSONObject goodsJSONObject = goodsJSONArray.getJSONObject(b);
                            String data = goodsJSONObject.toString();
                            OrderGoodsItem orderGoodsItem = JSON.parseObject(data, OrderGoodsItem.class);
                            if(orderGoodsItem != null)
                            {
                                orderGoodsItems.add(orderGoodsItem);

                                orderItem.setFlag(orderGoodsItem.getFlag());
                                orderItem.setOrderNo(orderGoodsItem.getOrderNo());

                                quantity += orderGoodsItem.getQuantity();
                                money += (orderGoodsItem.getMoney() * orderGoodsItem.getQuantity());

                            }

                        }
                        orderItem.setOrderGoodsItems(orderGoodsItems);
                        orderItem.setMoney(money);
                        orderItem.setQuantity(quantity);

                        orderList.add(orderItem);
                    }

                    orderAll.setOrderList(orderList);

                    orderAll.setSuccess(true);

                }
                else
                {
                    orderAll.setSuccess(false);
                    orderAll.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderAll;

    }

    // 上门订单保存
    public SmOrderSaveList smOrderSaveAction(SmOrderSaveList smOrderSaveList)
    {
        smOrderSaveList.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(smOrderSaveList);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SmOrderSaveAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                Log.v("SmOrderSaveAction", resultString);
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    smOrderSaveList = JSON.parseObject(data, SmOrderSaveList.class);
                    smOrderSaveList.setSuccess(true);

                }
                else
                {
                    smOrderSaveList.setSuccess(false);
                    smOrderSaveList.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return smOrderSaveList;

    }
//    SmOrderSaveAction.action  上门订单

    // --  删减到店订单列表  --
    public OrderUpdate yyOrderUpdateAction(OrderUpdate orderUpdate)
    {
        orderUpdate.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderUpdate);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("YyOrderUpdateAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    orderUpdate = JSON.parseObject(data, OrderUpdate.class);
                    orderUpdate.setSuccess(true);

                }
                else
                {
                    orderUpdate.setSuccess(false);
                    orderUpdate.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderUpdate;

    }

    // --  删减上门订单列表  --
    public OrderUpdate smOrderUpdateAction(OrderUpdate orderUpdate)
    {
        orderUpdate.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderUpdate);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SmOrderUpdateAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    orderUpdate = JSON.parseObject(data, OrderUpdate.class);
                    orderUpdate.setSuccess(true);

                }
                else
                {
                    orderUpdate.setSuccess(false);
                    orderUpdate.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderUpdate;

    }

    // --  上门支付成功  --
    public OrderSuccess smOrderSuccessAction(OrderSuccess orderSuccess)
    {
        orderSuccess.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderSuccess);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("SmOrderSuccessAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    orderSuccess = JSON.parseObject(data, OrderSuccess.class);
                    orderSuccess.setSuccess(true);

                }
                else
                {
                    orderSuccess.setSuccess(false);
                    orderSuccess.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderSuccess;

    }

    //  --  预约支付成功  --
    public OrderSuccess yyOrderSuccessAction(OrderSuccess orderSuccess)
    {
        orderSuccess.addVersion(context);
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderSuccess);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("YyOrderSuccessAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    orderSuccess = JSON.parseObject(data, OrderSuccess.class);
                    orderSuccess.setSuccess(true);

                }
                else
                {
                    orderSuccess.setSuccess(false);
                    orderSuccess.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderSuccess;

    }

    // --  确认收货  --
    public ConfirmOrder confirmOrderAction(ConfirmOrder confirmOrder)
    {
        confirmOrder.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(confirmOrder);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("ConfirmOrderAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    confirmOrder = JSON.parseObject(data, ConfirmOrder.class);
                    confirmOrder.setSuccess(true);

                }
                else
                {
                    confirmOrder.setSuccess(false);
                    confirmOrder.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return confirmOrder;
    }

    // 查询上门订单状态
    public GetSmorderStatusList getSmorderStatusListAction(GetSmorderStatusList getSmorderStatusList)
    {
        getSmorderStatusList.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(getSmorderStatusList);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetSmorderStatusListAction.action"), CommonUtils.generateParams(jsonParam));
            if(resultString != null)
            {
                Log.v("SmorderStatus", resultString);
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONArray orderStatusJSONArray = jsonObject.getJSONArray("data");

                    if(orderStatusJSONArray.length() > 0)
                    {
                        ArrayList<OrderStatusListItem> orderStatusListItems = new ArrayList<OrderStatusListItem>();
                        for(int a=0; a<orderStatusJSONArray.length();a++)
                        {
                            JSONObject orderStatusJSONObject = orderStatusJSONArray.getJSONObject(a);
                            String data = orderStatusJSONObject.toString();
                            OrderStatusListItem orderStatusListItem = JSON.parseObject(data, OrderStatusListItem.class);

                            orderStatusListItems.add(orderStatusListItem);
                        }

                        getSmorderStatusList.setOrderStatusListItems(orderStatusListItems);
                    }
                    else
                    {
                        getSmorderStatusList.setMsg("尚未有订单信息");
                    }

                    getSmorderStatusList.setSuccess(true);

                }
                else
                {
                    getSmorderStatusList.setSuccess(false);
                    getSmorderStatusList.setMsg(jsonObject.getString("msg"));
                }

            }
            else
            {
                getSmorderStatusList.setMsg("获取订单信息失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return getSmorderStatusList;
    }

    //首单满减
    public ManJianAll manJainAllAction(ManJianAll manJianAll)
    {
        manJianAll.addVersion(context);   //添加App版本信息
        String resultString = null;
        String jsonParam = JSON.toJSONString(manJianAll);

        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("ManJainAllAction.action"), CommonUtils.generateParams(jsonParam));

            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String data = dataObject.toString();
                    manJianAll = JSON.parseObject(data, ManJianAll.class);
                    manJianAll.setSuccess(true);

                }
                else
                {
                    manJianAll.setSuccess(false);
                    manJianAll.setMsg(jsonObject.getString("msg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return manJianAll;

    }


}
