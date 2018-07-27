package com.njdp.njdp_drivers.items.myplan;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.items.myplan.Weiwancheng.PlanWeiwancheng;
import com.njdp.njdp_drivers.items.myplan.Yiwancheng.PlanYiiwancheng;
import com.njdp.njdp_drivers.items.mywork.adapter.MyPagerAdapter;
import com.njdp.njdp_drivers.items.mywork.fragment.Frag_yiwancheng;

import java.util.ArrayList;
import java.util.List;

public class MyPlan extends AppCompatActivity {
    private TabLayout mTb;
    private ViewPager mVp;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_layout);

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
        mFragmentList.add(new PlanYiiwancheng());
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

}
