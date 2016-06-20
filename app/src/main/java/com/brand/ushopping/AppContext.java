package com.brand.ushopping;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.AppBrandCollectItem;
import com.brand.ushopping.model.AppGoodsCollectItem;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.UDBHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/2.
 */
public class AppContext  extends Application
{
    private User user;
    private String sessionid;
    private int screenWidth;
    private int screenHeight;
    private ArrayList<Address> addressList;
    private String defaultAddress;
    private long defaultAddressId;
    private String city;
    private Main main;
    private HomeRe homeRe;
    private DownloadManager dMgr;
    private long downloadId;
    private boolean neetworkEnable;
    private UDBHelper udbHelper;
    private double longitude;
    private double latitude;
    private String imie;

    @Override
    public void onCreate() {
        super.onCreate();

        //Image loader设置
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.logo) // resource or drawable
                .showImageForEmptyUri(R.drawable.logo) // resource or drawable
                .showImageOnFail(R.drawable.logo) // resource or drawable
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        File cacheDir = StorageUtils.getOwnCacheDirectory(this,
                "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(displayImageOptions)
                .memoryCacheExtraOptions(320, 240) // default = device screen dimensions
                .diskCacheExtraOptions(320, 240, null)
                .threadPoolSize(5) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new FIFOLimitedMemoryCache(16 * 1024 * 1024))
                .memoryCacheSize(16 * 1024 * 1024)
                .memoryCacheSizePercentage(40) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 8 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .diskCacheFileCount(1024)
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        dMgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        //数据库对象
        udbHelper = new UDBHelper(getApplicationContext(), "ushopping");

        //分享
        PlatformConfig.setWeixin("wx632d6c8a00776b9d", "0e141405d57f49123643fd771dacc039");
        PlatformConfig.setSinaWeibo("866304116", "f233c114205bd034797fd4904553d71e");
        PlatformConfig.setQQZone("1105140517", "xTKlL6zUVLeXgUgN");

        //bugly崩溃统计
        CrashReport.initCrashReport(getApplicationContext(), "900028246", false);

    }

    //------------------新版app下载------------------
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                ParcelFileDescriptor pfd = dMgr.openDownloadedFile(downloadId);


            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    public void downloadApp(String url)
    {
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url)
        );
        request.setTitle("升级包下载中...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);

        downloadId = dMgr.enqueue(request);

    }

    //收藏
    private ArrayList<AppGoodsCollectItem> appGoodsCollectItems;
    private ArrayList<AppBrandCollectItem> appBrandCollectItems;

    //我的足迹
    private ArrayList<AppgoodsId> goodsViewHistory;

    public void addGoodsViewHistory(AppgoodsId appgoodsId)
    {
        if(goodsViewHistory == null)
        {
            goodsViewHistory = new ArrayList<AppgoodsId>();

        }
        goodsViewHistory.add(appgoodsId);
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public long getDefaultAddressId() {
        return defaultAddressId;
    }

    public void setDefaultAddressId(long defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    public ArrayList<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public HomeRe getHomeRe() {
        return homeRe;
    }

    public void setHomeRe(HomeRe homeRe) {
        this.homeRe = homeRe;
    }

    public boolean isNeetworkEnable() {
        return neetworkEnable;
    }

    public void setNeetworkEnable(boolean neetworkEnable) {
        this.neetworkEnable = neetworkEnable;
    }

    public ArrayList<AppGoodsCollectItem> getAppGoodsCollectItems() {
        return appGoodsCollectItems;
    }

    public void setAppGoodsCollectItems(ArrayList<AppGoodsCollectItem> appGoodsCollectItems) {
        this.appGoodsCollectItems = appGoodsCollectItems;
    }

    public ArrayList<AppBrandCollectItem> getAppBrandCollectItems() {
        return appBrandCollectItems;
    }

    public void setAppBrandCollectItems(ArrayList<AppBrandCollectItem> appBrandCollectItems) {
        this.appBrandCollectItems = appBrandCollectItems;
    }

    public ArrayList<AppgoodsId> getGoodsViewHistory() {
        return goodsViewHistory;
    }

    public void setGoodsViewHistory(ArrayList<AppgoodsId> goodsViewHistory) {
        this.goodsViewHistory = goodsViewHistory;
    }

    public UDBHelper getUdbHelper() {
        return udbHelper;
    }

    public void setUdbHelper(UDBHelper udbHelper) {
        this.udbHelper = udbHelper;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }
}
