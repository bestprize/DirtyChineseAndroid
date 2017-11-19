package com.fengxingshifang.dirtychineseandroid;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.fengxingshifang.dirtychineseandroid.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by bestprize on 2017/11/11.
 */

public class WelcomeActivity extends Activity {

    private ViewPager mViewPager;
    private Button btnStart;

    private ArrayList<ImageView> mImageViewList; // imageView集合

    // 引导页图片id数组
    private int[] mImageIds = new int[] { R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题,
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_welcome);

        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        btnStart = (Button) findViewById(R.id.btn_start);

        initData();// 先初始化数据
        mViewPager.setAdapter(new GuideAdapter());// 设置数据




        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 某个页面被选中
                if (position == mImageViewList.size() - 1) {// 最后一个页面显示开始体验的按钮
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面状态发生变化的回调
            }
        });






        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //更新sp, 已经不是第一次进入了
//                PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);

                //跳到主页面
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    // 初始化数据
    private void initData() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);// 通过设置背景,可以让宽高填充布局
            // view.setImageResource(resId)
            mImageViewList.add(view);


        }
    }

    class GuideAdapter extends PagerAdapter {

        // item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // 初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        // 销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
