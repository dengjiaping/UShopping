package com.brand.ushopping.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.UserRewardActivity;

/**
 * Created by Administrator on 2016/2/17.
 */
public class RewardConfirmDialog extends PopupWindow {
    private Dialog dialog;
    private UserRewardActivity activity;

    private TextView addressTextView;
    private Button chooseAddressBtn;
    private Button confirmBtn;
    private Button cancelBtn;

    public RewardConfirmDialog(Context context)
    {
        this.activity = (UserRewardActivity) context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.reward_confirm_dialog);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);

        addressTextView = (TextView) dialog.findViewById(R.id.address);
        chooseAddressBtn = (Button) dialog.findViewById(R.id.choose_address);
        chooseAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.pickAddress();

            }
        });

        confirmBtn = (Button) dialog.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.rewardAction();

            }
        });

        cancelBtn = (Button) dialog.findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }


    public void setAddressTextView(String deaddress)
    {
        addressTextView.setText(deaddress);

    }
}
