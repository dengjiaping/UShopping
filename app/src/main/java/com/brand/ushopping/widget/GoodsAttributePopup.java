package com.brand.ushopping.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.GoodsInfo;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/20.
 */
public class GoodsAttributePopup extends PopupWindow
{
    private Context context;
    private GoodsActivity activity;
    private GoodsInfo goodsInfo;

    private ImageView imgView;
    private TextView priceTextView;
    private TextView storageTextView;
    private TextView sizeSelectedTextView;
    private TextView colorSelectedTextView;
    private ImageView closeBtn;
    private EditText countEditText;

    private RadioGroup sizeLayout;
    private RadioGroup colorLayout;

    private ImageView increaseBtn;
    private ImageView decreaseBtn;
    private Button confirmBtn;

    private int count = 1;
    private int type = StaticValues.callAttributePopupNormal;

    HashMap<String ,String[]> attributes;

    public GoodsAttributePopup(final Context context, GoodsInfo goodsInfo, String sizeSelected, final String colorSelected, final int type)
    {
        super(context);

        this.context = context;
        this.activity = (GoodsActivity) context;
        this.goodsInfo = goodsInfo;
        this.type = type;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.goods_attribute, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        sizeLayout = (RadioGroup) view.findViewById(R.id.size_layout);
        colorLayout = (RadioGroup) view.findViewById(R.id.color_layout);
        imgView = (ImageView) view.findViewById(R.id.img);
        priceTextView = (TextView) view.findViewById(R.id.price);
        storageTextView = (TextView) view.findViewById(R.id.storage);
        sizeSelectedTextView = (TextView) view.findViewById(R.id.size);
        colorSelectedTextView = (TextView) view.findViewById(R.id.color);
        closeBtn = (ImageView) view.findViewById(R.id.close);
        countEditText = (EditText) view.findViewById(R.id.count);
        increaseBtn = (ImageView) view.findViewById(R.id.increase);
        decreaseBtn = (ImageView) view.findViewById(R.id.decrease);
        confirmBtn = (Button) view.findViewById(R.id.confirm);

        Goods goods = goodsInfo.getGoods();

        ImageLoader.getInstance().displayImage(goods.getLogopicUrl(), imgView);

        priceTextView.setText(CommonUtils.df.format(goods.getPromotionPrice()));

        attributes = goodsInfo.getAttribute();

        //排序
        List<Map.Entry<String,String[]>> list=new ArrayList();
        list.addAll(attributes.entrySet());
        Collections.sort(list, new SizeComparator());


        Iterator iterator = list.iterator();
        while(iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            final String key = (String) entry.getKey();

            final RadioButton sizeBtn = new RadioButton(context);
            sizeBtn.setText(key);
            sizeBtn.setBackgroundColor(view.getResources().getColor(R.color.bg_grey));
//            sizeBtn.setBackground(view.getResources().getDrawable(R.drawable.rounded_squre_grey));
            ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(180, 75);
            sizeBtn.setLayoutParams(para);
            sizeBtn.setPadding(8, 8, 8, 8);
            sizeBtn.setGravity(Gravity.CENTER);
            sizeBtn.setButtonDrawable(R.drawable.blank);
            sizeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sizeBtn.setBackgroundColor(view.getResources().getColor(R.color.buttonl_bg_green));
//                        sizeBtn.setBackground(view.getResources().getDrawable(R.drawable.rounded_squre_green));
                    } else {
                        sizeBtn.setBackgroundColor(view.getResources().getColor(R.color.bg_grey));
//                        sizeBtn.setBackground(view.getResources().getDrawable(R.drawable.rounded_squre_grey));
                    }

                }
            });
            sizeBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            if(key.equals(sizeSelected))
            {
                sizeBtn.setSelected(true);
            }

            sizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sizeSelectedTextView.setText(key);
                    activity.setSizeSelected(key);

                    colorLayout.removeAllViewsInLayout();
                    String[] values = attributes.get(key);
                    for (int a = 0; a < values.length; a++) {
                        final String value = values[a];
                        final RadioButton button = new RadioButton(context);
                        button.setText(value);
                        button.setBackgroundColor(view.getResources().getColor(R.color.bg_grey));
//                        button.setBackground(view.getResources().getDrawable(R.drawable.rounded_squre_grey));
                        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(180, 75);

                        button.setLayoutParams(para);
                        button.setPadding(8, 8, 8, 8);
                        button.setButtonDrawable(R.drawable.blank);
                        button.setGravity(Gravity.CENTER);
                        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    button.setBackgroundColor(view.getResources().getColor(R.color.buttonl_bg_green));
//                                    button.setBackground(view.getResources().getDrawable(R.drawable.rounded_squre_green));
                                } else {
                                    button.setBackgroundColor(view.getResources().getColor(R.color.bg_grey));
//                                    button.setBackground(view.getResources().getDrawable(R.drawable.rounded_squre_grey));
                                }

                            }
                        });
                        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                        if (value.equals((colorSelected))) {
                            button.setChecked(true);
                        }

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                colorSelectedTextView.setText(value);
                                activity.setColorSelected(value);

                            }
                        });

                        colorLayout.addView(button);

                        Space space = new Space(context);
                        para = new ViewGroup.LayoutParams(18, 60);
                        space.setLayoutParams(para);
                        colorLayout.addView(space);

                    }

                }
            });
            sizeLayout.addView(sizeBtn);

            Space space = new Space(context);
            para = new ViewGroup.LayoutParams(18, 60);
            space.setLayoutParams(para);
            sizeLayout.addView(space);

            if(sizeSelected != null && colorSelected != null)
            {
                sizeSelectedTextView.setText(sizeSelected);
                colorSelectedTextView.setText(colorSelected);
            }

            Object val = entry.getValue();

        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsAttributePopup.this.dismiss();

            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 1;
                countEditText.setText(Integer.toString(count));
                activity.setCount(count);

            }
        });
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count > 1)
                {
                    count -= 1;
                    countEditText.setText(Integer.toString(count));
                    activity.setCount(count);

                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.setColorSelected();
//                activity.setSizeSelected();
                activity.setCount(count);

                switch (type)
                {
                    case StaticValues.callAttributePopupNormal:
                        break;
                    case StaticValues.callAttributePopupAddToCart:
                        activity.addToCart();
                        break;
                    case StaticValues.callAttributePopupBuy:
                        activity.buyit();
                        break;

                }

                GoodsAttributePopup.this.dismiss();
            }
        });

        this.setContentView(view);
    }

    private class SizeComparator implements Comparator<Map.Entry<String, String[]>>
    {
        public int compare(Map.Entry<String, String[]> mp1, Map.Entry<String, String[]> mp2)
        {
            return mp1.getKey().compareTo(mp2.getKey());
        }
    }
}
