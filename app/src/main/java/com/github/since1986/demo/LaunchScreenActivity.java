package com.github.since1986.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class LaunchScreenActivity extends Activity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        viewPager = findViewById(R.id.view_pager_activity_launch_screen);
        linearLayout = findViewById(R.id.linear_layout_activity_launch_screen);

        final List<View> viewList = new ArrayList<>();

        ImageView imageView1 = new ImageView(this);
        imageView1.setAdjustViewBounds(true);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView1.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.bg_launch_screen_1));

        ImageView imageView2 = new ImageView(this);
        imageView2.setAdjustViewBounds(true);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView2.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.bg_launch_screen_2));

        ImageView imageView3 = new ImageView(this);
        imageView3.setAdjustViewBounds(true);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView3.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.bg_launch_screen_3));
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
            }
        });

        final float[] actionDownY = new float[1];
        final float[] actionDownX = new float[1];
        imageView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { //设置Toolbar双击滚动到顶
                //这里没有使用 GestureDetector 中的 onFling来处理，而是回归到原始的 switch (event.getAction()) case 的方式，是因为不知道为什么会有一定几率会发出ACTION_CANCEL 因而无法触发 onFling
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        actionDownY[0] = event.getRawY();
                        actionDownX[0] = event.getRawX();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (event.getRawX() - actionDownX[0] < 0) {
                            startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
                        }
                        break;
                }

                return true;
            }
        });

        viewList.add(imageView1);
        viewList.add(imageView2);
        viewList.add(imageView3);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.d(getLocalClassName(), "instantiateItem position: " + position);
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.d(getLocalClassName(), "destroyItem position: " + position);
                container.removeView(viewList.get(position));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0f) {
                    linearLayout.getChildAt(position).setActivated(true);
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        if (i != position) {
                            linearLayout.getChildAt(i).setActivated(false);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(getLocalClassName(), "state: " + state);
            }
        });
    }
}
