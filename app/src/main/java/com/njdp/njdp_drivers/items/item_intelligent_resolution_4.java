package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter_white_1;
import com.njdp.njdp_drivers.db.SavedFieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

import bean.SavedFiledInfo;

public class item_intelligent_resolution_4 extends Fragment implements View.OnClickListener {
    private String TAG=item_intelligent_resolution_4.class.getSimpleName();
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private Spinner sp_site;
    private ArrayAdapter siteAdapter;
    private String token;
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private ProgressDialog pDialog;
    private List<SavedFiledInfo> navigationDeploy=new ArrayList<SavedFiledInfo>();//选择的调配方案对应的农田信息
    private SavedFieldInfoDao savedFieldInfoDao;
    private String[] sites;//地点的数组
    private int sl_site;//下拉选中的地点序号0，1，.....

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_1_intelligent_resolution_4, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.getNavigation).setOnClickListener(this);
        this.sp_site=(Spinner)view.findViewById(R.id.site);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        savedFieldInfoDao=new SavedFieldInfoDao(getActivity());

        try {
            navigationDeploy.addAll(savedFieldInfoDao.allFieldInfo());
            sites=new String[savedFieldInfoDao.countOfField()];
            for(int i=0;i<savedFieldInfoDao.countOfField();i++)
            {
                sites[i]=navigationDeploy.get(i).getCropLand_site();
            }
        }catch (Exception e){
            Log.e(TAG,e.toString());
        };

        sessionManager=new SessionManager(getActivity());
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        token=sessionManager.getToken();
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);

        siteAdapter=new SpinnerAdapter_white_1(mainMenu,sites);
        sp_site.setAdapter(siteAdapter);
        sp_site.setOnItemSelectedListener(new siteItemSelected());

        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.getNavigation:
                double gps_longitude=navigationDeploy.get(sl_site).getLongitude();//经度
                double gps_latitude;navigationDeploy.get(sl_site).getLatitude();//纬度
                //导航
                break;
            default:
                break;
        }
    }

    //ProgressDialog显示与隐藏
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private class siteItemSelected implements AdapterView.OnItemSelectedListener//地点选择下拉监听
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sl_site=position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
