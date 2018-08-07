package com.njdp.njdp_drivers.items;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter_white_1;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SavedFieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.njdp.njdp_drivers.util.NormalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bean.FieldInfo;
import bean.SavedFiledInfo;
import overlayutil.OverlayManager;

public class item_intelligent_resolution_4 extends Fragment implements View.OnClickListener {
    private String TAG=item_intelligent_resolution_4.class.getSimpleName();
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private Spinner sp_site;
    private ArrayAdapter siteAdapter;
    private String token;
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private ProgressDialog pDialog;
    private List<SavedFiledInfo> navigationDeploy=new ArrayList<SavedFiledInfo>();//选择的调配方案对应的农田信息
    private SavedFieldInfoDao savedFieldInfoDao;
    private String[] sites;//地点的数组
    private int sl_site=0;//下拉选中的地点序号0，1，.....

    private String machine_id;//机器ID
    private NetUtil netUtil;
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    private boolean text_gps_flag = false;//GPS定位是否成功


    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////地图用变量///////////////////////////////
    private MapView mMapView;
    BaiduMap mBaiduMap;
    View markerpopwindow;

    //定位用变量
    /**
     * 当前定位的模式
     */
    private LocationService locationService;
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private boolean isFristLocation = true;
    //当前位置
    private BDLocation curlocation;
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int authBaseRequestCode = 1;

    //路径规划用变量
    RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    int nodeIndex = -1;//节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    List<LatLng> points;
    List<Integer> colors;
    //导航
     /*
    *导航用变量
    * */
    public static List<Activity> activityList = new LinkedList<Activity>();
    private static final String APP_FOLDER_NAME = "BNSDK";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private String mSDCardPath = null;

    ///////////////////////////////地图用变量结束//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ////////////////////////地图代码////////////////////////////////////
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        ////////////////////////地图代码结束////////////////////////////////////


        View view = inflater.inflate(R.layout.activity_1_intelligent_resolution_4, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.getNavigation).setOnClickListener(this);
        this.sp_site=(Spinner)view.findViewById(R.id.site);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        savedFieldInfoDao=new SavedFieldInfoDao(mainMenu);

        try {
            navigationDeploy.addAll(savedFieldInfoDao.allFieldInfo());
            sites=new String[navigationDeploy.size()+1];
            sites[0]="请选择作业地点";
            for(int i=0;i<navigationDeploy.size();i++) {
                sites[i + 1] = navigationDeploy.get(i).getCropLand_site();
            }
        }catch (Exception e){
            Log.e(TAG,e.toString());
        };

        sessionManager=new SessionManager();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        token=sessionManager.getToken();
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);

        siteAdapter=new SpinnerAdapter_white_1(mainMenu,sites);
        sp_site.setAdapter(siteAdapter);
        sp_site.setOnItemSelectedListener(new siteItemSelected());


        //////////////////////////////////////////////
        ////////////////地图代码/////////////////////
        // 开启图层定位
        // -----------location config ------------

        locationService = ((AppController) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的

        //注册监听
        locationService.registerListener(new mListener());
        locationService.setLocationOption(locationService.getOption());
        locationService.start();// 定位SDK

        ///////////////////////路径规划////////////////////////
        //获取第一个位置和最后一个位置,获取中间节点，进行驾车路径规划
        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            if(machine_id!=null){
                //根据农机IP向服务器请求获取农机经纬度
                gps_MachineLocation(machine_id);//获取GPS位置,经纬度信息
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //请求定位权限
        //requestPower();
         /*
        * 导航用
        * */
        activityList.add(getActivity());
        BNOuterLogUtil.setLogSwitcher(true);
        if (initDirs()) {
           // initNavi();
        }
        /*
        * 导航用结束
        * */
        /////////////////////地图代码结束/////////////////////////
        /////////////////////////////////////////////////////////

        return view;
    }
    /**动态请求权限
    public void requestPower() {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (getActivity().shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                getActivity().requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }*/
    /**动态请求权限
    public void requestAllPower() {
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (getActivity().shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                getActivity().requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.getNavigation:
                if(sl_site!=0) {
                    double gps_longitude = navigationDeploy.get(sl_site-1).getLongitude();//经度
                    double gps_latitude=navigationDeploy.get(sl_site-1).getLatitude();//纬度
                    Log.e("导航开始-----","地点："+sites[sl_site]+"，经度："+String.valueOf(gps_longitude)+"，维度："+String.valueOf(gps_latitude));
                    //开始导航
                    GohereListener gohere =  new GohereListener(gps_longitude,gps_latitude);
                    gohere.routeplanToNavi();

                }else{
                    commonUtil.error_hint_short("请选择作业地点！");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            //land
        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            //port
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
//            Toast.makeText(getActivity(), "算路失败", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"算路失败");
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

    private boolean hasBasePhoneAuth() {
        PackageManager pm = getActivity().getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, getActivity().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(getActivity(),
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(getActivity(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();

                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                        Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void initTTS()
    {
        BaiduNaviManagerFactory.getTTSManager().initTTS(getActivity(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }
    class mListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //保存当前location
            curlocation = location;
        }
    }

    ////////////////////////////地图代码结束//////////////////////////
    //////////////////////////////////////////////////////////////


    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////
    public void gps_MachineLocation(final String machine_id) {
        String tag_string_req = "req_GPS";
        //服务器请求
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_findFlyComByUser, new locationSuccessListener(), new locationErrorListener()) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("fm_id", sessionManager.getUserId());
                //params.put("token", token);

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
                    commonUtil.error_hint_short("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                } else if (status == 0) {

                    ///////////////////////////获取服务器农机，经纬度/////////////////////
                    JSONArray location = jObj.getJSONArray("result");
                    GPS_longitude = location.getJSONObject(0).getString("com_longitude");
                    GPS_latitude = location.getJSONObject(0).getString("com_latitude");
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
        } else {
            Log.e(TAG, "GPS自动定位失败,开启百度定位！");
            try {
                GPS_latitude = String.valueOf(curlocation.getLatitude());
                GPS_longitude = String.valueOf(curlocation.getLongitude());
            }catch (Exception e)
            {
                commonUtil.error_hint_short("自动定位失败，请重试！");
                Log.e(TAG, "Location Error:" + "自动定位失败" + e.getMessage());
            }
        }
    }

}
