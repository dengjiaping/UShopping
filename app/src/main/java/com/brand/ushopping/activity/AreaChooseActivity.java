package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AreaAction;
import com.brand.ushopping.model.Area;
import com.brand.ushopping.model.AreaItem;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AreaChooseActivity extends AppCompatActivity {
    private AppContext appContext;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner countySpinner;
    private Spinner districtSpinner;
    private Button cancelBtn;
    private Button confirmBtn;
    private ImageView backBtn;
    private TextView titleTextView;
    private String areaName;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_choose);
        appContext = (AppContext) getApplicationContext();

        provinceSpinner = (Spinner) findViewById(R.id.province);
        citySpinner = (Spinner) findViewById(R.id.city);
        countySpinner = (Spinner) findViewById(R.id.county);
        districtSpinner = (Spinner) findViewById(R.id.district);
        cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CommonUtils.isValueEmpty(areaName))
                {
                    Intent intent = new Intent(AreaChooseActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("areaName", areaName);
                    if(!CommonUtils.isValueEmpty(cityName))
                    {
                        bundle.putString("cityName", cityName);
                    }
                    else
                    {
                        bundle.putString("cityName", areaName);
                    }
                    intent.putExtras(bundle);
                    appContext.setBundleObj(bundle);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(AreaChooseActivity.this, "请选择位置", Toast.LENGTH_SHORT).show();
                }

            }
        });
        confirmBtn.setEnabled(false);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());
//        areaNameTextView = (TextView) findViewById(R.id.area_name);

        citySpinner.setVisibility(View.GONE);
        countySpinner.setVisibility(View.GONE);
        districtSpinner.setVisibility(View.GONE);

        //加载省列表
        Area area = new Area();
        area.setType(StaticValues.AREA_TYPE_PROVINCE);
        new GetChildrenTask().execute(area);

    }

    public class GetChildrenTask extends AsyncTask<Area, Void, Area>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Area doInBackground(Area... areas) {
            return new AreaAction(AreaChooseActivity.this).getChildren(areas[0]);
        }

        @Override
        protected void onPostExecute(Area result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
                    final ArrayList<AreaItem> areaItems = result.getAreaItems();
                    for (AreaItem areaItem: areaItems)
                    {
                        if(areaItem.getId() != 0)
                        {
                            Map line = new HashMap();
                            line.put("id", areaItem.getId());
                            line.put("name", areaItem.getName());

                            listData.add(line);
                        }
                    }
                    SimpleAdapter adapter = new SimpleAdapter(AreaChooseActivity.this, listData, R.layout.spinner_item, new String[] {"name"}, new int[] {R.id.name});

                    switch (result.getType())
                    {
                        case StaticValues.AREA_TYPE_PROVINCE:
                            if(areaItems.size() > 0)
                            {
                                provinceSpinner.setVisibility(View.VISIBLE);
                            }
                            provinceSpinner.setAdapter(adapter);
                            provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    areaName = (String) ((Map) provinceSpinner.getItemAtPosition(i)).get("name");
                                    confirmBtn.setEnabled(true);
                                    //加载市列表
                                    long pid = (long) ((Map) provinceSpinner.getItemAtPosition(i)).get("id");
                                    Area area = new Area();
                                    area.setType(StaticValues.AREA_TYPE_CITY);
                                    area.setPid(pid);
                                    new GetChildrenTask().execute(area);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            citySpinner.removeAllViewsInLayout();
                            countySpinner.removeAllViewsInLayout();
                            districtSpinner.removeAllViewsInLayout();

                            break;
                        case StaticValues.AREA_TYPE_CITY:
                            if(areaItems.size() > 0)
                            {
                                citySpinner.setVisibility(View.VISIBLE);
                            }
                            citySpinner.setAdapter(adapter);
                            citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    areaName = (String) ((Map) citySpinner.getItemAtPosition(i)).get("name");
                                    cityName = areaName;
                                    confirmBtn.setEnabled(true);
                                    //加载县列表
                                    long pid = (long) ((Map) citySpinner.getItemAtPosition(i)).get("id");
                                    Area area = new Area();
                                    area.setType(StaticValues.AREA_TYPE_COUNTY);
                                    area.setPid(pid);
                                    new GetChildrenTask().execute(area);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            countySpinner.removeAllViewsInLayout();
                            districtSpinner.removeAllViewsInLayout();

                            break;
                        case StaticValues.AREA_TYPE_COUNTY:
                            if(areaItems.size() > 0)
                            {
                                countySpinner.setVisibility(View.VISIBLE);
                            }
                            countySpinner.setAdapter(adapter);
                            countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    areaName = (String) ((Map) countySpinner.getItemAtPosition(i)).get("name");
                                    confirmBtn.setEnabled(true);
                                    //加载地区列表
                                    long pid = (long) ((Map) countySpinner.getItemAtPosition(i)).get("id");
                                    Area area = new Area();
                                    area.setType(StaticValues.AREA_TYPE_DISTRICT);
                                    area.setPid(pid);
                                    new GetChildrenTask().execute(area);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            districtSpinner.removeAllViewsInLayout();

                            break;
                        case StaticValues.AREA_TYPE_DISTRICT:
                            if(areaItems.size() > 0)
                            {
                                districtSpinner.setVisibility(View.VISIBLE);
                            }
                            districtSpinner.setAdapter(adapter);
                            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    areaName = (String) ((Map) districtSpinner.getItemAtPosition(i)).get("name");
                                    confirmBtn.setEnabled(true);

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                            break;
                    }

                }
                else
                {

                }

            }
        }
    }

}
