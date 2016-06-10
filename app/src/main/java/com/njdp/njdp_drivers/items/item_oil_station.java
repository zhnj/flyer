package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter_white_1;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import java.util.ArrayList;

import bean.RepairInfo;

public class item_oil_station extends Fragment implements View.OnClickListener  {

    private StringRequest strReq;
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private Spinner spinner_area;
    private ArrayAdapter area_adapter;
    private boolean sl_area_flag=false;
    private ProgressDialog pDialog;
    private static final String TAG = item_repair_station.class.getSimpleName();
    private String url;//服务器地址
    private String sl_area="50";
    private String token;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private Gson gson;
    private SessionManager sessionManager;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ////////////////////////地图代码////////////////////////////////////
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        ////////////////////////地图代码结束////////////////////////////////////

        view = inflater.inflate(R.layout.activity_7_oil_station, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;

        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        sessionManager=new SessionManager(mainMenu);
        gson=new Gson();

        token=sessionManager.getToken();

//        this.spinner_area=(Spinner)view.findViewById(R.id.search_area);
//        area_adapter=new SpinnerAdapter_white_1(mainMenu, getResources().getStringArray(R.array.area));
//        spinner_area.setAdapter(area_adapter);
//        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sl_area_flag = true;
//                sl_area = spinner_area.getSelectedItem().toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        url= AppConfig.URL_REPAIRE;


        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.my_location:
                break;
            default:
                break;
        }
    }
}
