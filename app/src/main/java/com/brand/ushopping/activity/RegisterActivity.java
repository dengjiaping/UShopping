package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.RefAction;
import com.brand.ushopping.action.UserAction;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;

public class RegisterActivity extends Activity {
    private EditText mobileEditText;
    private EditText passEditText;
    private EditText smsEditText;
    private Button getSmsBtn;
    private Button registerBtn;
    private AppContext appContext;
    private ImageView backBtn;
    private TextView titleTextView;

    private String mobile;
    private String pass;
    private String smsCode;

    private int smsIntervalTime = 60000;
    private long currentTimeMil;
    private TimeoutbleProgressDialog registerDialog;
    private boolean smsBtnEnable = true;

    //是否启用短信验证码
    private boolean smsEnable = true;

    private Handler getSmsCodeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        appContext = (AppContext) getApplicationContext();

        registerDialog = TimeoutbleProgressDialog.createProgressDialog(RegisterActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                registerDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("网络连接失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });

        mobileEditText = (EditText) findViewById(R.id.mobile);
        passEditText = (EditText) findViewById(R.id.pass);
        smsEditText = (EditText) findViewById(R.id.sms);
        getSmsBtn = (Button) findViewById(R.id.get_sms);
        getSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(smsBtnEnable)
                {
                    mobile = mobileEditText.getText().toString();
                    if(mobile.isEmpty())
                    {
                        Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        smsBtnEnable = false;
                        getSmsBtn.setBackgroundColor(getResources().getColor(R.color.bg_grey));
                        currentTimeMil = System.currentTimeMillis();
                        new SmsIntervalTask().start();
                        SMSSDK.getVerificationCode("86", mobile);

                    }

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "验证码获取间隔时间为一分钟,请稍等...", Toast.LENGTH_SHORT).show();

                }

            }
        });

        SMSSDK.initSDK(this, "ef5e51f8a0e4", "a8bb954f68d7e26c5b3d748f6c76d7c8");
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);

        if(!smsEnable)
        {
            getSmsBtn.setVisibility(View.GONE);
            smsEditText.setVisibility(View.GONE);

        }

        registerBtn = (Button) findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsCode = smsEditText.getText().toString();
                mobile = mobileEditText.getText().toString();
                pass = passEditText.getText().toString();

                if(mobile == null || mobile.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //手机号校验
                if(mobile.length() != 11)
                {
                    Toast.makeText(RegisterActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass == null || pass.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(smsEnable)
                {
                    if(smsCode.isEmpty())
                    {
                        Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if(smsEnable)
                {
                    SMSSDK.submitVerificationCode("86", mobile, smsCode);

                }
                else
                {
                    register();

                }

            }
        });

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        getSmsCodeHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case StaticValues.GET_SMS_CODE_HANDLER:
                        smsBtnEnable = true;
                        getSmsBtn.setBackgroundColor(getResources().getColor(R.color.buttonl_bg_yellow));

                        break;
                }
                super.handleMessage(msg);
            }
        };

    }

    private void register()
    {
        User user = new User();

        user.setMobile(mobile);
        user.setPass(pass);
        user.setToken(appContext.getXgPushToken());

        new RegisterTask().execute(user);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();

                    //注册
                    register();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();

                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();

                }
            } else {
                ((Throwable) data).printStackTrace();
                int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
                Toast.makeText(RegisterActivity.this, "验证码发送错误", Toast.LENGTH_SHORT).show();
                if (resId > 0) {
                    Toast.makeText(RegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
                }
            }

        }

    };

    //注册事件
    public class RegisterTask extends AsyncTask<User, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerDialog.show();
            registerBtn.setEnabled(false);

        }

        @Override
        protected User doInBackground(User... user) {
            return new UserAction(RegisterActivity.this).register(user[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            registerDialog.dismiss();
            registerBtn.setEnabled(true);

            if(user != null)
            {
                if(user.getSuccess())
                {
                    //注册成功
                    new RefAction(RegisterActivity.this).setUser(user);
                    appContext.setUser(user);
                    appContext.setSessionid(user.getSessionid());

                    RegisterActivity.this.finish();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, user.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class SmsIntervalTask extends Thread
    {
        @Override
        public void run() {
            int timeInterval = (int) (System.currentTimeMillis() - currentTimeMil);
            if(timeInterval < smsIntervalTime)
            {
                try {
                    sleep(smsIntervalTime - timeInterval);
                    Log.v("uregister", Long.toString(timeInterval / 60));

                    Message message = new Message();
                    message.what = StaticValues.GET_SMS_CODE_HANDLER;
                    message.arg1 = timeInterval;

                    getSmsCodeHandler.sendMessage(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                getSmsBtn.setText("获取验证码");

                smsBtnEnable = true;

            }

        }

    }

}
