package com.brand.ushopping.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.Recommend;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/1/6.
 */
public class RecommendItem extends RelativeLayout {
    private Recommend recommend;
    private ImageView imgView;
    private TextView introTextView;

    public RecommendItem(final Context context, AttributeSet attrs, final Recommend recommend) {
        super(context, attrs);

        this.recommend = recommend;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recommend_item, this, true);

        imgView = (ImageView) view.findViewById(R.id.img);
        introTextView = (TextView) view.findViewById(R.id.intro);

        ImageLoader.getInstance().displayImage(recommend.getImg(), imgView);
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

                    context.startActivity(intent);

                }
            }
        });

    }
}
