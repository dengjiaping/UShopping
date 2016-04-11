package com.brand.ushopping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.brand.ushopping.R;
import com.brand.ushopping.model.AppStoresListItem;

import java.util.ArrayList;

public class MapLocateActivity extends Activity  implements AMap.OnMapLoadedListener {
    private MapView mapView;
    private AMap aMap;
    private ArrayList<AppStoresListItem> appStoresListItems;
    private AppStoresListItem appStoresListItem;
    private ImageView backBtn;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map_locate);

        Bundle bundle = getIntent().getExtras();
        appStoresListItems = bundle.getParcelableArrayList("appStoresListItems");

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        init();

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        if(appStoresListItems != null && !appStoresListItems.isEmpty())
        {
            addMarkersToMap();// 往地图上添加marker
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onMapLoaded() {
        if(appStoresListItems != null && !appStoresListItems.isEmpty())
        {
            appStoresListItem = appStoresListItems.get(0);

            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(new LatLng(appStoresListItem.getLatitude(), appStoresListItem.getLongitude())).build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1));

        }

    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
//        byte[] byteArray = mapMarker.getIcon();
        for(AppStoresListItem appStoresListItem: appStoresListItems)
        {
            aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(appStoresListItem.getLatitude(), appStoresListItem.getLongitude())).title(appStoresListItem.getShopName())
                    .draggable(false));

        }

    }

}
