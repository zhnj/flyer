package com.njdp.njdp_drivers.items;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
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
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter_white_1;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import bean.RepairInfo;

public class item_repair_station extends Fragment implements View.OnClickListener {

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

    //**************维修站变量********************************
    private RepairInfo repairInfo;
    private List<RepairInfo> repairInfos;
    private String machine_id;//机器ID
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    private boolean text_gps_flag = false;//GPS定位是否成功


    ////////////////////////地图变量//////////////////////////
    private TextureMapView mMapView = null;
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
    private BDLocation curlocation ; //当前位置
    ////////////////////////地图变量//////////////////////////


    ////********************/////////////上拉抽屉变量*******************////////
    private MySlidingDrawer mdrawer;
    private ImageButton mdraerbtn;
    private LinearLayout otherHandler;
    private View view;
    private Button btn_add_cut;// 真实抽屉左上角按钮
    int i = 1;// 定义成员变量，协助点击左上角按钮时改变图标，点击+变成2，点击-变成1
    private ExpandableListView list1;
    private ExpandableListView list2;
    MyBaseExpandableListAdapter adapter1;
    MyBaseExpandableListAdapter adapter2;
    MyBaseExpandableListAdapter adapter;
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ////////////////////////地图代码////////////////////////////////////
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        ////////////////////////地图代码结束////////////////////////////////////

        view = inflater.inflate(R.layout.activity_4_repair_station, container,
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
        area_adapter=new SpinnerAdapter_white_1(mainMenu, getResources().getStringArray(R.array.area));
        spinner_area.setAdapter(area_adapter);
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sl_area_flag = true;
                sl_area = spinner_area.getSelectedItem().toString();
                initRepairStationInfo(sl_area, token);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);
        url= AppConfig.URL_REPAIRE;


        //////////////////////////地图代码////////////////////////////
        //获取地图控件引用

        //mMapView = (MapView) getActivity().findViewById(R.id.bmapView);
        mMapView = (TextureMapView) view.findViewById(R.id.bmapView);
        mMapView.showScaleControl(true);

        mBaiduMap = mMapView.getMap();

        // 改变地图状态
        //MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        //mBaiduMap.setMapStatus(msu);

        //注册回到当前位置按钮监听事件
        ImageButton locationBtn = (ImageButton)view.findViewById(R.id.my_location);
        locationBtn.setOnClickListener(new goBackListener());


        // 开启图层定位
        // -----------location config ------------

        locationService = ((AppController) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的

        //注册监听
        locationService.registerListener(new mListener());
        locationService.setLocationOption(locationService.getOption());


        mBaiduMap.setMyLocationEnabled(true);
        locationService.start();// 定位SDK

        /////////////////地图代码结束////////////////////////


        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            if(machine_id!=null){
                //根据农机IP向服务器请求获取农机经纬度
                gps_MachineLocation(machine_id);//获取GPS位置,经纬度信息
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        repairInfos = new ArrayList<RepairInfo>();



        //************上拉抽屉操作***********************///
        findview();
        addlistener();



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


    //获取维修站信息
    private void initRepairStationInfo(final String area,final String  token)
    {
        String tag_string_req = "req_repairStation";

        mainMenu.pDialog.setMessage("正在载入 ...");
        mainMenu.showDialog();

        if(!netUtil.checkNet(mainMenu)){
            commonUtil.error_hint("网络连接错误");
            mainMenu.hideDialog();
        }else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, RSuccessListener, mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    if(null == area){
                        params.put("range", "50");
                    }else{
                        if(area.equals("默认距离")){
                            params.put("range","50");
                        }else if(area.equals("全部")){
                            params.put("range","100");
                        }else {
                            params.put("range", area);
                        }
                    }
                    params.put("token",token);
                    if(!text_gps_flag){
                        params.put("latitude",String.valueOf(GPS_latitude));
                        params.put("longitude", String.valueOf(GPS_longitude));

                    }else{
                        params.put("latitude",String.valueOf(curlocation.getLatitude()));
                        params.put("longitude", String.valueOf(curlocation.getLongitude()));
                    }


                    return netUtil.checkParams(params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    }

    //获取维修站信息数据响应服务器成功
    private Response.Listener<String> RSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d(TAG, "AreaInit Response: " + response);
            mainMenu.hideDialog();
            //if (netUtil.checkNet(mainMenu)) {
                //commonUtil.error_hint2_short(R.string.net_error);
            //} else {
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
                        repairInfos.clear();
                        JSONArray jObjs = jObj.getJSONArray("result");
                        Log.i("cccccccc",String.valueOf(jObjs.length()));
                        for(int i = 0; i < jObjs.length(); i++) {
                            RepairInfo ri = new RepairInfo();
                            JSONObject object = (JSONObject)jObjs.opt(i);
                            ri.setRepair_name(object.getString("repair_name"));
                            ri.setRepair_telephone(object.getString("repair_telephone"));
                            ri.setRepair_qq(object.getString("repair_qq"));
                            ri.setRepair_weixin(object.getString("repair_weixin"));
                            ri.setRepair_address(object.getString("repair_address"));
                            ri.setRepair_latitude(Double.parseDouble(object.getString("repair_latitude")));
                            ri.setRepair_longitude(Double.parseDouble(object.getString("repair_longitude")));

                            repairInfos.add(ri);

                        }
                        markRepairStation(repairInfos);

                    } else {
                        commonUtil.error_hint("其它位置错误");
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.e(TAG, "Error,Json error：response错误:" + e.getMessage());
               // }
                mainMenu.hideDialog();
            }
        }
    };


    /////////////地图代码/////////////////////
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
            Log.i("wwwwwwwwwwwwwwww", location.getLatitude() + "---" + location.getLongitude());


            //保存百度地图定位的当前位置
            curlocation = location;

            // 第一次定位时，将地图位置移动到当前位置，这里有问题，先定位到河北农业大学
            /*if (isFristLocation) {
                isFristLocation = false;


                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);


                //第一次初始化维修站
                initRepairStationInfo(sl_area,token);
            }*/
        }
    }

    //回到当前位置按钮点击事件,将当前位置定位到屏幕中心
    class goBackListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if (!text_gps_flag) {
                LatLng ll = new LatLng(Double.parseDouble(GPS_latitude),
                        Double.parseDouble(GPS_longitude));
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }else{
                LatLng ll = new LatLng(curlocation.getLatitude(),
                        curlocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }
    }
    //标记农田,参数经纬度
    private void markRepairStation(final List<RepairInfo> repairInfos) {
        //清楚覆盖物Marker,重新加载
        mBaiduMap.clear();

        Integer[] marks = new Integer[]{R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5,
                R.drawable.s6, R.drawable.s7, R.drawable.s8, R.drawable.s9, R.drawable.s10, R.drawable.s11, R.drawable.s12,
                R.drawable.s13, R.drawable.s14, R.drawable.s15, R.drawable.s16, R.drawable.s17, R.drawable.s18, R.drawable.s19,
                R.drawable.s20, R.drawable.s21, R.drawable.s22, R.drawable.s23, R.drawable.s24, R.drawable.s25, R.drawable.s26,
                R.drawable.s27, R.drawable.s28, R.drawable.s29, R.drawable.s30};
        for (int i = 0; i < repairInfos.size(); i++) {
            RepairInfo r = repairInfos.get(i);
            LatLng point = new LatLng(r.getRepair_latitude(), r.getRepair_longitude());

            int icon ;
            if(i<30){
                icon=marks[i];
            }else{
                icon= R.drawable.icon_gcoding;
            }

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(icon);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .zIndex(20)
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Marker marker = (Marker) mBaiduMap.addOverlay(option);

            //FieldInfo fieldInfo = new FieldInfo();
            //fieldInfo.setLatitude(numthree[i][0]);
            //fieldInfo.setLongitude(numthree[i][1]);
            //fieldInfo.setTelephone("13483208987");

            Bundle bundle = new Bundle();
            bundle.putSerializable("repairInfo", r);
            marker.setExtraInfo(bundle);

            //添加覆盖物鼠标点击事件
            mBaiduMap.setOnMarkerClickListener(new markerClicklistener());
        }

        mMapView.refreshDrawableState();



        //**********************初始化listview***************/
        //绑定Layout里面的ListView1
        list1 = (ExpandableListView) view.findViewById(R.id.repair_station_listview1);

        //生成动态数组(前三个)，加入数据
        ArrayList<HashMap<String, Object>> listItem1 = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<repairInfos.size();i++)
        {
            if(i>2){
                break;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemName", (i+1)+". "+repairInfos.get(i).getRepair_name());
            map.put("ItemAddress", repairInfos.get(i).getRepair_address());
            map.put("ItemRange",repairInfos.get(i).getDistance());
            map.put("ItemPhone",repairInfos.get(i).getRepair_telephone());
            map.put("ItemQQ",repairInfos.get(i).getRepair_qq());
            map.put("ItemWeixin",repairInfos.get(i).getRepair_weixin());
            map.put("ItemOthers",repairInfos.get(i).getRerepair_chief());

            listItem1.add(map);
            listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        /*SimpleAdapter listItemAdapter1 = new SimpleAdapter(getActivity(),listItem1,//数据源
                R.layout.updrawerlistview,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"ItemName","ItemAddress"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.ItemName,R.id.ItemAddress}
        );*/
        adapter1 = new MyBaseExpandableListAdapter(getActivity(),listItem1);
        //添加并且显示
        list1.setAdapter(adapter1);


        //绑定Layout里面的ListView2
        list2 = (ExpandableListView) view.findViewById(R.id.repair_station_listview2);

        //生成动态数组(前三个)，加入数据
        ArrayList<HashMap<String, Object>> listItem2 = new ArrayList<HashMap<String, Object>>();
        for(int i=3;i<repairInfos.size();i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemName", (i+1)+". "+repairInfos.get(i).getRepair_name());
            map.put("ItemAddress", repairInfos.get(i).getRepair_address());
            map.put("ItemRange",repairInfos.get(i).getDistance());
            map.put("ItemPhone",repairInfos.get(i).getRepair_telephone());
            map.put("ItemQQ",repairInfos.get(i).getRepair_qq());
            map.put("ItemWeixin",repairInfos.get(i).getRepair_weixin());
            map.put("ItemOthers",repairInfos.get(i).getRerepair_chief());

            listItem2.add(map);
            listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        /*SimpleAdapter listItemAdapter2 = new SimpleAdapter(getActivity(),listItem2,//数据源
                R.layout.updrawerlistview,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[] {"ItemName","ItemAddress"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.ItemName,R.id.ItemAddress}
        );*/
        //adapter2 = new MyBaseExpandableListAdapter(getActivity(),listItem2);
        //添加并且显示
        //list2.setAdapter(adapter2);
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
            return showPopWindow(marker,null);
        }
    }

    //点击气泡弹出窗体
    public boolean showPopWindow(Marker marker,RepairInfo ri){
        if(marker!=null) {
            repairInfo = (RepairInfo) marker.getExtraInfo().get("repairInfo");
        }else if(ri!=null){
            repairInfo = ri;
        }
        InfoWindow infoWindow;

        //构造弹出layout
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        View markerpopwindow = inflater.inflate(R.layout.markerpopwindow, null);

        TextView tv = (TextView) markerpopwindow.findViewById(R.id.markinfo);
        String markinfo = "加油站:"+repairInfo.getRepair_name()+"\n"+
                "电话:" + repairInfo.getRepair_telephone() + "\n" +
                "位置:" + repairInfo.getRepair_address();
        tv.setText(markinfo);


        ImageButton tellBtn = (ImageButton) markerpopwindow.findViewById(R.id.markerphone);
        tellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + repairInfo.getRepair_telephone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

       // LatLng ll = marker.getPosition();
        LatLng ll = new LatLng(repairInfo.getRepair_latitude(),repairInfo.getRepair_longitude());
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
            LatLng ll = new LatLng(Double.valueOf(GPS_latitude),
                    Double.valueOf(GPS_longitude));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
            commonUtil.error_hint("GPS定位成功");
            initRepairStationInfo(sl_area, token);
        } else {
            Log.e(TAG, "GPS自动定位失败,开启百度定位！");
            try {
                GPS_latitude = String.valueOf(curlocation.getLatitude());
                GPS_longitude = String.valueOf(curlocation.getLongitude());
                LatLng ll = new LatLng(curlocation.getLatitude(),
                        curlocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);

                initRepairStationInfo(sl_area, token);
            }catch (Exception e)
            {
                commonUtil.error_hint("自动定位失败，请重试！");
                Log.e(TAG, "Location Error:" + "自动定位失败" + e.getMessage());
            }
        }
    }

    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////


    //***********************上拉抽屉代码*****************************************************/
    private boolean slidingup = false;
    /** 初始化组件 */
    private void findview() {
        mdrawer = (MySlidingDrawer) view.findViewById(R.id.stockDrawer);
        // 传入抽屉手柄的id
        mdrawer.setHandleId(R.id.mbutton);
        mdrawer.setTouchableIds(new int[]{R.id.otherHandler});
        mdraerbtn = (ImageButton) view.findViewById(R.id.mbutton);
        otherHandler = (LinearLayout) view.findViewById(R.id.otherHandler);
    }
    /**
     * 给各个组建添加事件监听
     */
    private void addlistener() {
        final MyBaseExpandableListAdapter nullAdapter = null;
        // 抽屉打开
        mdrawer.setOnDrawerOpenListener(new MySlidingDrawer.OnDrawerOpenListener() {
            public void onDrawerOpened() {

                list1.setAdapter(nullAdapter);
                adapter = new MyBaseExpandableListAdapter(getActivity(),listItem);
                list2.setAdapter(adapter);
                mdraerbtn.setImageResource(R.drawable.sliding_down);

                slidingup = true;
            }
        });
        // 抽屉关闭
        mdrawer.setOnDrawerCloseListener(new MySlidingDrawer.OnDrawerCloseListener() {
            public void onDrawerClosed() {
                list1.setAdapter(adapter1);
                list2.setAdapter(nullAdapter);
                mdraerbtn.setImageResource(R.drawable.sliding_up);

                slidingup = false;
            }
        });
        // 抽屉正在拉动或停止拉动
        mdrawer.setOnDrawerScrollListener(new MySlidingDrawer.OnDrawerScrollListener() {
            public void onScrollStarted() {
            }

            public void onScrollEnded() {
            }
        });
    }


    class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ArrayList<HashMap<String, Object>> listItem;

        public MyAdapter(Context c,ArrayList<HashMap<String, Object>> listItem) {
            this.inflater = LayoutInflater.from(c);
            this.listItem = listItem;
        }

        @Override
        public int getCount() {
            return listItem.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        /**
         * 设置数据源与行View关联 设置行中个组件的事件响应 返回设置好的View
         */
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // 取得要显示的行View
            View myView;
            ImageButton button_phone;
            ImageButton button_zhidao;
            if(slidingup){

                myView = inflater.inflate(R.layout.updrawerlistview, null);
                button_phone = (ImageButton) myView.findViewById(R.id.mark_phone);
                button_zhidao = (ImageButton) myView.findViewById(R.id.mark_zhidao);
                final int arg = arg0;
                // 添加事件响应
                button_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + repairInfos.get(arg).getRepair_telephone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                button_zhidao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url="mqqwpa://im/chat?chat_type=wpa&uin="+repairInfos.get(arg).getRepair_qq();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });

            }else{
                myView = inflater.inflate(R.layout.updrawerlistview_nobutton, null);
            }

            RelativeLayout layout = (RelativeLayout) myView
                    .findViewById(R.id.RelativeLayout01);
            /*if (arg0 % 2 == 0) {
                layout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.bg1));
            } else {
                layout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.bg2));
            }*/

            TextView textname = (TextView) myView.findViewById(R.id.ItemName);
            //TextView textaddress = (TextView) myView.findViewById(R.id.ItemAddress);
            //TextView textrange = (TextView) myView.findViewById(R.id.ItemRange);
            // 让行View的每个组件与数据源相关联
            Log.i("ggggggggggg", String.valueOf(arg0));
            textname.setText((String) listItem.get(arg0).get("ItemName"));
            //if(listItem.get(arg0).get("ItemAddress")==null){
                //textaddress.setText("未知");
            //}else{
                //textaddress.setText((String) listItem.get(arg0).get("ItemAddress"));
            //}
            //if(listItem.get(arg0).get("ItemRange")==null) {
                //textrange.setText("未知");
            //}else{
                //textrange.setText((String) listItem.get(arg0).get("ItemRange"));
            //}
            return myView;
        }
    }

    class MyBaseExpandableListAdapter extends BaseExpandableListAdapter{
        private LayoutInflater inflater;
        private ArrayList<HashMap<String, Object>> listItem;

        public MyBaseExpandableListAdapter(Context c,ArrayList<HashMap<String, Object>> listItem){
            this.inflater = LayoutInflater.from(c);
            this.listItem = listItem;
        }

        @Override
        public int getGroupCount() {
            return listItem.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listItem.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listItem.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View myView;
            ImageButton button_phone;
            ImageButton button_zhidao;

            myView = inflater.inflate(R.layout.updrawerlistview_nobutton, null);

            RelativeLayout layout = (RelativeLayout) myView
                    .findViewById(R.id.RelativeLayout01);
            /*if (arg0 % 2 == 0) {
                layout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.bg1));
            } else {
                layout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.bg2));
            }*/

            TextView textname = (TextView) myView.findViewById(R.id.ItemName);
            //TextView textaddress = (TextView) myView.findViewById(R.id.ItemAddress);
            //TextView textrange = (TextView) myView.findViewById(R.id.ItemRange);

            textname.setText((String) listItem.get(groupPosition).get("ItemName"));
            //textaddress.setText("地址:"+(String) listItem.get(groupPosition).get("ItemAddress"));
            //textrange.setText("距离:"+(String) listItem.get(groupPosition).get("ItemRange"));

            return myView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View myView;

            Log.i("ggggg",String.valueOf(groupPosition)+"dddddd"+String.valueOf(childPosition));

            myView = inflater.inflate(R.layout.updrawinglistview_child, null);

            TextView textrange = (TextView)myView.findViewById(R.id.ytext1);
            TextView textaddress = (TextView)myView.findViewById(R.id.ytext2);
            TextView textphone = (TextView)myView.findViewById(R.id.ytext3);
            Button btnTell = (Button)myView.findViewById(R.id.ytext31);//打电话
            TextView textqq = (TextView)myView.findViewById(R.id.ytext4);
            Button btnQQ = (Button)myView.findViewById(R.id.ytext41);//打开QQ
            TextView textweixin = (TextView)myView.findViewById(R.id.ytext5);
            TextView textchief = (TextView)myView.findViewById(R.id.ytext6);

            if(listItem.get(groupPosition).get("ItemRange")!=null) {
                textrange.setText("距离:" + (String) listItem.get(groupPosition).get("ItemRange"));
            }else{
                textrange.setText("距离:未知");
            }
            if(listItem.get(groupPosition).get("ItemAddress")!=null) {
                textaddress.setText("地址:" + (String) listItem.get(groupPosition).get("ItemAddress"));
            }else{
                textaddress.setText("地址:未知");
            }
            if(listItem.get(groupPosition).get("ItemPhone")!=null) {
                textphone.setText("联系电话:" + (String) listItem.get(groupPosition).get("ItemPhone"));

                final int tellindex = groupPosition;
                btnTell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + listItem.get(tellindex).get("ItemPhone")));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }else{
                textphone.setText("联系电话:未知");
            }

            if(listItem.get(groupPosition).get("ItemQQ")!=null) {
                textqq.setText("QQ:" + (String) listItem.get(groupPosition).get("ItemQQ"));

                final int qqindex = groupPosition;
                btnQQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url="mqqwpa://im/chat?chat_type=wpa&uin="+listItem.get(qqindex).get("ItemQQ");
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });
            }else{
                textqq.setText("QQ:未知");
            }
            if(listItem.get(groupPosition).get("ItemWeixin")!=null) {
                textweixin.setText("微信:"+(String) listItem.get(groupPosition).get("ItemWeixin"));
            }else{
                textweixin.setText("微信:未知");
            }
            if(listItem.get(groupPosition).get("ItemOthers")==null){
                textchief.setText("其它说明:无");
            }else{
                textchief.setText("其它说明:"+(String) listItem.get(groupPosition).get("ItemOthers"));
            }


            return myView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

}