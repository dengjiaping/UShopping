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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.adapter.CategoryGoodsAdapter;
import com.brand.ushopping.model.AppGoodsTypeId;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.EndlessGridRecyclerOnScrollListener;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CategoryActivity extends Activity {
    private AppContext appContext;
    private User user;
    private ImageView backBtn;
    private TextView titleTextView;
    private long categoryId;
    private String categoryName;
    private RecyclerView goodsGridView;
    private int currentGoodsCount = 0;
    private ArrayList<Goods> goodsList;
    private CategoryGoodsAdapter goodsAdapter;
    private GridLayoutManager gridLayoutManager;

    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ArrayList<Map<String,Object>> goodsListData;

    private TextView categoryNew;
    private TextView categoryPrice;
    private TextView categorySale;
    private TextView categoryUndef;
    private ImageView categoryNewIdc;
    private ImageView categoryPriceIdc;
    private ImageView categorySaleIdc;
    private ImageView categoryUndefIdc;
    private int brandGoodsType;
    private int brandGoodsTypePrev;
    private int currentArrenge = StaticValues.ARRENGE_NONE;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        Bundle bundle = getIntent().getExtras();
        categoryId = bundle.getLong("categoryId");
        Log.v("categoryId", Long.toString(categoryId));

        categoryName = bundle.getString("categoryName");

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(categoryName);

        goodsGridView = (RecyclerView) findViewById(R.id.goods_grid);

//        goodsGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastVisibleItem = ((GridLayoutManager) gridLayoutManager).findLastVisibleItemPosition();
//                int totalItemCount = gridLayoutManager.getItemCount();
//                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
//                // dy>0 表示向下滑动
//                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
//                    Log.d("ushopping", "load more!");
//                    reload();
//
//                }
//            }
//        });

        /*
        goodsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", id);
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        goodsGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            AppGoodsTypeId appGoodsTypeId = new AppGoodsTypeId();
                            if(user != null)
                            {
                                appGoodsTypeId.setUserId(user.getUserId());
                                appGoodsTypeId.setSessionid(user.getSessionid());
                            }
                            appGoodsTypeId.setAppcategoryId(categoryId);
                            appGoodsTypeId.setMin(currentGoodsCount);
                            appGoodsTypeId.setMax(StaticValues.GOODS_PAGE_COUNT);

                            new GetAppGoodsTypeIdTask().execute(appGoodsTypeId);

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        */

        //没有数据时的提示
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        warningLayout.setVisibility(View.GONE);

        categoryNew = (TextView) findViewById(R.id.category_new);
        categoryNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;
                selectTab();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryPrice = (TextView) findViewById(R.id.category_price);
        categoryPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_PRICE;
                selectTab();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categorySale = (TextView) findViewById(R.id.category_sale);
        categorySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_SALE;
                selectTab();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryUndef = (TextView) findViewById(R.id.category_undef);
        categoryUndef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_FILTER;
                selectTab();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryNewIdc = (ImageView) findViewById(R.id.category_new_idc);
        categoryPriceIdc = (ImageView) findViewById(R.id.category_price_idc);
        categorySaleIdc = (ImageView) findViewById(R.id.category_sale_idc);
        categoryUndefIdc = (ImageView) findViewById(R.id.category_undef_idc);

        brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;
        brandGoodsTypePrev = brandGoodsType;

        gridLayoutManager = new GridLayoutManager(this, 2);
        goodsGridView.setLayoutManager(gridLayoutManager);

        selectTab();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                reload();

            }
        });

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
        goodsAdapter = null;
        goodsListData = null;
        goodsGridView.removeAllViewsInLayout();

        load();
    }

    public void load()
    {
        AppGoodsTypeId appGoodsTypeId = new AppGoodsTypeId();
        if(user != null)
        {
            appGoodsTypeId.setUserId(user.getUserId());
            appGoodsTypeId.setSessionid(user.getSessionid());
        }
        if(categoryId != 17)
        {
            appGoodsTypeId.setAppcategoryId(categoryId);
        }
        else
        {
            appGoodsTypeId.setAppcategoryId(null);
        }

        appGoodsTypeId.setMin(currentGoodsCount);
        appGoodsTypeId.setMax(StaticValues.GOODS_PAGE_COUNT);

        new GetAppGoodsTypeIdTask().execute(appGoodsTypeId);


    }

    //商品类目任务
    public class GetAppGoodsTypeIdTask extends AsyncTask<AppGoodsTypeId, Void, AppGoodsTypeId>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppGoodsTypeId doInBackground(AppGoodsTypeId... appGoodsTypeIds) {
            return new GoodsAction().getAppGoodsTypeId(appGoodsTypeIds[0]);

        }

        @Override
        protected void onPostExecute(AppGoodsTypeId result) {
            swipeRefreshLayout.setRefreshing(false);

            if(result != null)
            {
                if(result.isSuccess())
                {
                    goodsList = result.getGoods();
                    if(!goodsList.isEmpty())
                    {
                        if(goodsListData == null)
                        {
                            goodsListData = new ArrayList<Map<String,Object>>();
                        }

                        for(Goods goods: goodsList)
                        {
                            Map line = new HashMap();

                            line.put("id", goods.getId());
                            line.put("img", goods.getLogopicUrl());
                            line.put("name", goods.getGoodsName());
                            line.put("price", goods.getPromotionPrice());
                            line.put("favouriteCount", 123);

                            goodsListData.add(line);

                        }

                        if(goodsAdapter == null)
                        {
                            goodsAdapter = new CategoryGoodsAdapter(CategoryActivity.this, goodsListData);
                            goodsGridView.setAdapter(goodsAdapter);

                        }
                        else
                        {
                            goodsAdapter.notifyDataSetChanged();

                        }

                        currentGoodsCount += goodsList.size();

                        warningLayout.setVisibility(View.GONE);
                    }
                    else
                    {
                        if(currentGoodsCount == 0)
                        {
                            warningLayout.setVisibility(View.VISIBLE);
                            warningTextView.setText("此分类下暂时没有商品");
                        }
                        else
                        {
                            Toast.makeText(CategoryActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {
                    Toast.makeText(CategoryActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("获取分类商品失败..");

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
                if(goodsListData != null && !goodsListData.isEmpty())
                {
                    if(brandGoodsType == brandGoodsTypePrev)
                    {
                        Collections.sort(goodsListData, new BrandGoodsTimeDescComparator());
                        brandGoodsTypePrev = 0;
                        currentArrenge = StaticValues.ARRENGE_TIME_DESC;

                    }
                    else
                    {
                        Collections.sort(goodsListData, new BrandGoodsTimeAscComparator());
                        currentArrenge = StaticValues.ARRENGE_TIME_ASC;

                    }

                    goodsAdapter.notifyDataSetChanged();

                }

                break;
            case StaticValues.BRAND_GOODS_TYPE_PRICE:
                categoryPriceIdc.setVisibility(View.VISIBLE);
                if(goodsListData != null && !goodsListData.isEmpty())
                {
                    if(brandGoodsType == brandGoodsTypePrev)
                    {
                        Collections.sort(goodsListData, new BrandGoodsPriceDescComparator());
                        brandGoodsTypePrev = 0;
                        currentArrenge = StaticValues.ARRENGE_PRICE_DESC;

                    }
                    else
                    {
                        Collections.sort(goodsListData, new BrandGoodsPriceAscComparator());
                        currentArrenge = StaticValues.ARRENGE_PRICE_ASC;

                    }

                    goodsAdapter.notifyDataSetChanged();
                }

                break;
            case StaticValues.BRAND_GOODS_TYPE_SALE:
                categorySaleIdc.setVisibility(View.VISIBLE);
                break;
            case StaticValues.BRAND_GOODS_TYPE_FILTER:
                categoryUndefIdc.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void arrange()
    {
        switch (currentArrenge)
        {
            case StaticValues.ARRENGE_TIME_ASC:
                Collections.sort(goodsListData, new BrandGoodsTimeAscComparator());

                break;
            case StaticValues.ARRENGE_TIME_DESC:
                Collections.sort(goodsListData, new BrandGoodsTimeDescComparator());

                break;
            case StaticValues.ARRENGE_PRICE_ASC:
                Collections.sort(goodsListData, new BrandGoodsPriceAscComparator());

                break;
            case StaticValues.ARRENGE_PRICE_DESC:
                Collections.sort(goodsListData, new BrandGoodsPriceDescComparator());

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

            return time1.compareTo(time2);
        }
    }

    class BrandGoodsTimeDescComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Long time1 = (Long)lhs.get("reTime");
            Long time2 = (Long)rhs.get("reTime");

            return time2.compareTo(time1);
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
