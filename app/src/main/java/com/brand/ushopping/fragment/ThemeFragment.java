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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.ThemeAction;
import com.brand.ushopping.activity.MainActivity;
import com.brand.ushopping.activity.SearchActivity;
import com.brand.ushopping.adapter.ThemeItemAdapter;
import com.brand.ushopping.model.AppTheme;
import com.brand.ushopping.model.AppThemeItem;
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
 * {@link ThemeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThemeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TextView titleTextView;
    private ImageView searchBtn;
    private TimeoutbleProgressDialog getinfoDialog;
    private ListView themeListView;
    private ThemeItemAdapter themeItemAdapter;

    private AppContext appContext;
    private User user;
    private ArrayList<AppThemeItem> appThemeItems;

    private MainActivity mainActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ThemeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThemeFragment newInstance() {
        ThemeFragment fragment = new ThemeFragment();

        return fragment;
    }

    public ThemeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
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

        themeListView = (ListView) view.findViewById(R.id.theme_list);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appContext = (AppContext) getActivity().getApplicationContext();
        user = appContext.getUser();

        reload();
        mainActivity.setButtomBarEnable(true);
    }

    private void reload()
    {
        AppTheme appTheme = new AppTheme();
        if(user != null)
        {
            appTheme.setUserId(user.getUserId());
            appTheme.setSessionid(user.getSessionid());

        }

        new ThemeLoadTask().execute(appTheme);

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
    public class ThemeLoadTask extends AsyncTask<AppTheme, Void, AppTheme>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getinfoDialog.show();

        }

        @Override
        protected AppTheme doInBackground(AppTheme... appThemes) {
            return new ThemeAction().getAppThemeAllAction(getActivity(), appThemes[0]);
        }

        @Override
        protected void onPostExecute(AppTheme result) {
            getinfoDialog.dismiss();

            if(result != null)
            {
                if(result.isSuccess())
                {
                    appThemeItems = result.getAppThemeItems();

                    List listData = new ArrayList<Map<String,Object>>();
                    for(AppThemeItem appThemeItem: appThemeItems)
                    {
                        Map line = new HashMap();

                        line.put("id", appThemeItem.getId());
                        line.put("img", appThemeItem.getImg());
                        line.put("url", appThemeItem.getImgUrl());

                        listData.add(line);

                    }
                    themeItemAdapter = new ThemeItemAdapter(listData, getActivity());
                    themeListView.setAdapter(themeItemAdapter);

                }
                else
                {

                }
            }
            else
            {
                Toast.makeText(getActivity(), "未获取到品牌信息", Toast.LENGTH_SHORT).show();

            }

        }
    }


}
