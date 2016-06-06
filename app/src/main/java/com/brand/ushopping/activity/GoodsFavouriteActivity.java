package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.adapter.GoodsFavouriteItemAdapter;
import com.brand.ushopping.model.AppGoodsCollect;
import com.brand.ushopping.model.AppGoodsCollectItem;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoodsFavouriteActivity extends Activity {
    private ImageView backBtn;
    private TextView titleTextView;
    private ListView goodsListView;
    private AppContext appContext;
    private User user;

    private TimeoutbleProgressDialog goodsFavouriteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goods_favourite);

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

        goodsListView = (ListView) findViewById(R.id.goods_list);
        goodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GoodsFavouriteActivity.this, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", id);
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        goodsFavouriteDialog = TimeoutbleProgressDialog.createProgressDialog(GoodsFavouriteActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                goodsFavouriteDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(GoodsFavouriteActivity.this);
                builder.setMessage("登录失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user != null)
        {
            AppGoodsCollect appGoodsCollect = new AppGoodsCollect();
            appGoodsCollect.setUserId(user.getUserId());
            appGoodsCollect.setSessionid(user.getSessionid());

            new GetListAppGoodsCollectUserIdTask().execute(appGoodsCollect);
        }
        else
        {
            Toast.makeText(GoodsFavouriteActivity.this, "登录注册之后启用收藏功能", Toast.LENGTH_SHORT).show();

        }

    }

    //店铺收藏列表
    public class GetListAppGoodsCollectUserIdTask extends AsyncTask<AppGoodsCollect, Void, AppGoodsCollect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            goodsFavouriteDialog.show();

        }

        @Override
        protected AppGoodsCollect doInBackground(AppGoodsCollect... appGoodsCollects) {
            return new GoodsAction().getListAppGoodsCollectUserIdAction(appGoodsCollects[0]);

        }

        @Override
        protected void onPostExecute(AppGoodsCollect result) {
            goodsFavouriteDialog.dismiss();

            if(result != null)
            {
                if(result.isSuccess())
                {
                    ArrayList<Map<String,Object>> goodsListData = new ArrayList<Map<String,Object>>();

                    for(AppGoodsCollectItem appGoodsCollectItem: result.getAppGoodsCollectItems())
                    {
                        AppgoodsId appgoodsId = appGoodsCollectItem.getAppgoodsId();
                        Map line = new HashMap();

                        line.put("id", appgoodsId.getId());
                        line.put("img", appgoodsId.getLogopicUrl());
                        line.put("name", appgoodsId.getGoodsName());
                        line.put("price", appgoodsId.getPromotionPrice());

                        goodsListData.add(line);
                    }
                    GoodsFavouriteItemAdapter adapter = new GoodsFavouriteItemAdapter(goodsListData, GoodsFavouriteActivity.this);
                    goodsListView.setAdapter(adapter);

                }
                else
                {
                    Toast.makeText(GoodsFavouriteActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(GoodsFavouriteActivity.this, "获取收藏列表失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
