package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.adapter.GoodsAdapter;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewHistoryActivity extends Activity {
    private ImageView backBtn;
    private TextView titleTextView;
    private AppContext appContext;
    private ArrayList<AppgoodsId> goodsViewHistory;
    private GridView goodsView;
    private GoodsAdapter goodsAdapter;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private TextView clearAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_history);

        appContext = (AppContext) getApplicationContext();

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());
        goodsView = (GridView) findViewById(R.id.goods_view);
        goodsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewHistoryActivity.this, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", id);
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                startActivity(intent);
            }
        });

        clearAllBtn = (TextView) findViewById(R.id.clear_history);
        clearAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewHistoryActivity.this);
                builder.setMessage("确认清除所有的浏览记录.");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        new GoodsAction(ViewHistoryActivity.this).goodsViewHistoryClear(appContext.getUdbHelper());
                        ViewHistoryActivity.this.finish();

                    }
                });
                builder.setNegativeButton("取消", null);

                builder.create().show();
            }
        });

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();

    }

    private void reload()
    {
        goodsViewHistory = new GoodsAction(ViewHistoryActivity.this).getGoodsViewHistory(appContext.getUdbHelper());
//        goodsViewHistory = appContext.getGoodsViewHistory();

        if(goodsViewHistory != null && !goodsViewHistory.isEmpty())
        {
            ArrayList<Map<String,Object>> goodsListData = new ArrayList<Map<String,Object>>();
            for(AppgoodsId appgoodsId: goodsViewHistory)
            {
                Map line = new HashMap();

                line.put("id", appgoodsId.getId());
                line.put("img", appgoodsId.getLogopicUrl());
                line.put("name", appgoodsId.getGoodsName());
                line.put("price", appgoodsId.getPromotionPrice());

                goodsListData.add(line);

            }
            goodsAdapter = new GoodsAdapter(goodsListData, ViewHistoryActivity.this);
            goodsView.setAdapter(goodsAdapter);

            warningLayout.setVisibility(View.GONE);
        }
        else
        {
            warningLayout.setVisibility(View.VISIBLE);
            warningTextView.setText("暂时没有商品");

        }

    }
}
