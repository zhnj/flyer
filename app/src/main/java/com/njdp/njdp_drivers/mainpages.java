package com.njdp.njdp_drivers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.njdp.njdp_drivers.CircleMenu.CircleMenuLayout;

public class mainpages extends Activity {

    private FragmentTransaction transaction;

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts =new String[]{"我要发布","智能调度","需求查询","服务对象","历史作业","个人信息","维修站","加油站"};
    private int[] mItemImgs = new int[] {
            R.drawable.ic_release, R.drawable.ic_intell_arrange,
            R.drawable.ic_require, R.drawable.ic_server_object,
            R.drawable.ic_job_history,R.drawable.ic_personal_info,
            R.drawable.ic_repair_station, R.drawable.ic_oil_station};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpages);

        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);

        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {

            @Override
            public void itemClick(View view, int pos) {
                Intent intent = new Intent(mainpages.this, slidingMenu.class);
                Bundle index_bundle = new Bundle();
                index_bundle.putInt("index", pos);
                intent.putExtra("index_bundle", index_bundle);
                startActivity(intent);
            }

            @Override
            public void itemCenterClick(View view) {

            }
        });
    }

    //点击返回按钮，退出客户端界面，客户端后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}