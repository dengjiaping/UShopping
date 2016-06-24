package com.brand.ushopping.activity;
/**
 * 主题活动
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.MainpageAction;
import com.brand.ushopping.adapter.MoreGoodsAdapter;
import com.brand.ushopping.model.AppOnlineshopping;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.OnlineshoppingGoods;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.EndlessGridRecyclerOnScrollListener;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeActivity extends AppCompatActivity {
    private ImageView backBtn;
    private TextView titleTextView;
    private ImageView shareBtn;
    private ImageView bannerImageView1;
    private ImageView bannerImageView2;
    private TextView categoryPrice;
    private TextView categorySale;
    private ImageView categoryPriceIdc;
    private ImageView categorySaleIdc;
    private RecyclerView goodsGridView;
    private int pageCount = 0;
    private User user;
    private AppContext appContext;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List listData;
    private MoreGoodsAdapter goodsAdapter;
    private RelativeLayout actionBarLayout;

    private int brandGoodsType = StaticValues.BRAND_GOODS_TYPE_PRICE;
    private int currentArrenge = StaticValues.ARRENGE_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

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
        shareBtn = (ImageView) findViewById(R.id.share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bannerImageView1 = (ImageView) findViewById(R.id.banner_1);
        bannerImageView2 = (ImageView) findViewById(R.id.banner_2);

        categorySale = (TextView) findViewById(R.id.category_sale);
        categorySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab();
                reload();

                if(currentArrenge == StaticValues.ARRENGE_SALE_ASC)
                {
                    currentArrenge = StaticValues.ARRENGE_SALE_DESC;

                }
                else
                {
                    currentArrenge = StaticValues.ARRENGE_SALE_ASC;

                }

            }
        });
        categoryPrice = (TextView) findViewById(R.id.category_price);
        categoryPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab();
                reload();

                if(currentArrenge == StaticValues.ARRENGE_PRICE_ASC)
                {
                    currentArrenge = StaticValues.ARRENGE_PRICE_DESC;

                }
                else
                {
                    currentArrenge = StaticValues.ARRENGE_PRICE_ASC;

                }

            }
        });

        categoryPriceIdc = (ImageView) findViewById(R.id.category_price_idc);
        categorySaleIdc = (ImageView) findViewById(R.id.category_sale_idc);

        pageCount = 0;

        goodsGridView = (RecyclerView) findViewById(R.id.goods_list);
        gridLayoutManager = new GridLayoutManager(ThemeActivity.this, 2);
        goodsGridView.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                pageCount = 0;
                reload();

            }
        });

        goodsGridView.addOnScrollListener(new EndlessGridRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.v("recycler test", "load more");
                reload();

            }
        });

        actionBarLayout = (RelativeLayout) findViewById(R.id.action_bar);

        currentArrenge = StaticValues.ARRENGE_SALE_DESC;

    }

    @Override
    protected void onStart() {
        super.onStart();
        selectTab();
        goodsGridView.removeAllViewsInLayout();
        reload();
    }

    private void selectTab()
    {
        categoryPriceIdc.setVisibility(View.INVISIBLE);
        categorySaleIdc.setVisibility(View.INVISIBLE);

        switch (brandGoodsType)
        {
            case StaticValues.BRAND_GOODS_TYPE_PRICE:
                categoryPriceIdc.setVisibility(View.VISIBLE);

                break;
            case StaticValues.BRAND_GOODS_TYPE_SALE:
                categorySaleIdc.setVisibility(View.VISIBLE);
                break;

        }

    }

    private void reload()
    {
        OnlineshoppingGoods onlineshoppingGoods = new OnlineshoppingGoods();
        onlineshoppingGoods.setMin(pageCount);
        onlineshoppingGoods.setMax(StaticValues.GOODS_PAGE_COUNT);
        if(user != null)
        {
            onlineshoppingGoods.setUserId(user.getUserId());
            onlineshoppingGoods.setSessionid(user.getSessionid());
        }

        new OnlineshoppingGoodsTask().execute(onlineshoppingGoods);

    }

    //首页加载
    public class OnlineshoppingGoodsTask extends AsyncTask<OnlineshoppingGoods, Void, OnlineshoppingGoods>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OnlineshoppingGoods doInBackground(OnlineshoppingGoods... onlineshoppingGoodses) {
            return new MainpageAction(ThemeActivity.this).onlineshoppingGoodsAction(ThemeActivity.this, onlineshoppingGoodses[0]);
        }

        @Override
        protected void onPostExecute(OnlineshoppingGoods result) {
            swipeRefreshLayout.setRefreshing(false);

            if(result != null)
            {
                AppOnlineshopping appOnlineshopping = result.getAppOnlineshopping();
                if(appOnlineshopping != null)
                {
                    titleTextView.setText(appOnlineshopping.getBrannerFont());
                    String[] images = appOnlineshopping.getImages().split(";");

                    ImageLoader.getInstance().displayImage(images[0], bannerImageView1);
                    ImageLoader.getInstance().displayImage(images[1], bannerImageView2);

                    int color = Color.parseColor(appOnlineshopping.getBrannerColor());
                    actionBarLayout.setBackgroundColor(color);

                }

                ArrayList<AppgoodsId> appgoodsIds = result.getAppgoodsIds();

                if(!appgoodsIds.isEmpty())
                {
                    //活动商品列表
                    listData = new ArrayList<Map<String, Object>>();
                    for (int d = 0; d < appgoodsIds.size(); d++) {
                        final AppgoodsId appgoodsId = appgoodsIds.get(d);

                        Map line = new HashMap();

                        line.put("id", appgoodsId.getId());
                        line.put("img", appgoodsId.getLogopicUrl());
                        line.put("name", appgoodsId.getGoodsName());
                        line.put("price", appgoodsId.getPromotionPrice());
                        line.put("salesCount", appgoodsId.getSaleCount());
                        line.put("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);

                        listData.add(line);

                    }

                    pageCount += appgoodsIds.size();

                    arrange();

                    goodsAdapter = new MoreGoodsAdapter(ThemeActivity.this, listData);
                    goodsGridView.setAdapter(goodsAdapter);
                }
                else
                {
                    if(pageCount == 0)
                    {
                        Toast.makeText(ThemeActivity.this, "该品牌下暂时没有商品", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ThemeActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }
    }

    //根据价格排序
    class BrandGoodsPriceAscComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Double price1 = (Double)lhs.get("price");
            Double price2 = (Double)rhs.get("price");

            return price1.compareTo(price2);
        }
    }

    class BrandGoodsPriceDescComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Double price1 = (Double)lhs.get("price");
            Double price2 = (Double)rhs.get("price");

            return price2.compareTo(price1);
        }
    }

    //根据价格排序
    class GoodsSalesAscComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Integer salesCount1 = (Integer) lhs.get("salesCount");
            Integer salesCount2 = (Integer)rhs.get("salesCount");

            return salesCount1.compareTo(salesCount2);
        }
    }

    class GoodsSalesDescComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Integer salesCount1 = (Integer) lhs.get("salesCount");
            Integer salesCount2 = (Integer)rhs.get("salesCount");

            return salesCount2.compareTo(salesCount1);
        }
    }

    private void arrange()
    {
        switch (currentArrenge)
        {
            case StaticValues.ARRENGE_SALE_ASC:
                categorySale.setText("销量 ↑");
                categoryPrice.setText("价格");
                Collections.sort(listData, new GoodsSalesAscComparator());

                break;
            case StaticValues.ARRENGE_SALE_DESC:
                categorySale.setText("销量 ↓");
                categoryPrice.setText("价格");
                Collections.sort(listData, new GoodsSalesDescComparator());

                break;
            case StaticValues.ARRENGE_PRICE_ASC:
                categorySale.setText("新品");
                categoryPrice.setText("价格 ↑");
                Collections.sort(listData, new BrandGoodsPriceAscComparator());

                break;
            case StaticValues.ARRENGE_PRICE_DESC:
                categorySale.setText("新品");
                categoryPrice.setText("价格 ↓");
                Collections.sort(listData, new BrandGoodsPriceDescComparator());

                break;
        }

    }


}
