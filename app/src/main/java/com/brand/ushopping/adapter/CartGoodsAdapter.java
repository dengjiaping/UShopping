package com.brand.ushopping.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.fragment.CartFragment;
import com.brand.ushopping.model.AppShopcart;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/9.
 */
public class CartGoodsAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;
    private AppContext appContext;
    private CartFragment cartFragment;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public CartGoodsAdapter(List<Map<String,Object>> list,Activity context, CartFragment cartFragment){
        this.context=context;
        this.appContext = (AppContext) context.getApplicationContext();
        this.cartFragment = cartFragment;
        this.list=list;
        inflater=LayoutInflater.from(context);

    }

    private int width;
    private int height;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (Long)(list.get(position).get("id"));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.cart_goods_item, null);

            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.attribute = (TextView) convertView.findViewById(R.id.attribute);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.originalPrice = (TextView) convertView.findViewById(R.id.original_price);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.checked = (CheckBox) convertView.findViewById(R.id.checked);
            holder.remove = (TextView) convertView.findViewById(R.id.remove);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(list.get(position).get("logopic").toString(), holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", (Long) list.get(position).get("goodsId"));
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                context.startActivity(intent);
            }
        });

        holder.name.setText(list.get(position).get("name").toString());
        holder.attribute.setText(list.get(position).get("attribute").toString());
        holder.price.setText(list.get(position).get("price").toString());
        holder.originalPrice.setText(list.get(position).get("originalPrice").toString());
        holder.count.setText(list.get(position).get("count").toString());

        holder.checked.setChecked((Boolean) list.get(position).get("checked"));
        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cartFragment.addCartItem((AppShopcart) list.get(position).get("obj"));

                } else {
                    cartFragment.removeCartItem((Long) list.get(position).get("id"));

                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否从购物车中删除该商品?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cartFragment.deleteItem((Long) list.get(position).get("id"));

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }

    static class ViewHolder {
        CheckBox checked;
        ImageView img;
        TextView name;
        TextView attribute;
        TextView price;
        TextView originalPrice;
        TextView count;
        TextView remove;

    }

}
