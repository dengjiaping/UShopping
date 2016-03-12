package com.brand.ushopping.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.ProfileActivity;

/**
 * Created by Administrator on 2016/2/26.
 */
public class BirthdayPickDialog extends PopupWindow {
    private Dialog dialog;
    private ProfileActivity activity;

    private DatePicker datePicker;
    private Button confirmBtn;
    private Button cancelBtn;

    public BirthdayPickDialog(Context context)
    {
        this.activity = (ProfileActivity) context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.birthday_pick_dialog);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);

        datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        confirmBtn = (Button) dialog.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthday = datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth();
                Log.v("birthday", birthday);

                activity.setBirthday(birthday);
                dialog.dismiss();

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

}
