package com.brand.ushopping.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.adapter.GoodsAdapter;
import com.brand.ushopping.model.Category;
import com.brand.ushopping.model.FilterParams;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.SearchAppGoods;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.FilterCategoryItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoodsFilterActivity extends AppCompatActivity {
    private AppContext appContext;
    private User user;
    private ImageView backBtn;
    private TextView titleTextView;
    private Spinner yearSpinner;
    private Spinner seasonSpnner;
    private EditText priceMinEditText;
    private EditText priceMaxEditText;
    private Button confirmBtn;
    private Button cancelBtn;
    private GridLayout categoryGridView;
    private FilterParams filterParams;
    private ArrayList<Category> categoryList;   //商品类别列表
    private ArrayList<FilterCategoryItemView> categoryItemList;   //商品类别列表
    private static final String [] yearData = {
        "", "2016", "2015", "2014", "2013", "2012"
    };
    private static final String [] seasonData = {
        "", "春", "夏", "秋", "冬"
    };
    private String yearSelected = null;
    private String seasonSelected = null;
    private Long appCategoryIdSelected = null;//类型ID
    private ViewGroup filterParamViewGroup;
    private GridView filterResultGridView;
    private ArrayList<Goods> goodses;
    private GoodsAdapter goodsAdapter = null;
    private ArrayList<Map<String,Object>> listData;
    private int goodsCount = 0;
    private long brandId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_filter);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

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
            brandId = bundle.getLong("brandId");
        }
        else
        {
            finish();
        }

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        yearSpinner = (Spinner) findViewById(R.id.year);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearSelected = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                yearSelected = null;
            }
        });
        seasonSpnner = (Spinner) findViewById(R.id.season);
        seasonSpnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seasonSelected = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seasonSelected = null;
            }
        });
        priceMinEditText = (EditText) findViewById(R.id.price_min);
        priceMaxEditText = (EditText) findViewById(R.id.price_max);
        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(yearSelected == null)
//                {
//                    Toast.makeText(GoodsFilterActivity.this, "请选择年份", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(seasonSelected == null)
//                {
//                    Toast.makeText(GoodsFilterActivity.this, "请选择季节", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(priceMinEditText.getText() == null || priceMaxEditText.getText() == null)
//                {
//                    Toast.makeText(GoodsFilterActivity.this, "请选择价格区间", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(appCategoryIdSelected == null)
//                {
//                    Toast.makeText(GoodsFilterActivity.this, "请选择类别", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                search();

                /*
                filterParams = new FilterParams();
                filterParams.setYear(yearSelected);
                filterParams.setSeason(seasonSelected);
                filterParams.setPriceMin(priceMinEditText.getText().toString());
                filterParams.setPriceMax(priceMaxEditText.getText().toString());
                filterParams.setAppCategoryId(appCategoryIdSelected);

                Intent intent = new Intent(GoodsFilterActivity.this, BrandMoreGoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("filterParams", filterParams);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
                */
            }
        });

        cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        categoryGridView = (GridLayout) findViewById(R.id.category_grid);
        filterParamViewGroup = (ViewGroup) findViewById(R.id.filter_param);
        filterResultGridView = (GridView) findViewById(R.id.filter_grid);

        filterParamViewGroup.setVisibility(View.VISIBLE);
        filterResultGridView.setVisibility(View.GONE);
        filterResultGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GoodsFilterActivity.this, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", id);
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                startActivity(intent);
            }
        });
        filterResultGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            search();

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

    //搜索
    private void search()
    {
        SearchAppGoods searchAppGoods = new SearchAppGoods();
        if(!CommonUtils.isValueEmpty(yearSelected))
        {
            searchAppGoods.setYear(yearSelected);
        }
        if(!CommonUtils.isValueEmpty(seasonSelected))
        {
            searchAppGoods.setQuarter(seasonSelected);
        }
        if(!CommonUtils.isValueEmpty(priceMinEditText.getText().toString()))
        {
            searchAppGoods.setMinMoney(Double.valueOf(priceMinEditText.getText().toString()));
        }
        if(!CommonUtils.isValueEmpty(priceMaxEditText.getText().toString()))
        {
            searchAppGoods.setMaxMoney(Double.valueOf(priceMaxEditText.getText().toString()));
        }
        searchAppGoods.setAppCategoryId(appCategoryIdSelected);
        if(user != null)
        {
            searchAppGoods.setUserId(user.getUserId());
            searchAppGoods.setSessionid(user.getSessionid());
        }
        searchAppGoods.setMin(goodsCount);
        searchAppGoods.setMax(StaticValues.GOODS_PAGE_COUNT);
        searchAppGoods.setAppBranId(brandId);

        new SearchAppGoodsActionTask().execute(searchAppGoods);
    }

    @Override
    protected void onStart() {
        super.onStart();

        yearSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, yearData));
        seasonSpnner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, seasonData));

        categoryList = appContext.getMain().getCategory();
        categoryItemList = new ArrayList<FilterCategoryItemView>();
        for(Category category : categoryList)
        {
            FilterCategoryItemView categoryItemView = new FilterCategoryItemView(GoodsFilterActivity.this, null, category);
            categoryGridView.addView(categoryItemView);
            categoryItemList.add(categoryItemView);
        }

    }

    public void setAppCategoryIdSelected(long id)
    {
        appCategoryIdSelected = id;

    }

    public void categoryClearSelected()
    {
        for(FilterCategoryItemView filterCategoryItemView : categoryItemList)
        {
            filterCategoryItemView.unselect();
        }
    }

    //搜索任务
    public class SearchAppGoodsActionTask extends AsyncTask<SearchAppGoods, Void, SearchAppGoods>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected SearchAppGoods doInBackground(SearchAppGoods... searchAppGoodses) {
            return new GoodsAction(GoodsFilterActivity.this).searchAppGoodsAction(searchAppGoodses[0]);
        }

        @Override
        protected void onPostExecute(SearchAppGoods result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    goodses = result.getGoodses();
                    filterParamViewGroup.setVisibility(View.GONE);
                    filterResultGridView.setVisibility(View.VISIBLE);
                    setValues();

                }
                else
                {
                    Toast.makeText(GoodsFilterActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(GoodsFilterActivity.this, "未搜索到商品", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void setValues() {
        if (goodses != null)
        {
            if(!goodses.isEmpty())
            {
                //UI更新
                if(listData == null)
                {
                    listData = new ArrayList<Map<String,Object>>();
                }

                for(Goods goods: goodses)
                {
                    Map line = new HashMap();

                    line.put("id", goods.getId());
                    line.put("name", goods.getGoodsName());
                    if(goods.getLogopicUrl() != null)
                    {
                        line.put("img", goods.getLogopicUrl());
                    }
                    else
                    {
                        line.put("img", "");
                    }
                    line.put("price", goods.getPromotionPrice());
                    line.put("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);

                    listData.add(line);
                }
                goodsCount += goodses.size();

                if(goodsAdapter == null)
                {
                    goodsAdapter = new GoodsAdapter(listData, GoodsFilterActivity.this);
                    filterResultGridView.setAdapter(goodsAdapter);

                }
                else
                {
                    goodsAdapter.notifyDataSetChanged();

                }

            }
            else
            {
                Toast.makeText(GoodsFilterActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
