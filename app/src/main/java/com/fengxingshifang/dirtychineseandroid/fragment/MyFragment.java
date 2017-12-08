package com.fengxingshifang.dirtychineseandroid.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fengxingshifang.dirtychineseandroid.InfoActivity;
import com.fengxingshifang.dirtychineseandroid.LoginActivity;
import com.fengxingshifang.dirtychineseandroid.MainActivity;
import com.fengxingshifang.dirtychineseandroid.MycontentActivity;
import com.fengxingshifang.dirtychineseandroid.R;
import com.fengxingshifang.dirtychineseandroid.SettingActivity;

import java.util.ArrayList;

/**
 * Created by git on 2017/12/6.
 */

public class MyFragment extends BaseFragment {

    private static final String TAG = MyFragment.class.getSimpleName();

    private ListView my_lv_list_mine;
    private ListView my_lv_list_setting;
    private ArrayList<String> myLvListMineData;
    private ArrayList<String> myLvListSettingData;
    private MineAdapter mMineAdapter;
    private SettingAdapter mSettingAdapter;
    private Button btn_login;




    @Override
    public View initView() {
        Log.e(TAG,"my Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_my,null);
        my_lv_list_mine = (ListView) view.findViewById(R.id.my_lv_list_mine);
        my_lv_list_mine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data =  myLvListMineData.get(position);
//                Toast.makeText(mContext, "data=="+data, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MycontentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("position", position);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        my_lv_list_setting = (ListView) view.findViewById(R.id.my_lv_list_setting);
        my_lv_list_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data =  myLvListSettingData.get(position);
//                Toast.makeText(mContext, "data=="+data, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        return view;
    }

    @Override
    public void initData() {
        myLvListMineData = new ArrayList<String>();
        myLvListMineData.add("我的收藏");
        myLvListMineData.add("我看过的");
        myLvListMineData.add("我发表的");
        myLvListMineData.add("我暂存的");
        myLvListMineData.add("我评论过的");
        mMineAdapter = new MineAdapter();
        my_lv_list_mine.setAdapter(mMineAdapter);

        myLvListSettingData = new ArrayList<String>();
        myLvListSettingData.add("设置");
        mSettingAdapter = new SettingAdapter();
        my_lv_list_setting.setAdapter(mSettingAdapter);
    }



    class MineAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return myLvListMineData.size();
    }

    @Override
    public String getItem(int position) {
        return myLvListMineData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(mContext);
        textView.setPadding(10,10,0,10);
        textView.setTextColor(Color.GRAY);
        textView.setTextSize(14);
        textView.setText(myLvListMineData.get(position));
        return textView;
    }

    }

    class SettingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myLvListSettingData.size();
        }

        @Override
        public String getItem(int position) {
            return myLvListSettingData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setPadding(10,10,0,10);
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(14);
            textView.setText(myLvListSettingData.get(position));
            return textView;
        }

    }
    public void showToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
