package com.brand.ushopping.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.brand.ushopping.widget.GoodsFilterPopup;

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
    private ViewGroup rootView;

    private long brandId;
    private String brandName;

    private TextView categoryNew;
    private TextView categoryPrice;
    private TextView categorySale;
    private TextView categoryFilter;
    private ImageView categoryNewIdc;
    private ImageView categoryPriceIdc;
    private ImageView categorySaleIdc;
    private ImageView categoryFilterIdc;

    private int brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;
//    private int brandGoodsTypePrev;
    private int currentArrenge = StaticValues.ARRENGE_TIME_DESC;
    private int boughtType;

    private GoodsFilterPopup goodsFilterPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        boughtType = bundle.getInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);

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
        categoryFilter = (TextView) findViewById(R.id.category_filter);
        categoryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_FILTER;
                selectTab();

//                callGoodsFilterPopup();

            }
        });
        categoryNewIdc = (ImageView) findViewById(R.id.category_new_idc);
        categoryPriceIdc = (ImageView) findViewById(R.id.category_price_idc);
        categorySaleIdc = (ImageView) findViewById(R.id.category_sale_idc);
        categoryFilterIdc = (ImageView) findViewById(R.id.category_filter_idc);

        brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;
//        brandGoodsTypePrev = brandGoodsType;

        rootView = (ViewGroup) findViewById(R.id.root_view);

        selectTab();

        reload();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void reload()
    {
        if(listData != null)
        {
            listData.clear();
        }

        if(moreGoodsAdapter != null)
        {
//            moreGoodsAdapter.notifyItemRangeRemoved(0, currentGoodsCount);
            moreGoodsAdapter.notifyDataSetChanged();

        }

        currentGoodsCount = 0;

//        goodsGridView.removeAllViewsInLayout();


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
            return new StoreAction(BrandMoreGoodsActivity.this).getAppStoresIdAll(brandGoodsLists[0]);
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
                            line.put("boughtType", boughtType);
                            line.put("salesCount", appgoodsId.getSaleCount());

                            //去重
                            boolean insertAvaliable = true;
                            for(Map<String,Object> item : listData)
                            {
                                long id1 = (long) item.get("id");
                                long id2 = (long) line.get("id");
                                if(id1 == id2)
                                {
                                    insertAvaliable = false;
                                    break;
                                }

                            }

                            if(insertAvaliable)
                            {
                                listData.add(line);
                            }

                        }
                        currentGoodsCount += appgoodsIds.size();

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
        categoryFilterIdc.setVisibility(View.INVISIBLE);

        switch (brandGoodsType)
        {
            case StaticValues.BRAND_GOODS_TYPE_NEW:
                categoryNewIdc.setVisibility(View.VISIBLE);

                break;
            case StaticValues.BRAND_GOODS_TYPE_PRICE:
                categoryPriceIdc.setVisibility(View.VISIBLE);

                break;
            case StaticValues.BRAND_GOODS_TYPE_SALE:
                categorySaleIdc.setVisibility(View.VISIBLE);
                break;
            case StaticValues.BRAND_GOODS_TYPE_FILTER:
                categoryFilterIdc.setVisibility(View.VISIBLE);
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
                categorySale.setText("销量");
                Collections.sort(listData, new BrandGoodsTimeAscComparator());

                break;
            case StaticValues.ARRENGE_TIME_DESC:
                categoryNew.setText("新品 ↓");
                categoryPrice.setText("价格");
                categorySale.setText("销量");
                Collections.sort(listData, new BrandGoodsTimeDescComparator());

                break;
            case StaticValues.ARRENGE_PRICE_ASC:
                categoryNew.setText("新品");
                categoryPrice.setText("价格 ↑");
                categorySale.setText("销量");
                Collections.sort(listData, new BrandGoodsPriceAscComparator());

                break;
            case StaticValues.ARRENGE_PRICE_DESC:
                categoryNew.setText("新品");
                categoryPrice.setText("价格 ↓");
                categorySale.setText("销量");
                Collections.sort(listData, new BrandGoodsPriceDescComparator());

                break;

            case StaticValues.ARRENGE_SALE_ASC:
                categoryNew.setText("新品");
                categoryPrice.setText("价格");
                categorySale.setText("销量 ↑");
                Collections.sort(listData, new BrandGoodsSalseCountAscComparator());

                break;
            case StaticValues.ARRENGE_SALE_DESC:
                categoryNew.setText("新品");
                categoryPrice.setText("价格");
                categorySale.setText("销量 ↓");
                Collections.sort(listData, new BrandGoodsSalseCountDescComparator());

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

    private void callGoodsFilterPopup()
    {
        if(goodsFilterPopup == null)
        {
            goodsFilterPopup = new GoodsFilterPopup(BrandMoreGoodsActivity.this);

        }
        goodsFilterPopup.showAtLocation(rootView, Gravity.LEFT | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //根据销量排序
    class BrandGoodsSalseCountAscComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Integer salesCount1 = (Integer) lhs.get("salesCount");
            Integer salesCount2 = (Integer)rhs.get("salesCount");

            return salesCount1.compareTo(salesCount2);
        }
    }

    class BrandGoodsSalseCountDescComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Integer salesCount1 = (Integer)lhs.get("salesCount");
            Integer salesCount2 = (Integer)rhs.get("salesCount");

            return salesCount2.compareTo(salesCount1);
        }
    }

    //去重操作
//    private void distinctList()
//    {
//        ArrayList<Map<String,Object>> newListData = new ArrayList<Map<String,Object>>();
//        HashSet set = new HashSet();
//        for(Map<String,Object> item : listData)
//        {
//            set.put
//            item.get("id");
//
//        }
//
//    }


}
