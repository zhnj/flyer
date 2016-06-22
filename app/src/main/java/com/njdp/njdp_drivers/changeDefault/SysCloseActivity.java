package com.njdp.njdp_drivers.changeDefault;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.njdp.njdp_drivers.login;

import java.util.LinkedList;
import java.util.List;

/**
 * 一个类 用来结束所有后台activity
 * Created by SC on 2016/6/22.
 *
 */
public class SysCloseActivity extends Application {
    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static SysCloseActivity instance;
    //构造方法
    private SysCloseActivity(){}
    //实例化一次
    public synchronized static SysCloseActivity getInstance(){
        if (null == instance) {
            instance = new SysCloseActivity();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
