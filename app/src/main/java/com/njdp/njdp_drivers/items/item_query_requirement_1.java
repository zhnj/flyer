package com.njdp.njdp_drivers.items;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.FieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bean.FieldInfo;
import bean.FieldInfoPost;

public class item_query_requirement_1 extends Fragment  implements View.OnClickListener{
    private static final String TAG = item_query_requirement_1.class.getSimpleName();
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private Button countInfo;
    private Button pop_countInfo;
    private SimpleDateFormat format;
    private SimpleDateFormat format2;
    private String token;
    private String machine_id;
    private ProgressDialog pDialog;
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    private boolean text_gps_flag = false;//GPS定位是否成功
    private List<FieldInfo> fieldInfos=new ArrayList<FieldInfo>();//农田信息
    private View parentView;//主View
    private View infoView;
    private PopupWindow btn_popup;
    private boolean btn_pop_flag=false;
    private String telephone;
    private double nav_longitude;
    private double nav_latitude;
    ////////////////////////地图变量//////////////////////////
//    private MapView mMapView = null;
    private MapView mMapView=null;
    private BaiduMap mBaiduMap = null;
    private boolean isFristLocation = true;
    /**
     * 当前定位的模式
     */
    private LocationService locationService;
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    /**
     * 定位的客户端
     */
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    private BDLocation curlocation ; //当前位置

    //导航
     /*
    *导航用变量
    * */
    public static List<Activity> activityList = new LinkedList<Activity>();
    private static final String APP_FOLDER_NAME = "BNSDK";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private String mSDCardPath = null;
    ////////////////////////地图变量//////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_2_query_requirement_1,container,
                false);

        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.my_location).setOnClickListener(this);
        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;

        countInfo=(Button)view.findViewById(R.id.infos);
        countInfo.setOnClickListener(this);
        countInfo.setText("共找到"+String.valueOf(mainMenu.selectedFieldInfo.size())+"块农田，点击查看");

        format = new SimpleDateFormat("yyyy-MM-dd");
        format2 = new SimpleDateFormat("yyyy/M/d");
        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            Log.e(TAG, machine_id);
            fieldInfos.addAll(mainMenu.selectedFieldInfo);
        }catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }
        sessionManager=new SessionManager();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        token=sessionManager.getToken();
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);
        parentView = LayoutInflater.from(mainMenu).inflate(R.layout.activity_2_query_requirement_1, null);

        infoView = mainMenu.getLayoutInflater().inflate(R.layout.require_pop, null);//需要改动
        expandableListView=(ExpandableListView)infoView.findViewById(R.id.fieldInfo_expand);
        adapter=new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return mainMenu.selectedFieldInfo.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 1;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return mainMenu.selectedFieldInfo.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return mainMenu.selectedFieldInfo.get(groupPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(final int groupPosition,final boolean isExpanded, View convertView, ViewGroup parent) {

                final FieldInfo fieldInfo;
                final FieldInfoPost fieldInfoPost;
                final String farmId;
                //获取对应序列的农田信息
                if (mainMenu.selectedFieldInfo.size() >= 1) {
                    fieldInfo = mainMenu.selectedFieldInfo.get(groupPosition);//农田信息
                    farmId = fieldInfo.getFarm_id();
                    telephone=fieldInfo.getUser_name();
                    nav_longitude=fieldInfo.getLongitude();
                    nav_latitude=fieldInfo.getLatitude();
                    Log.e(TAG, "农田序号:" + farmId);

                    if (convertView == null) {
                        convertView = LayoutInflater.from(mainMenu).inflate(R.layout.expandablelistview_query_parent, null);
                    }
                    LinearLayout textLayout = (LinearLayout) convertView.findViewById(R.id.expandParent_text);
                    TextView tv4 = (TextView) convertView.findViewById(R.id.field_getPhone);
                    TextView tv5 = (TextView) convertView.findViewById(R.id.field_navigation);
                    try {
                        TextView tv1 = (TextView) convertView.findViewById(R.id.field_site);
                        tv1.setText(fieldInfo.getCropLand_site());
                        TextView tv2 = (TextView) convertView.findViewById(R.id.field_telephone);
                        tv2.setText(fieldInfo.getUser_name());
                        TextView tv3 = (TextView) convertView.findViewById(R.id.field_distance);
                        tv3.setText("距您"+fieldInfo.getDistance() + "公里");
                    } catch (Exception e) {
                        Log.e(TAG, "布局错误1：" + e.toString());
                    }
                    final ImageView imv=(ImageView)convertView.findViewById(R.id.arrow_drop_down);
                    convertView.findViewById(R.id.field_telephone).setOnClickListener(new callListener());
                    convertView.findViewById(R.id.field_navigation).setOnClickListener(new navigationListener());

                    textLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//展开二级菜单
                            if (isExpanded) {
                                imv.setBackgroundResource(R.drawable.ic_arrow_drop_down_white_36dp);
                                expandableListView.collapseGroup(groupPosition);
                            } else {
                                imv.setBackgroundResource(R.drawable.ic_arrow_drop_up_white_36dp);
                                expandableListView.expandGroup(groupPosition);
                            }
                        }
                    });
                    tv4.setOnClickListener(new View.OnClickListener() {//拨打电话
                        @Override
                        public void onClick(View v) {
                            Log.i("dddddd",fieldInfo.getTelephone()+"ddddddddddd");
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + fieldInfo.getUser_name()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    tv5.setOnClickListener(new View.OnClickListener() {//开始导航
                        @Override
                        public void onClick(View v) {//导航
                            Double longtitude=fieldInfo.getLongitude();
                            Double latitude=fieldInfo.getLatitude();

                            //导航
                            GohereListener gohere =  new GohereListener(longtitude,latitude);
                            gohere.routeplanToNavi();

                            Log.i("jjjjjjjjjjj",String.valueOf(longtitude)+"ggg"+String.valueOf(latitude));
                        }
                    });
                }else {
                    Log.e(TAG, "未知错误1：未获取到农田信息，请返回上一个界面重新选择！");
                }
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                FieldInfo fieldInfo1= mainMenu.selectedFieldInfo.get(groupPosition);
                if (convertView == null) {
                    convertView = LayoutInflater.from(mainMenu).inflate(R.layout.expandablelistview_query_child, null);
                }
                TextView tv1=(TextView)convertView.findViewById(R.id.text1);
                tv1.setText("农田面积："+fieldInfo1.getArea_num()+"亩");
                TextView tv2=(TextView)convertView.findViewById(R.id.text2);
                tv2.setText("作物类型："+commonUtil.transferCropKind(fieldInfo1.getCrops_kind()));
                TextView tv3=(TextView)convertView.findViewById(R.id.text3);
                tv3.setText("单价："+fieldInfo1.getUnit_price()+"元/亩");
                TextView tv4=(TextView)convertView.findViewById(R.id.text4);
                tv4.setText("地块类型："+fieldInfo1.getBlock_type());
                Date date1=new Date();
                Date date2=new Date();
                try {
                    date1 = format.parse(fieldInfo1.getStart_time());
                    date2 = format.parse(fieldInfo1.getEnd_time());
                }catch (Exception e)
                {
                    Log.e(TAG,e.toString());
                }
                TextView tv5=(TextView)convertView.findViewById(R.id.text5);
                tv5.setText("起止日期：" + format2.format(date1) + "--" + format2.format(date2));
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {

                return true;
            }
        };
        expandableListView.setAdapter(adapter);

        initBtnPopup();
        pop_countInfo=(Button)infoView.findViewById(R.id.pop_infos);
        pop_countInfo.setOnClickListener(this);
        pop_countInfo.setText("共找到" + String.valueOf(mainMenu.selectedFieldInfo.size()) + "块农田，点击关闭");


        //////////////////////////地图代码////////////////////////////
        //获取农机并获取农机经纬度
        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            if(machine_id!=null){
                //根据农机IP向服务器请求获取农机经纬度
                gps_MachineLocation(machine_id);//获取GPS位置,经纬度信息
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        //获取地图控件引用

//        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mMapView = (MapView)view.findViewById(R.id.bmapView);
        mMapView.showScaleControl(true);

        mBaiduMap = mMapView.getMap();

        // 改变地图状态
        //MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        //mBaiduMap.setMapStatus(msu);

        //注册回到当前位置按钮监听事件
        ImageButton locationBtn = (ImageButton)view.findViewById(R.id.my_location);
        locationBtn.setOnClickListener(new goBackListener());

        //给服务器传递参数，启动该Activity获取50公里的维修站点经纬度
        Log.i(TAG, "启动activity获取50公里农田经纬度");


        // 开启图层定位
        // -----------location config ------------

        locationService = ((AppController) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的

        //注册监听
        locationService.registerListener(new mListener());
        locationService.setLocationOption(locationService.getOption());


        mBaiduMap.setMyLocationEnabled(true);
        locationService.start();// 定位SDK

        markRepairStation(mainMenu.selectedFieldInfo);
        /////////////////地图代码结束////////////////////////


         /*
        * 导航用
        * */
        activityList.add(getActivity());
        BNOuterLogUtil.setLogSwitcher(true);
        if (initDirs()) {
            initNavi();
        }
        /*
        * 导航用结束
        * */


        return view;
    }

    //监听返回按键
    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if(btn_pop_flag){//关闭确认信息弹窗
                        btn_pop_flag=false;
                        btn_popup.dismiss();
                        return true;
                    }else {
                        mainMenu.getSupportFragmentManager().popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        if (mMapView!=null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.clearFieldData();//清空缓存的农田数据
                try {
                    mainMenu.selectedFieldInfo.clear();
                }catch (Exception e){}
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.my_location:
                gps_MachineLocation(machine_id);
                break;
            case R.id.infos:
                btn_pop_flag=true;
                btn_popup.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.pop_infos:
                btn_pop_flag=false;
                btn_popup.dismiss();
                break;
        }
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
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
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
            LatLng ll = new LatLng(Double.valueOf(GPS_latitude),Double.valueOf(GPS_longitude));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
            commonUtil.error_hint("自动定位成功");
        } else{
            Log.e(TAG, "GPS自动定位失败,开启百度定位！");
            try {
                GPS_latitude = String.valueOf(curlocation.getLatitude());
                GPS_longitude = String.valueOf(curlocation.getLongitude());
                LatLng ll = new LatLng(curlocation.getLatitude(),
                        curlocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                commonUtil.error_hint("自动定位成功");
            }catch (Exception e)
            {
                commonUtil.error_hint("自动定位失败，请重试！");
                Log.e(TAG, "Location Error:" + "自动定位失败" + e.getMessage());
            }
        }
    }
    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////

    ////////////////////////////地图代码开始//////////////////////////////////
    class mListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 构造定位数据
            MyLocationData locData;
            if(!text_gps_flag){//成功获取农机经纬度
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(Double.parseDouble(GPS_latitude))
                        .longitude(Double.parseDouble(GPS_longitude))
                        .build();
            }else{
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
            }

            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_geo);
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, false, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            Log.i("tttttttttt", location.getLatitude() + "---" + location.getLongitude());


            //保存百度地图定位的当前位置
            curlocation = location;

            // 第一次定位时，将地图位置移动到当前位置，这里有问题，先定位到河北农业大学
            /*if (isFristLocation) {
                isFristLocation = false;

                //保存当前location
                curlocation = location;

                if(!text_gps_flag) {//成功获取农机位置
                    LatLng ll = new LatLng(Double.parseDouble(GPS_latitude),
                            Double.parseDouble(GPS_longitude));
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(u);
                }else{
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(u);
                }
            }*/
        }
    }

    //标记农田,参数经纬度
    private void markRepairStation(List<FieldInfo> fieldInfos) {
        //清楚覆盖物Marker,重新加载
        mBaiduMap.clear();

        Integer[] marks = new Integer[]{R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5,
                R.drawable.s6, R.drawable.s7, R.drawable.s8, R.drawable.s9, R.drawable.s10,R.drawable.s11, R.drawable.s12,
                R.drawable.s13, R.drawable.s14, R.drawable.s15, R.drawable.s16, R.drawable.s17, R.drawable.s18, R.drawable.s19,
                R.drawable.s20, R.drawable.s21, R.drawable.s22, R.drawable.s23, R.drawable.s24, R.drawable.s25, R.drawable.s26,
                R.drawable.s27, R.drawable.s28, R.drawable.s29, R.drawable.s30};
        for (int i = 0; i < fieldInfos.size(); i++) {
            FieldInfo f = fieldInfos.get(i);
            LatLng point = new LatLng(f.getLatitude(), f.getLongitude());

            int icon ;
            if(i<30){
                icon=marks[i];
            }else{
                icon=R.drawable.icon_gcoding;
            }

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(icon);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Marker marker = (Marker) mBaiduMap.addOverlay(option);

            //FieldInfo fieldInfo = new FieldInfo();
            //fieldInfo.setLatitude(numthree[i][0]);
            //fieldInfo.setLongitude(numthree[i][1]);
            //fieldInfo.setTelephone("13483208987");

            Bundle bundle = new Bundle();
            bundle.putSerializable("fieldInfo", f);
            marker.setExtraInfo(bundle);

            //添加覆盖物鼠标点击事件
            mBaiduMap.setOnMarkerClickListener(new markerClicklistener());
        }

        mMapView.refreshDrawableState();
    }

    //地图图标点击事件监听类
    class markerClicklistener implements BaiduMap.OnMarkerClickListener {

        /**
         * 地图 Marker 覆盖物点击事件监听函数
         *
         * @param marker 被点击的 marker
         */
        @Override
        public boolean onMarkerClick(Marker marker) {
            final FieldInfo fieldInfo = (FieldInfo) marker.getExtraInfo().get("fieldInfo");
            InfoWindow infoWindow;

            //构造弹出layout
            LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
            View markerpopwindow = inflater.inflate(R.layout.markerpopwindow, null);

            TextView tv = (TextView) markerpopwindow.findViewById(R.id.markinfo);
            String markinfo = "位置:"+fieldInfo.getVillage()+"\n"+
                    "电话:" + fieldInfo.getUser_name() + "\n" +
                    "面积:" + fieldInfo.getArea_num() + "\n" +
                    "单价:"+fieldInfo.getUnit_price()+"\n"+
                    "开始时间:" + fieldInfo.getStart_time()+"\n"+
                    "结束时间:"+fieldInfo.getEnd_time();
            Log.i("markinfo", markinfo);
            tv.setText(markinfo);


            ImageButton tellBtn = (ImageButton) markerpopwindow.findViewById(R.id.markerphone);
            tellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + fieldInfo.getUser_name()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            LatLng ll = marker.getPosition();
            //将marker所在的经纬度的信息转化成屏幕上的坐标
            Point p = mBaiduMap.getProjection().toScreenLocation(ll);
            p.y -= 90;
            LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
            //初始化infoWindow，最后那个参数表示显示的位置相对于覆盖物的竖直偏移量，这里也可以传入一个监听器
            infoWindow = new InfoWindow(markerpopwindow, llInfo, 0);
            mBaiduMap.showInfoWindow(infoWindow);//显示此infoWindow
            mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    mBaiduMap.hideInfoWindow();
                }
            });
            //让地图以备点击的覆盖物为中心
            MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.setMapStatus(status);
            return true;
        }
    }

    //回到当前位置按钮点击事件,将当前位置定位到屏幕中心
    class goBackListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            LatLng ll = new LatLng(Double.parseDouble(GPS_latitude),
                    Double.parseDouble(GPS_longitude));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);

        }
    }

    ////////////////////////////地图代码结束/////////////////////////////////



    private void initBtnPopup()//初始化信息弹窗
    {
        btn_popup = new PopupWindow(infoView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_popup.setAnimationStyle(R.style.slideAnimation_bottom);
        btn_popup.setOutsideTouchable(true);
        btn_popup.setOutsideTouchable(true);
        btn_popup.setFocusable(true);
        btn_popup.setBackgroundDrawable(new ColorDrawable(0x55000000));
    }

    private class callListener implements View.OnClickListener//拨打电话telephone
    {
        @Override
        public void onClick(View v) {

        }
    }

    private class navigationListener implements View.OnClickListener//开始导航nav_longitude，nav_latitude
    {
        @Override
        public void onClick(View v) {

        }
    }

    /*
   * 导航使用
   * */

    /*
    点击去这里按钮进行导航监听类
    * */
    class GohereListener implements View.OnClickListener {
        private Double latitude;
        private Double logtitude;

        public GohereListener(Double latitude,Double logtitude) {
            this.latitude=latitude;
            this.logtitude=logtitude;
        }

        @Override
        public void onClick(View v) {
            //使用百度地图进行导航
            routeplanToNavi();
        }


        private void routeplanToNavi() {
            BNRoutePlanNode sNode = null;
            BNRoutePlanNode eNode = null;

            sNode = new BNRoutePlanNode( Double.parseDouble(GPS_longitude), Double.parseDouble(GPS_latitude),"", null, BNRoutePlanNode.CoordinateType.BD09LL);
            eNode = new BNRoutePlanNode( this.latitude,this.logtitude, "", null, BNRoutePlanNode.CoordinateType.BD09LL);

            if (sNode != null && eNode != null) {
                List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
                list.add(sNode);
                list.add(eNode);
                /*发起算路操作并在算路成功后通过回调监听器进入导航过程.
                 *参数:
                 *activity - 建议是应用的主Activity
                 *nodes - 传入的算路节点，顺序是起点、途经点、终点，其中途经点最多三个，参考 BNRoutePlanNode
                 *preference - 算路偏好，参考RoutePlanPreference定义 [推荐:1,高速优先(用时最少):2,少走高速（路径最短）:4,少收费:8,躲避拥堵:16]
                 *isGPSNav - true表示真实GPS导航，false表示模拟导航
                 *listener - 开始导航回调监听器，在该监听器里一般是进入导航过程页面
                 * */
                BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 2, true, new GohereRoutePlanListener(sNode));

            }
        }
    }

    class GohereRoutePlanListener implements BaiduNaviManager.RoutePlanListener {
        private BNRoutePlanNode mBNRoutePlanNode = null;

        public GohereRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
			 */
            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDGuideActivity")) {

                    return;
                }
            }

            Intent intent = new Intent(getActivity(), BNDGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(getActivity(), "算路失败", Toast.LENGTH_SHORT).show();
        }
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

    private void initNavi() {
        BNOuterTTSPlayerCallback ttsCallback = null;
        BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int i, String s) {
                if (0 == i) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + s;
                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void initStart() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "百度导航引擎初始化开始");
            }

            @Override
            public void initSuccess() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
                Log.e(TAG, "百度导航引擎初始化成功");
            }

            @Override
            public void initFailed() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "百度导航引擎初始化失败");
            }
        }, null, ttsHandler, null);
    }

    private void initSetting() {
        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
//                    Toast.makeText(getActivity(), "Handler : TTS play start", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Handler : TTS play start");
                    //showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
//                    Toast.makeText(getActivity(), "Handler : TTS play end", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Handler : TTS play end");
                    //showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    ////////////////////////////地图代码结束//////////////////////////
    //////////////////////////////////////////////////////////////
}
