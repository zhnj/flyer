package com.njdp.njdp_drivers.items.myplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;

/**
 * Created by Rock on 2018/7/27.
 */

public class PlanDetail extends AppCompatActivity {
    RecyclerView rv;
    LinearLayoutManager layoutManager;//纵向线性布局
    PlanDetailBean planDetailBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_detail);
        initData();
        initView();
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.detail_rv);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DetialAdapter adapter = new DetialAdapter(planDetailBean.getResult());
        rv.setAdapter(adapter);
    }

    private void initData() {
        Intent intent=getIntent();
        String detail =intent.getStringExtra("detail");
        planDetailBean = new Gson().fromJson(detail,PlanDetailBean.class);
        Log.i("info","1111111"+detail);
    }
}
