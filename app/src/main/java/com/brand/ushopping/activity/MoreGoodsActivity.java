package com.brand.ushopping.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.MainpageAction;
import com.brand.ushopping.adapter.MoreGoodsAdapter;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.EndlessGridRecyclerOnScrollListener;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoreGoodsActivity extends AppCompatActivity {
    private AppContext appContext;
    private User user;
    private ImageView backBtn;
    private TextView titleTextView;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ArrayList<Map<String,Object>> goodsListData;
    private RecyclerView goodsGridView;
    private int currentGoodsCount = 0;
    private MoreGoodsAdapter moreGoodsAdapter;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int boughtType = StaticValues.BOUTHT_TYPE_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_more_goods);

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

        goodsGridView = (RecyclerView) findViewById(R.id.goods_grid);
        gridLayoutManager = new GridLayoutManager(MoreGoodsActivity.this, 2);
        goodsGridView.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                reload();

            }
        });

        boughtType = getIntent().getExtras().getInt("boughtType");

    }

    @Override
    protected void onStart() {
        super.onStart();

        goodsGridView.addOnScrollListener(new EndlessGridRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.v("recycler test", "load more");
                load();

            }
        });

        reload();

    }

    public void reload()
    {
        currentGoodsCount = 0;
        moreGoodsAdapter = null;
        goodsListData = null;
        goodsGridView.removeAllViewsInLayout();

        load();

    }

    public void load()
    {
        HomeRe homeRe = new HomeRe();
        if (user != null) {
            homeRe.setUserId(user.getUserId());
            homeRe.setSessionid(user.getSessionid());
        }
        homeRe.setMin(currentGoodsCount);
        homeRe.setMax(StaticValues.RECOMMEND_PAGE_COUNT);
        Log.v("ushopping", "home re min " + homeRe.getMin() + " max " + homeRe.getMax());
        new MainReLoadTask().execute(homeRe);

    }

    //首页下拉加载
    public class MainReLoadTask extends AsyncTask<HomeRe, Void, HomeRe>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            homereLoadDialog.show();

        }

        @Override
        protected HomeRe doInBackground(HomeRe... homeRe) {
            return new MainpageAction().homeRe(MoreGoodsActivity.this, homeRe[0]);
        }

        @Override
        protected void onPostExecute(HomeRe result) {
//            homereLoadDialog.dismiss();

            if(result != null)
            {
                if(result.isSuccess())
                {
                    if(result.getAppgoodsId() != null)
                    {
                        ArrayList<AppgoodsId> appgoodsIds = result.getAppgoodsId();
                        if(appgoodsIds.size() > 0)
                        {
                            currentGoodsCount += appgoodsIds.size();

                            if(goodsListData == null)
                            {
                                goodsListData = new ArrayList<Map<String,Object>>();

                            }

                            for(AppgoodsId appgoodsId: appgoodsIds)
                            {
                                Map line = new HashMap();

                                line.put("id", appgoodsId.getId());
                                line.put("img", appgoodsId.getLogopicUrl());
                                line.put("name", appgoodsId.getGoodsName());
                                line.put("price", appgoodsId.getPromotionPrice());
                                line.put("boughtType", boughtType);
                                line.put("favouriteCount", 123);

                                goodsListData.add(line);
                            }

                            if(moreGoodsAdapter == null)
                            {
                                moreGoodsAdapter = new MoreGoodsAdapter(MoreGoodsActivity.this, goodsListData);
                                goodsGridView.setAdapter(moreGoodsAdapter);

                            }
                            else
                            {
                                moreGoodsAdapter.notifyDataSetChanged();

                            }

                            currentGoodsCount += goodsListData.size();

//                            warningLayout.setVisibility(View.GONE);

//                            goodsAdapter.notifyDataSetChanged();

                        }

                    }
                    else
                    {
                        Toast.makeText(MoreGoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(MoreGoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

}
