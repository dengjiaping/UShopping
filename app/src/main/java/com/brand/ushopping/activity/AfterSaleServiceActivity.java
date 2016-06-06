package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.model.AppCustomer;
import com.brand.ushopping.model.ApporderId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AfterSaleServiceActivity extends Activity {
    private User user;
    private AppContext appContext;
    private ImageView backBtn;
    private TextView titleTextView;

    private ImageView goodsImgView;
    private TextView goodsNameTextView;
    private TextView attributeTextView;
    private TextView priceTextView;
    private TextView contentTextView;
    private Button sumbitBtn;
    private ViewGroup submitLayout;
    private ViewGroup applyLayout;
    private TextView statusTextView;

    private TextView reSummaryTextView;
    private TextView reTimeTextView;
    private TextView orderNoTextView;
    private TextView reContentTextView;

    private long orderId;
    private String orderNo;
    private String customerContent;
    private long startTime;
    private long endTime;
    private int count;
    private double money;
    private String reContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_after_sale_service);
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        goodsImgView = (ImageView) findViewById(R.id.img);
        goodsNameTextView = (TextView) findViewById(R.id.goods_name);
        attributeTextView = (TextView) findViewById(R.id.attribute);
        priceTextView = (TextView) findViewById(R.id.price);
        contentTextView = (TextView) findViewById(R.id.content);
        sumbitBtn = (Button) findViewById(R.id.submit);

        statusTextView = (TextView) findViewById(R.id.status);

        submitLayout = (ViewGroup) findViewById(R.id.submit_layout);
        submitLayout.setVisibility(View.GONE);
        applyLayout = (ViewGroup) findViewById(R.id.apply_layout);
        applyLayout.setVisibility(View.GONE);

        reSummaryTextView = (TextView) findViewById(R.id.re_summary);
        reTimeTextView = (TextView) findViewById(R.id.re_time);
        orderNoTextView = (TextView) findViewById(R.id.order_no);
        reContentTextView = (TextView) findViewById(R.id.re_content);

        Bundle bundle = getIntent().getExtras();

        ImageLoader.getInstance().displayImage(bundle.getString("img", ""), goodsImgView);
        goodsNameTextView.setText(bundle.getString("goodsName", ""));
        attributeTextView.setText(bundle.getString("attribute", ""));
        priceTextView.setText(Double.toString(bundle.getDouble("price", 0)));

        orderId = bundle.getLong("orderId");
        orderNo = bundle.getString("orderNo");
        startTime =bundle.getLong("startTime");
        endTime = bundle.getLong("endTime");
        reContent = bundle.getString("customerContent");
        money = bundle.getDouble("money");

        int customerFlag = bundle.getInt("customerFlag", 0);

        if(customerFlag == 0)
        {
            //申请售后
            submitLayout.setVisibility(View.VISIBLE);

        }
        else
        {
            applyLayout.setVisibility(View.VISIBLE);
            switch (customerFlag)
            {
                case StaticValues.CUSTOMER_FLAG_APPLY:
                    //待处理
                    statusTextView.setText("待处理");
                    reTimeTextView.setText(CommonUtils.timestampToDatetime(startTime));
                    contentTextView.setText(reContent);
                    orderNoTextView.setText(orderNo);
                    reSummaryTextView.setText(Double.toString(money));

                    break;
                case StaticValues.CUSTOMER_FLAG_SUBMITED:
                    //已申请
                    statusTextView.setText("已申请");
                    reTimeTextView.setText(CommonUtils.timestampToDatetime(startTime));
                    contentTextView.setText(reContent);
                    orderNoTextView.setText(orderNo);
                    reSummaryTextView.setText(Double.toString(money));

                    break;
                case StaticValues.CUSTOMER_FLAG_DONE:
                    //已完成
                    statusTextView.setText("已完成");
                    reTimeTextView.setText(CommonUtils.timestampToDatetime(endTime));
                    contentTextView.setText(reContent);
                    orderNoTextView.setText(orderNo);
                    reSummaryTextView.setText(Double.toString(money));

                    break;

            }

        }



        customerContent = bundle.getString("customerContent", "");

        startTime = bundle.getLong("startTime", 0);
        endTime = bundle.getLong("endTime", 0);
        count = bundle.getInt("count", 0);

        sumbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customerContent = contentTextView.getText().toString();
                if(!customerContent.isEmpty())
                {
                    AppCustomer appCustomer = new AppCustomer();
                    appCustomer.setUserId(user.getUserId());
                    appCustomer.setSessionid(user.getSessionid());

                    AppuserId appuserId = new AppuserId();
                    appuserId.setUserId(user.getUserId());
                    appCustomer.setAppuserId(appuserId);

                    ApporderId apporderId = new ApporderId();
                    apporderId.setId(orderId);
                    appCustomer.setApporderId(apporderId);

                    appCustomer.setContent(customerContent);

                    new SaveAppCustomerTask().execute(appCustomer);

                }
                else
                {
                    Toast.makeText(AfterSaleServiceActivity.this, "请填写备注", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public class SaveAppCustomerTask extends AsyncTask<AppCustomer, Void, AppCustomer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppCustomer doInBackground(AppCustomer... appCustomers) {
            return new GoodsAction().saveAppCustomerAction(appCustomers[0]);
        }

        @Override
        protected void onPostExecute(AppCustomer result) {
            if(result != null)
            {
                if(result.isSuccess()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AfterSaleServiceActivity.this);
                    builder.setMessage("退货申请提交成功,我们会在3到5个工作日退款至您的银行账户.");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            AfterSaleServiceActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
            }
            else
            {

            }
        }
    }

}
