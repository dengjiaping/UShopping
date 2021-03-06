package com.brand.ushopping;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.AppBrandCollectItem;
import com.brand.ushopping.model.AppGoodsCollectItem;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.WeiboUser;
import com.brand.ushopping.thread.DownloadSplashThread;
import com.brand.ushopping.utils.UDBHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
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
    private String area;
    private String city;
    private Main main;
    private HomeRe homeRe;
    private DownloadManager dMgr;
    private long downloadId;
    private UDBHelper udbHelper;
    private double longitude;
    private double latitude;
    private String imie = "";
    private WeiboUser weiboUser = null;
    private String xgPushToken;
    //----Bundle参数,用于activity传值----
    private Bundle bundleObj = null;
    public DownloadSplashThread downloadSplashThread = null;
    private boolean thirdPartyLoginEnabled = false;
    private boolean lowMemory = false;
    private int networkType;

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

        lowerSersionCompacity();
        Fresco.initialize(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void lowerSersionCompacity()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //分享
            PlatformConfig.setWeixin("wx632d6c8a00776b9d", "0e141405d57f49123643fd771dacc039");
            PlatformConfig.setSinaWeibo("866304116", "cc642572970f33f26b656f7d59912208");
            PlatformConfig.setQQZone("1105140517", "xTKlL6zUVLeXgUgN");
            this.thirdPartyLoginEnabled = true;

            //bugly崩溃统计
            CrashReport.initCrashReport(getApplicationContext(), "900028246", false);
        }

    }


    //------------------新版app下载------------------
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
//                ParcelFileDescriptor pfd = dMgr.openDownloadedFile(downloadId);
                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = dMgr.getUriForDownloadedFile(downloadId);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(install);



            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    public void downloadApp(String url)
    {
        Log.v("ushopping", "download from: " + url);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url)
        );

        // 设置下载路径和文件名
        request.setTitle("升级包下载中...");
        // request.setDescription(“MeiLiShuo desc”); //设置下载中通知栏提示的介绍
        request.setDestinationInExternalPublicDir("download", "UShopping.apk");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        request.allowScanningByMediaScanner();    // 设置为可被媒体扫描器找到
        request.setVisibleInDownloadsUi(true);  // 设置为可见和可管理
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);

        downloadId = dMgr.enqueue(request);
    }

    //浏览器打开特定url
    public void openUrlinBrowser(Context context, String url)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
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

    public String getDeaddressFromId(long id)
    {
        String result  = null;

        if(addressList != null)
        {
            for(Address address: addressList)
            {
                if(id == address.getId())
                {
                    result = address.getDeaddress();
                }
            }
        }

        return result;
    }

    //低内存情况下的操作
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        lowMemory = true;

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

    public WeiboUser getWeiboUser() {
        return weiboUser;
    }

    public void setWeiboUser(WeiboUser weiboUser) {
        this.weiboUser = weiboUser;
    }

    public Bundle getBundleObj() {
        return bundleObj;
    }

    public void setBundleObj(Bundle bundleObj) {
        this.bundleObj = bundleObj;
    }

    public String getXgPushToken() {
        return xgPushToken;
    }

    public void setXgPushToken(String xgPushToken) {
        this.xgPushToken = xgPushToken;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isThirdPartyLoginEnabled() {
        return thirdPartyLoginEnabled;
    }

    public void setThirdPartyLoginEnabled(boolean thirdPartyLoginEnabled) {
        this.thirdPartyLoginEnabled = thirdPartyLoginEnabled;
    }

    public boolean isLowMemory() {
        return lowMemory;
    }

    public void setLowMemory(boolean lowMemory) {
        this.lowMemory = lowMemory;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }
}
