package com.brand.ushopping.action;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.ConfirmOrder;
import com.brand.ushopping.model.OrderAll;
import com.brand.ushopping.model.OrderGoodsItem;
import com.brand.ushopping.model.OrderItem;
import com.brand.ushopping.model.OrderSaveList;
import com.brand.ushopping.model.OrderSuccess;
import com.brand.ushopping.model.OrderUpdate;
import com.brand.ushopping.model.SmOrderSaveList;
import com.brand.ushopping.model.YyOrderSaveList;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/27.
 */
public class OrderAction
{
    //提交订单
    public OrderSaveList orderSaveAction(OrderSaveList orderSaveList)
    {
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderSaveList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("OrderSaveAction.action", params);
            resultString = URLConnectionUtil.post("OrderSaveAction.action", jsonParam);
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
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderAll);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        String result = null;
        try
        {
//            resultString = HttpClientUtil.post("GetOrderAllAction.action", params);
            resultString = URLConnectionUtil.post("GetOrderAllAction.action", jsonParam);
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
        String resultString = null;
        String jsonParam = JSON.toJSONString(yyOrderSaveList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("YyOrderSaveAction.action", params);
            resultString = URLConnectionUtil.post("YyOrderSaveAction.action", jsonParam);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderAll);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetYyOrderAllAction.action", params);
            resultString = URLConnectionUtil.post("GetYyOrderAllAction.action", jsonParam);
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
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderAll);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("GetSmOrderAllAction.action", params);
            resultString = URLConnectionUtil.post("GetSmOrderAllAction.action", jsonParam);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(smOrderSaveList);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("SmOrderSaveAction.action", params);
            resultString = URLConnectionUtil.post("SmOrderSaveAction.action", jsonParam);

            if(resultString != null)
            {
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
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderUpdate);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("YyOrderUpdateAction.action", params);
            resultString = URLConnectionUtil.post("YyOrderUpdateAction.action", jsonParam);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderUpdate);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("SmOrderUpdateAction.action", params);
            resultString = URLConnectionUtil.post("SmOrderUpdateAction.action", jsonParam);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderSuccess);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("SmOrderSuccessAction.action", params);
            resultString = URLConnectionUtil.post("SmOrderSuccessAction.action", jsonParam);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(orderSuccess);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));

        try
        {
//            resultString = HttpClientUtil.post("YyOrderSuccessAction.action", params);
            resultString = URLConnectionUtil.post("YyOrderSuccessAction.action", jsonParam);

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
        String resultString = null;
        String jsonParam = JSON.toJSONString(confirmOrder);
        List params = new ArrayList();
        params.add(new BasicNameValuePair("param", jsonParam));
        Log.v("ConfirmOrderAction", jsonParam);
        try
        {
//            resultString = HttpClientUtil.post("ConfirmOrderAction.action", params);
            resultString = URLConnectionUtil.post("ConfirmOrderAction.action", jsonParam);

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

}
