package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.brand.ushopping.model.WeiboUser;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class LoginActivity extends Activity implements IWXAPIEventHandler {
    private EditText usernameEditText;
    private EditText passEditText;
    private Button loginBtn;
    private String username;
    private String pass;
    private AppContext appContext;
    private User user;

    private ImageView backBtn;
    private TextView titleTextView;
    private TextView registerTextView;

    private ImageView wxLoginBtn;
    private ImageView qqLoginBtn;
    private ImageView weiboLoginBtn;

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private UsersAPI usersAPI;
    private String weiboUserName;
    private String weiboAccessToken;

    private Tencent mTencent = null;
    private IWXAPI iwxapi = null;

    private TimeoutbleProgressDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        appContext = (AppContext) getApplicationContext();

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());
        registerTextView = (TextView) findViewById(R.id.register);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();

            }
        });

        usernameEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.pass);
        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                if(username == null || username.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "用户名/手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                pass = passEditText.getText().toString();
                if(pass == null || pass.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User();
                if(CommonUtils.isNumber(username))
                {
                    user.setMobile(username);
                }
                else
                {
                    user.setUserName(username);

                }
                user.setPass(pass);

                new LoginTask().execute(user);
            }
        });

        wxLoginBtn = (ImageView) findViewById(R.id.wx_login);
        qqLoginBtn = (ImageView) findViewById(R.id.qq_login);

        // 创建授权认证信息
        mAuthInfo = new AuthInfo(this, StaticValues.APP_KEY, StaticValues.REDIRECT_URL, StaticValues.SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

        weiboLoginBtn = (ImageView) findViewById(R.id.weibo_login);
        weiboLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });

        qqLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTencent == null)
                {
                    mTencent = Tencent.createInstance(StaticValues.TENCENT_APP_ID, appContext);

                    if (!mTencent.isSessionValid())
                    {
                          mTencent.login(LoginActivity.this, "all", new TencentUiListener());

                    }
                }
            }
        });

        wxLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iwxapi == null)
                {
                    iwxapi = WXAPIFactory.createWXAPI(LoginActivity.this, StaticValues.WX_APP_ID, true);
                    iwxapi.registerApp(StaticValues.WX_APP_ID);

                }
                if (!iwxapi.isWXAppInstalled()) {
                    //提醒用户没有按照微信
                    Toast.makeText(LoginActivity.this, "没有安装微信", Toast.LENGTH_SHORT).show();
                    return;

                }

                iwxapi.registerApp(StaticValues.WX_APP_ID);

                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                iwxapi.sendReq(req);


            }
        });

        loginDialog = TimeoutbleProgressDialog.createProgressDialog(LoginActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                loginDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("登录失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginBtn.setEnabled(true);
                    }
                });

                builder.create().show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Toast.makeText(this, "微信登录成功", Toast.LENGTH_LONG).show();

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "微信登录取消", Toast.LENGTH_LONG).show();

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "微信登录失败", Toast.LENGTH_LONG).show();

                break;
            default:
                Toast.makeText(this, "未知错误", Toast.LENGTH_LONG).show();

                break;
        }


    }

    //登录事件
    public class LoginTask extends AsyncTask<User, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginBtn.setEnabled(false);
            loginDialog.show();

        }

        @Override
        protected User doInBackground(User... user) {
            return new UserAction().login(user[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            loginBtn.setEnabled(true);
            loginDialog.dismiss();

            if(user != null)
            {
                if(user.getSuccess())
                {
                    //登录成功
                    new RefAction().setUser(LoginActivity.this, user);
                    appContext.setUser(user);
                    appContext.setSessionid(user.getSessionid());

                    LoginActivity.this.finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, user.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            weiboUserName = values.getString("userName");
            weiboAccessToken = values.getString("access_token");

            usersAPI = new UsersAPI(LoginActivity.this, StaticValues.APP_KEY, mAccessToken);
            long uid = Long.parseLong(mAccessToken.getUid());
            usersAPI.show(uid, new UserListener());

        }

        class UserListener implements RequestListener
        {
            @Override
            public void onComplete(String response) {
                if(!response.isEmpty())
                {
                    com.sina.weibo.sdk.openapi.models.User user = com.sina.weibo.sdk.openapi.models.User.parse(response);
                    if(user != null)
                    {
                        //{"id":1662713331,"idstr":"1662713331","class":1,"screen_name":"Viator42X","name":"Viator42X","province":"37","city":"1","location":"山东 济南","description":"","url":"","profile_image_url":"http://tp4.sinaimg.cn/1662713331/50/5721488565/1","profile_url":"u/1662713331","domain":"","weihao":"","gender":"m","followers_count":71,"friends_count":639,"pagefriends_count":4,"statuses_count":415,"favourites_count":58,"created_at":"Sat Nov 21 23:25:29 +0800 2009","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","status":{"created_at":"Thu Jun 18 08:04:11 +0800 2015","id":3854994828619589,"mid":"3854994828619589","idstr":"3854994828619589","text":"转发微博","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/c66T5g\" rel=\"nofollow\">Android客户端</a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"annotations":[{"client_mblogid":"0101d166-a2a8-4135-bec5-463c2bcc01ba"}],"reposts_count":0,"comments_count":0,"attitudes_count":0,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"userType":0},"ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/1662713331/180/5721488565/1","avatar_hd":"http://ww4.sinaimg.cn/crop.108.24.266.266.1024/631afdf3jw8eq80d9v4kbj20dc0a0gn7.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":9,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":13}
                        WeiboUser weiboUser = new WeiboUser();
                        weiboUser.setSina_id(user.id);
                        weiboUser.setAccess_token(weiboAccessToken);
                        weiboUser.setUserName(user.screen_name);
                        weiboUser.setHeadImg(user.profile_image_url);
                        weiboUser.setUrl(user.url);
                        weiboUser.setGender(user.gender);
                        weiboUser.setFollowMe(user.follow_me);
                        weiboUser.setVerified(user.verified);
                        new SinaLoginTask().execute(weiboUser);

                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        }

        @Override
        public void onCancel() {
//            Toast.makeText(LoginActivity.this,
//                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //微博登录事件
    public class SinaLoginTask extends AsyncTask<WeiboUser, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(WeiboUser... weiboUsers) {
            return new UserAction().sinaRegistered(weiboUsers[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            if(user != null)
            {
                if(user.getSuccess())
                {
                    //登录成功
                    new RefAction().setUser(LoginActivity.this, user);
                    appContext.setUser(user);
                    appContext.setSessionid(user.getSessionid());

                    LoginActivity.this.finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, user.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class TencentUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            JSONObject jsonObject = (JSONObject) response;

        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_SHORT).show();
        }
    }

}
