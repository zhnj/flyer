package com.njdp.njdp_drivers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.njdp.njdp_drivers.CircleMenu.CircleMenuLayout;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NormalUtils;

import java.io.File;
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

        if (initDirs()) {
            initNavi();
        }
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
    //初始化导航用文件件
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //得到SD卡路径
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    //初始化导航配置
    String authinfo = null;
    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(mainpages.this, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(mainpages.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(mainpages.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();

                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                        Toast.makeText(mainpages.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int authBaseRequestCode = 1;
    private static final String APP_FOLDER_NAME = "BNSDK";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private String mSDCardPath = null;
    public void initTTS()
    {
        BaiduNaviManagerFactory.getTTSManager().initTTS(this,
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }
}