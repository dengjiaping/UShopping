package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AddressAction;
import com.brand.ushopping.adapter.AddressAdapter;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressesActivity extends Activity {
    private AppContext appContext;
    private User user;
    private ArrayList<Address> addressList;

    private ImageView backBtn;
    private TextView titleTextView;
    private TextView addBtn;
    private ListView listView;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private AddressAdapter adapter;
    private List listData;

    private int enterMode = StaticValues.ADDRESSES_ENTER_MODE_EDIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_addresses);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());
        addBtn = (TextView) findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressesActivity.this, AddAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("obj", null);
                bundle.putInt("action", StaticValues.ADDRESS_ACTION_NEW);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.address_list);

        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);

    }

    @Override
    protected void onStart() {
        super.onStart();

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        enterMode = getIntent().getExtras().getInt("enterMode", StaticValues.ADDRESSES_ENTER_MODE_EDIT);

        switch (enterMode)
        {
            case StaticValues.ADDRESSES_ENTER_MODE_EDIT:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Address address;
                        for (int a = 0; a < addressList.size(); a++) {
                            address = addressList.get(a);
                            if (address.getAddressId() == id) {
                                Intent intent = new Intent(AddressesActivity.this, AddAddressActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("obj", address);
                                bundle.putInt("enterMode", enterMode);
                                bundle.putInt("action", StaticValues.ADDRESS_ACTION_UPDATE);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        }
                    }
                });

                break;
            case StaticValues.ADDRESSES_ENTER_MODE_PICK:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Address address;
                        for (int a = 0; a < addressList.size(); a++) {
                            address = addressList.get(a);
                            if (address.getAddressId() == id) {
                                Intent intent = new Intent(AddressesActivity.this, OrderConfirmActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putLong("addressId", address.getAddressId());
                                bundle.putString("deaddress", address.getDeaddress());
                                intent.putExtras(bundle);

                                setResult(Activity.RESULT_OK, intent);
                                finish();

                            }
                        }
                    }
                });

                break;

        }


        reloadList();
    }

    public void reloadList()
    {
        //没有数据时的提示
        warningLayout.setVisibility(View.GONE);

        Address address = new Address();
        if(user != null)
        {
            address.setUserId(user.getUserId());
            address.setSessionid(user.getSessionid());
            new AddressListTask().execute(address);
        }
    }

    //地址列表
    public class AddressListTask extends AsyncTask<Address, Void, ArrayList<Address>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Address> doInBackground(Address... address) {
            return new AddressAction().GetAppAddressAllAction(address[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Address> result) {
            if(result != null)
            {
                if(result.size() > 0)
                {
                    addressList = result;
                    //UI更新
                    listData = new ArrayList<Map<String,Object>>();
                    for(Address address: result)
                    {
                        Map line = new HashMap();

                        line.put("id", address.getAddressId());
                        line.put("consignee", address.getConsignee());
                        line.put("mobile", address.getMobile());
                        line.put("area", address.getArea());
                        line.put("deaddress", address.getDeaddress());
                        line.put("zipcode", address.getZipcode());

                        line.put("userId", user.getUserId());
                        line.put("sessionid", user.getSessionid());

                        listData.add(line);
                    }

                    adapter = new AddressAdapter(listData, AddressesActivity.this);
                    listView.setAdapter(adapter);

                    appContext.setAddressList(addressList);
                }
                else
                {
                    warningLayout.setVisibility(View.VISIBLE);
                    warningTextView.setText("地址列表为空,如果您添加过地址,请重新登录账户");

                }

            }
            else
            {
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("地址列表为空,如果您添加过地址,请重新登录账户");

            }
        }
    }

}
