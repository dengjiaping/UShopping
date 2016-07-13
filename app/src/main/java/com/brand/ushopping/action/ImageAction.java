package com.brand.ushopping.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.brand.ushopping.model.Image;
import com.brand.ushopping.utils.BitmapTools;
import com.brand.ushopping.utils.HttpClientUtil;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/10.
 */
public class ImageAction extends BaseAction
{
    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/com.brand.ushopping/";

    public ImageAction(Context context) {
        super(context);
    }

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

    //下载图片到sd卡
//    public Bitmap downloadImg(String url) throws Exception
//    {
//        String filePath = "http://img.my.csdn.net/uploads/201402/24/1393242467_3999.jpg";
//        String mFileName = "test.jpg";
//
//        byte[] data = getImage(filePath);
//        if(data!=null){
//            Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
//        }else{
//
//        }
//
//    }

    //根据url获取图片
    public byte[] getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return readStream(inStream);
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存文件
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 检查文件是否存在
     */
    public boolean checkFileExists(String fileName) throws IOException {
        boolean result = false;

        File dirFile = new File(ALBUM_PATH + fileName);
        if(dirFile.exists()){
            result = true;
        }

        return result;
    }

}
