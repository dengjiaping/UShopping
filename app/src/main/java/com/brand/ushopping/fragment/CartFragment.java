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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.CartAction;
import com.brand.ushopping.activity.LoginActivity;
import com.brand.ushopping.activity.MainActivity;
import com.brand.ushopping.activity.OrderConfirmActivity;
import com.brand.ushopping.activity.SelectDateActivity;
import com.brand.ushopping.model.AppShopcart;
import com.brand.ushopping.model.AppShopcartBrand;
import com.brand.ushopping.model.AppShopcartIdList;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.AppsmShopcart;
import com.brand.ushopping.model.AppyyShopcart;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.CartItemView;
import com.brand.ushopping.widget.CartTypePopup;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TextView titleTextView;
    private TextView editAllTextView;
    private TimeoutbleProgressDialog getinfoDialog;
    private AppContext appContext;
    private User user;
    private AppShopcartIdList appShopcartIdList;
    private TableLayout contentLayout;
    private Button checkoutBtn;
    private CheckBox selectAllCheckBox;
    private TextView summaryTextView;
    private double summary = 0;
    private CartTypePopup cartTypePopup;
    private HashMap<Long, Goods> cartItemSelected = new HashMap<Long, Goods>();
    private ArrayList<CartItemView> cartItemViews = new ArrayList<CartItemView>();
    private int boughtType = StaticValues.BOUTHT_TYPE_NORMAL;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MainActivity mainActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();

        return fragment;
    }

    public CartFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        warningLayout = (FrameLayout) view.findViewById(R.id.warning_layout);
        warningTextView = (TextView) view.findViewById(R.id.warning_text);

        titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartTypePopup = new CartTypePopup(getActivity(), CartFragment.this);
                cartTypePopup.showAsDropDown(titleTextView);

            }
        });

        editAllTextView = (TextView) view.findViewById(R.id.edit_all);
        editAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "编辑所有", Toast.LENGTH_SHORT).show();
            }
        });

        getinfoDialog = TimeoutbleProgressDialog.createProgressDialog(getActivity(), StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getinfoDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("未获取到购物车信息");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                builder.create().show();
            }
        });

        contentLayout = (TableLayout) view.findViewById(R.id.content);

        selectAllCheckBox = (CheckBox) view.findViewById(R.id.select_all);
        selectAllCheckBox.setChecked(true);
        selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (CartItemView cartItemView: cartItemViews)
                {
                    cartItemView.setChecked(isChecked);

                }

            }
        });

        summaryTextView = (TextView) view.findViewById(R.id.summary);

        checkoutBtn = (Button) view.findViewById(R.id.checkout);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //结算
                if(!cartItemSelected.isEmpty())
                {
                    Intent intent = new Intent();

                    ArrayList<Goods> goodsList = new ArrayList<Goods>();
                    Iterator i = cartItemSelected.keySet().iterator();
                    while(i.hasNext())
                    {
                        goodsList.add(cartItemSelected.get(i.next()));
                    }

                    switch (boughtType)
                    {
                        case StaticValues.BOUTHT_TYPE_NORMAL:
                            intent.setClass(getActivity(), OrderConfirmActivity.class);
                            break;

                        case StaticValues.BOUTHT_TYPE_RESERVATION:
                        case StaticValues.BOUTHT_TYPE_TRYIT:
                            intent.setClass(getActivity(), SelectDateActivity.class);
                            break;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putLong("reservationDate", 0);
                    bundle.putInt("boughtType", boughtType);
                    bundle.putParcelableArrayList("goods", goodsList);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), "至少选择一件商品", Toast.LENGTH_SHORT).show();

                }

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reload();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appContext = (AppContext) getActivity().getApplicationContext();
        user = appContext.getUser();

        if(user == null)
        {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        reload();

        mainActivity.setButtomBarEnable(true);
    }

    public void reload()
    {
        //没有数据时的提示
        warningLayout.setVisibility(View.GONE);

        if(user != null)
        {
            appShopcartIdList = new AppShopcartIdList();
            appShopcartIdList.setUserId(user.getUserId());
            appShopcartIdList.setSessionid(user.getSessionid());

            new CartLoadTask().execute(appShopcartIdList);
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(cartTypePopup != null)
        {
            cartTypePopup.dismiss();
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

    //切换购物车类型
    protected void setType(int cartType)
    {

    }

    // 购物车查询
    public class CartLoadTask extends AsyncTask<AppShopcartIdList, Void, AppShopcartIdList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getinfoDialog.show();

        }

        @Override
        protected AppShopcartIdList doInBackground(AppShopcartIdList... appShopcartIdLists) {
            switch (boughtType)
            {
                case StaticValues.BOUTHT_TYPE_NORMAL:
                    return new CartAction(getActivity()).getAppShopcartIdListAction(appShopcartIdLists[0]);

                case StaticValues.BOUTHT_TYPE_RESERVATION:
                    return new CartAction(getActivity()).getAppyyShopcartIdList(appShopcartIdLists[0]);

                case StaticValues.BOUTHT_TYPE_TRYIT:
                    return new CartAction(getActivity()).getAppsmShopcartIdList(appShopcartIdLists[0]);

                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(AppShopcartIdList result) {
            getinfoDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            //清除所有
            cartItemSelected.clear();
            contentLayout.removeAllViewsInLayout();

            if(result != null)
            {
                if(result.isSuccess())
                {
                    ArrayList<AppShopcartBrand> appShopcartBrands = result.getAppShopcartBrands();
                    for(int a=0; a<appShopcartBrands.size(); a++)
                    {
                        AppShopcartBrand appShopcartBrand = appShopcartBrands.get(a);
                        if(appShopcartBrand != null)
                        {
                            CartItemView cartItemView = new CartItemView(getActivity(), CartFragment.this, null, appShopcartBrand);
                            contentLayout.addView(cartItemView);

                            cartItemViews.add(cartItemView);
                        }
                    }

                }
                else
                {
                    warningLayout.setVisibility(View.VISIBLE);
                    warningTextView.setText(result.getMsg());

                }
            }
            else
            {
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("未获取到购物车信息");

            }

            summaryCal();

        }
    }

    //购物车结算添加
    public void addCartItem(AppShopcart appShopcart)
    {
        AppgoodsId appgoodsId = appShopcart.getAppgoodsId();
        Goods goods = new Goods();

        goods.setId(appgoodsId.getId());
        goods.setGoodsName(appgoodsId.getGoodsName());
        goods.setLogopicUrl(appgoodsId.getLogopicUrl());
        goods.setAttribute(appShopcart.getAttribute());
        goods.setPromotionPrice(appgoodsId.getPromotionPrice());
        goods.setCount(appShopcart.getQuantity());
        goods.setAppbrandId(appgoodsId.getAppbrandId());

        cartItemSelected.put(appShopcart.getId(), goods);

        summaryCal();
    }

    //购物车结算移除
    public void removeCartItem(long id)
    {
        cartItemSelected.remove(id);

        summaryCal();
    }

    //计算总金额
    private void summaryCal()
    {
        summary = 0;
        Iterator i = cartItemSelected.keySet().iterator();
        while(i.hasNext())
        {
            Goods goods = cartItemSelected.get(i.next());
            summary += (goods.getPromotionPrice() * goods.getCount());

        }
        summaryTextView.setText(CommonUtils.df.format(summary));
    }

    //购物车商品移除
    public void deleteItem(long shopcartId)
    {
        switch (boughtType)
        {
            case StaticValues.BOUTHT_TYPE_NORMAL:
                AppShopcartIdList appShopcartIdList = new AppShopcartIdList();
                appShopcartIdList.setShopcartId(shopcartId);
                appShopcartIdList.setUserId(user.getUserId());
                appShopcartIdList.setSessionid(user.getSessionid());

                new DeleteItemTask().execute(appShopcartIdList);
                break;

            case StaticValues.BOUTHT_TYPE_RESERVATION:
                AppyyShopcart appyyShopcart = new AppyyShopcart();
                appyyShopcart.setUserId(user.getUserId());
                appyyShopcart.setSessionid(user.getSessionid());
                appyyShopcart.setAppyyshopcartId(shopcartId);

                new UpdateAppyyShopcart().execute(appyyShopcart);
                break;

            case StaticValues.BOUTHT_TYPE_TRYIT:
                AppsmShopcart appsmShopcart = new AppsmShopcart();
                appsmShopcart.setUserId(user.getUserId());
                appsmShopcart.setSessionid(user.getSessionid());
                appsmShopcart.setAppsmshopcartId(shopcartId);

                new UpdateAppsmShopcart().execute(appsmShopcart);
                break;
        }

    }

    //删除购物车
    protected class DeleteItemTask extends AsyncTask<AppShopcartIdList, Void, AppShopcartIdList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppShopcartIdList doInBackground(AppShopcartIdList... appShopcartIdList) {
            return new CartAction(getActivity()).deleteShopcartId(appShopcartIdList[0]);
        }

        @Override
        protected void onPostExecute(AppShopcartIdList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    //刷新
                    Toast.makeText(getActivity(), "购物车商品已删除", Toast.LENGTH_SHORT).show();
                    reload();

                }
                else
                {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getActivity(), "购物车删除商品失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //删除预约购物车
    protected class UpdateAppyyShopcart extends AsyncTask<AppyyShopcart, Void, AppyyShopcart>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppyyShopcart doInBackground(AppyyShopcart... appyyShopcarts) {
            return new CartAction(getActivity()).updateAppyyShopcart(appyyShopcarts[0]);
        }

        @Override
        protected void onPostExecute(AppyyShopcart result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    //刷新
                    Toast.makeText(getActivity(), "购物车商品已删除", Toast.LENGTH_SHORT).show();
                    reload();

                }
                else
                {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getActivity(), "购物车删除商品失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //删除上门购物车
    protected class UpdateAppsmShopcart extends AsyncTask<AppsmShopcart, Void, AppsmShopcart>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppsmShopcart doInBackground(AppsmShopcart... appsmShopcarts) {
            return new CartAction(getActivity()).updateAppsmShopcart(appsmShopcarts[0]);
        }

        @Override
        protected void onPostExecute(AppsmShopcart result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    //刷新
                    Toast.makeText(getActivity(), "购物车商品已删除", Toast.LENGTH_SHORT).show();
                    reload();

                }
                else
                {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getActivity(), "购物车删除商品失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public double getSummary() {
        return summary;
    }

    public void setSummary(double summary) {
        this.summary = summary;
    }

    public void setCartType(int currentCartType)
    {
        this.boughtType = currentCartType;
        reload();

    }

    public void setCartTypeText(String currentCartTypeText)
    {
        this.titleTextView.setText(currentCartTypeText);

    }

}
