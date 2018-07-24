package com.njdp.njdp_drivers.items.mywork.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Rock on 2018/7/10.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    //添加fragment的集合
    private List<Fragment> mFragmentList;
    //添加标题的集合
    private List<String> mTilteLis;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<String> tilteLis) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mTilteLis = tilteLis;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTilteLis.get(position);
    }
}
