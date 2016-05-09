package com.brand.ushopping.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.BrandAction;
import com.brand.ushopping.action.CartAction;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.adapter.GoodsCommentAdapter;
import com.brand.ushopping.model.AddAppShopcart;
import com.brand.ushopping.model.AppEvaluate;
import com.brand.ushopping.model.AppEvaluateItem;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppexpressId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.Brand;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.GoodsInfo;
import com.brand.ushopping.model.SaveAppBrandCollect;
import com.brand.ushopping.model.SaveAppGoodsCollect;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.GoodsAttributePopup;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoodsActivity extends UActivity {
    private TabHost tabHost;
    private AppContext appContext;
    private User user;
    private long goodsId;
    private GoodsInfo goodsInfo;
    private Goods goods;
    private View rootView;

    private SliderLayout slider;
    private TextView goodsNameTextView;
    private TextView priceTextView;
    private TextView priceOriginalTextView;
    private TextView appExpressPriceTextView;
    private ImageView brandLogoView;
    private TextView brandNameView;
//    private TextView brandDetailView;
    private Button brandEnterBtn;
    private Button brandFavouriteBtn;

    private View attributesBtn;
    private Button addToCartBtn;
    private Button buyitBtn;
    private ViewGroup rewardBtn;
    private ViewGroup voucherBtn;
    private ViewGroup customerServiceBtn;
    private ViewGroup enterStoreBtn;
    private ImageView backBtn;
    private ListView commentListView;

    private String sizeSelected = null;
    private String colorSelected = null;
    private int count = 1;

    private ViewGroup addFavouriteBtn;
    private ImageView favouriteBtn;
    private GoodsAttributePopup goodsAttributePopup;

    private TextView infoPageBtn;
    private TextView detailPageBtn;
    private TextView commentPageBtn;
    private ImageView infoIdc;
    private ImageView detailIdc;
    private ImageView commentIdc;
    private ScrollView infoView;
    private ScrollView detailView;
    private ViewGroup commentView;
    private WebView detailWebView;
    private ViewGroup goodsIntroBtn;
    private ViewGroup goodsIntroContainerView;
    private TextView goodsIntroTextView;
    private boolean introVisible = false;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ImageView cartBtn;

    private int boughtType = StaticValues.BOUTHT_TYPE_NORMAL;
    private int goodsViewPage = StaticValues.GOODS_VIEW_PAGE_INFO;

    private GoodsCommentAdapter goodsCommentAdapter;

    private TimeoutbleProgressDialog addToCartDialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goods);
        appContext = (AppContext) getApplicationContext();

        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);

        rootView = findViewById(R.id.root_view);

        goodsNameTextView = (TextView) findViewById(R.id.goods_name);
        priceTextView = (TextView) findViewById(R.id.price);
        priceOriginalTextView = (TextView) findViewById(R.id.original_price);
        appExpressPriceTextView = (TextView) findViewById(R.id.app_express);

        slider = (SliderLayout) findViewById(R.id.slider);

        brandLogoView = (ImageView) findViewById(R.id.brand_logo);
        brandNameView = (TextView) findViewById(R.id.brand_name);
//        brandDetailView = (TextView) findViewById(R.id.brand_detail);
        brandEnterBtn = (Button) findViewById(R.id.brand_enter);
        brandFavouriteBtn = (Button) findViewById(R.id.brand_favourite);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsActivity.this.finish();
            }
        });

        goodsIntroBtn = (ViewGroup) findViewById(R.id.goods_intro_btn);
        goodsIntroContainerView = (ViewGroup) findViewById(R.id.goods_intro_container);
        goodsIntroTextView = (TextView) findViewById(R.id.goods_intro);

        attributesBtn = findViewById(R.id.attributes);
        attributesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAttributePopup(StaticValues.callAttributePopupNormal);
                /*
                if(goodsAttributePopup == null)
                {
                    goodsAttributePopup = new GoodsAttributePopup(GoodsActivity.this, goodsInfo, sizeSelected, colorSelected);
                }
                goodsAttributePopup.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                */
            }
        });

        addToCartDialog = TimeoutbleProgressDialog.createProgressDialog(GoodsActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                addToCartDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
                builder.setMessage("添加到购物车失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });

        addToCartBtn = (Button) findViewById(R.id.add_to_cart);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
//                    Toast.makeText(GoodsActivity.this, "请先登录或注册", Toast.LENGTH_SHORT).show();
                    loginOrRegister();
                }
                else
                {
                    if(orderValidate())
                    {
                        addToCart();

                    }
                    else
                    {
                        callAttributePopup(StaticValues.callAttributePopupAddToCart);

                    }

                }

            }
        });

        buyitBtn = (Button) findViewById(R.id.buy_it);
        buyitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    loginOrRegister();
                }
                else
                {
                    //立即购买
                    if(orderValidate())
                    {
                        buyit();

                    }
                    else
                    {
                        callAttributePopup(StaticValues.callAttributePopupBuy);

                    }

                }

            }
        });

        rewardBtn = (ViewGroup) findViewById(R.id.reward);
        rewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
//                    Toast.makeText(GoodsActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();
                    loginOrRegister();

                } else {
                    Intent intent = new Intent(GoodsActivity.this, UserRewardActivity.class);
                    startActivity(intent);
                }

            }
        });

        voucherBtn = (ViewGroup) findViewById(R.id.voucher);
        voucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
//                    Toast.makeText(GoodsActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();
                    loginOrRegister();

                } else {
                    Intent intent = new Intent(GoodsActivity.this, VoucherActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("enterType", StaticValues.VOUCHER_ENTER_MINE);
                    intent.putExtras(bundle);

                    startActivity(intent);

                }

            }
        });

        cartBtn = (ImageView) findViewById(R.id.cart);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("initTab", StaticValues.MAIN_ACTIVITY_TAB_CART);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        ViewGroup.LayoutParams params = slider.getLayoutParams();
        params.height = appContext.getScreenWidth();
        slider.setLayoutParams(params);

        Bundle bundle = getIntent().getExtras();
        goodsId = bundle.getLong("goodsId", 0);
        if(goodsId == 0)
        {
            GoodsActivity.this.finish();
        }

        if(bundle.getInt("boughtType") != 0)
        {
            boughtType = bundle.getInt("boughtType");
        }

        addFavouriteBtn = (ViewGroup) findViewById(R.id.add_favourite);
        favouriteBtn = (ImageView) findViewById(R.id.favourite);

        infoPageBtn = (TextView) findViewById(R.id.info);
        infoPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsViewPage = StaticValues.GOODS_VIEW_PAGE_INFO;
                hideAllPages();
                infoView.setVisibility(View.VISIBLE);
            }
        });
        detailPageBtn = (TextView) findViewById(R.id.detail);
        detailPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsViewPage = StaticValues.GOODS_VIEW_PAGE_DETAIL;
                hideAllPages();
                detailView.setVisibility(View.VISIBLE);
            }
        });
        commentPageBtn = (TextView) findViewById(R.id.comment);
        commentPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsViewPage = StaticValues.GOODS_VIEW_PAGE_COMMENT;
                hideAllPages();
                commentView.setVisibility(View.VISIBLE);
            }
        });

        infoIdc = (ImageView) findViewById(R.id.info_idc);
        detailIdc = (ImageView) findViewById(R.id.detail_idc);
        commentIdc = (ImageView) findViewById(R.id.comment_idc);

        infoView = (ScrollView) findViewById(R.id.goods_info);
        detailView = (ScrollView) findViewById(R.id.goods_detail);
        commentView = (ViewGroup) findViewById(R.id.goods_comment);

        hideAllPages();
        infoView.setVisibility(View.VISIBLE);

        customerServiceBtn = (ViewGroup) findViewById(R.id.customer_service);
        customerServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GoodsActivity.this, "尚未添加客服", Toast.LENGTH_SHORT).show();
            }
        });

        enterStoreBtn = (ViewGroup) findViewById(R.id.enter_store);
        enterStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterStore();

            }
        });

        detailWebView = (WebView) findViewById(R.id.detail_view);

        goodsIntroContainerView.setVisibility(View.GONE);
        goodsIntroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (introVisible) {
                    goodsIntroContainerView.setVisibility(View.GONE);
                    introVisible = false;
                } else {
                    goodsIntroContainerView.setVisibility(View.VISIBLE);
                    introVisible = true;
                }
            }
        });

        commentListView = (ListView) findViewById(R.id.comment_list);

        //没有数据时的提示
        warningLayout.setVisibility(View.GONE);

        switch (boughtType)
        {
            case StaticValues.BOUTHT_TYPE_NORMAL:
                buyitBtn.setText("立即购买");

                break;

            case StaticValues.BOUTHT_TYPE_RESERVATION:
                buyitBtn.setText("到店预订");

                break;
            case StaticValues.BOUTHT_TYPE_TRYIT:
                buyitBtn.setText("上门试衣");

                break;
        }

    }



    //立即购买
    public void buyit()
    {
        //检查属性是否选择
        if(CommonUtils.isValueEmpty(colorSelected))
        {
            Toast.makeText(GoodsActivity.this, "请选择颜色", Toast.LENGTH_SHORT).show();
            return;
        }

        if(CommonUtils.isValueEmpty(sizeSelected))
        {
            Toast.makeText(GoodsActivity.this, "请选择尺码", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();

        Goods goods = goodsInfo.getGoods();
        goods.setAttribute("颜色:"+colorSelected+" 尺码:"+sizeSelected);
        goods.setCount(count);

        ArrayList<Goods> goodsList = new ArrayList<Goods>();
        goodsList.add(goods);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("goods", goodsList);
        bundle.putInt("boughtType", boughtType);

        switch (boughtType)
        {
            case StaticValues.BOUTHT_TYPE_NORMAL:

                bundle.putLong("reservationDate", 0);
                intent.setClass(GoodsActivity.this, OrderConfirmActivity.class);

                break;

            case StaticValues.BOUTHT_TYPE_RESERVATION:
            case StaticValues.BOUTHT_TYPE_TRYIT:
                intent.setClass(GoodsActivity.this, SelectDateActivity.class);

                break;
        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

    //加入购物车
    public void addToCart()
    {
        //检查属性是否选择
        if(CommonUtils.isValueEmpty(colorSelected))
        {
            Toast.makeText(GoodsActivity.this, "请选择颜色", Toast.LENGTH_SHORT).show();
            return;
        }

        if(CommonUtils.isValueEmpty(sizeSelected))
        {
            Toast.makeText(GoodsActivity.this, "请选择尺码", Toast.LENGTH_SHORT).show();
            return;
        }

        AddAppShopcart addAppShopcart = new AddAppShopcart();
        AppuserId appuserId = new AppuserId();
        appuserId.setUserId(user.getUserId());
        addAppShopcart.setAppuserId(appuserId);
        AppgoodsId appgoodsId = new AppgoodsId();
        appgoodsId.setId(goodsId);
        addAppShopcart.setUserId(user.getUserId());
        addAppShopcart.setSessionid(user.getSessionid());

        addAppShopcart.setPrice(goods.getPromotionPrice());
        addAppShopcart.setQuantity(count);

        String attributes = "颜色分类:"+colorSelected+";尺码:"+sizeSelected;
        addAppShopcart.setAttribute(attributes);
        addAppShopcart.setAppgoodsId(appgoodsId);
        new AddAppShopcartActionTask().execute(addAppShopcart);

    }

    private void callAttributePopup(int type)
    {
        if(goodsAttributePopup == null)
        {
            goodsAttributePopup = new GoodsAttributePopup(GoodsActivity.this, goodsInfo, sizeSelected, colorSelected, type);
        }
        goodsAttributePopup.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void hideAllPages()
    {
        infoView.setVisibility(View.GONE);
        detailView.setVisibility(View.GONE);
        commentView.setVisibility(View.GONE);

        infoIdc.setVisibility(View.INVISIBLE);
        detailIdc.setVisibility(View.INVISIBLE);
        commentIdc.setVisibility(View.INVISIBLE);

        switch (goodsViewPage)
        {
            case StaticValues.GOODS_VIEW_PAGE_INFO:
                infoIdc.setVisibility(View.VISIBLE);
                break;
            case StaticValues.GOODS_VIEW_PAGE_DETAIL:
                detailIdc.setVisibility(View.VISIBLE);
                break;
            case StaticValues.GOODS_VIEW_PAGE_COMMENT:
                commentIdc.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = appContext.getUser();

        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setGoodsId(goodsId);

        if(user != null)
        {
            goodsInfo.setUserId(user.getUserId());
            goodsInfo.setSessionid(user.getSessionid());

        }
        new GetAppGoodsIdTask().execute(goodsInfo);

        AppEvaluate appEvaluate = new AppEvaluate();
        if(user != null)
        {
            appEvaluate.setUserId(user.getUserId());
            appEvaluate.setSessionid(user.getSessionid());
        }
        appEvaluate.setGoodsId(goodsId);

        new GetAppEvaluateTask().execute(appEvaluate);

    }

    //获取商品详情
    public class GetAppGoodsIdTask extends AsyncTask<GoodsInfo, Void, GoodsInfo>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GoodsInfo doInBackground(GoodsInfo... goodsInfos) {
            return new GoodsAction().getAppGoodsIdAction(goodsInfos[0]);
        }

        @Override
        protected void onPostExecute(GoodsInfo result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    goodsInfo = result;
                    goods = result.getGoods();
                    goodsNameTextView.setText(goods.getGoodsName());
                    priceTextView.setText(CommonUtils.df.format(goods.getPromotionPrice()));
                    priceOriginalTextView.setText(Double.toString(goods.getOriginalPrice()));
                    AppexpressId appexpressId = goods.getAppexpressId();
//                    appExpressPriceTextView.setText(Double.toString(appexpressId.getPrice()));
                    goodsIntroTextView.setText(goods.getGoodsIntro());

                    slider.removeAllSliders();
                    String imgStr = goods.getImages();
                    if(imgStr != null && !imgStr.isEmpty())
                    {
                        final String[] imgList = imgStr.split(";");
                        for(int a = 0; a<imgList.length; a++)
                        {
                            DefaultSliderView sliderView = new DefaultSliderView(GoodsActivity.this);
                            // initialize a SliderLayout
                            final String imageUrl = imgList[a];
                            sliderView.image(imageUrl);
                            sliderView.setScaleType(BaseSliderView.ScaleType.Fit);

                            sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(GoodsActivity.this, ScaleImageViewActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArray("imgList", imgList);
                                    intent.putExtras(bundle);

                                    startActivity(intent);
                                }
                            });

                            slider.addSlider(sliderView);
                        }
                    }

                    //商品店铺信息
                    final AppbrandId appbrandId = goods.getAppbrandId();

                    ImageLoader.getInstance().displayImage(appbrandId.getLogopic(), brandLogoView);

                    brandNameView.setText(appbrandId.getBrandName());
//                    brandDetailView.setText(appbrandId.getDetail());
                    brandEnterBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enterStore();

                        }
                    });

                    int brandFlag = appbrandId.getFlag();
                    switch (brandFlag)
                    {
                        case StaticValues.GOODS_FAVOURITE_ON:
                            brandFavouriteBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (user != null) {
                                        Toast.makeText(GoodsActivity.this, "您已收藏该品牌", Toast.LENGTH_SHORT).show();

                                    } else {
//                                        Toast.makeText(GoodsActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();
                                        loginOrRegister();

                                    }
                                }
                            });
                            break;

                        case StaticValues.GOODS_FAVOURITE_OFF:
                            brandFavouriteBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(user != null)
                                    {
                                        //收藏品牌
                                        SaveAppBrandCollect saveAppBrandCollect = new SaveAppBrandCollect();
                                        saveAppBrandCollect.setUserId(user.getUserId());
                                        saveAppBrandCollect.setSessionid(user.getSessionid());
                                        AppuserId appuserId = new AppuserId();
                                        appuserId.setUserId(user.getUserId());
                                        saveAppBrandCollect.setAppuserId(appuserId);
                                        saveAppBrandCollect.setAppbrandId(appbrandId);

                                        new SaveAppBrandCollectTask().execute(saveAppBrandCollect);

                                        brandFavouriteBtn.setEnabled(false);
                                    }
                                    else
                                    {
//                                        Toast.makeText(GoodsActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();
                                        loginOrRegister();

                                    }
                                }
                            });
                            break;
                    }

                    if(!CommonUtils.isValueEmpty(goods.getGoodsDetail()))
                    {
                        detailWebView.loadData(goods.getGoodsDetail(), "text/html", "utf-8");
//                        ImageLoader.getInstance().displayImage( goods.getGoodsDetail(), detailImageView, options);

                    }

                    //添加到浏览历史
                    if(!new GoodsAction().isGoodsViewHistoryExists(appContext.getUdbHelper(), goods.getId()))
                    {
                        AppgoodsId appgoodsId = new AppgoodsId();

                        appgoodsId.setId(goods.getId());
                        appgoodsId.setGoodsName(goods.getGoodsName());
                        appgoodsId.setLogopicUrl(goods.getLogopicUrl());
                        appgoodsId.setPromotionPrice(goods.getPromotionPrice());

                        new GoodsAction().addGoodsViewHistory(appContext.getUdbHelper(), appgoodsId);
//                    appContext.addGoodsViewHistory(appgoodsId);

                    }

                    int flag = goods.getFlag();
                    switch (flag)
                    {
                        case StaticValues.GOODS_FAVOURITE_ON:
                            favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.favourited));
                            addFavouriteBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(user != null)
                                    {
                                        Toast.makeText(GoodsActivity.this, "您已收藏该商品", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        loginOrRegister();

                                    }
                                }
                            });
                            break;

                        case StaticValues.GOODS_FAVOURITE_OFF:
                            addFavouriteBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(user != null)
                                    {
                                        SaveAppGoodsCollect saveAppGoodsCollect = new SaveAppGoodsCollect();
                                        saveAppGoodsCollect.setUserId(user.getUserId());
                                        saveAppGoodsCollect.setSessionid(user.getSessionid());
                                        AppuserId appuserId = new AppuserId();
                                        appuserId.setUserId(user.getUserId());
                                        saveAppGoodsCollect.setAppuserId(appuserId);
                                        AppgoodsId appgoodsId = new AppgoodsId();
                                        appgoodsId.setId(goodsId);
                                        saveAppGoodsCollect.setAppgoodsId(appgoodsId);

                                        new SaveAppGoodsCollectActionTask().execute(saveAppGoodsCollect);

                                        addFavouriteBtn.setEnabled(false);
                                    }
                                    else
                                    {
                                        loginOrRegister();

                                    }
                                }
                            });
                            break;
                    }


                }
                else
                {
                    Toast.makeText(GoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(GoodsActivity.this, "商品信息获取失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //进入店铺
    private void enterStore()
    {
        AppbrandId appbrandId = goods.getAppbrandId();

        if(appbrandId != null)
        {
            Intent intent = new Intent(GoodsActivity.this, BrandActivity.class);
            Brand brand = new Brand();
            brand.setId(appbrandId.getId());
            brand.setLogopic(appbrandId.getLogopic());
            brand.setBrandName(appbrandId.getBrandName());
            brand.setShowpic(appbrandId.getShowpic());

            Bundle bundle = new Bundle();
            bundle.putParcelable("brand", brand);
            bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
            bundle.putInt("enterType", StaticValues.BRAND_ENTER_TYPE_NORMAL);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(GoodsActivity.this, "未获取到商品的店铺信息", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean orderValidate()
    {
        boolean result = true;
        if(user == null)
        {
//            Toast.makeText(GoodsActivity.this, "请先注册或登录", Toast.LENGTH_SHORT).show();
            loginOrRegister();


            return false;
        }
        if(sizeSelected == null)
        {
            Toast.makeText(GoodsActivity.this, "请选择尺码", Toast.LENGTH_SHORT).show();
            return false;

        }
        if(colorSelected == null)
        {
            Toast.makeText(GoodsActivity.this, "请选择颜色", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(count < 1)
        {
            Toast.makeText(GoodsActivity.this, "购买数量不能小于1", Toast.LENGTH_SHORT).show();
            return false;
        }

        return result;
    }

    //添加到购物车任务
    public class AddAppShopcartActionTask extends AsyncTask<AddAppShopcart, Void, AddAppShopcart>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addToCartDialog.show();
            addToCartBtn.setEnabled(false);

        }

        @Override
        protected AddAppShopcart doInBackground(AddAppShopcart... addAppShopcarts) {
            switch (boughtType)
            {
                case StaticValues.BOUTHT_TYPE_NORMAL:
                    return new CartAction().addAppShopcartAction(addAppShopcarts[0]);

                case StaticValues.BOUTHT_TYPE_RESERVATION:
                    return new CartAction().saveInsertAppyyShopcart(addAppShopcarts[0]);

                case StaticValues.BOUTHT_TYPE_TRYIT:
                    return new CartAction().SaveInsertAppsmShopcart(addAppShopcarts[0]);

                default:
                    return null;

            }
        }

        @Override
        protected void onPostExecute(AddAppShopcart result) {
            addToCartDialog.dismiss();
            addToCartBtn.setEnabled(false);

            if(result != null)
            {
                if(result.isSuccess())
                {

                    Toast.makeText(GoodsActivity.this, "商品添加成功", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(GoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(GoodsActivity.this, "商品添加失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //添加收藏任务
    public class SaveAppGoodsCollectActionTask extends AsyncTask<SaveAppGoodsCollect, Void, SaveAppGoodsCollect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SaveAppGoodsCollect doInBackground(SaveAppGoodsCollect... saveAppGoodsCollects) {
            return new GoodsAction().saveAppGoodsCollectAction(saveAppGoodsCollects[0]);
        }

        @Override
        protected void onPostExecute(SaveAppGoodsCollect result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    Toast.makeText(GoodsActivity.this, "商品收藏成功", Toast.LENGTH_SHORT).show();
                    favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.favourited));
                    addFavouriteBtn.setEnabled(false);

                }
                else
                {
                    Toast.makeText(GoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(GoodsActivity.this, "商品添加失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //获取评价任务
    public class GetAppEvaluateTask extends AsyncTask<AppEvaluate, Void, AppEvaluate>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppEvaluate doInBackground(AppEvaluate... appEvaluates) {
            return new GoodsAction().getAppEvaluateAction(appEvaluates[0]);
        }

        @Override
        protected void onPostExecute(AppEvaluate result) {
            if(result != null)
            {
                ArrayList<AppEvaluateItem> appEvaluateItems = result.getAppEvaluateItems();

                if(result.isSuccess())
                {
                    ArrayList<Map<String,Object>> commentListData = new ArrayList<Map<String,Object>>();
                    if(appEvaluateItems.isEmpty())
                    {
                        warningLayout.setVisibility(View.VISIBLE);
                        warningTextView.setText("商品尚未有评论");

                    }
                    else
                    {
                        for(AppEvaluateItem appEvaluateItem: appEvaluateItems)
                        {
                            Map line = new HashMap();

                            line.put("id", appEvaluateItem.getId());
                            line.put("username", appEvaluateItem.getAppuserId().getUserName());
                            line.put("content", appEvaluateItem.getContent());
                            line.put("attribute", appEvaluateItem.getAttribute());
                            line.put("datetime", CommonUtils.timestampToDatetime(appEvaluateItem.getReTime()));

                            commentListData.add(line);

                        }
                        goodsCommentAdapter = new GoodsCommentAdapter(commentListData, GoodsActivity.this);
                        commentListView.setAdapter(goodsCommentAdapter);
                    }

                }
                else
                {

                    Toast.makeText(GoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(GoodsActivity.this, "获取商品评价失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //店铺收藏
    public class SaveAppBrandCollectTask extends AsyncTask<SaveAppBrandCollect, Void, SaveAppBrandCollect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SaveAppBrandCollect doInBackground(SaveAppBrandCollect... saveAppBrandCollects) {
            return new BrandAction().saveAppBrandCollectAction(GoodsActivity.this, saveAppBrandCollects[0]);

        }

        @Override
        protected void onPostExecute(SaveAppBrandCollect result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    Toast.makeText(GoodsActivity.this, "收藏添加成功", Toast.LENGTH_SHORT).show();

                    brandFavouriteBtn.setEnabled(false);
                }
                else
                {
                    Toast.makeText(GoodsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(GoodsActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public String getSizeSelected() {
        return sizeSelected;
    }

    public void setSizeSelected(String sizeSelected) {
        this.sizeSelected = sizeSelected;
    }

    public String getColorSelected() {
        return colorSelected;
    }

    public void setColorSelected(String colorSelected) {
        this.colorSelected = colorSelected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public void addGoods()
    {

    }

}
