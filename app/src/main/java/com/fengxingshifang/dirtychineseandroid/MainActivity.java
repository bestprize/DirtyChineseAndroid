package com.fengxingshifang.dirtychineseandroid;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.fengxingshifang.dirtychineseandroid.fragment.BaseFragment;
import com.fengxingshifang.dirtychineseandroid.fragment.HomeFragment;
import com.fengxingshifang.dirtychineseandroid.fragment.MyFragment;
import com.fengxingshifang.dirtychineseandroid.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bestprize on 2017/11/11.
 */

public class MainActivity  extends FragmentActivity {

    private RadioGroup mRg_main;
    private List<BaseFragment> mBaseFragment;
    private View title_bar_home;
    private View title_bar_my;
    private Button rb_publish;

    /**
     * 选中的Fragment的对应的位置
     */
    private int position;

    /**
     * 上次切换的Fragment
     */
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化View
        initView();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
    }

    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_home);

        rb_publish = (Button) findViewById(R.id.rb_publish);
        rb_publish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PublishActivity.class));
            }
        });
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            title_bar_home = findViewById(R.id.title_bar_home);
            title_bar_my = findViewById(R.id.title_bar_my);
            switch (checkedId){
                case R.id.rb_home://home
                    position = 0;
                    title_bar_home.setVisibility(View.VISIBLE);
                    title_bar_my.setVisibility(View.GONE);
                    break;
                case R.id.rb_my://my
                    position = 1;
                    title_bar_my.setVisibility(View.VISIBLE);
                    title_bar_home.setVisibility(View.GONE);
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();
            //替换
            switchFrament(mContent,to);

        }
    }


    /**
     *
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to 马上要切换到的Fragment，一会要显示
     */
    private void switchFrament(Fragment from,Fragment to) {
        if(from != to){
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if(!to.isAdded()){
                //to没有被添加
                //from隐藏
                if(from != null){
                    ft.hide(from);
                }
                //添加to
                if(to != null){
                    ft.add(R.id.fl_content,to).commit();
                }
            }else{
                //to已经被添加
                // from隐藏
                if(from != null){
                    ft.hide(from);
                }
                //显示to
                if(to != null){
                    ft.show(to).commit();
                }
            }
        }

    }

//    private void switchFrament(BaseFragment fragment) {
//        //1.得到FragmentManger
//        FragmentManager fm = getSupportFragmentManager();
//        //2.开启事务
//        FragmentTransaction transaction = fm.beginTransaction();
//        //3.替换
//        transaction.replace(R.id.fl_content, fragment);
//        //4.提交事务
//        transaction.commit();
//    }

    /**
     * 根据位置得到对应的Fragment
     * @return
     */
    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void initFragment() {
        //临时数据
//        PrefUtils.setString(this, "token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE0MDE0NzIwLCJleHAiOjE1MTQwMTgzMjAsIm5iZiI6MTUxNDAxNDcyMCwianRpIjoiQ2RqQk1lRDZFQjQ4bXA5WCIsInN1YiI6MCwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.bs14pvFzsNLCEayWUyYQkzaCer_OUYnwkRu6GN00xUg");
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new HomeFragment());//home Fragment
        mBaseFragment.add(new MyFragment());//my Fragment
        BaseFragment homeFragment = getFragment();
        switchFrament(mContent,homeFragment);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mRg_main = (RadioGroup) findViewById(R.id.rg_group);

    }
}
































/*
public class MainActivity extends AppCompatActivity {

    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private NoScrollViewPager mViewPager;
    private RadioGroup rgGroup;
    private Button btnPublish;

    private ArrayList<BasePager> mPagers;// 五个标签页的集合

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);



        mViewPager = (NoScrollViewPager) findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) findViewById(R.id.rg_group);
        btnPublish = (Button) findViewById(R.id.rb_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PublishActivity.class));
            }
        });

        {
            mPagers = new ArrayList<BasePager>();

            // 添加五个标签页
            mPagers.add(new HomePager(this));
//        mPagers.add(new PublishPager(mActivity));
            mPagers.add(new MycontentPager(this));

            mViewPager.setAdapter(new ContentAdapter());

            // 底栏标签切换监听
            rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rb_home:
                            // 列表
                            // mViewPager.setCurrentItem(0);
                            mViewPager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                            break;
//                    case R.id.rb_publish:
//                        // 发表
////                        mViewPager.setCurrentItem(1, false);
//                        Intent intent = new Intent(mActivity,
//                                PublishActivity.class);
//
//                        startActivity(intent);
//                        break;
                        case R.id.rb_mycontent:
                            // 我的
                            mViewPager.setCurrentItem(1, false);
                            break;

                        default:
                            break;
                    }
                }
            });

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    BasePager pager = mPagers.get(position);
                    pager.initData();


                }

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });



            // 手动加载第一页数据
            mPagers.get(0).initData();
        }

//        setBehindContentView(R.layout.left_menu);
        // configure the SlidingMenu
//        SlidingMenu slidingMenu = new SlidingMenu(this);
//        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
//        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//        slidingMenu.setShadowDrawable(R.color.colorAccent);

        // 设置滑动菜单视图的宽度
//        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
//        slidingMenu.setFadeDegree(0.35f);
//        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
//        slidingMenu.setMenu(R.layout.left_menu);

//        initFragment();


//    private void initFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();// 开始事务
//        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
//                TAG_LEFT_MENU);// 用fragment替换帧布局;参1:帧布局容器的id;参2:是要替换的fragment;参3:标记
//        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
//        transaction.commit();// 提交事务
//        // Fragment fragment =
//        // fm.findFragmentByTag(TAG_LEFT_MENU);//根据标记找到对应的fragment
//    }

//    // 获取侧边栏fragment对象
//    public LeftMenuFragment getLeftMenuFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        LeftMenuFragment fragment = (LeftMenuFragment) fm
//                .findFragmentByTag(TAG_LEFT_MENU);// 根据标记找到对应的fragment
//        return fragment;
//    }
//
//    // 获取主页fragment对象
//    public ContentFragment getContentFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        ContentFragment fragment = (ContentFragment) fm
//                .findFragmentByTag(TAG_CONTENT);// 根据标记找到对应的fragment
//        return fragment;
//    }

//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//    }
//
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }


    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView;// 获取当前页面对象的布局

            // pager.initData();// 初始化数据, viewpager会默认加载下一个页面,
            // 为了节省流量和性能,不要在此处调用初始化数据的方法

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }



    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
*/