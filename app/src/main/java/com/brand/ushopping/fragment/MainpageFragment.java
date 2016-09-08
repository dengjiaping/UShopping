package com.brand.ushopping.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AppAction;
import com.brand.ushopping.action.ImageAction;
import com.brand.ushopping.action.MainpageAction;
import com.brand.ushopping.action.RefAction;
import com.brand.ushopping.activity.AroundActivity;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.activity.MainActivity;
import com.brand.ushopping.activity.MoreGoodsActivity;
import com.brand.ushopping.activity.SearchActivity;
import com.brand.ushopping.activity.SignActivity;
import com.brand.ushopping.activity.UserRewardActivity;
import com.brand.ushopping.activity.WebViewActivity;
import com.brand.ushopping.adapter.ActivityItemAdapter;
import com.brand.ushopping.adapter.HomeReAdapter;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.Banner;
import com.brand.ushopping.model.Category;
import com.brand.ushopping.model.GetSelectAppStartpicture;
import com.brand.ushopping.model.HomeRe;
import com.brand.ushopping.model.Location;
import com.brand.ushopping.model.Main;
import com.brand.ushopping.model.Recommend;
import com.brand.ushopping.model.User;
import com.brand.ushopping.thread.DownloadSplashThread;
import com.brand.ushopping.utils.CommonUtils;
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
public class MainpageFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private AppContext appContext;
    private User user;
    private ImageView searchBtn;
    private SliderLayout slider;
    private ScrollViewX mainScrollView;

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

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView moreGoodsBtn;
    private Button moreGoods2Btn;

    private MainActivity mainActivity;

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
        appContext = (AppContext) getActivity().getApplicationContext();
        user = appContext.getUser();

        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity.setButtomBarEnable(false);
        View view = inflater.inflate(R.layout.fragment_mainpage, container, false);

        searchBtn = (ImageView) view.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                intent.putExtras(bundle);
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
                appContext.setBundleObj(bundle);
                startActivity(intent);
            }
        });

        // 主页关闭商品加载更多
        /*
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
        */

        signBtn = (ViewGroup) view.findViewById(R.id.sign);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();


                } else {
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
                appContext.setBundleObj(bundle);
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
                appContext.setBundleObj(bundle);
                startActivity(intent);

            }
        });

        cityTextView = (TextView) view.findViewById(R.id.city);
        cityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //手动选择地区
//                Intent intent = new Intent(getActivity(), AreaChooseActivity.class);
//                startActivityForResult(intent, StaticValues.REQUEST_CODE_AREA_PICK);
            }
        });

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

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                Main main = new Main();
                if(user != null)
                {
                    main.setUserId(user.getUserId());
                    main.setSessionid(user.getSessionid());
                }
                main.setUseCache(false);
                main.setArea(appContext.getArea());
                new MainpageLoadTask().execute(main);

                HomeRe homeRe = new HomeRe();
                if (user != null) {
                    homeRe.setUserId(user.getUserId());
                    homeRe.setSessionid(user.getSessionid());
                }
                homeRe.setArea(appContext.getArea());
                homeRe.setMin(homeReCount);
                homeRe.setMax(StaticValues.RECOMMEND_PAGE_COUNT);
                Log.v("ushopping", "home re min " + homeRe.getMin() + " max " + homeRe.getMax());
                new MainReLoadTask().execute(homeRe);

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        moreGoodsBtn = (TextView) view.findViewById(R.id.more_goods);
        moreGoodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                moreGoods();

            }
        });
        moreGoods2Btn = (Button) view.findViewById(R.id.more_goods_2);
        moreGoods2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                moreGoods();

            }
        });

        setValue();

        return view;
    }

    private void moreGoods()
    {
        Intent intent = new Intent(getActivity(), MoreGoodsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
        intent.putExtras(bundle);
        appContext.setBundleObj(bundle);
        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        user = appContext.getUser();

        String city = appContext.getCity();
        if(!CommonUtils.isValueEmpty(city))
        {
            cityTextView.setText(city);
        }

        GetSelectAppStartpicture getSelectAppStartpicture = new GetSelectAppStartpicture();
        if(user != null)
        {
            getSelectAppStartpicture.setUserId(user.getUserId());
            getSelectAppStartpicture.setSessionid(user.getSessionid());
        }
        getSelectAppStartpicture.setArea(appContext.getCity());
        new GetSelectAppStartpictureTask().execute(getSelectAppStartpicture);

        mainActivity.setButtomBarEnable(true);
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
        slider.removeAllSliders();

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
                    categoryLayout.addView(categoryItemView);

                }

                //首页滚屏
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
                            appContext.setBundleObj(bundle);
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
            Main main = new Main();
            if(user != null)
            {
                main.setUserId(user.getUserId());
                main.setSessionid(user.getSessionid());
            }

            new MainpageLoadTask().execute(main);

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
                    line.put("salesCount", appgoodsId.getSaleCount());

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
            return new MainpageAction(getActivity()).homeRe(homeRe[0]);
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
                                line.put("salesCount", appgoodsId.getSaleCount());

                                homeReListData.add(line);
                            }

                            homeReAdapter.notifyDataSetChanged();
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
    public class MainpageLoadTask extends AsyncTask<Main, Void, Main>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainpageLoadDialog.show();
        }

        @Override
        protected Main doInBackground(Main... mains) {
            return new MainpageAction(getActivity()).home(getActivity(), mains[0]);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == StaticValues.REQUEST_CODE_AREA_PICK && resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String areaName = bundle.getString("areaName");
            String cityName = bundle.getString("cityName");
            cityTextView.setText(cityName);
            appContext.setArea(areaName);
            appContext.setCity(cityName);

            //存入缓存
            Location location = new Location();
            location.setCity(cityName);
            location.setArea(areaName);
            location.setLongitude(Double.toString(0));
            location.setLatitude(Double.toString(0));

            new RefAction(getActivity()).setLocation(getActivity(), location);
        }
    }

    //下载AdSplash图片Task
    public class GetSelectAppStartpictureTask extends AsyncTask<GetSelectAppStartpicture, Void, GetSelectAppStartpicture>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetSelectAppStartpicture doInBackground(GetSelectAppStartpicture... getSelectAppStartpictures) {
            return new AppAction(getActivity()).getSelectAppStartpictureAction(getSelectAppStartpictures[0]);
        }

        @Override
        protected void onPostExecute(final GetSelectAppStartpicture result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    try
                    {
                        if(!new ImageAction(getActivity()).checkFileExists("splash.png"))
                        {
                            new DownloadSplashThread(getActivity(), result.getImages()).start();
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
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
