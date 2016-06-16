package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.R;

public class AboutUsActivity extends Activity {
    private ImageView backBtn;
    private TextView titleTextView;
    private Button devBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_us);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        devBtn = (Button) findViewById(R.id.dev);
        devBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AboutUsActivity.this, SelectDateActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("goods", null);
//                bundle.putInt("boughtType", 1);
//                intent.putExtras(bundle);
//                startActivity(intent);

                Intent intent = new Intent(AboutUsActivity.this, DevActivity.class);
                startActivity(intent);

            }
        });


    }



}
