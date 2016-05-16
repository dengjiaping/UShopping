package com.brand.ushopping.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.brand.ushopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/13.
 */
public class GoodsDetailAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public GoodsDetailAdapter(List<Map<String,Object>> list,Activity context){
        this.context=context;
        this.list=list;
        inflater = LayoutInflater.from(context);
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
//        return (Long)(list.get(position).get("id"));
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView =inflater.inflate(R.layout.goods_detail_item, null);

            holder.img = (ImageView)convertView.findViewById(R.id.img);
            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        if(list.get(position).get("img") != null)
        {

            final ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.setDefaultLoadingListener(new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    imageLoader.displayImage(list.get(position).get("img").toString(), holder.img);
//                    Toast.makeText(context, "image loading failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            imageLoader.displayImage(list.get(position).get("img").toString(), holder.img);
            /*
            ImageLoader.getInstance().displayImage(list.get(position).get("img").toString(), holder.img);
            */

        }

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width, height);

        return convertView;
    }

    static class ViewHolder {
        ImageView img;

    }
}
