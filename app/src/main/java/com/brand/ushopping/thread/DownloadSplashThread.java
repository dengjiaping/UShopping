package com.brand.ushopping.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.brand.ushopping.action.ImageAction;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DownloadSplashThread extends Thread
{
    private String url;
    private Context context;

    public DownloadSplashThread(Context context, String url)
    {
        this.url = url;
        this.context = context;
    }
    @Override
    public void run() {
        super.run();

        byte[] data = null;
        try {
            data = new ImageAction(context).getImage(url);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(data != null)
        {
            Bitmap mBitmap = null;
            try {
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }catch (OutOfMemoryError e)
            {
                System.gc();
                System.runFinalization();
            }

            if(mBitmap != null)
            {
                try {
                    new ImageAction(context).saveFile(mBitmap, "splash.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return;
//            new ImageAction(MainActivity.this).downloadImg(url);
    }
}
