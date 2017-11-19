package com.fengxingshifang.dirtychineseandroid.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fengxingshifang.dirtychineseandroid.base.BasePager;

/**
 * Created by bestprize on 2017/11/19.
 */

public class PublishPager extends BasePager {

    public PublishPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("设置初始化啦...");

        // 要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("发表");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);

        // 修改页面标题
        tvTitle.setText("发表");

        // 隐藏菜单按钮
        btnMenu.setVisibility(View.GONE);
    }


}
