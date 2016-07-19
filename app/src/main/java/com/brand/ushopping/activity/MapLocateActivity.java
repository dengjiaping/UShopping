package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.model.AppStoresListItem;

import java.util.ArrayList;

public class MapLocateActivity extends Activity
        implements LocationSource, AMapLocationListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener {
    private MapView mapView;
    private AMap aMap;
    private ArrayList<AppStoresListItem> appStoresListItems;
    private AppStoresListItem appStoresListItem;
    private ImageView backBtn;
    private TextView titleTextView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AppContext appContext;
    private Double currentLng;
    private Double currentLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map_locate);
        appContext = (AppContext) getApplicationContext();

        Bundle bundle = null;
        try
        {
            bundle = getIntent().getExtras();
        }
        catch (Exception e)
        {
            bundle = appContext.getBundleObj();
        }
        if(bundle != null)
        {
            appStoresListItems = bundle.getParcelableArrayList("appStoresListItems");
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
            aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(final LatLng latLng) {
                    //地图长按事件
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapLocateActivity.this);
                    builder.setMessage("是否要导航到此位置");
                    builder.setTitle("登录");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MapLocateActivity.this, NavActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putDouble("startLng", currentLng);
                            bundle.putDouble("startLat", currentLat);
                            bundle.putDouble("destLng", latLng.longitude);
                            bundle.putDouble("destLat", latLng.latitude);

                            intent.putExtras(bundle);
                            appContext.setBundleObj(bundle);
                            startActivity(intent);

                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();

                }
            });

        }

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);
        // aMap.setMyLocationType()


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

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    public void onMapLoaded() {
        if(appStoresListItems != null && !appStoresListItems.isEmpty())
        {
            appStoresListItem = appStoresListItems.get(0);

            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(new LatLng(appStoresListItem.getLatitude(), appStoresListItem.getLongitude())).build();
//            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1));

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
                    .position(new LatLng(appStoresListItem.getLatitude(), appStoresListItem.getLongitude())).title(appStoresListItem.getShopName()).snippet("    ")
                    .draggable(false).visible(true));

        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                currentLng = amapLocation.getLongitude();
                currentLat = amapLocation.getLatitude();

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setToTop();
        return false;
    }
}
