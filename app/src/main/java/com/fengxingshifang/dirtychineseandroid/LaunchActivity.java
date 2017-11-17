package com.fengxingshifang.dirtychineseandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fengxingshifang.dirtychineseandroid.utils.PrefUtils;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //在主线程中执行
                startMainActivity();
            }
        }, 500);
    }

    private void startMainActivity() {
        boolean isFirstEnter = PrefUtils.getBoolean(
                this, "is_first_enter", true);

        Intent intent;
        if (isFirstEnter) {
            // 新手引导
            intent = new Intent(getApplicationContext(),
                    WelcomeActivity.class);
        } else {
            // 主页面
            intent = new Intent(getApplicationContext(),
                    MainActivity.class);
        }

        startActivity(intent);

        finish();// 结束当前页面
    }
}
