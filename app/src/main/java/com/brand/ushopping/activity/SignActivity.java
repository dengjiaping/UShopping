package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.SignAction;
import com.brand.ushopping.model.Sign;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;

public class SignActivity extends Activity {
    private AppContext appContext;
    private User user;
    private TextView fateTextView;
    private int fate;
    private long reTime;
    private TextView warningTextView;
    private Button signBtn;
    private Button shareBtn;

    private Sign sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign);
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        fateTextView = (TextView) findViewById(R.id.fate);
        warningTextView = (TextView) findViewById(R.id.warning);

        signBtn = (Button) findViewById(R.id.sign);
        signBtn.setEnabled(false);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = new Sign();
                sign.setUserId(user.getUserId());
                sign.setSessionid(user.getSessionid());
                new SignActionTask().execute(sign);
            }
        });

        shareBtn = (Button) findViewById(R.id.share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

//        sign = new RefAction().getSign(SignActivity.this);
        reload();
    }

    private void reload()
    {
        Sign sign = new Sign();
        sign.setUserId(user.getUserId());
        sign.setSessionid(user.getSessionid());
        new GetSignActionTask().execute(sign);
    }

    //签到天数任务
    public class GetSignActionTask extends AsyncTask<Sign, Void, Sign>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Sign doInBackground(Sign... signs) {
            return new SignAction().getSignAction(signs[0]);
        }

        @Override
        protected void onPostExecute(Sign result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    fate = result.getFate();
                    fateTextView.setText(Integer.toString(fate));
                    reTime = result.getReTime();
                    Log.v("ushopping", "last sign time " + reTime + "  " + CommonUtils.timestampToDatetime(reTime));

                    if(reTime == 0)
                    {
                        signBtn.setEnabled(true);

                    }
                    else
                    {
                        if(CommonUtils.isSameDay(reTime, System.currentTimeMillis()))
                        {
                            warningTextView.setText("您今天已签到");

                        }
                        else
                        {
                            signBtn.setEnabled(true);

                        }
                    }

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignActivity.this);
                    builder.setMessage("获取签到天数失败,请重新登录");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            SignActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignActivity.this);
                builder.setMessage("获取签到天数失败,请重新登录");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SignActivity.this.finish();

                    }
                });
                builder.create().show();

            }

        }
    }

    //签到任务
    public class SignActionTask extends AsyncTask<Sign, Void, Sign>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Sign doInBackground(Sign... signs) {
            return new SignAction().signAction(signs[0]);
        }

        @Override
        protected void onPostExecute(Sign result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    Toast.makeText(SignActivity.this, "签到已完成", Toast.LENGTH_SHORT).show();

                    fate += 1;
//                    Sign sign = new Sign();
//                    sign.setFate(fate);
//                    sign.setReTime(new Date().getTime());
//                    new RefAction().setSign(SignActivity.this, result);

                    fateTextView.setText(Integer.toString(fate));

                    signBtn.setEnabled(false);
                }
                else
                {
                    Toast.makeText(SignActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(SignActivity.this, "获取签到天数失败", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
