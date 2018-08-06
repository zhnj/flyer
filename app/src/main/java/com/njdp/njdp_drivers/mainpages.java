package com.njdp.njdp_drivers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.njdp.njdp_drivers.CircleMenu.CircleMenuLayout;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.util.CommonUtil;

import java.util.Timer;
import java.util.TimerTask;

public class mainpages extends Activity {

    private static final String TAG = mainpages.class.getSimpleName();
    private int first_KeyBack = 0;

    private CommonUtil commonUtil;

    private FragmentTransaction transaction;

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts =new String[]{"我要发布","去哪儿好","需求查询","历史作业","个人信息"};
    private int[] mItemImgs = new int[] {
            R.drawable.ic_release_1, R.drawable.ic_intell_arrange_1,
            R.drawable.ic_require_1,
            R.drawable.ic_job_history_1,R.drawable.ic_personal_info_1
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置沉浸模式
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }


        setContentView(R.layout.activity_mainpages);
        SysCloseActivity.getInstance().addActivity(this);




        commonUtil =new CommonUtil(mainpages.this);

        /**
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
         */
    }


    public void onDispatcher(View view)
    {
        int pos=-1;
        switch (view.getId()) {
            case R.id.imageButton_arrange:
                pos = 1;
                break;
            case R.id.imageButton_release:
                pos=0;
                break;
            case R.id.imageButton_require:
                pos=2;
                break;
            case R.id.imageButton_history:
                pos=3;
                break;
            case R.id.imageButton_personal:
                pos=4;
                break;
            case R.id.imageButton_uav:
                pos=6;
                break;
        }
        Intent intent = new Intent(mainpages.this, slidingMenu.class);
        Bundle index_bundle = new Bundle();
        index_bundle.putInt("index", pos);
        intent.putExtra("index_bundle", index_bundle);
        startActivity(intent);
    }
//    //点击返回按钮，退出客户端界面，客户端后台运行
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "主界面禁止返回,只能双击退出");
            Timer tExit = null;
            if (first_KeyBack == 0) {
                first_KeyBack = 1; // 准备退出
                commonUtil.error_hint2_long(R.string.exit_notice);
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        first_KeyBack = 0; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            } else {
              SysCloseActivity.getInstance().exit();
            }
        }
        return false;
    }

}