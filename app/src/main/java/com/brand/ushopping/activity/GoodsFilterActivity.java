package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.model.Category;
import com.brand.ushopping.model.FilterParams;
import com.brand.ushopping.model.User;
import com.brand.ushopping.widget.FilterCategoryItemView;

import java.util.ArrayList;

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
        "2016", "2015", "2014", "2013", "2012"
    };
    private static final String [] seasonData = {
        "春", "夏", "秋", "冬"
    };
    private String yearSelected = null;
    private String seasonSelected = null;
    private Long appCategoryIdSelected = null;//类型ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_filter);

        appContext = (AppContext) getApplicationContext();

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
                if(yearSelected == null)
                {
                    Toast.makeText(GoodsFilterActivity.this, "请选择年份", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(seasonSelected == null)
                {
                    Toast.makeText(GoodsFilterActivity.this, "请选择季节", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(priceMinEditText.getText() == null || priceMaxEditText.getText() == null)
                {
                    Toast.makeText(GoodsFilterActivity.this, "请选择价格区间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(appCategoryIdSelected == null)
                {
                    Toast.makeText(GoodsFilterActivity.this, "请选择类别", Toast.LENGTH_SHORT).show();
                    return;
                }

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
            }
        });

        cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        categoryGridView = (GridLayout) findViewById(R.id.category_grid);

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

}
