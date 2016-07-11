package com.brand.ushopping.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.BrandAction;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.activity.BrandFavouriteActivity;
import com.brand.ushopping.activity.GoodsFavouriteActivity;
import com.brand.ushopping.activity.HelpActivity;
import com.brand.ushopping.activity.LoginActivity;
import com.brand.ushopping.activity.MainActivity;
import com.brand.ushopping.activity.OrderActivity;
import com.brand.ushopping.activity.ProfileActivity;
import com.brand.ushopping.activity.RefActivity;
import com.brand.ushopping.activity.RegisterActivity;
import com.brand.ushopping.activity.ReservationActivity;
import com.brand.ushopping.activity.ReturnRulesActivity;
import com.brand.ushopping.activity.SettingsActivity;
import com.brand.ushopping.activity.TryoutActivity;
import com.brand.ushopping.activity.UserRewardActivity;
import com.brand.ushopping.activity.ViewHistoryActivity;
import com.brand.ushopping.activity.VoucherActivity;
import com.brand.ushopping.model.AppBrandCollect;
import com.brand.ushopping.model.AppGoodsCollect;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TextView loginBtn;
    private TextView registerBtn;
    private ImageView headImgImageView;
    private TextView userNameTextView;
    private AppContext appContext;
    private User user = null;
    private RelativeLayout loginLayout;
    private LinearLayout mineInfoLayout;
    private ImageView settingsBtn;
    private ViewGroup refBtn;
    private ViewGroup orderBtn;
    private ViewGroup reservationBtn;
    private ViewGroup tryoutBtn;
    private ViewGroup rewardBtn;
    private ViewGroup helpBtn;
    private ViewGroup goodsFavouriteBtn;
    private ViewGroup brandFavouriteBtn;
    private ViewGroup viewHistoryBtn;
    private ViewGroup voucherBtn;
    private ViewGroup returnsBtn;
    private int goodsFavouriteCount = 0;
    private int brandFavouriteCount = 0;
    private int viewHistoryCount = 0;

    private TextView goodsFavouriteCountTextView;
    private TextView brandFavouriteCountTextView;
    private TextView viewHistoryCountTextView;

    private ViewGroup unpaidBtn;
    private ViewGroup paidBtn;
    private ViewGroup deliveredBtn;

    private ArrayList<AppgoodsId> goodsViewHistory;
    private MainActivity mainActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity.setButtomBarEnable(false);
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        // Inflate the layout for this fragment
        loginBtn = (TextView) view.findViewById(R.id.login);
        registerBtn = (TextView) view.findViewById(R.id.register);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);

            }
        });

        goodsFavouriteCountTextView = (TextView) view.findViewById(R.id.goods_favourite_count);
        brandFavouriteCountTextView = (TextView) view.findViewById(R.id.brand_favourite_count);
        viewHistoryCountTextView = (TextView) view.findViewById(R.id.view_history_count);

        headImgImageView = (ImageView) view.findViewById(R.id.head_img);
        userNameTextView = (TextView) view.findViewById(R.id.username);

        loginLayout = (RelativeLayout) view.findViewById(R.id.login_layout);
        loginLayout.setVisibility(View.GONE);
        mineInfoLayout = (LinearLayout) view.findViewById(R.id.mine_info_layout);
        mineInfoLayout.setVisibility(View.GONE);

        settingsBtn = (ImageView) view.findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);

            }
        });

        refBtn = (ViewGroup) view.findViewById(R.id.ref);
        refBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent intent = new Intent(getActivity(), RefActivity.class);
                    startActivity(intent);
                }

            }
        });

        orderBtn = (ViewGroup) view.findViewById(R.id.order);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("enterType", StaticValues.ORDER_FLAG_UNPAY);
                    intent.putExtras(bundle);
                    appContext.setBundleObj(bundle);
                    startActivity(intent);
                }

            }
        });

        reservationBtn = (ViewGroup) view.findViewById(R.id.reservation);
        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), ReservationActivity.class);
                    startActivity(intent);
                }

            }
        });

        tryoutBtn = (ViewGroup) view.findViewById(R.id.tryout);
        tryoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), TryoutActivity.class);
                    startActivity(intent);
                }

            }
        });

        rewardBtn = (ViewGroup) view.findViewById(R.id.reward);
        rewardBtn.setOnClickListener(new View.OnClickListener() {
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

        goodsFavouriteBtn = (ViewGroup) view.findViewById(R.id.goods_favourite);
        goodsFavouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null)
                {
                    Intent intent = new Intent(getActivity(), GoodsFavouriteActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
            }
        });

        brandFavouriteBtn = (ViewGroup) view.findViewById(R.id.brand_favourite);
        brandFavouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null)
                {
                    Intent intent = new Intent(getActivity(), BrandFavouriteActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHistoryBtn = (ViewGroup) view.findViewById(R.id.view_history);
        viewHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewHistoryActivity.class);
                startActivity(intent);

            }
        });

        helpBtn = (ViewGroup) view.findViewById(R.id.help);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);

            }
        });

        voucherBtn = (ViewGroup) view.findViewById(R.id.voucher);
        voucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VoucherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("enterType", StaticValues.VOUCHER_ENTER_MINE);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                startActivity(intent);

//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("我的卡券功能尚未上线,敬请期待");
//                builder.setTitle("提示");
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.create().show();



            }
        });

        unpaidBtn = (ViewGroup) view.findViewById(R.id.unpaid);
        unpaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("enterType", StaticValues.ORDER_FLAG_UNPAY);
                    intent.putExtras(bundle);
                    appContext.setBundleObj(bundle);
                    startActivity(intent);
                }
            }
        });

        paidBtn = (ViewGroup) view.findViewById(R.id.paid);
        paidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("enterType", StaticValues.ORDER_FLAG_DELIVERED);
                    intent.putExtras(bundle);
                    appContext.setBundleObj(bundle);
                    startActivity(intent);
                }

            }
        });

        deliveredBtn = (ViewGroup) view.findViewById(R.id.comment);
        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(getActivity(), "请登录或注册", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("enterType", StaticValues.ORDER_FLAG_CONFIRMED);
                    intent.putExtras(bundle);
                    appContext.setBundleObj(bundle);
                    startActivity(intent);
                }

            }
        });

        returnsBtn = (ViewGroup) view.findViewById(R.id.returns);
        returnsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReturnRulesActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appContext = (AppContext) getActivity().getApplicationContext();
        user = appContext.getUser();

        if(user != null)
        {
            mineInfoLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);

            if(CommonUtils.isValueEmpty(user.getHeadImg()))
            {
                ImageLoader.getInstance().displayImage("drawable://"+R.drawable.logo, headImgImageView);
            }
            else
            {
                ImageLoader.getInstance().displayImage(user.getHeadImg(), headImgImageView);
            }

            if(CommonUtils.isValueEmpty(user.getUserName()))
            {
                userNameTextView.setText("新用户");

            }
            else
            {
                userNameTextView.setText(user.getUserName());
            }

            headImgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //我的资料
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);

                }
            });

        }
        else
        {
            loginLayout.setVisibility(View.VISIBLE);
            mineInfoLayout.setVisibility(View.GONE);
        }

        if(user != null)
        {
            AppGoodsCollect appGoodsCollect = new AppGoodsCollect();
            appGoodsCollect.setUserId(user.getUserId());
            appGoodsCollect.setSessionid(user.getSessionid());
            new GetListAppGoodsCollectUserIdTask().execute(appGoodsCollect);

            AppBrandCollect appBrandCollect = new AppBrandCollect();
            appBrandCollect.setUserId(user.getUserId());
            appBrandCollect.setSessionid(user.getSessionid());
            new GetListAppBrandCollectUserIdTask().execute(appBrandCollect);

        }

        goodsViewHistory = new GoodsAction(getActivity()).getGoodsViewHistory(appContext.getUdbHelper());
        if(goodsViewHistory != null && !goodsViewHistory.isEmpty())
        {
            viewHistoryCountTextView.setText(Integer.toString(goodsViewHistory.size()));

        }

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

    //获取收藏
    //商品收藏列表
    public class GetListAppGoodsCollectUserIdTask extends AsyncTask<AppGoodsCollect, Void, AppGoodsCollect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppGoodsCollect doInBackground(AppGoodsCollect... appGoodsCollects) {
            return new GoodsAction(getActivity()).getListAppGoodsCollectUserIdAction(appGoodsCollects[0]);

        }

        @Override
        protected void onPostExecute(AppGoodsCollect result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    goodsFavouriteCountTextView.setText(Integer.toString(result.getAppGoodsCollectItems().size()));
                    result.setAppGoodsCollectItems(result.getAppGoodsCollectItems());

                }
            }

        }
    }

    //店铺收藏列表
    public class GetListAppBrandCollectUserIdTask extends AsyncTask<AppBrandCollect, Void, AppBrandCollect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppBrandCollect doInBackground(AppBrandCollect... appBrandCollects) {
            return new BrandAction(getActivity()).getListAppBrandCollectUserIdAction(appBrandCollects[0]);

        }

        @Override
        protected void onPostExecute(AppBrandCollect result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    brandFavouriteCountTextView.setText(Integer.toString(result.getAppBrandCollectItems().size()));
                    appContext.setAppBrandCollectItems(result.getAppBrandCollectItems());

                }

            }

        }
    }

}
