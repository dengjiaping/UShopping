package com.brand.ushopping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.brand.ushopping.R;
import com.brand.ushopping.widget.ScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ScaleImageViewActivity extends Activity  implements android.view.GestureDetector.OnGestureListener{
    private ScaleImageView imageview;
    private ImageView closeBtn;

    private ViewFlipper flipper;
    private GestureDetector gestureDetector;
    private int currentPage = 0;
    private int totalPage;

    private Button prevBtn;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scale_image_view);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        gestureDetector = new GestureDetector(this);    // 声明检测手势事件

        Bundle bundle = getIntent().getExtras();
        String[] imgList = bundle.getStringArray("imgList");

        totalPage = imgList.length;

        for(int a = 0; a<imgList.length; a++)
        {
            ScaleImageView iv = new ScaleImageView(this);
//            iv.setImageResource(imgList[a]);
            ImageLoader.getInstance().displayImage(imgList[a], iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            flipper.addView(iv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        }

        prevBtn = (Button) findViewById(R.id.prev);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipPrev();
            }
        });

        nextBtn = (Button) findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipNext();
            }
        });

        closeBtn = (ImageView) findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleImageViewActivity.this.finish();
            }
        });

//        String url = bundle.getString("url");

//        imageview = (ScaleImageView) findViewById(R.id.imgview);
//        ImageLoader.getInstance().displayImage(url, imageview);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        flipper.stopFlipping();             // 点击事件后，停止自动播放
        flipper.setAutoStart(false);
        return gestureDetector.onTouchEvent(event);         // 注册手势事件
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
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getX() - e1.getX() > 120) {            // 从左向右滑动（左进右出）
            flipNext();

            return true;
        } else if (e2.getX() - e1.getX() < -120) {        // 从右向左滑动（右进左出）
            flipPrev();

            return true;
        }
        return true;
    }

    private void flipPrev()
    {
        if(currentPage > 0)
        {
            Animation rInAnim = AnimationUtils.loadAnimation(ScaleImageViewActivity.this, R.anim.push_right_in);  // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(ScaleImageViewActivity.this, R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            flipper.setInAnimation(rInAnim);
            flipper.setOutAnimation(rOutAnim);
            flipper.showPrevious();
            currentPage--;
        }
    }

    private void flipNext()
    {
        if(currentPage < totalPage)
        {
            Animation lInAnim = AnimationUtils.loadAnimation(ScaleImageViewActivity.this, R.anim.push_left_in);       // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
            Animation lOutAnim = AnimationUtils.loadAnimation(ScaleImageViewActivity.this, R.anim.push_left_out);     // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

            flipper.setInAnimation(lInAnim);
            flipper.setOutAnimation(lOutAnim);
            flipper.showNext();
            currentPage++;
        }
    }

}
