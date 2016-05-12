package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.FieldInfo;

public class item_repair_station extends Fragment implements View.OnClickListener {

    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private Spinner spinner_area;
    private ArrayAdapter area_adapter;
    private boolean sl_area_flag=false;
    private ProgressDialog pDialog;
    private static final String TAG = item_repair_station.class.getSimpleName();
    private String url;//服务器地址
    private String sl_area="50公里";
    private String token;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private Gson gson;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_4_repair_station, container,
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

        this.spinner_area=(Spinner)view.findViewById(R.id.search_area);
        area_adapter=ArrayAdapter.createFromResource(mainMenu,R.array.area,android.R.layout.simple_spinner_item);
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_area.setAdapter(area_adapter);
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sl_area_flag = true;
                sl_area = spinner_area.getSelectedItem().toString();
                initRepairStationInfo(sl_area);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);
        url= AppConfig.URL_REGISTER;

        initRepairStationInfo(sl_area);

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
                menu.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
    }

    //标记地图？？？？？？？？？？？？？？？？
    private void markMap()
    {

    }

    //获取维修站信息
    private void initRepairStationInfo(final String area)
    {
        String tag_string_req = "req_init";

        mainMenu.pDialog.setMessage("正在载入 ...");
        mainMenu.showDialog();

        if(!netUtil.checkNet(mainMenu)){
            commonUtil.error_hint("网络连接错误");
            mainMenu.hideDialog();
        }else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, initSuccessListener, mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("area", area);
                    params.put("token",token);

                    return netUtil.checkParams(params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    }

    //获取农田信息数据响应服务器成功
    private Response.Listener<String> initSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d(TAG, "AreaInit Response: " + response);
            mainMenu.hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONObject repairStationInfo=jObj.getJSONObject("repairStationInfo");
                        String str=gson.toJson(repairStationInfo);
                        //替换成维修站信息????????????
                        mainMenu.fieldInfos=gson.fromJson(str,new TypeToken<List<FieldInfo>>(){}.getType());
                        if( mainMenu.fieldInfos.size()<1){
                            commonUtil.error_hint("未搜索到符合条件的维修站信息，请重新设置搜索条件！");
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Log.e(TAG, "Json error：response错误:" + errorMsg);
                        commonUtil.error_hint2_short(R.string.register_error2);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Log.e(TAG, "Json error：response错误:" + e.getMessage());
                    commonUtil.error_hint2_short(R.string.connect_error);
                }
                mainMenu.hideDialog();
            }
    };
}