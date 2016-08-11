package com.brand.ushopping.action;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.model.Area;
import com.brand.ushopping.model.AreaItem;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.URLConnectionUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/10.
 */
public class AreaAction extends BaseAction
{
    public AreaAction(Context context) {
        super(context);
    }

    public Area getChildren(Area area)
    {
        String resultString = null;
        area.addVersion(context);
        String jsonParam = JSON.toJSONString(area);
        try
        {
            resultString = URLConnectionUtil.post(CommonUtils.getAbsoluteUrl("GetChinaAction.action"), CommonUtils.generateParams(jsonParam));
            if(resultString != null)
            {
                JSONObject jsonObject = new JSONObject(resultString);
                area.setSuccess(jsonObject.getBoolean("success"));
                if(area.isSuccess())
                {
                    //赋值
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    ArrayList<AreaItem> areaItems = new ArrayList<AreaItem>();
                    for(int a=0; a<dataArray.length(); a++)
                    {
                        JSONObject dataObject = dataArray.getJSONObject(a);
                        String data = dataObject.toString();
                        areaItems.add(JSON.parseObject(data, AreaItem.class));

                    }
                    area.setAreaItems(areaItems);

                    area.setSuccess(true);
                }
                else
                {
                    area.setSuccess(false);
                    area.setMsg(jsonObject.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return area;
    }

}
