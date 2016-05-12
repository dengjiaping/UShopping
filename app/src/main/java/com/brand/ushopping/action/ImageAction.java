package com.brand.ushopping.action;

import android.graphics.Bitmap;
import android.util.Log;

import com.brand.ushopping.model.Image;
import com.brand.ushopping.utils.BitmapTools;
import com.brand.ushopping.utils.HttpClientUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/10.
 */
public class ImageAction
{
    //图片上传 返回url
    public Image ImageUpload(Bitmap bitmap)
    {
        Image image = new Image();
        image.setUploadSuccess(false);
        String url = null;

        try {
            String resultString = HttpClientUtil.uploadImage(BitmapTools.zoomImg(bitmap));
            if (resultString != null) {
                Log.v("supai resultString", resultString);
                JSONObject jsonObject = new JSONObject(resultString);
                if(jsonObject.getBoolean("success"))
                {
                    //赋值
                    image.setUrl(jsonObject.getString("path"));
                    image.setUploadSuccess(jsonObject.getBoolean("success"));

                }
                image.setMsg(jsonObject.getString("msg"));

            }

        } catch (Exception e) {
            e.printStackTrace();
            image.setMsg("图片上传失败");

        }

        return image;
    }

}
