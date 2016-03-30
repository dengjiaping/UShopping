package com.brand.ushopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.GoodsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MoreGoodsAdapter extends RecyclerView.Adapter<MoreGoodsAdapter.ViewHolder>  {
    private List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
    private Context context;

    public MoreGoodsAdapter(Context context, List<Map<String,Object>> list)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public MoreGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goods_item, viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MoreGoodsAdapter.ViewHolder holder, final int position) {
        if(list.get(position).get("img") != null)
        {
            ImageLoader.getInstance().displayImage(list.get(position).get("img").toString(), holder.img);
        }

        holder.name.setText(list.get(position).get("name").toString());
        holder.price.setText(list.get(position).get("price").toString());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", (Long) list.get(position).get("id"));
                bundle.putInt("boughtType", (int) list.get(position).get("boughtType"));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView name;
        public TextView price;

        public ViewHolder(View view){
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);

        }
    }

}
