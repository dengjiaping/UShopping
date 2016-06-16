package com.brand.ushopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.brand.ushopping.activity.AddressesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/13.
 * 首页推荐
 */
public class RecommendAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private AddressesActivity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public RecommendAdapter(List<Map<String,Object>> list,AddressesActivity context){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    private int width;
    private int height;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
