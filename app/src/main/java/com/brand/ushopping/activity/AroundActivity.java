package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.StoreAction;
import com.brand.ushopping.adapter.AroundItemAdapter;
import com.brand.ushopping.model.AppStoresList;
import com.brand.ushopping.model.AppStoresListItem;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AroundActivity extends Activity {
    private AppContext appContext;
    private User user;
    private ArrayList<AppStoresListItem> appStoresListItems;
    private ListView listView;

    private TextView titleTextView;
    private TextView areaTextView;
    private TextView clostBtn;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ImageView mapBtn;
    private int currentBoughtType;
    private TimeoutbleProgressDialog aroundDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_around);
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

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
            currentBoughtType = bundle.getInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
        }
        else
        {
            finish();
        }

        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());
        areaTextView = (TextView) findViewById(R.id.area);
        areaTextView.setText(appContext.getCity());
        clostBtn = (TextView) findViewById(R.id.close);
        clostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AroundActivity.this.finish();

            }
        });

        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);
        mapBtn = (ImageView) findViewById(R.id.map);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AroundActivity.this, MapLocateActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("appStoresListItems", appStoresListItems);
                intent.putExtras(bundle1);
                appContext.setBundleObj(bundle1);

                startActivity(intent);

            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        aroundDialog = TimeoutbleProgressDialog.createProgressDialog(AroundActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                aroundDialog.dismiss();

                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("获取周边门店失败");

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();

    }

    private void reload()
    {
        //没有数据时的提示
        warningLayout.setVisibility(View.GONE);

        String city = appContext.getCity();
        if(city != null)
        {
            AppStoresList appStoresList = new AppStoresList();
            if(user != null)
            {
                appStoresList.setUserId(user.getUserId());
                appStoresList.setSessionid(user.getSessionid());
            }
            appStoresList.setCity(city);

            new GettAppStoresListTask().execute(appStoresList);

        }
        else
        {
            warningLayout.setVisibility(View.VISIBLE);
            warningTextView.setText("未获取到位置信息");

        }

    }

    //周边门店
    public class GettAppStoresListTask extends AsyncTask<AppStoresList, Void, AppStoresList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            aroundDialog.show();

        }

        @Override
        protected AppStoresList doInBackground(AppStoresList... appStoresLists) {
            return new StoreAction(AroundActivity.this).gettAppStoresList(appStoresLists[0]);
        }

        @Override
        protected void onPostExecute(AppStoresList result) {
            aroundDialog.dismiss();
            if(result != null)
            {
                if(result.isSuccess())
                {
                    appStoresListItems = result.getAppStoresListItems();

                    if(appStoresListItems.size() > 0)
                    {
                        mapBtn.setVisibility(View.VISIBLE);

                    }

                    //计算距离
                    for(AppStoresListItem appStoresListItem: appStoresListItems)
                    {
                        appStoresListItem.setDistance(CommonUtils.getDistance(appContext.getLongitude(), appContext.getLatitude(), appStoresListItem.getLongitude(), appContext.getLatitude()));

                    }

                    //UI更新
                    List listData = new ArrayList<Map<String,Object>>();
                    for(AppStoresListItem appStoresListItem: appStoresListItems)
                    {
                        Map line = new HashMap();
                        AppbrandId appbrandId = appStoresListItem.getAppbrandId();
                        line.put("id", appbrandId.getId());
                        line.put("shopName", appStoresListItem.getShopName());
                        line.put("logopicUrl", appStoresListItem.getLogopicUrl());
                        line.put("showpic", appbrandId.getShowpic());
                        line.put("shopAddr", appStoresListItem.getShopAddr());
                        line.put("latitude", appStoresListItem.getLatitude());
                        line.put("longitude", appStoresListItem.getLongitude());
                        line.put("flag", appStoresListItem.getFlag());
                        line.put("door", appStoresListItem.getDoor());
                        line.put("distance", appStoresListItem.getDistance());

                        if(CommonUtils.isValueEmpty(appStoresListItem.getShopTele()))
                        {
                            line.put("shopTele", "");
                        }
                        else
                        {
                            line.put("shopTele", appStoresListItem.getShopTele());
                        }

                        if(CommonUtils.isValueEmpty(appStoresListItem.getBusinessHours()))
                        {
                            line.put("businessHours", "");
                        }
                        else
                        {
                            line.put("businessHours", appStoresListItem.getBusinessHours());
                        }

                        line.put("boughtType", currentBoughtType);

                        listData.add(line);
                    }

                    //排序
                    Collections.sort(listData, new DistanceComparator());

                    AroundItemAdapter adapter = new AroundItemAdapter(listData, AroundActivity.this);
                    listView.setAdapter(adapter);
                }
                else
                {


                }

            }
        }
    }

    //根据距离排序
    class DistanceComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
            Double distance1 = (Double)lhs.get("distance");
            Double distance2 = (Double)rhs.get("distance");

            return distance1.compareTo(distance2);
        }
    }

}
