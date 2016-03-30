package com.brand.ushopping.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.brand.ushopping.action.StoreAction;
import com.brand.ushopping.adapter.MoreGoodsAdapter;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.BrandGoodsList;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.EndlessGridRecyclerOnScrollListener;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class BrandMoreGoodsActivity extends Activity {
    private AppContext appContext;
    private User user;
    private ImageView backBtn;
    private TextView titleTextView;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ArrayList<AppgoodsId> appgoodsIds;
    private ArrayList<Map<String,Object>> listData = null;
    private RecyclerView goodsGridView;
    private int currentGoodsCount = 0;
    private MoreGoodsAdapter moreGoodsAdapter;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private long brandId;
    private String brandName;

    private TextView categoryNew;
    private TextView categoryPrice;
    private TextView categorySale;
    private TextView categoryUndef;
    private ImageView categoryNewIdc;
    private ImageView categoryPriceIdc;
    private ImageView categorySaleIdc;
    private ImageView categoryUndefIdc;

    private int brandGoodsType;
//    private int brandGoodsTypePrev;
    private int currentArrenge = StaticValues.ARRENGE_TIME_DESC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_brand_more_goods);

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

        goodsGridView = (RecyclerView) findViewById(R.id.goods_grid);
        gridLayoutManager = new GridLayoutManager(BrandMoreGoodsActivity.this, 2);
        goodsGridView.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                reload();

            }
        });

        Bundle bundle = getIntent().getExtras();
        brandId = bundle.getLong("brandId");
        brandName = bundle.getString("brandName");

        titleTextView.setText(brandName);

        categoryNew = (TextView) findViewById(R.id.category_new);
        categoryNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;

                selectTab();
                reload();

                if(currentArrenge == StaticValues.ARRENGE_TIME_ASC)
                {
                    currentArrenge = StaticValues.ARRENGE_TIME_DESC;

                }
                else
                {
                    currentArrenge = StaticValues.ARRENGE_TIME_ASC;

                }

//                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryPrice = (TextView) findViewById(R.id.category_price);
        categoryPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_PRICE;
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

//                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categorySale = (TextView) findViewById(R.id.category_sale);
        categorySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_SALE;
                selectTab();
//                load();
//                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryUndef = (TextView) findViewById(R.id.category_undef);
        categoryUndef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_UNDEF;
                selectTab();
//                load();
//                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryNewIdc = (ImageView) findViewById(R.id.category_new_idc);
        categoryPriceIdc = (ImageView) findViewById(R.id.category_price_idc);
        categorySaleIdc = (ImageView) findViewById(R.id.category_sale_idc);
        categoryUndefIdc = (ImageView) findViewById(R.id.category_undef_idc);

        brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;
//        brandGoodsTypePrev = brandGoodsType;

        selectTab();
    }

    @Override
    protected void onStart() {
        super.onStart();

        reload();

    }

    public void reload()
    {
        currentGoodsCount = 0;
        moreGoodsAdapter = null;
        listData = null;
        goodsGridView.removeAllViewsInLayout();

        goodsGridView.addOnScrollListener(new EndlessGridRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.v("recycler test", "load more");
                load();

            }
        });

        load();

    }

    public void load()
    {
        BrandGoodsList brandGoodsList = new BrandGoodsList();
        if(user != null)
        {
            brandGoodsList.setUserId(user.getUserId());
            brandGoodsList.setSessionid(user.getSessionid());
        }
        brandGoodsList.setAppbrandId(Long.toString(brandId) + ",");
        brandGoodsList.setMin(currentGoodsCount);
        brandGoodsList.setMax(StaticValues.GOODS_PAGE_COUNT);

        new GetAppStoresIdAllTask().execute(brandGoodsList);

    }

    //店铺商品任务
    public class GetAppStoresIdAllTask extends AsyncTask<BrandGoodsList, Void, BrandGoodsList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BrandGoodsList doInBackground(BrandGoodsList... brandGoodsLists) {
            return new StoreAction().getAppStoresIdAll(BrandMoreGoodsActivity.this, brandGoodsLists[0]);
        }

        @Override
        protected void onPostExecute(BrandGoodsList result) {
            swipeRefreshLayout.setRefreshing(false);

            if(result != null)
            {
                if(result.isSuccess())
                {
                    appgoodsIds = result.getAppgoodsIds();

                    if(!appgoodsIds.isEmpty())
                    {
                        //UI更新
                        if(listData == null)
                        {
                            listData = new ArrayList<Map<String,Object>>();
                        }

                        for(AppgoodsId appgoodsId: appgoodsIds)
                        {
                            Map line = new HashMap();

                            line.put("id", appgoodsId.getId());
                            line.put("name", appgoodsId.getGoodsName());
                            if(appgoodsId.getLogopicUrl() != null)
                            {
                                line.put("img", appgoodsId.getLogopicUrl());
                            }
                            else
                            {
                                line.put("img", "");
                            }

                            line.put("price", appgoodsId.getPromotionPrice());

                            line.put("reTime",appgoodsId.getReTime());

                            line.put("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);

                            listData.add(line);
                        }
                        currentGoodsCount += listData.size();

                        arrange();

                        if(moreGoodsAdapter == null)
                        {
                            moreGoodsAdapter = new MoreGoodsAdapter(BrandMoreGoodsActivity.this, listData);
                            goodsGridView.setAdapter(moreGoodsAdapter);

                        }
                        else
                        {
                            moreGoodsAdapter.notifyDataSetChanged();

                        }

                        currentGoodsCount += listData.size();

                    }
                    else
                    {
                        if(currentGoodsCount == 0)
                        {
                            Toast.makeText(BrandMoreGoodsActivity.this, "该品牌下暂时没有商品", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(BrandMoreGoodsActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                else
                {
                    Toast.makeText(BrandMoreGoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(BrandMoreGoodsActivity.this, "获取商品列表失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void selectTab()
    {
        categoryNewIdc.setVisibility(View.INVISIBLE);
        categoryPriceIdc.setVisibility(View.INVISIBLE);
        categorySaleIdc.setVisibility(View.INVISIBLE);
        categoryUndefIdc.setVisibility(View.INVISIBLE);

        switch (brandGoodsType)
        {
            case StaticValues.BRAND_GOODS_TYPE_NEW:
                categoryNewIdc.setVisibility(View.VISIBLE);
                /*
                if(listData != null && !listData.isEmpty())
                {
                    if(currentArrenge == StaticValues.ARRENGE_TIME_DESC)
                    {
                        Collections.sort(listData, new BrandGoodsTimeDescComparator());
                        currentArrenge = StaticValues.ARRENGE_TIME_ASC;

                    }
                    if(currentArrenge == StaticValues.ARRENGE_TIME_ASC)
                    {
                        Collections.sort(listData, new BrandGoodsTimeAscComparator());
                        currentArrenge = StaticValues.ARRENGE_TIME_DESC;

                    }

                    ↑↓←→
                    if(brandGoodsType == brandGoodsTypePrev)
                    {
                        Collections.sort(listData, new BrandGoodsTimeDescComparator());
                        brandGoodsTypePrev = 0;
                        currentArrenge = StaticValues.ARRENGE_TIME_DESC;

                    }
                    else
                    {
                        Collections.sort(listData, new BrandGoodsTimeAscComparator());
                        currentArrenge = StaticValues.ARRENGE_TIME_ASC;

                    }
                    moreGoodsAdapter.notifyDataSetChanged();


                }
                */


                break;
            case StaticValues.BRAND_GOODS_TYPE_PRICE:
                categoryPriceIdc.setVisibility(View.VISIBLE);
                /*
                if(listData != null && !listData.isEmpty())
                {
                    if(currentArrenge == StaticValues.ARRENGE_PRICE_DESC)
                    {
                        Collections.sort(listData, new BrandGoodsPriceDescComparator());
                        currentArrenge = StaticValues.ARRENGE_PRICE_ASC;

                    }
                    if(currentArrenge == StaticValues.ARRENGE_PRICE_ASC)
                    {
                        Collections.sort(listData, new BrandGoodsPriceAscComparator());
                        currentArrenge = StaticValues.ARRENGE_PRICE_DESC;

                    }

                    /*
                    if(brandGoodsType == brandGoodsTypePrev)
                    {
                        Collections.sort(listData, new BrandGoodsPriceDescComparator());
                        brandGoodsTypePrev = 0;
                        currentArrenge = StaticValues.ARRENGE_PRICE_DESC;

                    }
                    else
                    {
                        Collections.sort(listData, new BrandGoodsPriceAscComparator());
                        currentArrenge = StaticValues.ARRENGE_PRICE_ASC;

                    }

                    moreGoodsAdapter.notifyDataSetChanged();

                }
                */
                break;
            case StaticValues.BRAND_GOODS_TYPE_SALE:
                categorySaleIdc.setVisibility(View.VISIBLE);
                break;
            case StaticValues.BRAND_GOODS_TYPE_UNDEF:
                categoryUndefIdc.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void arrange()
    {
        switch (currentArrenge)
        {
            case StaticValues.ARRENGE_TIME_ASC:
                categoryNew.setText("新品 ↑");
                categoryPrice.setText("价格");
                Collections.sort(listData, new BrandGoodsTimeAscComparator());

                break;
            case StaticValues.ARRENGE_TIME_DESC:
                categoryNew.setText("新品 ↓");
                categoryPrice.setText("价格");
                Collections.sort(listData, new BrandGoodsTimeDescComparator());

                break;
            case StaticValues.ARRENGE_PRICE_ASC:
                categoryNew.setText("新品");
                categoryPrice.setText("价格 ↑");
                Collections.sort(listData, new BrandGoodsPriceAscComparator());

                break;
            case StaticValues.ARRENGE_PRICE_DESC:
                categoryNew.setText("新品");
                categoryPrice.setText("价格 ↓");
                Collections.sort(listData, new BrandGoodsPriceDescComparator());

                break;
        }

    }


    //根据时间排序
    class BrandGoodsTimeAscComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Long time1 = (Long)lhs.get("reTime");
            Long time2 = (Long)rhs.get("reTime");

            if(time1 > time2)
            {
                return 1;
            }
            else if(time1 < time2)
            {
                return -1;
            }
            else
            {
                return time1.compareTo(time2);
            }

        }
    }

    class BrandGoodsTimeDescComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Long time1 = (Long)lhs.get("reTime");
            Long time2 = (Long)rhs.get("reTime");

            if(time1 < time2)
            {
                return 1;
            }
            else if(time1 > time2)
            {
                return -1;
            }
            else
            {
                return time1.compareTo(time2);
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

}
