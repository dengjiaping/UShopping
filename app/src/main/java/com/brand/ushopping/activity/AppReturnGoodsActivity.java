package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.model.AppReturngoodsSave;
import com.brand.ushopping.model.ApporderId;
import com.brand.ushopping.model.AppsmorderId;
import com.brand.ushopping.model.AppyyorderId;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.BitmapTools;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

public class AppReturnGoodsActivity extends Activity {
    private User user;
    private AppContext appContext;
    private ImageView backBtn;
    private TextView titleTextView;
    private ImageView goodsImgView;
    private TextView goodsNameTextView;
    private TextView attributeTextView;
    private TextView priceTextView;
    private Button sumbitBtn;
    private ViewGroup submitLayout;
    private ViewGroup applyLayout;
    private TextView statusTextView;
    private TextView reSummaryTextView;
    private TextView reTimeTitleTextView;
    private TextView reTimeTextView;
    private TextView orderNoTextView;
    private Button fromAlbumBtn;
    private Button fromCameraBtn;
    private LinearLayout imgsContainer;
    private RadioButton returnGoodsReasonQuality;
    private RadioButton returnGoodsReasonSize;
    private RadioButton returnGoodsReasonDislike;
    private EditText explainEditText;

    private long orderId;
    private String orderNo;
    private String customerContent;
    private long startTime;
    private long endTime;
    private int count;
    private double money;
    private int customerFlag;
    private ArrayList<Bitmap> imgList = new ArrayList<Bitmap>();
    private ArrayList<ImageView> imgViewList = new ArrayList<ImageView>();
    private int problem = 0;
    private int boughtType;
    private long reCenterTime;
    private long reTime;
    private String reProblem;
    private String reExplain = "暂无备注";
    private String explain;
    private TextView reExplainTextView;
    private TextView reProblemTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_return_goods);
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
        sumbitBtn = (Button) findViewById(R.id.submit);
        statusTextView = (TextView) findViewById(R.id.status);
        submitLayout = (ViewGroup) findViewById(R.id.submit_layout);
//        submitLayout.setVisibility(View.GONE);
        applyLayout = (ViewGroup) findViewById(R.id.apply_layout);
//        applyLayout.setVisibility(View.GONE);
        reSummaryTextView = (TextView) findViewById(R.id.re_summary);
        reTimeTextView = (TextView) findViewById(R.id.re_time);
        reTimeTitleTextView = (TextView) findViewById(R.id.re_time_title);
        orderNoTextView = (TextView) findViewById(R.id.order_no);
        fromAlbumBtn = (Button) findViewById(R.id.from_album);
        fromCameraBtn = (Button) findViewById(R.id.from_camera);
        imgsContainer = (LinearLayout) findViewById(R.id.imgs_container);
        explainEditText = (EditText) findViewById(R.id.explain);
        reExplainTextView = (TextView) findViewById(R.id.re_explain);
        reProblemTextView = (TextView) findViewById(R.id.re_problem);

        returnGoodsReasonQuality = (RadioButton) findViewById(R.id.return_goods_reason_quality);
        returnGoodsReasonQuality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    problem = StaticValues.RETURN_GOODS_REASON_QUALITY;
                }
            }
        });
        returnGoodsReasonSize = (RadioButton) findViewById(R.id.return_goods_reason_size);
        returnGoodsReasonSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    problem = StaticValues.RETURN_GOODS_REASON_SIZE;
                }
            }
        });
        returnGoodsReasonDislike = (RadioButton) findViewById(R.id.return_goods_reason_dislike);
        returnGoodsReasonDislike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    problem = StaticValues.RETURN_GOODS_REASON_DISLIKE;
                }
            }
        });

        Bundle bundle = null;
        try
        {
            bundle = getIntent().getExtras();
        }catch (Exception e)
        {
            e.printStackTrace();
            bundle = appContext.getBundleObj();
        }

        if(bundle != null)
        {
            ImageLoader.getInstance().displayImage(bundle.getString("img", ""), goodsImgView);
            goodsNameTextView.setText(bundle.getString("goodsName", ""));
            attributeTextView.setText(bundle.getString("attribute", ""));
            priceTextView.setText(Double.toString(bundle.getDouble("price", 0)));

            orderId = bundle.getLong("orderId");
            orderNo = bundle.getString("orderNo");
            startTime =bundle.getLong("startTime");
            endTime = bundle.getLong("endTime");
            money = bundle.getDouble("money");
            customerFlag = bundle.getInt("customerFlag", 0);
            boughtType = bundle.getInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
            reCenterTime = bundle.getLong("centerTime", 0);
            reTime = bundle.getLong("reTime", 0);
            reProblem = bundle.getString("problem", "");
            reExplain = bundle.getString("explain", "");
        }
        else
        {
            finish();
        }

        submitLayout.setVisibility(View.GONE);
        applyLayout.setVisibility(View.GONE);

        if(customerFlag == 0)
        {
            //申请售后
            submitLayout.setVisibility(View.VISIBLE);

        }
        else
        {
            applyLayout.setVisibility(View.VISIBLE);

            reExplainTextView.setText(reExplain);
            reProblemTextView.setText(reProblem);
            orderNoTextView.setText(orderNo);
            reSummaryTextView.setText(Double.toString(money));

            switch (customerFlag)
            {
                case StaticValues.CUSTOMER_FLAG_APPLY:
                    //待处理
                    statusTextView.setText("申请中");
                    reTimeTextView.setText(CommonUtils.timestampToDatetime(reTime));
                    reTimeTitleTextView.setText("申请时间");
                    break;

                case StaticValues.CUSTOMER_FLAG_SUBMITED:
                    //已申请
                    statusTextView.setText("店家同意");
                    reTimeTextView.setText(CommonUtils.timestampToDatetime(startTime));
                    reTimeTitleTextView.setText("处理时间");
                    break;

                case StaticValues.CUSTOMER_FLAG_DONE:
                    //已完成
                    statusTextView.setText("确认收货");
                    reTimeTextView.setText(CommonUtils.timestampToDatetime(reCenterTime));
                    reTimeTitleTextView.setText("收货时间");
                    break;

                case StaticValues.CUSTOMER_FLAG_RETURNED:
                    //已完成
                    statusTextView.setText("已退款");
                    reTimeTextView.setText(CommonUtils.timestampToDatetime(endTime));
                    reTimeTitleTextView.setText("收货时间");
                    break;

            }
        }

        sumbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(problem == 0)
                {
                    Toast.makeText(AppReturnGoodsActivity.this, "请选择退货原因", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imgList.isEmpty())
                {
                    Toast.makeText(AppReturnGoodsActivity.this, "至少添加一张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                explain = explainEditText.getText().toString();
                if(CommonUtils.isValueEmpty(explain))
                {
                    Toast.makeText(AppReturnGoodsActivity.this, "请填写退货原因", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppReturngoodsSave appReturngoodsSave = new AppReturngoodsSave();
                appReturngoodsSave.setUserId(user.getUserId());
                appReturngoodsSave.setSessionid(user.getSessionid());
                switch (boughtType)
                {
                    case StaticValues.BOUTHT_TYPE_NORMAL:
                        ApporderId apporderId = new ApporderId();
                        apporderId.setId(orderId);
                        appReturngoodsSave.setApporderId(apporderId);
                        break;
                    case StaticValues.BOUTHT_TYPE_TRYIT:
                        AppsmorderId appsmorderId = new AppsmorderId();
                        appsmorderId.setId(orderId);
                        appReturngoodsSave.setAppsmorderId(appsmorderId);
                        break;
                    case StaticValues.BOUTHT_TYPE_RESERVATION:
                        AppyyorderId appyyorderId = new AppyyorderId();
                        appyyorderId.setId(orderId);
                        appReturngoodsSave.setAppyyorderId(appyyorderId);
                        break;
                }
                ArrayList<String> images = new ArrayList<String>();
                ArrayList<String> lastFileName = new ArrayList<String>();
                for(Bitmap bitmap: imgList)
                {
                    images.add(BitmapTools.bitmapToBase64(bitmap));
                    lastFileName.add(StaticValues.LAST_FILE_NAME_JPG);
                }
                appReturngoodsSave.setLastFileName(lastFileName);
                appReturngoodsSave.setImages(images);
                appReturngoodsSave.setExplain(explain);

                new AppReturngoodsSaveActionTask().execute(appReturngoodsSave);
            }
        });

        fromAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相册
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data", true);

                startActivityForResult(intent, StaticValues.REQUEST_CODE_IMAGE_UPLOAD);
            }
        });

        fromCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, StaticValues.REQUEST_CODE_IMAGE_CAMERA);
            }
        });

    }


    public class AppReturngoodsSaveActionTask extends AsyncTask<AppReturngoodsSave, Void, AppReturngoodsSave>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppReturngoodsSave doInBackground(AppReturngoodsSave... appReturngoodsSaves) {
            return new OrderAction(AppReturnGoodsActivity.this).appReturngoodsSaveAction(appReturngoodsSaves[0]);
        }

        @Override
        protected void onPostExecute(AppReturngoodsSave result) {
            if(result != null)
            {
                if(result.isSuccess()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppReturnGoodsActivity.this);
                    builder.setMessage("退货申请提交成功,我们会在3到5个工作日退款至您的银行账户.");
                    builder.setCancelable(false);
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            AppReturnGoodsActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(AppReturnGoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(AppReturnGoodsActivity.this, "申请退货失败,请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticValues.REQUEST_CODE_IMAGE_UPLOAD && resultCode == RESULT_OK) {
            if(data != null)
            {
                Bitmap img = null;
                ContentResolver resolver = getContentResolver();
                Uri originalUri = data.getData();
                try {
                    img = BitmapTools.zoomImg(MediaStore.Images.Media.getBitmap(resolver, originalUri));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (img != null)
                {
                    //获取Bitmap
                    if(imgList.size() >= StaticValues.RETURN_GOODS_IMAGE_COUNT)
                    {
                        Toast.makeText(AppReturnGoodsActivity.this, "最多只能添加"+StaticValues.RETURN_GOODS_IMAGE_COUNT+"张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imgList.add(img);
                    reloadImgs();

                }
            }
            else
            {
                Toast.makeText(AppReturnGoodsActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == StaticValues.REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap img = (Bitmap)bundle.get("data");
            if (img != null)
            {
                //获取Bitmap
                if(imgList.size() >= StaticValues.RETURN_GOODS_IMAGE_COUNT)
                {
                    Toast.makeText(AppReturnGoodsActivity.this, "最多只能添加"+StaticValues.RETURN_GOODS_IMAGE_COUNT+"张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                imgList.add(img);
                reloadImgs();
            }
            else
            {
                Toast.makeText(AppReturnGoodsActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //重新读取图片
    private void reloadImgs()
    {
        imgsContainer.removeAllViewsInLayout();

        for(final Bitmap img: imgList)
        {
            final ImageView imgView = new ImageView(AppReturnGoodsActivity.this);
            imgView.setImageBitmap(img);
            ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(appContext.getScreenWidth() / 4, appContext.getScreenHeight() / 8);
            imgView.setLayoutParams(para);
            imgView.setPadding(8, 8, 8, 8);
            imgView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppReturnGoodsActivity.this);
                    builder.setMessage("确定移除这张图片");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            imgsContainer.removeView(imgView);
                            imgList.remove(img);
                            reloadImgs();

                        }
                    });
                    builder.create().show();

                    return true;
                }
            });

            imgsContainer.addView(imgView);
        }

    }

}
