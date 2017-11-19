package com.fengxingshifang.dirtychineseandroid.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fengxingshifang.dirtychineseandroid.MainActivity;
import com.fengxingshifang.dirtychineseandroid.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by bestprize on 2017/11/11.
 */

public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lvList;

    private ArrayList<String> mLeftMenuData;// 侧边栏网络数据对象

    private int mCurrentPos;// 当前被选中的item的位置

    private LeftMenuAdapter mAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        // lvList = (ListView) view.findViewById(R.id.lv_list);
        x.view().inject(this, view);// 注入view和事件
        return view;
    }

    @Override
    public void initData() {
        mLeftMenuData = new ArrayList<>();
        mLeftMenuData.add("注册");
        mLeftMenuData.add("登录");
        mLeftMenuData.add("退出");
        mLeftMenuData.add("设置");
        setMenuData(mLeftMenuData);
    }

    // 给侧边栏设置数据
    public void setMenuData(ArrayList<String> data) {
        mCurrentPos = 0;//当前选中的位置归零

        // 更新页面
        mLeftMenuData = data;

        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mCurrentPos = position;// 更新当前被选中的位置
                mAdapter.notifyDataSetChanged();// 刷新listview

                // 收起侧边栏
                toggle();

                // 侧边栏点击之后, 要修改新闻中心的FrameLayout中的内容
//                setCurrentDetailPager(position);
            }
        });
    }



    /**
     * 打开或者关闭侧边栏
     */
    protected void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLeftMenuData.size();
        }

        @Override
        public String getItem(int position) {
            return mLeftMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu,
                    null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);

            String item = getItem(position);
            tvMenu.setText(item);

            if (position == mCurrentPos) {
                // 被选中
                tvMenu.setEnabled(true);// 文字变为红色
            } else {
                // 未选中
                tvMenu.setEnabled(false);// 文字变为白色
            }

            return view;
        }

    }


}
