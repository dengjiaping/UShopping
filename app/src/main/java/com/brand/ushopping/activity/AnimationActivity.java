package com.brand.ushopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.brand.ushopping.R;

public class AnimationActivity extends AppCompatActivity {
    private ImageView icon1;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

//        animation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.translate_tester_1);
        animation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.rotate_tester_1);

        icon1 = (ImageView) findViewById(R.id.icon_1);
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon1.startAnimation(animation);

            }
        });

    }
}
