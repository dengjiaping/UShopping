package com.brand.ushopping.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AddressAction;
import com.brand.ushopping.action.RefAction;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.AppUser_id;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;

public class AddAddressActivity extends Activity implements AMapLocationListener {
    private ImageView backBtn;
    private TextView titleTextView;

    private AppContext appContext;
    private User user;
    private Address address;
    private int action;

    private EditText consigneeEditText;
    private EditText mobileEditText;
    private EditText telephoneEditText;
    private EditText areaEditText;
    private EditText deaddressEditText;
    private EditText zipcodeEditText;
    private Button saveBtn;
    private TextView setDefaultAddressBtn;

    private double longitude = 0;
    private double latitude = 0;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_address);

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
        titleTextView.setText(this.getTitle().toString());
        setDefaultAddressBtn = (TextView) findViewById(R.id.set_default);

        consigneeEditText = (EditText) findViewById(R.id.consignee);
        mobileEditText = (EditText) findViewById(R.id.mobile);
        telephoneEditText = (EditText) findViewById(R.id.telephone);
        areaEditText = (EditText) findViewById(R.id.area_text);
        deaddressEditText = (EditText) findViewById(R.id.detail_address);
        zipcodeEditText = (EditText) findViewById(R.id.zipcode);
        saveBtn = (Button) findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Address addressNew = new Address();
                    AppUser_id appUser_id = new AppUser_id();
                    appUser_id.setUserId(user.getUserId());
                    addressNew.setAppuserId(appUser_id);
                    addressNew.setSessionid(user.getSessionid());
                    addressNew.setConsignee(consigneeEditText.getText().toString());
                    addressNew.setMobile(mobileEditText.getText().toString());
                    addressNew.setTelephone(telephoneEditText.getText().toString());
                    addressNew.setArea(areaEditText.getText().toString());
                    addressNew.setDeaddress(deaddressEditText.getText().toString());
                    addressNew.setZipcode(zipcodeEditText.getText().toString());
                    addressNew.setLatitude(latitude);
                    addressNew.setLongitude(longitude);

                    if (addressNew.addValidation()) {
                        switch (action)
                        {
                            case StaticValues.ADDRESS_ACTION_NEW:
                                new AddAddressTask().execute(addressNew);
                                break;
                            case StaticValues.ADDRESS_ACTION_UPDATE:
                                addressNew.setAddressId(address.getAddressId());
                                new UpdateAddressTask().execute(addressNew);
                                break;
                        }

                    } else {
                        Toast.makeText(AddAddressActivity.this, addressNew.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        address = bundle.getParcelable("obj");
        action = bundle.getInt("action");
        if(address != null)
        {
            //修改
            setValue();
        }
        else
        {
            //新建
            locationClient = new AMapLocationClient(this.getApplicationContext());
            locationOption = new AMapLocationClientOption();
            // 设置定位模式为高精度模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationOption.setOnceLocation(true);
            locationOption.setInterval(10000);
            locationOption.setNeedAddress(true);

            //给定位客户端对象设置定位参数
            locationClient.setLocationOption(locationOption);

            // 设置定位监听
            locationClient.setLocationListener(this);

            // 启动定位
            locationClient.startLocation();

            //默认地址按钮隐藏
            setDefaultAddressBtn.setVisibility(View.GONE);

        }

        //设置默认地址
        setDefaultAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appContext.setDefaultAddress(address.getDeaddress());
                appContext.setDefaultAddressId(address.getAddressId());

                if(new RefAction(AddAddressActivity.this).setDefaultAddress(AddAddressActivity.this, address))
                {
                    Toast.makeText(AddAddressActivity.this, "默认地址已设置", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddAddressActivity.this, "默认地址设置失败", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setValue()
    {
        consigneeEditText.setText(address.getConsignee());
        mobileEditText.setText(address.getMobile());
        telephoneEditText.setText(address.getTelephone());
        areaEditText.setText(address.getArea());
        deaddressEditText.setText(address.getDeaddress());
        if(address.getZipcode() != null && !address.getZipcode().isEmpty())
        {
            zipcodeEditText.setText(address.getZipcode());
        }
        longitude = address.getLongitude();
        latitude = address.getLatitude();

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                /*
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getRoad();//街道信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                */
                areaEditText.setText(amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict());
//                deaddressEditText.setText(amapLocation.getAddress());

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }

    public class AddAddressTask extends AsyncTask<Address, Void, Address>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Address doInBackground(Address... address) {
            return new AddressAction(AddAddressActivity.this).addAddress(address[0]);
        }

        @Override
        protected void onPostExecute(Address result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AddAddressActivity.this.finish();

                }
                else
                {
                    Toast.makeText(AddAddressActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public class UpdateAddressTask extends AsyncTask<Address, Void, Address>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Address doInBackground(Address... address) {
            return new AddressAction(AddAddressActivity.this).updateAddress(address[0]);
        }

        @Override
        protected void onPostExecute(Address result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AddAddressActivity.this.finish();

                }
                else
                {
                    Toast.makeText(AddAddressActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
