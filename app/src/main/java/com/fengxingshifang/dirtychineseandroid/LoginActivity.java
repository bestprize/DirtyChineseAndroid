package com.fengxingshifang.dirtychineseandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by git on 2017/12/7.
 */

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_login);
    }
}
