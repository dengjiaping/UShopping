package com.brand.ushopping.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public class BrandGoodsRecyclerAdapter extends RecyclerView.Adapter<BrandGoodsRecyclerAdapter.ViewHolder>
{
    //填充数据的List
    private List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
    //用来导入布局
    private LayoutInflater inflater =null;
    //上下文
    private Activity context;

    //构造器
    public BrandGoodsRecyclerAdapter(List<Map<String, Object>> list, Activity context){
        this.context=context;
        this.list=list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_goods_item, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(list.get(position).get("logopic").toString(), holder.img);
        holder.name.setText(list.get(position).get("name").toString());
        holder.promotionPrice.setText(list.get(position).get("promotionPrice").toString());
        holder.originalPrice.setText(list.get(position).get("originalPrice").toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null){
                holder = new ViewHolder();
                convertView =inflater.inflate(R.layout.brand_goods_item, null);

                holder.img=(ImageView)convertView.findViewById(R.id.img);
                holder.name=(TextView)convertView.findViewById(R.id.name);
                holder.promotionPrice=(TextView)convertView.findViewById(R.id.promotion_price);
                holder.originalPrice=(TextView)convertView.findViewById(R.id.original_price);

                //为view设置标签
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(list.get(position).get("logopic").toString(), holder.img);
            holder.name.setText(list.get(position).get("name").toString());
            holder.promotionPrice.setText(list.get(position).get("promotionPrice").toString());
            holder.originalPrice.setText(list.get(position).get("originalPrice").toString());

            width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
            height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

            convertView.measure(width,height);

            height=convertView.getMeasuredHeight();
            width=convertView.getMeasuredWidth();

            return convertView;
        }
        */
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        TextView promotionPrice;
        TextView originalPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img);
            name=(TextView) itemView.findViewById(R.id.name);
            promotionPrice=(TextView) itemView.findViewById(R.id.promotion_price);
            originalPrice=(TextView) itemView.findViewById(R.id.original_price);

        }

    }

}
