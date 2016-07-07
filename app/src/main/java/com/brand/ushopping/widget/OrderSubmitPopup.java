package com.brand.ushopping.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.activity.OrderConfirmActivity;
import com.brand.ushopping.adapter.TextValueAdapter;
import com.brand.ushopping.model.ClientCharge;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brand.ushopping.R.id.pay_method_btn;

/**
 * Created by Administrator on 2015/11/26.
 */
public class OrderSubmitPopup extends PopupWindow
{
    private OrderConfirmActivity activity;
    private Context context;
    private RelativeLayout payConfirmLayout;
    private ListView payMethodListView;
    private ImageView backBtn;
    private ImageView closeBtn;
    private TextView titleTextView;
    private View payMethodBtn;
    private Button confirmBtn;
    private TextView summaryTextView;

    private String payMethod = null;
    private String payMethodText = null;

    private TextView payMethodTextView;
    private ArrayList<String> paymethodList;
    private List listData;
    private TextValueAdapter adapter;
    public OrderSubmitPopup(Context context, final ClientCharge clientCharge)
    {
        this.context = context;
        this.activity =(OrderConfirmActivity) context;

        this.setFocusable(true);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.order_submit_popup, null);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        payMethodListView = (ListView) view.findViewById(R.id.pay_method_list);
        payConfirmLayout = (RelativeLayout) view.findViewById(R.id.pay_confirm_layout);
        payMethodBtn = view.findViewById(pay_method_btn);
        payMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoosePayMethodMode();

            }
        });

        titleTextView = (TextView) view.findViewById(R.id.title);
        backBtn = (ImageView) view.findViewById(R.id.back);
        backBtn.setVisibility(View.GONE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayConfirmMode();

            }
        });

        closeBtn = (ImageView) view.findViewById(R.id.close);
        closeBtn.setVisibility(View.GONE);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderSubmitPopup.this.dismiss();

            }
        });

        summaryTextView = (TextView) view.findViewById(R.id.summary);
        summaryTextView.setText(CommonUtils.df.format(clientCharge.getSummary()));

        confirmBtn = (Button) view.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认付款
                activity.chargedCheck = true;
//                activity.upPay();
                confirmBtn.setEnabled(false);
                if (CommonUtils.isValueEmpty(payMethod))
                {
                    Toast.makeText(activity, "请选择一种支付方式", Toast.LENGTH_SHORT).show();
                    confirmBtn.setEnabled(true);

                } else
                {
                    clientCharge.setChannelVal(payMethod);

                    //根据不同的支付方式
                    if (payMethod.equals(StaticValues.PAY_METHOD_CASH)) {
                        Toast.makeText(activity, "普通订单不支持现金支付,请选择其他支付方式", Toast.LENGTH_SHORT).show();
                        confirmBtn.setEnabled(true);

                    } else {
                        new ReturnClientChargeTask().execute(clientCharge);
//                        activity.startPay(result);
//                        activity.startPay(clientCharge);

                    }

                }

            }
        });
        confirmBtn.setEnabled(true);

        payMethodTextView = (TextView) view.findViewById(R.id.pay_method);

        //支付方式列表
        listData = new ArrayList<Map<String,Object>>();

        Map line = new HashMap();
        line.put("text", "银联");
        line.put("value", StaticValues.PAY_METHOD_UPACP);
        listData.add(line);

        line = new HashMap();
        line.put("text", "支付宝");
        line.put("value", StaticValues.PAY_METHOD_ALIPAY);
        listData.add(line);

        line = new HashMap();
        line.put("text", "微信支付");
        line.put("value", StaticValues.PAY_METHOD_WX);
        listData.add(line);

        /*
        if(activity.getBoughtType() != StaticValues.BOUTHT_TYPE_NORMAL)
        {
            line = new HashMap();
            line.put("text", "线下支付");
            line.put("value", StaticValues.PAY_METHOD_CASH);
            listData.add(line);

        }
        */

        adapter = new TextValueAdapter(listData, activity);
        payMethodListView.setAdapter(adapter);
        payMethodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                payMethod = adapter.getValue(position);
                payMethodText = adapter.getText(position);

                setPayConfirmMode();
            }
        });

        setPayConfirmMode();

        this.setContentView(view);
    }

    private void setPayConfirmMode()
    {
        payConfirmLayout.setVisibility(View.VISIBLE);
        payMethodListView.setVisibility(View.GONE);

        titleTextView.setText("付款详情");
        closeBtn.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.GONE);

        if(!CommonUtils.isValueEmpty(payMethodText))
        {
            payMethodTextView.setText(payMethodText);

        }

    }

    private void setChoosePayMethodMode()
    {
        payMethodListView.setVisibility(View.VISIBLE);
        payMethodListView.setEnabled(true);
        payConfirmLayout.setVisibility(View.GONE);

        titleTextView.setText("选择付款方式");
        backBtn.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.GONE);

    }

    //发起付款请求 获取Charge对象
    public class ReturnClientChargeTask extends AsyncTask<ClientCharge, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ClientCharge... clientCharges) {
            return new GoodsAction(context).returnClientChargeAction(clientCharges[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!CommonUtils.isValueEmpty(result))
            {
                //支付对象获取成功
                activity.startPay(result);
//                callClientCharge(result);

            }
            else
            {
                Toast.makeText(activity, "支付对象获取失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
