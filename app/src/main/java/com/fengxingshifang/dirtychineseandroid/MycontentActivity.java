package com.fengxingshifang.dirtychineseandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;

/**
 * Created by git on 2017/12/7.
 */

public class MycontentActivity extends AppCompatActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_mycontent);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        position = (int) bundle.getSerializable("position");

    }
}
