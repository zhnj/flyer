package com.njdp.njdp_drivers.items.myplan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;

/**
 * Created by Rock on 2018/7/27.
 */

public class PlanDetail_wei extends AppCompatActivity {
    RecyclerView rv;
    LinearLayoutManager layoutManager;//纵向线性布局
    PlanDetailBean planDetailBean;
    String navi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        setContentView(R.layout.plan_detail);
        initData();
        initView();
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.detail_rv);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DetialAdapter_wei adapter = new DetialAdapter_wei(planDetailBean.getResult());
        rv.setAdapter(adapter);
    }

    private void initData() {
        Intent intent=getIntent();
        String detail = intent.getStringExtra("detail");
        planDetailBean = new Gson().fromJson(detail,PlanDetailBean.class);
        Log.i("info","1111111"+detail);
    }
}
