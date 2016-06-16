package com.brand.ushopping.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.BrandAction;
import com.brand.ushopping.activity.BrandActivity;
import com.brand.ushopping.activity.MainActivity;
import com.brand.ushopping.activity.SearchActivity;
import com.brand.ushopping.adapter.BrandAdapter;
import com.brand.ushopping.adapter.BrandAllAdapter;
import com.brand.ushopping.model.Brand;
import com.brand.ushopping.model.BrandRecommend;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrandFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrandFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private TextView titleTextView;
    private ImageView searchBtn;
    private AppContext appContext;
    private User user;
    private GridView brandAllView;
    private ListView recommendListView;
    private OnFragmentInteractionListener mListener;
    private TimeoutbleProgressDialog getinfoDialog;

    private ArrayList<Brand> appBrandAll;
    private ArrayList<Brand> recommend;

    private MainActivity mainActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BrandFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrandFragment newInstance() {
        BrandFragment fragment = new BrandFragment();

        return fragment;
    }

    public BrandFragment() {
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
        View view = inflater.inflate(R.layout.fragment_brand, container, false);

        titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText(getActivity().getTitle().toString());

        searchBtn = (ImageView) view.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

            }
        });

        brandAllView = (GridView) view.findViewById(R.id.brand_all);
        brandAllView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int a=0; a<appBrandAll.size(); a++)
                {
                    Brand brand = appBrandAll.get(a);
                    if(brand.getId() == id)
                    {
                        Intent intent = new Intent(getActivity(), BrandActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("brand", brand);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                }
            }
        });

        recommendListView = (ListView) view.findViewById(R.id.recommend);
        recommendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int a=0; a<recommend.size(); a++)
                {
                    Brand brand = recommend.get(a);
                    if(brand.getId() == id)
                    {
                        Intent intent = new Intent(getActivity(), BrandActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("brand", brand);
                        bundle.putInt("enterType", StaticValues.BRAND_ENTER_TYPE_NORMAL);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }

            }
        });

        getinfoDialog = TimeoutbleProgressDialog.createProgressDialog(getActivity(), StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                getinfoDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("未获取到品牌信息");
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appContext = (AppContext) getActivity().getApplicationContext();
        user = appContext.getUser();

        BrandRecommend brandRecommend = new BrandRecommend();
        if(user != null)
        {
            brandRecommend.setUserId(user.getUserId());
            brandRecommend.setSessionid(user.getSessionid());
        }

        new BrandLoadTask().execute(brandRecommend);

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

    //推荐品牌加载
    public class BrandLoadTask extends AsyncTask<BrandRecommend, Void, BrandRecommend>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getinfoDialog.show();

        }

        @Override
        protected BrandRecommend doInBackground(BrandRecommend... brands) {
            return new BrandAction().getRecommendAppBrandAction(getActivity(), brands[0]);
        }

        @Override
        protected void onPostExecute(BrandRecommend result) {
            getinfoDialog.dismiss();

            if(result != null)
            {
                if(result.isSuccess())
                {
                    appBrandAll = result.getAppBrandAll();
                    recommend = result.getRecommend();

                    //推荐品牌
                    List listData = new ArrayList<Map<String,Object>>();
                    for(Brand brand: result.getRecommend())
                    {
                        Map line = new HashMap();

                        line.put("id", brand.getId());
                        line.put("name", brand.getBrandName());
                        line.put("logopic", brand.getLogopic());
                        line.put("intro", brand.getDetail());
                        line.put("favouriteCount", 123);

                        listData.add(line);

                    }
                    BrandAdapter adapter = new BrandAdapter(listData, getActivity());
                    recommendListView.setAdapter(adapter);

                    int totalHeight = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        View listItem = adapter.getView(i, null, recommendListView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams params = recommendListView.getLayoutParams();
                    params.height = totalHeight + (recommendListView.getDividerHeight() * (adapter.getCount() - 1));
                    ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
                    recommendListView.setLayoutParams(params);

                    //全部品牌
                    listData = new ArrayList<Map<String,Object>>();
                    for(Brand brand: result.getAppBrandAll())
                    {
                        Map line = new HashMap();

                        line.put("id", brand.getId());
                        line.put("name", brand.getId());
                        line.put("logopic", brand.getLogopic());

                        listData.add(line);

                    }
                    BrandAllAdapter adapterAll = new BrandAllAdapter(listData, getActivity());
                    brandAllView.setAdapter(adapterAll);

                    totalHeight = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        View listItem = adapter.getView(i, null, recommendListView);
                        listItem.measure(0, 0);
                        if((i / 3) == 0)
                        {
                            totalHeight += listItem.getMeasuredHeight();
                        }
                    }

                    params = brandAllView.getLayoutParams();
                    params.height = totalHeight;
                    brandAllView.setLayoutParams(params);
                }
            }
            else
            {
                Toast.makeText(getActivity(), "未获取到品牌信息", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
