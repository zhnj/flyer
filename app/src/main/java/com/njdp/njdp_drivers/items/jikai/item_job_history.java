package com.njdp.njdp_drivers.items.jikai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.items.myplan.MyPlan;
import com.njdp.njdp_drivers.items.mywork.MyPart;
import com.njdp.njdp_drivers.slidingMenu;

import static com.njdp.njdp_drivers.R.id.mainMenu;

public class item_job_history extends Fragment implements View.OnClickListener {
    View view;
    private DrawerLayout menu;
    private slidingMenu mainMenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.activity_3_job_history, null,false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        initView();
        return view;
    }

    private void initView() {
        view.findViewById(R.id.myjob_bt_plan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MyPlan.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.myjob_bt_part).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到我的地块作业
                Intent intent = new Intent(getActivity(),MyPart.class);
                startActivity(intent);
            }
        });
    }
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
        }
    }
}
