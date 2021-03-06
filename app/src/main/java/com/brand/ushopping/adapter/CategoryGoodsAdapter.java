package com.brand.ushopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.utils.StaticValues;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/23.
 */
public class CategoryGoodsAdapter extends RecyclerView.Adapter<CategoryGoodsAdapter.ViewHolder>
{
    private List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
    private Context context;
    private AppContext appContext;

    public CategoryGoodsAdapter(Context context, List<Map<String,Object>> list)
    {
        this.list = list;
        this.context = context;
        this.appContext = (AppContext) context.getApplicationContext();
    }

    @Override
    public CategoryGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item,viewGroup,false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goods_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategoryGoodsAdapter.ViewHolder holder, final int position) {
        if(list.get(position).get("img") != null)
        {
            holder.img.setImageURI(Uri.parse(list.get(position).get("img").toString()));
        }

        holder.name.setText(list.get(position).get("name").toString());
        holder.price.setText(list.get(position).get("price").toString());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", (Long) list.get(position).get("id"));
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
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
        public SimpleDraweeView img;
        public TextView name;
        public TextView price;

        public ViewHolder(View view){
            super(view);
            img = (SimpleDraweeView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);

        }
    }

}
