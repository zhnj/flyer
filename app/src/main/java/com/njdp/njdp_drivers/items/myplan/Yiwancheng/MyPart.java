package com.njdp.njdp_drivers.items.myplan.Yiwancheng;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.items.mywork.adapter.MyPagerAdapter;
import com.njdp.njdp_drivers.items.mywork.fragment.Frag_weiwancheng;
import com.njdp.njdp_drivers.items.mywork.fragment.Frag_yishixiao;
import com.njdp.njdp_drivers.items.mywork.fragment.Frag_yiwancheng;
import com.njdp.njdp_drivers.slidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MyPart extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTb;
    private ViewPager mVp;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private DrawerLayout menu;
    private slidingMenu mainMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //设置沉浸模式
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }

        setContentView(R.layout.new_activity_layout_part);

        findViewById(R.id.getback).setOnClickListener(this);
        findViewById(R.id.menu).setOnClickListener(this);
        mainMenu=new slidingMenu();
        menu=mainMenu.drawer;

        //初始化控件
        initView();
        //添加标题
        initTitile();
        //添加fragment
        initFragment();
        //设置适配器
        mVp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList));
        //将tablayout与fragment关联
        mTb.setupWithViewPager(mVp);

    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new Frag_weiwancheng());
        mFragmentList.add(new Frag_yiwancheng());
        mFragmentList.add(new Frag_yishixiao());
    }

    private void initTitile() {
        mTitleList = new ArrayList<>();
        mTitleList.add("未完成");
        mTitleList.add("已完成");
        mTitleList.add("已失效");
        //设置tablayout模式
        mTb.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        mTb.addTab(mTb.newTab().setText(mTitleList.get(0)));
        mTb.addTab(mTb.newTab().setText(mTitleList.get(1)));
        mTb.addTab(mTb.newTab().setText(mTitleList.get(2)));
    }

    private void initView() {
        mTb = (TabLayout) findViewById(R.id.mTb);
        mVp = (ViewPager) findViewById(R.id.mVp);
    }
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.getback:
                finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
        }
    }
}
