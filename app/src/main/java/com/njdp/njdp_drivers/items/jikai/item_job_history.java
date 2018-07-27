package com.njdp.njdp_drivers.items.jikai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.items.myplan.MyPlan;
import com.njdp.njdp_drivers.items.mywork.MyPart;

public class item_job_history extends Fragment{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_3_job_history, container,false);
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



}
