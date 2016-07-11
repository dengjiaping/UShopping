package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.BrandAction;
import com.brand.ushopping.action.StoreAction;
import com.brand.ushopping.adapter.BrandGoodsAdapter;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.Brand;
import com.brand.ushopping.model.BrandGoodsList;
import com.brand.ushopping.model.SaveAppBrandCollect;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.MyGridView;
import com.brand.ushopping.widget.ScrollViewX;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class BrandActivity extends Activity {
    private AppContext appContext;
    private User user;
    private ImageView logoImageView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private ImageView showpicImageView;
    private ImageView favouriteBtn;
    private MyGridView goodsListView;

    private TextView categoryNew;
    private TextView categoryPrice;
    private TextView categorySale;
    private TextView categoryUndef;
    private ImageView categoryNewIdc;
    private ImageView categoryPriceIdc;
    private ImageView categorySaleIdc;
    private ImageView categoryUndefIdc;

    private ArrayList<AppgoodsId> appgoodsIds;
    private ArrayList<Map<String,Object>> listData = null;

    private Brand brand;
    private int boughtType;
    private int brandGoodsType;
    private int brandGoodsTypePrev;
    private ImageView backBtn;
    private TextView titleTextView;
    private int goodsCompareType;
    private BrandGoodsAdapter adapter = null;
    private int goodsCount = 0;
    private int enterType = StaticValues.BRAND_ENTER_TYPE_NORMAL;
    private ScrollViewX mainScrollView;
    private int currentArrenge = StaticValues.ARRENGE_NONE;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Button moreGoodsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_brand);
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        mainScrollView = (ScrollViewX) findViewById(R.id.main_scroll_view);
        /*
        mainScrollView.setOnScrollListener(new ScrollViewX.OnScrollListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                if (mainScrollView.isAtBottom())
                {
                    loadGoods();
                }
            }

            @Override
            public void onScrollStopped() {

            }

            @Override
            public void onScrolling() {

            }
        });
        */
        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

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
            brand = bundle.getParcelable("brand");
            boughtType = bundle.getInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
            if(brand == null)
            {
                finish();
            }
        }
        else
        {
            finish();
        }

        boughtType = bundle.getInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
        enterType = bundle.getInt("enterType", StaticValues.BRAND_ENTER_TYPE_NORMAL);

        logoImageView = (ImageView) findViewById(R.id.logo);
        nameTextView = (TextView) findViewById(R.id.name);
        descriptionTextView = (TextView) findViewById(R.id.description);
        showpicImageView = (ImageView) findViewById(R.id.showpic);
        favouriteBtn = (ImageView) findViewById(R.id.favourite);
//        favouriteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(user != null)
//                {
//                    SaveAppBrandCollect saveAppBrandCollect = new SaveAppBrandCollect();
//                    saveAppBrandCollect.setUserId(user.getUserId());
//                    saveAppBrandCollect.setSessionid(user.getSessionid());
//                    AppuserId appuserId = new AppuserId();
//                    appuserId.setUserId(user.getUserId());
//                    saveAppBrandCollect.setAppuserId(appuserId);
//                    AppbrandId appbrandId = new AppbrandId();
//                    appbrandId.setId(brand.getId());
//                    saveAppBrandCollect.setAppbrandId(appbrandId);
//
//                    new SaveAppBrandCollectTask().execute(saveAppBrandCollect);
//
//                }
//                else
//                {
//                    Toast.makeText(BrandActivity.this, "注册登录之后才能使用收藏", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });

        int flag = brand.getFlag();
        switch (flag)
        {
            case StaticValues.GOODS_FAVOURITE_ON:
                favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.favourited));
                favouriteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user != null)
                        {
                            Toast.makeText(BrandActivity.this, "您已收藏该品牌", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(BrandActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case StaticValues.GOODS_FAVOURITE_OFF:
                favouriteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user != null)
                        {
                            SaveAppBrandCollect saveAppBrandCollect = new SaveAppBrandCollect();
                            saveAppBrandCollect.setUserId(user.getUserId());
                            saveAppBrandCollect.setSessionid(user.getSessionid());
                            AppuserId appuserId = new AppuserId();
                            appuserId.setUserId(user.getUserId());
                            saveAppBrandCollect.setAppuserId(appuserId);
                            AppbrandId appbrandId = new AppbrandId();
                            appbrandId.setId(brand.getId());
                            saveAppBrandCollect.setAppbrandId(appbrandId);

                            new SaveAppBrandCollectTask().execute(saveAppBrandCollect);
                        }
                        else
                        {
                            Toast.makeText(BrandActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
     }

        if(enterType == StaticValues.BRAND_ENTER_TYPE_AROUND)
        {
            favouriteBtn.setVisibility(View.GONE);

        }

        goodsListView = (MyGridView) findViewById(R.id.goods_list);
        goodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BrandActivity.this, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", id);
                bundle.putInt("boughtType", boughtType);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                startActivity(intent);

            }
        });

        categoryNew = (TextView) findViewById(R.id.category_new);
        categoryNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;
                selectTab();
                reloadGoods();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryPrice = (TextView) findViewById(R.id.category_price);
        categoryPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_PRICE;
                selectTab();
                reloadGoods();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categorySale = (TextView) findViewById(R.id.category_sale);
        categorySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_SALE;
                selectTab();
                reloadGoods();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryUndef = (TextView) findViewById(R.id.category_undef);
        categoryUndef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandGoodsType = StaticValues.BRAND_GOODS_TYPE_FILTER;
                selectTab();
                reloadGoods();
                brandGoodsTypePrev = brandGoodsType;

            }
        });
        categoryNewIdc = (ImageView) findViewById(R.id.category_new_idc);
        categoryPriceIdc = (ImageView) findViewById(R.id.category_price_idc);
        categorySaleIdc = (ImageView) findViewById(R.id.category_sale_idc);
        categoryUndefIdc = (ImageView) findViewById(R.id.category_undef_idc);

        brandGoodsType = StaticValues.BRAND_GOODS_TYPE_NEW;
        brandGoodsTypePrev = brandGoodsType;

        selectTab();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reload();

            }
        });

        moreGoodsBtn = (Button) findViewById(R.id.more_goods);
        moreGoodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrandActivity.this, BrandMoreGoodsActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putLong("brandId", brand.getId());
                bundle1.putString("brandName", brand.getBrandName());
                bundle1.putInt("boughtType", boughtType);
                intent.putExtras(bundle1);
                appContext.setBundleObj(bundle1);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();

    }

    public void reload()
    {
        ImageLoader.getInstance().displayImage(brand.getLogopic(), logoImageView);
        ImageLoader.getInstance().displayImage(brand.getShowpic(), showpicImageView);

        nameTextView.setText(brand.getBrandName());
        descriptionTextView.setText(brand.getDetail());

        reloadGoods();
    }

    public void reloadGoods()
    {
        goodsCount = 0;
        adapter = null;
        listData = null;
        loadGoods();
    }

    public void loadGoods()
    {
        BrandGoodsList brandGoodsList = new BrandGoodsList();
        if(user != null)
        {
            brandGoodsList.setUserId(user.getUserId());
            brandGoodsList.setSessionid(user.getSessionid());
        }
        brandGoodsList.setAppbrandId(Long.toString(brand.getId())+",");
        brandGoodsList.setMin(goodsCount);
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
            return new StoreAction(BrandActivity.this).getAppStoresIdAll(brandGoodsLists[0]);
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
                                line.put("logopic", appgoodsId.getLogopicUrl());
                            }
                            else
                            {
                                line.put("logopic", "");
                            }

                            line.put("promotionPrice", appgoodsId.getPromotionPrice());
                            line.put("originalPrice", appgoodsId.getPromotionPrice());
                            line.put("reTime",appgoodsId.getReTime());
                            line.put("boughtType", boughtType);
                            line.put("salesCount", appgoodsId.getSaleCount());

                            listData.add(line);
                        }
                        goodsCount += listData.size();

                        arrange();

                        if(adapter == null)
                        {
                            adapter = new BrandGoodsAdapter(listData, BrandActivity.this);
                            goodsListView.setAdapter(adapter);

                        }
                        else
                        {
                            adapter.notifyDataSetChanged();

                        }

                    }
                    else
                    {
                        if(goodsCount == 0)
                        {
                            Toast.makeText(BrandActivity.this,"该品牌下暂时没有商品" , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(BrandActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                        }

                    }



                    /*
                    int totalHeight = 0;
                    for (int i = 0; i < appgoodsIds.size(); i++) {
                        View listItem = adapter.getView(i, null, goodsListView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();

                    }

                    ViewGroup.LayoutParams params = goodsListView.getLayoutParams();
                    params.height = totalHeight + 100;
                    ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
                    goodsListView.setLayoutParams(params);
                    */

                }
                else
                {
                    Toast.makeText(BrandActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(BrandActivity.this, "获取商品列表失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //店铺收藏
    public class SaveAppBrandCollectTask extends AsyncTask<SaveAppBrandCollect, Void, SaveAppBrandCollect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SaveAppBrandCollect doInBackground(SaveAppBrandCollect... saveAppBrandCollects) {
            return new BrandAction(BrandActivity.this).saveAppBrandCollectAction(saveAppBrandCollects[0]);

        }

        @Override
        protected void onPostExecute(SaveAppBrandCollect result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    Toast.makeText(BrandActivity.this, "收藏添加成功", Toast.LENGTH_SHORT).show();
                    favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.favourited));
                    favouriteBtn.setEnabled(false);
                }
                else
                {
                    Toast.makeText(BrandActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(BrandActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();

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
                if(listData != null && !listData.isEmpty())
                {
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

                    adapter.notifyDataSetChanged();

                }

                break;
            case StaticValues.BRAND_GOODS_TYPE_PRICE:
                categoryPriceIdc.setVisibility(View.VISIBLE);
                if(listData != null && !listData.isEmpty())
                {
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

                    adapter.notifyDataSetChanged();
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
                Collections.sort(listData, new BrandGoodsTimeAscComparator());

                break;
            case StaticValues.ARRENGE_TIME_DESC:
                Collections.sort(listData, new BrandGoodsTimeDescComparator());

                break;
            case StaticValues.ARRENGE_PRICE_ASC:
                Collections.sort(listData, new BrandGoodsPriceAscComparator());

                break;
            case StaticValues.ARRENGE_PRICE_DESC:
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
            Double price1 = (Double)lhs.get("promotionPrice");
            Double price2 = (Double)rhs.get("promotionPrice");

            return price1.compareTo(price2);
        }
    }

    class BrandGoodsPriceDescComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Double price1 = (Double)lhs.get("promotionPrice");
            Double price2 = (Double)rhs.get("promotionPrice");

            return price2.compareTo(price1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

//        ImageLoader.getInstance().clearMemoryCache();

    }
}
