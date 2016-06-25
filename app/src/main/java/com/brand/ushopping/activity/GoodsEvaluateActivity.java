package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.SaveAppEvaluate;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GoodsEvaluateActivity extends Activity {
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
    private String content;
    private long goodsId;
    private String attribute;
    private int evaluateType = StaticValues.GOODS_EVALUATE_TYPE_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goods_evaluate);

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
        sumbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                content = contentTextView.getText().toString();
                if(!content.isEmpty())
                {
                    SaveAppEvaluate saveAppEvaluate = new SaveAppEvaluate();

                    saveAppEvaluate.setUserId(user.getUserId());
                    saveAppEvaluate.setSessionid(user.getSessionid());

                    AppuserId appuserId = new AppuserId();
                    appuserId.setUserId(user.getUserId());
                    saveAppEvaluate.setAppuserId(appuserId);

                    AppgoodsId appgoodsId = new AppgoodsId();
                    appgoodsId.setId(goodsId);
                    saveAppEvaluate.setAppgoodsId(appgoodsId);

                    saveAppEvaluate.setAttribute(attribute);
                    saveAppEvaluate.setContent(content);
                    saveAppEvaluate.setFlag(evaluateType);

                    new SaveAppEvaluateTask().execute(saveAppEvaluate);

                }
                else
                {
                    Toast.makeText(GoodsEvaluateActivity.this, "评价内容不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();

        ImageLoader.getInstance().displayImage(bundle.getString("img", ""), goodsImgView);

        goodsId = bundle.getLong("goodsId", 0);
        attribute = bundle.getString("attribute", "");

        goodsNameTextView.setText(bundle.getString("goodsName", ""));
        attributeTextView.setText(attribute);
        priceTextView.setText(Double.toString(bundle.getDouble("price", 0)));

    }

    public class SaveAppEvaluateTask extends AsyncTask<SaveAppEvaluate, Void, SaveAppEvaluate>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SaveAppEvaluate doInBackground(SaveAppEvaluate... saveAppEvaluates) {
            return new GoodsAction(GoodsEvaluateActivity.this).saveAppEvaluateAction(saveAppEvaluates[0]);
        }

        @Override
        protected void onPostExecute(SaveAppEvaluate result) {
            if(result != null)
            {
                if(result.isSuccess()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoodsEvaluateActivity.this);
                    builder.setMessage("评价发布成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            GoodsEvaluateActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(GoodsEvaluateActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }
            else
            {
                Toast.makeText(GoodsEvaluateActivity.this, "评价发布失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
