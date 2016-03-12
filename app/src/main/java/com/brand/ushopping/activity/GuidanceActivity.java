package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.brand.ushopping.R;

public class GuidanceActivity extends Activity implements android.view.GestureDetector.OnGestureListener{
    private ViewFlipper flipper;
    private GestureDetector gestureDetector;

    private int[] imgs = { R.drawable.guidence_1, R.drawable.guidence_2,
            R.drawable.guidence_3};

    private int currentPage = 0;
    private int totalPage = imgs.length - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guidance);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        gestureDetector = new GestureDetector(this);    // 声明检测手势事件

        for (int i = 0; i < imgs.length; i++) {          // 添加图片源
            ImageView iv = new ImageView(this);
            iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            flipper.addView(iv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        Intent intent = new Intent(GuidanceActivity.this, SplashActivity.class);
        startActivity(intent);
        GuidanceActivity.this.finish();

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        flipper.stopFlipping();             // 点击事件后，停止自动播放
        flipper.setAutoStart(false);
        return gestureDetector.onTouchEvent(event);         // 注册手势事件
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getX() - e1.getX() > 120) {            // 从左向右滑动（左进右出）
            if(currentPage > 0)
            {
                Animation rInAnim = AnimationUtils.loadAnimation(GuidanceActivity.this, R.anim.push_left_in);  // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
                Animation rOutAnim = AnimationUtils.loadAnimation(GuidanceActivity.this, R.anim.push_left_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

                flipper.setInAnimation(rInAnim);
                flipper.setOutAnimation(rOutAnim);
                flipper.showPrevious();
                currentPage--;
            }

            return true;
        } else if (e2.getX() - e1.getX() < -120) {        // 从右向左滑动（右进左出）
            if(currentPage < totalPage)
            {
                Animation lInAnim = AnimationUtils.loadAnimation(GuidanceActivity.this, R.anim.push_left_in);       // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
                Animation lOutAnim = AnimationUtils.loadAnimation(GuidanceActivity.this, R.anim.push_left_out);     // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

                flipper.setInAnimation(lInAnim);
                flipper.setOutAnimation(lOutAnim);
                flipper.showNext();
                currentPage++;
            }

            return true;
        }
        return true;
    }
}
