package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.widget.BirthdayPickDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends Activity {
    private ImageView backBtn;
    private TextView titleTextView;
    private AppContext appContext;
    private User user;

    private TextView editTextView;
    private ImageView headImgView;
    private TextView userNameTextView;
    private TextView telTextView;
    private TextView addressTextView;
    private TextView birthdayTextView;

    private EditText userNameEditText;
    private EditText addressEditText;
    private TextView birthdayEditText;
    private ViewGroup confirmEditLayout;
    private Button confirmBtn;
    private Button cancelBtn;
    private BirthdayPickDialog birthdayPickDialog;

    private int currentMode;

    private static final int MODE_VIEW = 1;
    private static final int MODE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

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

        headImgView = (ImageView) findViewById(R.id.head_img);
        userNameTextView = (TextView) findViewById(R.id.username);
        telTextView = (TextView) findViewById(R.id.tel);
        addressTextView = (TextView) findViewById(R.id.address);
        birthdayTextView = (TextView) findViewById(R.id.birthday);

        userNameEditText = (EditText) findViewById(R.id.username_edit);
        addressEditText = (EditText) findViewById(R.id.address_edit);
        birthdayEditText = (TextView) findViewById(R.id.birthday_edit);

        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayPickDialog = new BirthdayPickDialog(ProfileActivity.this);

            }
        });

        editTextView = (TextView) findViewById(R.id.edit);
        editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameEditText.setText(userNameTextView.getText());
                addressEditText.setText(addressTextView.getText());
                birthdayEditText.setText(birthdayTextView.getText());

                setMode(MODE_EDIT);

            }
        });

        confirmEditLayout = (ViewGroup) findViewById(R.id.confirm_edit_layout);
        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUserName(userNameEditText.getText().toString());
                user.setAddress(addressEditText.getText().toString());
                user.setBirthday(birthdayEditText.getText().toString());

                if(user.updateValidation())
                {
                    new UpdateAppUserTask().execute(user);
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, user.getMsg(), Toast.LENGTH_SHORT).show();

                }

            }
        });

        cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(MODE_VIEW);

            }
        });

        setMode(MODE_VIEW);

        headImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ScaleImageViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", user.getHeadImg());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();

    }

    private void reload()
    {
        user = appContext.getUser();

        if(CommonUtils.isValueEmpty(user.getHeadImg()))
        {
            ImageLoader.getInstance().displayImage("drawable://"+R.drawable.logo, headImgView);
        }
        else
        {
            ImageLoader.getInstance().displayImage(user.getHeadImg(), headImgView);
        }

        if(CommonUtils.isValueEmpty(user.getUserName()))
        {
            userNameTextView.setText("新用户");
        }
        else
        {
            userNameTextView.setText(user.getUserName());
        }

        telTextView.setText(user.getMobile());
        addressTextView.setText(user.getAddress());
        birthdayTextView.setText(user.getBirthday());

    }

    private void setMode(int mode)
    {
        switch (mode)
        {
            case MODE_VIEW:
                userNameTextView.setVisibility(View.VISIBLE);
                addressTextView.setVisibility(View.VISIBLE);
                birthdayTextView.setVisibility(View.VISIBLE);

                userNameEditText.setVisibility(View.GONE);
                addressEditText.setVisibility(View.GONE);
                birthdayEditText.setVisibility(View.GONE);

                confirmEditLayout.setVisibility(View.GONE);

                break;

            case MODE_EDIT:
                userNameTextView.setVisibility(View.GONE);
                addressTextView.setVisibility(View.GONE);
                birthdayTextView.setVisibility(View.GONE);

                userNameEditText.setVisibility(View.VISIBLE);
                addressEditText.setVisibility(View.VISIBLE);
                birthdayEditText.setVisibility(View.VISIBLE);

                confirmEditLayout.setVisibility(View.VISIBLE);

                break;
        }

    }

    //用户信息更新
    public class UpdateAppUserTask extends AsyncTask<User, Void, User>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(User... users) {
            return new UserAction().updateAppUser(users[0]);
        }

        @Override
        protected void onPostExecute(User result) {
            if(result != null)
            {
                if(result.getSuccess())
                {
                    userNameTextView.setText(userNameEditText.getText());
                    addressTextView.setText(addressEditText.getText());
                    birthdayTextView.setText(birthdayEditText.getText());

                    user.setUserName(userNameEditText.getText().toString());
                    user.setAddress(addressEditText.getText().toString());
                    user.setBirthday(birthdayEditText.getText().toString());
                    appContext.setUser(user);
                    new RefAction().setUser(ProfileActivity.this, user);

                    setMode(MODE_VIEW);

                }
                else
                {
                    Toast.makeText(ProfileActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(ProfileActivity.this, "用户信息修改失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setBirthday(String birthday)
    {
        birthdayEditText.setText(birthday);

    }

}
