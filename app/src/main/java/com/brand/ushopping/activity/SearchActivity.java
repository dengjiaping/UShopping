package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.action.RefAction;
import com.brand.ushopping.adapter.GoodsAdapter;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.SearchAppGoods;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SearchActivity extends Activity {
    private User user;

    private AppContext appContext;

    private EditText searchEditText;
    private ImageView searchBtn;
    private TextView cancelBtn;
    private ViewGroup searchPropertyView;
    private GridView productGridView;
    private GridLayout recentSearchLayout;
    private HashSet<String> recent;
    private String keyword;
    private ArrayList<Goods> goodses;
    private GoodsAdapter goodsAdapter = null;
    private ArrayList<Map<String,Object>> listData;
    private int goodsCount = 0;
    private int currentSearchMode = StaticValues.SEARCH_MODE_PROPERTY;
    private Button cleatHistoryBtn;
    private static final String[] searchTypes = {StaticValues.SEARCH_TYPE_NAME, StaticValues.SEARCH_TYPE_CODE};
    private Spinner searchTypeSpinner;
    private String searchTypeSelected = StaticValues.SEARCH_TYPE_NAME;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private int boughtType = StaticValues.BOUTHT_TYPE_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        searchTypeSpinner = (Spinner) findViewById(R.id.search_type);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.search_type_spinner_item, searchTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(adapter);
        searchTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchTypeSelected = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        searchEditText = (EditText) findViewById(R.id.search_text);
        searchBtn = (ImageView) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = searchEditText.getText().toString();
                if (!keyword.isEmpty())
                {
                    goodsCount = 0;
                    goodsAdapter = null;
                    listData = null;
                    productGridView.removeAllViewsInLayout();

                    search();

                }
                else
                {
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelBtn = (TextView) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();

            }
        });

        cleatHistoryBtn = (Button) findViewById(R.id.clear_history);
        cleatHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RefAction(SearchActivity.this).clearRecentSearch(SearchActivity.this);
                loadRecent();

            }
        });

        searchPropertyView = (ViewGroup) findViewById(R.id.search_property_view);
        productGridView = (GridView) findViewById(R.id.product_grid);
        productGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", id);
                bundle.putInt("boughtType", boughtType);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                startActivity(intent);
            }
        });

        productGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        recentSearchLayout = (GridLayout) findViewById(R.id.recent);

        Bundle bundle = null;
        try
        {
            bundle = getIntent().getExtras();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            bundle = appContext.getBundleObj();
        }
        if(bundle != null)
        {
            boughtType = bundle.getInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
        }
        else
        {
            finish();
        }

        chooseMode();
        loadRecent();

    }

    private void loadRecent() {
        recentSearchLayout.removeAllViewsInLayout();

        recent = new RefAction(SearchActivity.this).getRecentSearch(SearchActivity.this);
        if(recent != null)
        {
            Log.v("recent search", recent.toString());

            for(final String keyword : recent){
                final TextView textView = new TextView(SearchActivity.this);
                textView.setText(keyword);
                textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchActivity.this.keyword = textView.getText().toString();
                        searchEditText.setText(keyword);
                        search();

                    }
                });
                textView.setTextColor(getResources().getColor(R.color.text_light_grey));

                ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(120, 48);
                textView.setLayoutParams(para);
                textView.setTextColor(getResources().getColor(R.color.text_grey));
                textView.setBackgroundColor(getResources().getColor(R.color.text_white));
                textView.setPadding(8, 8, 8, 8);

                recentSearchLayout.addView(textView);

                Space space = new Space(SearchActivity.this);
                para = new ViewGroup.LayoutParams(18, 48);
                space.setLayoutParams(para);
                recentSearchLayout.addView(space);

            }

        }
    }

    //搜索
    private void search()
    {
        SearchAppGoods searchAppGoods = new SearchAppGoods();

        if(searchTypeSelected.equals(StaticValues.SEARCH_TYPE_NAME))
        {
            searchAppGoods.setGoodsName(keyword);
        }
        if(searchTypeSelected.equals(StaticValues.SEARCH_TYPE_CODE)) {
            searchAppGoods.setBarCode(keyword);
        }

        searchAppGoods.setMin(goodsCount);
        searchAppGoods.setMax(StaticValues.GOODS_PAGE_COUNT);

        if(user != null)
        {
            searchAppGoods.setUserId(user.getUserId());
            searchAppGoods.setSessionid(user.getSessionid());
        }

        Log.v("ushopping", "search page min: " + searchAppGoods.getMin() + "max: "+searchAppGoods.getMax());

        new SearchAppGoodsActionTask().execute(searchAppGoods);

        currentSearchMode = StaticValues.SEARCH_MODE_RESULT;
        chooseMode();

    }

    //搜索任务
    public class SearchAppGoodsActionTask extends AsyncTask<SearchAppGoods, Void, SearchAppGoods>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            warningLayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected SearchAppGoods doInBackground(SearchAppGoods... searchAppGoodses) {
            return new GoodsAction(SearchActivity.this).searchAppGoodsAction(searchAppGoodses[0]);
        }

        @Override
        protected void onPostExecute(SearchAppGoods result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    goodses = result.getGoodses();

                    setValues();

                }
                else
                {
                    Toast.makeText(SearchActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(SearchActivity.this, "未搜索到商品", Toast.LENGTH_SHORT).show();

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
                    line.put("boughtType", boughtType);

                    listData.add(line);
                }
                goodsCount += goodses.size();

                if(goodsAdapter == null)
                {
                    goodsAdapter = new GoodsAdapter(listData, SearchActivity.this);
                    productGridView.setAdapter(goodsAdapter);

                }
                else
                {
                    goodsAdapter.notifyDataSetChanged();

                }

                new RefAction(SearchActivity.this).setRecentSearch(SearchActivity.this, keyword);
                loadRecent();

            }
            else
            {
                Toast.makeText(SearchActivity.this, "没有更多", Toast.LENGTH_SHORT).show();

            }

        }

    }

    private void chooseMode()
    {
        switch (currentSearchMode)
        {
            case StaticValues.SEARCH_MODE_PROPERTY:
                searchPropertyView.setVisibility(ViewGroup.VISIBLE);
                productGridView.setVisibility(View.GONE);
                warningLayout.setVisibility(View.GONE);

                break;

            case StaticValues.SEARCH_MODE_RESULT:
                searchPropertyView.setVisibility(ViewGroup.GONE);
                productGridView.setVisibility(View.VISIBLE);
                warningLayout.setVisibility(View.GONE);

                break;
        }

    }

    private void backAction()
    {
        switch (currentSearchMode)
        {
            case StaticValues.SEARCH_MODE_PROPERTY:
                SearchActivity.this.finish();

                break;

            case StaticValues.SEARCH_MODE_RESULT:
                currentSearchMode = StaticValues.SEARCH_MODE_PROPERTY;
                chooseMode();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        backAction();

    }

}
