package com.brand.ushopping.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.Recommend;
import com.brand.ushopping.utils.StaticValues;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/1/6.
 */
public class RecommendItem extends RelativeLayout {
    private Recommend recommend;
    private SimpleDraweeView imgView;
    private TextView introTextView;
    private AppContext appContext;

    public RecommendItem(final Context context, AttributeSet attrs, final Recommend recommend) {
        super(context, attrs);
        appContext = (AppContext) context.getApplicationContext();
        this.recommend = recommend;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recommend_item, this, true);

        imgView = (SimpleDraweeView) view.findViewById(R.id.img);
        introTextView = (TextView) view.findViewById(R.id.intro);

        imgView.setImageURI(Uri.parse(recommend.getImg()));

        introTextView.setText(recommend.getIntro());

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppgoodsId appgoodsId = recommend.getAppgoodsId();
                if(appgoodsId != null)
                {
                    Intent intent = new Intent(context, GoodsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("goodsId", appgoodsId.getId());
                    bundle.putLong("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                    intent.putExtras(bundle);
                    appContext.setBundleObj(bundle);
                    context.startActivity(intent);

                }
            }
        });

    }
}
