package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/4/5.
 */
public class UActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void loginOrRegister()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请先注册或登录");
        builder.setTitle("提示");
        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                Intent intent = new Intent(UActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                Intent intent = new Intent(UActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        builder.create().show();
    }

}
