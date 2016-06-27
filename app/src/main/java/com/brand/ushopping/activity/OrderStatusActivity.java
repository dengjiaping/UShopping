package com.brand.ushopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;

public class OrderStatusActivity extends AppCompatActivity {
    private AppContext appContext;
    private ListView orderStatusListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        appContext = (AppContext) getApplicationContext();
        orderStatusListView = (ListView) findViewById(R.id.status_list);

    }
}
