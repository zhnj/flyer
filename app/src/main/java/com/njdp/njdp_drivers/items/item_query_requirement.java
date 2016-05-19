package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter_white;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.FieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.FieldInfo;

public class item_query_requirement  extends Fragment implements View.OnClickListener {

    private static final String TAG = item_query_requirement.class.getSimpleName();
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private com.beardedhen.androidbootstrap.BootstrapButton hintButton;
    private Spinner sp_area;
    private Spinner sp_type;
    private TextView t_startDate;
    private TextView t_endDate;
    private LinearLayout ll_hint;
    private RadioGroup ll_crop;
    private RadioGroup ll_gd;
    private RadioButton ck_wheat;
    private RadioButton ck_corn;
    private RadioButton ck_soybean;
    private RadioButton ck_rice;
    private RadioButton ck_SS;
    private RadioButton ck_PD;
    private boolean ck_wheat_flag=false;
    private boolean ck_corn_flag=false;
    private boolean ck_soybean_flag=false;
    private boolean ck_rice_flag=false;
    private boolean ck_SS_flag=false;
    private boolean ck_PD_flag=false;
    private int selectedType;
    private StringBuilder machine_type;
    private String s_machine_type;
    private StringBuilder machine_cropType;
    private String s_machine_cropType;
    private String sl_area="50";
    private List<String> crop_type=new ArrayList<String>();
    private List<String> gd_type=new ArrayList<String>();
    private CalendarPickerView calendarPickerView;
    private ArrayAdapter areaAdapter;
    private ArrayAdapter typeAdapter;
    private View dateView;//日期选择View
    private View parentView;//主View
    private PopupWindow datePickerPop;//日期选择器弹出
    private boolean popup_flag=false;//日期选择器是否弹出的标志
    private ArrayList<Date> dates = new ArrayList<Date>();//选择的起始日期
    //范围下拉项选择是否改动的标志，默认都未改动
    private boolean sl_area_flag=false;
    private boolean sl_type_flag=false;
    private ProgressDialog pDialog;
    private String url;//服务器地址
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private Gson gson;
    private FieldInfoDao fieldInfoDao;
    private String token;
    private String startTime;
    private String endTime;
    private String machine_id;
    private List<FieldInfo> fieldInfos=new ArrayList<FieldInfo>();//返回的农田信息
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    private boolean text_gps_flag = false;//GPS定位是否成功
    private Date first_date;
    private SimpleDateFormat format;
    private SimpleDateFormat format2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_2_query_requirement, container,
                false);
        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;

        fieldInfoDao=new FieldInfoDao(mainMenu);
        sessionManager=new SessionManager(getActivity());
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        format = new SimpleDateFormat("yyyy/M/d");
        format2= new SimpleDateFormat("yyyy-MM-dd");
        url=AppConfig.URL_SEARCHFIELD;
        token=sessionManager.getToken();
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);

        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.jobDate).setOnClickListener(this);
        view.findViewById(R.id.clear_hint).setOnClickListener(this);
        view.findViewById(R.id.query).setOnClickListener(this);
        this.ll_hint=(LinearLayout)view.findViewById(R.id.hint);
        this.t_startDate=(TextView)view.findViewById(R.id.startDate);
        this.t_endDate=(TextView)view.findViewById(R.id.endDate);
        this.sp_type=(Spinner)view.findViewById(R.id.type);
        this.sp_area=(Spinner)view.findViewById(R.id.area);
        this.ck_wheat=(RadioButton)view.findViewById(R.id.selectWheat);
        this.ck_corn=(RadioButton)view.findViewById(R.id.selectCorn);
        this.ck_soybean=(RadioButton)view.findViewById(R.id.selectSoybean);
        this.ck_rice=(RadioButton)view.findViewById(R.id.selectRice);
        this.ck_SS=(RadioButton)view.findViewById(R.id.selectSS);
        this.ck_PD=(RadioButton)view.findViewById(R.id.selectPD);
        this.ll_crop=(RadioGroup)view.findViewById(R.id.ck_crop);
        this.ll_gd=(RadioGroup)view .findViewById(R.id.ck_arableLand);
        this.ll_hint=(LinearLayout)view.findViewById(R.id.hint);
        ck_wheat.setOnCheckedChangeListener(new ckWheatListener());
        ck_corn.setOnCheckedChangeListener(new ckCornListener());
        ck_soybean.setOnCheckedChangeListener(new ckSoybeanListener());
        ck_rice.setOnCheckedChangeListener(new ckRiceListener());
        ck_SS.setOnCheckedChangeListener(new ckSSListener());
        ck_PD.setOnCheckedChangeListener(new ckPDListener());
        this.parentView = LayoutInflater.from(mainMenu).inflate(R.layout.activity_2_query_requirement, null);

        dateView = mainMenu.getLayoutInflater().inflate(R.layout.datepicker, null);
        dateInit();//日期选择初始化
        initDatePicker();//日历选择器监听
        dateView.findViewById(R.id.getBack).setOnClickListener(this);
        dateView.findViewById(R.id.getHelp).setOnClickListener(this);
        hintButton=(com.beardedhen.androidbootstrap.BootstrapButton)dateView.findViewById(R.id.hintButton);
        calendarPickerView.setOnDateSelectedListener(new clDateSelectedListener());//选一个范围的日期
        calendarPickerView.setCellClickInterceptor(new clCellClick());//选一天的日期

        //下拉菜单选择项初始化
        //农田范围
        areaAdapter= new SpinnerAdapter_white(mainMenu, getResources().getStringArray(R.array.area));
        sp_area.setAdapter(areaAdapter);
        sp_area.setOnItemSelectedListener(new areaListener());
        //农机服务类型
        typeAdapter= new SpinnerAdapter_white(mainMenu, getResources().getStringArray(R.array.machine_type));
        sp_type.setAdapter(typeAdapter);
        sp_type.setOnItemSelectedListener(new typeSelectedListener());


        //获取machine_id
        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            Log.e(TAG, machine_id);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
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
            case R.id.clear_hint:
                ll_hint.setVisibility(View.GONE);
                break;
            case R.id.jobDate:
                popup_flag=true;
                calendarPickerView.clearChoices();
                datePickerPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.query:
                checkQuery();

//                ///////////////////////////////////////////////测试/////////////////////////////////
//                String response = "{\"status\":0,\"result\":[" +
//                    "{\"farm_id\":1,\"user_name\":\"12345678901\",\"crops_kind\":\"小麦\",\"area_num\":\"28.0\",\"unit_price\":\"68.0\",\"block_type\":\"规则\",\"province\":\"河北\",\"city\":\"邯郸\",\"county\":\"肥乡县\",\"town\":\"测试乡\",\"village\":\"成功村\",\"longitude\":\"115.45580333333334\",\"latitude\":\"38.920224999999995\",\"start_time\":\"2016-04-28\",\"end_time\":\"2016-04-29\"}," +
//                    "{\"farm_id\":2,\"user_name\":\"12345678901\",\"crops_kind\":\"小麦\",\"area_num\":\"28.0\",\"unit_price\":\"68.0\",\"block_type\":\"规则\",\"province\":\"河北\",\"city\":\"邯郸\",\"county\":\"肥乡县\",\"town\":\"测试乡\",\"village\":\"成功村\",\"longitude\":\"115.45580333333334\",\"latitude\":\"38.920224999999995\",\"start_time\":\"2016-04-28\",\"end_time\":\"2016-04-29\"}," +
//                    "{\"farm_id\":3,\"user_name\":\"12345678901\",\"crops_kind\":\"小麦\",\"area_num\":\"58.0\",\"unit_price\":\"68.0\",\"block_type\":\"规则\",\"province\":\"河北\",\"city\":\"邯郸\",\"county\":\"大名县\",\"town\":\"测试乡\",\"village\":\"成功村\",\"longitude\":\"115.45580333333334\",\"latitude\":\"38.920224999999995\",\"start_time\":\"2016-04-28\",\"end_time\":\"2016-04-29\"}," +
//                    "{\"farm_id\":4,\"user_name\":\"12345678901\",\"crops_kind\":\"小麦\",\"area_num\":\"50.0\",\"unit_price\":\"68.0\",\"block_type\":\"规则\",\"province\":\"河北\",\"city\":\"邯郸\",\"county\":\"峰峰矿区\",\"town\":\"测试乡\",\"village\":\"成功村\",\"longitude\":\"115.47697500000001\",\"latitude\":\"38.85114166666666\",\"start_time\":\"2016-04-29\",\"end_time\":\"2016-04-30\"}," +
//                    "{\"farm_id\":5,\"user_name\":\"12345678901\",\"crops_kind\":\"小麦\",\"area_num\":\"50.0\",\"unit_price\":\"68.0\",\"block_type\":\"规则\",\"province\":\"河北\",\"city\":\"邯郸\",\"county\":\"峰峰矿区\",\"town\":\"测试乡\",\"village\":\"成功村\",\"longitude\":\"115.47697500000001\",\"latitude\":\"38.85114166666666\",\"start_time\":\"2016-04-29\",\"end_time\":\"2016-04-30\"}," +
//                    "{\"farm_id\":6,\"user_name\":\"12345678901\",\"crops_kind\":\"小麦\",\"area_num\":\"58.0\",\"unit_price\":\"68.0\",\"block_type\":\"规则\",\"province\":\"河北\",\"city\":\"邯郸\",\"county\":\"成安县\",\"town\":\"测试乡\",\"village\":\"成功村\",\"longitude\":\"115.47697500000001\",\"latitude\":\"38.85114166666666\",\"start_time\":\"2016-04-29\",\"end_time\":\"2016-04-29\"}]}";
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    String s_t = jObj.getString("result");
//                    mainMenu.selectedFieldInfo = gson.fromJson(s_t, new TypeToken<List<FieldInfo>>() {
//                    }.getType());//存储农田信息
//                    mainMenu.addBackFragment(new item_query_requirement_1());
//                }catch (Exception e){}
//                ///////////////////////////////////////////////测试/////////////////////////////////

                break;
            case R.id.getBack:
                popup_flag=false;
                datePickerPop.dismiss();
                break;
            case R.id.getHelp:
                commonUtil.error_hint("请选择作业起止日期！");
                break;
            default:
                break;
        }
    }

    //监听返回按键
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (popup_flag) {
                        popup_flag = false;
                        datePickerPop.dismiss();
                        return true;
                    } else {
                        mainMenu.finish();
                    }
                }
                return false;
            }
        });
    }

    private void checkQuery()//检查发布信息
    {
        if (selectedType == 0) {
            commonUtil.error_hint2_short(R.string.error_machineId3);
        } else
        {
            checkByType(selectedType);
        }
    }

    private void checkByType(int selectedType)//根据农机服务类型,不同验证
    {
        switch (selectedType)
        {
            case 1:
                if(!(ck_wheat_flag||ck_corn_flag||ck_soybean_flag||ck_rice_flag))
                {
                    commonUtil.error_hint2_short(R.string.err_cropCheck);
                }else {
                    gps_MachineLocation(machine_id);
                }
                break;
            case 2:
                if(!(ck_SS_flag||ck_PD_flag))
                {
                    commonUtil.error_hint2_short(R.string.err_cropCheck);
                }else {
                    gps_MachineLocation(machine_id);
                }
                break;
            case 3:
                if(!(ck_wheat_flag||ck_corn_flag||ck_soybean_flag||ck_rice_flag))
                {
                    commonUtil.error_hint2_short(R.string.err_cropCheck);
                }else {
                    gps_MachineLocation(machine_id);
                }
                break;
            default:
                break;
        }
    }

    private void queryInfo()//存储s_machine_cropType
    {
        switch (selectedType)
        {
            case 1:
                machine_cropType=new StringBuilder(crop_type.size()*3);//收获作物类型
                for(int i=0;i<crop_type.size();i++){
                    if(i>0)
                    {
                        machine_cropType.append("&");
                    }
                    machine_cropType.append("H"+crop_type.get(i));
                }
                s_machine_cropType=machine_cropType.toString();
                break;
            case 2:
                machine_cropType=new StringBuilder(gd_type.size()*3);//耕地类型
                for(int i=0;i<gd_type.size();i++){
                    if(i>0)
                    {
                        machine_cropType.append("&");
                    }
                    machine_cropType.append("C"+gd_type.get(i));
                }
                s_machine_cropType=machine_cropType.toString();
                break;
            case 3:
                machine_cropType=new StringBuilder(crop_type.size()*3);//播种作物类型
                for(int i=0;i<crop_type.size();i++){
                    if(i>0)
                    {
                        machine_cropType.append("&");
                    }
                    machine_cropType.append("S"+crop_type.get(i));
                }
                s_machine_cropType=machine_cropType.toString();
                break;
            default:
                break;
        }
        query_field();
    }

    private void query_field() //查询农田信息
    {
        Log.e(TAG,token);
        Log.e(TAG,machine_id);
        Log.e(TAG, sl_area);
        Log.e(TAG, s_machine_type);
        Log.e(TAG, s_machine_cropType);
        Log.e(TAG, startTime);
        Log.e(TAG, endTime);
        Log.e(TAG, GPS_longitude);
        Log.e(TAG, GPS_latitude);

        initFieldInfo();
    }

    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////
    public void gps_MachineLocation(final String machine_id) {
        String tag_string_req = "req_GPS";
        //服务器请求
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MACHINELOCATION, new locationSuccessListener(), new locationErrorListener()) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("machine_id", machine_id);
                params.put("token", token);

                return netUtil.checkParams(params);
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private class locationSuccessListener implements Response.Listener<String>//获取农机位置响应服务器成功
    {
        @Override
        public void onResponse(String response) {
            hideDialog();
            Log.d(TAG, "AreaInit Response: " + response);
            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");

                if (status == 1) {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    Intent intent = new Intent(mainMenu, login.class);
                    startActivity(intent);
                    mainMenu.finish();
                } else if (status == 0) {

                    ///////////////////////////获取服务器农机，经纬度/////////////////////
                    JSONObject location = jObj.getJSONObject("result");
                    GPS_longitude = location.getString("x");
                    GPS_latitude = location.getString("y");
                    ///////////////////////////获取服务器农机，经纬度/////////////////////

                    text_gps_flag = false;
                    isGetLocation();
                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "GPSLocation Error:" + errorMsg);
                    text_gps_flag = true;
                    isGetLocation();
                }
            } catch (JSONException e) {
                // JSON error
                Log.e(TAG, "GPSLocation Error,Json error：response错误:" + e.getMessage());
                text_gps_flag = true;
                isGetLocation();
            }
        }
    }

    private class locationErrorListener implements  Response.ErrorListener//定位服务器响应失败
    {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideDialog();
            Log.e(TAG, "ConnectService Error: " + error.getMessage());
            netUtil.testVolley(error);
            text_gps_flag = true;
            isGetLocation();
        }
    }

    //定位成功或者失败后的响应
    private void isGetLocation() {
        if (!text_gps_flag) {
            Log.e(TAG, "GPS自动定位成功");
            queryInfo();
            commonUtil.error_hint("自动定位成功");
        } else {
            Log.e(TAG, "定位失败！");
            commonUtil.error_hint("查询失败,请重试");
        }
    }
    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////

    ////////////////////////////////////获取农田信息////////////////////////////////////////////////
    private void initFieldInfo()
    {
        String tag_string_req = "req_init";
        mainMenu.pDialog.setMessage("正在载入 ...");
        mainMenu.showDialog();

        if (!netUtil.checkNet(mainMenu)) {
            commonUtil.error_hint("网络连接错误");
            mainMenu.hideDialog();
        } else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new initSuccessListener(), mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("Search_range", sl_area);//需要按照实际范围变动
                    params.put("crops_kind", s_machine_cropType);
                    params.put("start_date", startTime);
                    params.put("end_date", endTime);
                    params.put("Machine_longitude", GPS_longitude);
                    params.put("Machine_Latitude", GPS_latitude);
                    Log.e(TAG,gson.toJson(params));

                    return netUtil.checkParams(params);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private class initSuccessListener implements Response.Listener<String>//获取农田信息数据响应服务器成功
    {
        @Override
        public void onResponse(String response) {
            Log.e(TAG, "AreaInit Response: " + response);
            mainMenu.hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");
                if (status==1)
                {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    Intent intent = new Intent(mainMenu, login.class);
                    startActivity(intent);
                    mainMenu.finish();
                }else if(status==0){
                    ///////////////////////////农田信息，包括经纬度/////////////////////////////////

                    /////////////////////////////////测试有多少个农田信息///////////////////////////
//                    JSONArray s_post=jObj.getJSONArray("machine_farm_d");
                    JSONArray s_info=jObj.getJSONArray("result");
//                    Log.e(TAG, String.valueOf(s_post.length()));
                    Log.e(TAG, String.valueOf(s_info.length()));
                    /////////////////////////////////测试有多少个农田信息///////////////////////////
                    try {
                        mainMenu.selectedFieldInfo.clear();
//                        fieldInfos.clear();
                    }catch (Exception e){}
                    try {
                        mainMenu.fieldInfoPosts.clear();
                    }catch (Exception e){}
                    String s_t=jObj.getString("result");
//                    String s_p=jObj.getString("machine_farm_d");
//                    List<FieldInfo> fieldInfos=gson.fromJson(s_t,new TypeToken<List<FieldInfo>>() {}.getType());//存储农田信息
                    mainMenu.selectedFieldInfo=gson.fromJson(s_t,new TypeToken<List<FieldInfo>>() {}.getType());//存储农田信息
//                    mainMenu.fieldInfoPosts=gson.fromJson(s_p,new TypeToken<List<FieldInfoPost>>() {}.getType());//存储距离信息
                    try {
                        Log.e(TAG, mainMenu.fieldInfoPosts.get(1).getDistance());
//                        Log.e(TAG, fieldInfos.get(0).getCropLand_site());
                    }catch (Exception e)
                    {
                        Log.e(TAG,"没有获取到数据"+e.getMessage());
                    }

                    ///////////////////////////农田信息，包括经纬度/////////////////////////////////

                    if(mainMenu.selectedFieldInfo.size()<1){
                        commonUtil.error_hint("未查询到符合要求的农田信息，请重新查询！");
                    }else{
                        mainMenu.addBackFragment(new item_query_requirement_1());
                    }
//                    else
//                    {
//                        //存储到本地库
//                        saveFieldInfo(fieldInfoDao,fieldInfos);
//                        //按距离的排序好的农田
//                        arrangeField();
//                        mainMenu.addBackFragment(new item_query_requirement_1());//跳转执行查找
//                        commonUtil.error_hint("数据加载完成");
//                    }
                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("服务器数据错误1：response错误:" + errorMsg);
                }
            } catch (JSONException e) {
                // JSON error
                Log.e(TAG, "2 服务器数据错误：response错误:" + e.getMessage());
                commonUtil.error_hint("服务器数据错误2：response错误:" + e.getMessage());
            }
        }
    }
    ////////////////////////////////////获取农田信息////////////////////////////////////////////////

    //农田信息存入本地数据库
    private void saveFieldInfo(FieldInfoDao fieldInfoDao,List<FieldInfo> fieldInfos)
    {
        if(fieldInfoDao.countOfField()>0)
        {
            //本地库里是否存在农田信息，存在则删除
            List<FieldInfo> testFieldInfos=fieldInfoDao.allFieldInfo();
            for (int i = 0; i < testFieldInfos.size(); i++) {
                FieldInfo fieldInfo = testFieldInfos.get(i);
                fieldInfoDao.delete(fieldInfo);
                Log.e(TAG, "删除第" + String.valueOf(fieldInfo.getId()) + "条");
            }
        }
        for (int i = 0; i < fieldInfos.size(); i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            fieldInfo.setId(i + 1);
            fieldInfoDao.add(fieldInfo);
            Log.e(TAG, "存储第" + String.valueOf(fieldInfo.getId()) + "条");
            Log.e(TAG, fieldInfoDao.getFieldInfo(i + 1).getFarm_id());
        }
        fieldInfos.clear();
    }

    //从本地读取，按mainMenu.fieldInfoPosts顺序排序存储到mainMenu.selectedFieldInfo
    private void arrangeField() {
        for (int i = 0; i < mainMenu.fieldInfoPosts.size(); i++) {
            FieldInfo fieldInfo = fieldInfoDao.getFieldInfoByFieldId(mainMenu.fieldInfoPosts.get(i).getFarm_id());
            Log.e(TAG, mainMenu.fieldInfoPosts.get(i).getFarm_id());
            Log.e(TAG, "找到一条：" + fieldInfo.getFarm_id());
            mainMenu.selectedFieldInfo.add(fieldInfo);
        }
    }

    //起始日期，终止日期初始化
    private void dateInit()
    {
        SimpleDateFormat format=new SimpleDateFormat("yyyy/M/d");
        Calendar calendar = Calendar.getInstance();
        Date defaultStartDate=calendar.getTime();
        calendar.add(calendar.DATE, 1);
        Date defaultEndDate=calendar.getTime();
        t_startDate.setText("开始日期：" + format.format(defaultStartDate));
        startTime=format2.format(defaultStartDate);
        t_endDate.setText("结束日期：" + format.format(defaultEndDate));//显示默认的起止年月日
        endTime=format2.format(defaultEndDate);
    }

    //初始化日期选择器popupWindow
    private void initDatePicker() {
        Calendar maxDate=Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 2);
        dateView = mainMenu.getLayoutInflater().inflate(R.layout.datepicker, null);
        dateView.findViewById(R.id.getBack).setOnClickListener(this);
        dateView.findViewById(R.id.getHelp).setOnClickListener(this);
        calendarPickerView=(CalendarPickerView)dateView.findViewById(R.id.calendarDatePicker);
        calendarPickerView.init(new Date(), maxDate.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        datePickerPop = new PopupWindow(dateView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        datePickerPop.setAnimationStyle(R.style.slideAnimation_bottom);
        datePickerPop.setOutsideTouchable(true);
    }

    private class areaListener implements AdapterView.OnItemSelectedListener//农田范围监听
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sl_area_flag = true;
            sl_area = sp_area.getSelectedItem().toString();
            switch (sl_area)
            {
                case "50公里":
                    sl_area="50";
                    break;
                case "80公里":
                    sl_area="80";
                    break;
                case "100公里":
                    sl_area="100";
                    break;
                case "全部":
                    sl_area="100";
                    break;
                case "默认距离":
                    sl_area="50";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    //日期选择单天监听
    private class clCellClick implements CalendarPickerView.CellClickInterceptor
    {
        @Override
        public boolean onCellClicked(Date date) {
            int size = (calendarPickerView.getSelectedDates()).size();
            if (size == 1) {
                hintButton.setText("请选择作业结束日期");
            }
            if (first_date == null) {
                hintButton.setText("请选择作业结束日期");
            }
            if(date==first_date)
            {
                String first_dateTime = format.format(date);
                t_startDate.setText(getResources().getString(R.string.jobStart) + first_dateTime);
                t_endDate.setText(getResources().getString(R.string.jobEnd) + first_dateTime);
                startTime = format2.format(date);
                endTime = format2.format(date);
                dates.clear();
                popup_flag=false;
                first_date= null;
                hintButton.setText("请选择作业开始日期");
                datePickerPop.dismiss();
            }
            return false;
        }
    }
    //日期选择器范围时间监听
    private class clDateSelectedListener implements CalendarPickerView.OnDateSelectedListener
    {
        @Override
        public void onDateSelected(Date date) {
            first_date=date;
            int size = (calendarPickerView.getSelectedDates()).size();
            if (size >= 2) {
                dates.addAll(calendarPickerView.getSelectedDates());
                t_startDate.setText(getResources().getString(R.string.jobStart) + format.format(dates.get(0)));
                t_endDate.setText(getResources().getString(R.string.jobEnd) + format.format(dates.get(size - 1)));
                startTime = format2.format(dates.get(0));
                endTime = format2.format(dates.get(size - 1));
                dates.clear();
                popup_flag=false;
                first_date=null;
                hintButton.setText("请选择作业开始日期");
                datePickerPop.dismiss();
            }
        }

        @Override
        public void onDateUnselected(Date date) {

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

    private class typeSelectedListener implements AdapterView.OnItemSelectedListener//农机类型下拉菜单监听
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sl_type_flag = true;
            if(position!=0) {
                s_machine_type = sp_type.getSelectedItem().toString();
            }
            selectedType = position;
//            if (position == 4) {
//                text_type.setVisibility(View.VISIBLE);
//                text_crop.setVisibility(View.VISIBLE);
//                ll_crop.setVisibility(View.GONE);
//                ll_gd.setVisibility(View.GONE);
//                showType.setText("类型：");
//            } else {
//                text_type.setVisibility(View.GONE);
//                text_crop.setVisibility(View.GONE);
//            }
            switch (position) {
                case 1:
                    ll_crop.setVisibility(View.VISIBLE);
                    ll_gd.setVisibility(View.GONE);
                    break;
                case 2:
                    ll_crop.setVisibility(View.GONE);
                    ll_gd.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    ll_crop.setVisibility(View.VISIBLE);
                    ll_gd.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ckWheatListener implements CompoundButton.OnCheckedChangeListener//小麦选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_wheat_flag=isChecked;
            if(isChecked){
                crop_type.add("WH");
            }else{
                crop_type.remove("WH");
            }
        }
    }

    private class ckCornListener implements CompoundButton.OnCheckedChangeListener//玉米选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_corn_flag=isChecked;
            if(isChecked){
                crop_type.add("CO");
            }else{
                crop_type.remove("CO");
            }
        }
    }

    private class ckSoybeanListener implements CompoundButton.OnCheckedChangeListener//谷子选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_soybean_flag=isChecked;
            if(isChecked){
                crop_type.add("GR");
            }else{
                crop_type.remove("GR");
            }
        }
    }

    private class ckRiceListener implements CompoundButton.OnCheckedChangeListener//水稻选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_rice_flag = isChecked;
            if (isChecked) {
                crop_type.add("RC");
            } else {
                crop_type.remove("RC");
            }
        }
    }

    private class ckSSListener implements CompoundButton.OnCheckedChangeListener//深松选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_SS_flag = isChecked;
            if (isChecked) {
                gd_type.add("SS");
            } else {
                gd_type.remove("SS");
            }
        }
    }

    private class ckPDListener implements CompoundButton.OnCheckedChangeListener//平地面选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_PD_flag = isChecked;
            if (isChecked) {
                gd_type.add("HA");
            } else {
                gd_type.remove("HA");
            }
        }
    }

}