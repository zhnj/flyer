package com.njdp.njdp_drivers.items.myplan;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.items.myplan.Weiwancheng.PlanWeiwancheng;
import com.njdp.njdp_drivers.items.myplan.Yiwancheng.PlanYiwancheng;
import com.njdp.njdp_drivers.items.mywork.adapter.MyPagerAdapter;
import com.njdp.njdp_drivers.slidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MyPlan extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTb;
    private ViewPager mVp;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private DrawerLayout menu;
    private slidingMenu mainMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_layout_plan);

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
        mFragmentList.add(new PlanWeiwancheng());
        mFragmentList.add(new PlanYiwancheng());
    }

    private void initTitile() {
        mTitleList = new ArrayList<>();
        mTitleList.add("未完成");
        mTitleList.add("已完成");
        //设置tablayout模式
        mTb.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        mTb.addTab(mTb.newTab().setText(mTitleList.get(0)));
        mTb.addTab(mTb.newTab().setText(mTitleList.get(1)));
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
