package com.brand.ushopping.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.MainpageAction;
import com.brand.ushopping.activity.AroundActivity;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.activity.SearchActivity;
import com.brand.ushopping.activity.SignActivity;
import com.brand.ushopping.activity.UserRewardActivity;
import com.brand.ushopping.activity.WebViewActivity;
import com.brand.ushopping.adapter.ActivityItemAdapter;
import com.brand.ushopping.adapter.HomeReAdapter;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.Banner;
import com.brand.ushopping.model.Category;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.Recommend;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.CategoryItemView;
import com.brand.ushopping.widget.MyGridView;
import com.brand.ushopping.widget.MyListView;
import com.brand.ushopping.widget.RecommendItem;
import com.brand.ushopping.widget.ScrollViewX;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainpageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainpageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainpageFragment extends Fragment implements AMapLocationListener {
    private OnFragmentInteractionListener mListener;
    private AppContext appContext;
    private User user;
    private ImageView searchBtn;
    private SliderLayout slider;
    private ScrollViewX mainScrollView;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private LinearLayout categoryLayout;
    private LinearLayout recommendLayout;
    private MyListView activityLayout;

    private MyGridView homeReLayout;

    private ViewGroup signBtn;
    private ViewGroup userRewardBtn;
    private ViewGroup reservationBtn;
    private ViewGroup tryitBtn;
    private Main main;
    private HomeRe homeRe;
    private int recommendMin = 0;
    private TextView cityTextView;

    private int homeReCount = 0;
    private List homeReListData;
    private HomeReAdapter homeReAdapter;

    private TimeoutbleProgressDialog mainpageLoadDialog;
    private TimeoutbleProgressDialog homereLoadDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainpageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainpageFragment newInstance() {
        MainpageFragment fragment = new MainpageFragment();

        return fragment;
    }

    public MainpageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainpage, container, false);

        searchBtn = (ImageView) view.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

            }
        });

        categoryLayout = (LinearLayout) view.findViewById(R.id.category);
        categoryLayout.setHorizontalScrollBarEnabled(false);

        slider = (SliderLayout) view.findViewById(R.id.slider);
        slider.setDuration(10000);

        recommendLayout = (LinearLayout) view.findViewById(R.id.recommend);
        activityLayout = (MyListView) view.findViewById(R.id.activity);
        homeReLayout = (MyGridView) view.findViewById(R.id.home_re);
        homeReLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", id);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        boolean pauseOnScroll = false; // or true
        boolean pauseOnFling = true; // or false
        PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling);
        homeReLayout.setOnScrollListener(listener);

        mainScrollView = (ScrollViewX) view.findViewById(R.id.main_scroll_view);
        mainScrollView.setOnScrollListener(new ScrollViewX.OnScrollListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                if (mainScrollView.isAtBottom()) {
//                    Toast.makeText(getActivity(), "到底了", Toast.LENGTH_SHORT).show();

                    HomeRe homeRe = new HomeRe();
                    if (user != null) {
                        homeRe.setUserId(user.getUserId());
                        homeRe.setSessionid(user.getSessionid());
                    }
                    homeRe.setMin(homeReCount);
                    homeRe.setMax(StaticValues.RECOMMEND_PAGE_COUNT);
                    Log.v("ushopping", "home re min " + homeRe.getMin() + " max " + homeRe.getMax());
                    new MainReLoadTask().execute(homeRe);

                }
            }

            @Override
            public void onScrollStopped() {

            }

            @Override
            public void onScrolling() {

            }
        });

        signBtn = (ViewGroup) view.findViewById(R.id.sign);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent intent = new Intent(getActivity(), SignActivity.class);
                    startActivity(intent);

                }

            }
        });

        userRewardBtn = (ViewGroup) view.findViewById(R.id.user_reward);
        userRewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent intent = new Intent(getActivity(), UserRewardActivity.class);
                    startActivity(intent);
                }

            }
        });

        reservationBtn = (ViewGroup) view.findViewById(R.id.reservation);
        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AroundActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_RESERVATION);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        tryitBtn = (ViewGroup) view.findViewById(R.id.tryit);
        tryitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AroundActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_TRYIT);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        cityTextView = (TextView) view.findViewById(R.id.city);

        mainpageLoadDialog = TimeoutbleProgressDialog.createProgressDialog(getActivity(), StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                mainpageLoadDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("主页信息加载失败,请检查网络连接");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });

        homereLoadDialog = TimeoutbleProgressDialog.createProgressDialog(getActivity(), StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                homereLoadDialog.dismiss();

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appContext = (AppContext) getActivity().getApplicationContext();
        user = appContext.getUser();

        //新建
        locationClient = new AMapLocationClient(appContext);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setOnceLocation(true);
        locationOption.setInterval(10000);
        locationOption.setNeedAddress(true);

        //给定位客户端对象设置定位参数
        locationClient.setLocationOption(locationOption);

        // 设置定位监听
        locationClient.setLocationListener(this);

        // 启动定位
        locationClient.startLocation();

        setValue();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //位置相关
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                /*
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getRoad();//街道信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                */

                String city = aMapLocation.getCity();
                cityTextView.setText(city);
                appContext.setCity(city);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setValue()
    {
        main = appContext.getMain();
        homeRe = appContext.getHomeRe();

        //首页
        if(main != null)
        {
            if(main.isSuccess())
            {
                //分类
                categoryLayout.removeAllViewsInLayout();
                ArrayList<Category> categories = main.getCategory();
                for(int a=0; a<categories.size(); a++)
                {
                    final Category category = categories.get(a);

                    CategoryItemView categoryItemView = new CategoryItemView(getActivity(), null, category);

//                    TextView textView = new TextView(getActivity());
//                    textView.setText(category.getName());
//                    textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
//                    textView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(), CategoryActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putLong("categoryId", category.getId());
//                            bundle.putString("categoryName", category.getName());
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//
////                                Toast.makeText(getActivity(), Long.toString(category.getId()), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    textView.setTextColor(getResources().getColor(R.color.text_light_grey));
//
//                    ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(100, 38);
//                    textView.setLayoutParams(para);
//                    textView.setTextColor(getResources().getColor(R.color.text_grey));
//
//                    textView.setPadding(8,8,8,8);
//                    textView.setTextColor(R.color.text_grey);
                    categoryLayout.addView(categoryItemView);
                }

                ArrayList<Banner> banners = main.getBanner();
                for(int b=0; b<banners.size(); b++)
                {
                    final Banner banner = banners.get(b);

                    DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                    // initialize a SliderLayout
                    sliderView.image(banner.getImage());
                    sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                    sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", banner.getImgUrl());
                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    });
                    slider.addSlider(sliderView);

                }

                //推荐
                recommendLayout.removeAllViewsInLayout();
                ArrayList<Recommend> recommends = main.getRecommend();
                for(int c=0; c<recommends.size(); c++)
                {
                    Recommend recommend = recommends.get(c);

                    /*
                    FrameLayout frameLayout = new FrameLayout(getActivity());
                    ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(appContext.getScreenWidth() / 4, appContext.getScreenWidth() / 4);
                    frameLayout.setLayoutParams(para);
                    frameLayout.setPadding(8, 8, 8, 8);

                    ImageView imageView = new ImageView(getActivity());
                    ImageLoader.getInstance().displayImage(recommend.getImg(), imageView, options);
                    frameLayout.addView(imageView);

                    TextView introTextView = new TextView(getActivity());
                    introTextView.setText(recommend.getIntro());
                    introTextView.setTextColor(Color.WHITE);
                    introTextView.setBackgroundColor(Color.BLACK);
                    introTextView.setAlpha(StaticValues.ALPHA_HALF);
                    para = new ViewGroup.LayoutParams(appContext.getScreenWidth() / 4, 45);
                    introTextView.setLayoutParams(para);
                    introTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                    frameLayout.addView(introTextView);

                    recommendLayout.addView(frameLayout);
                    */

                    RecommendItem recommendItem = new RecommendItem(getActivity(), null, recommend);
                    recommendLayout.addView(recommendItem);

                }

                //活动
                activityLayout.removeAllViewsInLayout();
                ArrayList<com.brand.ushopping.model.Activity> activities = main.getActivity();

                List listData = new ArrayList<Map<String,Object>>();
                for(int d=0; d<activities.size(); d++)
                {
                    final com.brand.ushopping.model.Activity activity = activities.get(d);

                    Map line = new HashMap();

                    line.put("id", activity.getId());
                    line.put("img", activity.getImg());
                    line.put("url", activity.getImgUrl());
                    line.put("flag", activity.getFlag());

                    listData.add(line);

                }

                ActivityItemAdapter themeItemAdapter = new ActivityItemAdapter(listData, getActivity());
                activityLayout.setAdapter(themeItemAdapter);

//                int totalHeight = 0;
//                for (int i = 0; i < themeItemAdapter.getCount(); i++) {
//                    View listItem = themeItemAdapter.getView(i, null, activityLayout);
//                    listItem.measure(0, 0);
//                    totalHeight += listItem.getMeasuredHeight();
//                }
//
//                ViewGroup.LayoutParams params = activityLayout.getLayoutParams();
//                params.height = totalHeight + (activityLayout.getDividerHeight() * (themeItemAdapter.getCount() - 1));
//                ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
//                activityLayout.setLayoutParams(params);

            }

        }
        else
        {
            User user1 = new User();
            if(user != null)
            {
                user1.setUserId(user.getUserId());
                user1.setSessionid(user.getSessionid());
            }

            new MainpageLoadTask().execute(user1);

        }

        //首页下拉
        if(homeRe != null)
        {
            homeReLayout.removeAllViewsInLayout();

            if(homeRe.isSuccess()) {
                ArrayList<AppgoodsId> appgoodsIds = homeRe.getAppgoodsId();

                homeReCount += appgoodsIds.size();

                homeReListData = new ArrayList<Map<String,Object>>();
                for(AppgoodsId appgoodsId: appgoodsIds)
                {
                    Map line = new HashMap();

                    line.put("id", appgoodsId.getId());
                    line.put("img", appgoodsId.getLogopicUrl());
                    line.put("name", appgoodsId.getGoodsName());
                    line.put("price", appgoodsId.getPromotionPrice());
                    line.put("favouriteCount", 123);

                    homeReListData.add(line);
                }
                homeReAdapter = new HomeReAdapter(homeReListData, getActivity());
                homeReLayout.setAdapter(homeReAdapter);

                /*
                int totalHeight = 0;
                for (int i = 0; i < appgoodsIds.size(); i++) {
                    View listItem = homeReAdapter.getView(i, null, homeReLayout);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();

                }

                ViewGroup.LayoutParams params = homeReLayout.getLayoutParams();
                params.height = totalHeight + 100;
                ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
                homeReLayout.setLayoutParams(params);
                */

            }
        }
        else
        {
            Toast.makeText(getActivity(), "首页商品加载失败", Toast.LENGTH_SHORT).show();
        }

        /*
        new MainLoadTask().execute(user);

        HomeRe homeRe = new HomeRe();
        if(user != null)
        {
            homeRe.setUserId(user.getUserId());
            homeRe.setSessionid(user.getSessionid());
        }
        homeRe.setMin(recommendMin);
        homeRe.setMax(StaticValues.RECOMMEND_PAGE_COUNT);
        new MainReLoadTask().execute(homeRe);
        */
    }

    /*
    //首页加载事件
    public class MainLoadTask extends AsyncTask<User, Void, Main>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Main doInBackground(User... user) {
            return new MainpageAction().home(user[0]);
        }

        @Override
        protected void onPostExecute(Main result) {

        }
    }
    */

    //首页下拉加载
    public class MainReLoadTask extends AsyncTask<HomeRe, Void, HomeRe>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            homereLoadDialog.show();

        }

        @Override
        protected HomeRe doInBackground(HomeRe... homeRe) {
            return new MainpageAction().homeRe(getActivity(), homeRe[0]);
        }

        @Override
        protected void onPostExecute(HomeRe result) {
//            homereLoadDialog.dismiss();

            if(result != null)
            {
                if(result.isSuccess())
                {
                    if(result.getAppgoodsId() != null)
                        {ArrayList<AppgoodsId> appgoodsIds = result.getAppgoodsId();
                        if(appgoodsIds.size() > 0)
                        {
                            homeReCount += appgoodsIds.size();
//                    homeReListData = new ArrayList<Map<String,Object>>();
                            for(AppgoodsId appgoodsId: appgoodsIds)
                            {
                                Map line = new HashMap();

                                line.put("id", appgoodsId.getId());
                                line.put("img", appgoodsId.getLogopicUrl());
                                line.put("name", appgoodsId.getGoodsName());
                                line.put("price", appgoodsId.getPromotionPrice());
                                line.put("favouriteCount", 123);

                                homeReListData.add(line);
                            }

                            homeReAdapter.notifyDataSetChanged();

                            /*
                            int totalHeight = 0;
                            for (int i = 0; i < appgoodsIds.size(); i++) {
                                View listItem = homeReAdapter.getView(i, null, homeReLayout);
                                listItem.measure(0, 0);
                                totalHeight += listItem.getMeasuredHeight();

                            }

                            ViewGroup.LayoutParams params = homeReLayout.getLayoutParams();
                            params.height += (totalHeight + 100);
                            ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
                            homeReLayout.setLayoutParams(params);
                            */
                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

    //首页加载
    public class MainpageLoadTask extends AsyncTask<User, Void, Main>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainpageLoadDialog.show();
        }

        @Override
        protected Main doInBackground(User... users) {
            return new MainpageAction().home(getActivity(), users[0]);
        }

        @Override
        protected void onPostExecute(Main result) {
            mainpageLoadDialog.dismiss();
            if(result != null)
            {
                appContext.setMain(result);
                setValue();
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("ushopping", "destroy cache");
//        ImageLoader.getInstance().clearMemoryCache();
//        ImageLoader.getInstance().clearDiskCache();

        ImageLoader.getInstance().clearMemoryCache();

    }
}
