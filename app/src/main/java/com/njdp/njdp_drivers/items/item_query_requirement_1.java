package com.njdp.njdp_drivers.items;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.baidu.mapapi.map.MapStatus;
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
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter_up_white_1;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.FieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.items.jikai.bean.WeatherBean;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.njdp.njdp_drivers.util.NormalUtils;
import com.squareup.timessquare.CalendarPickerView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private com.beardedhen.androidbootstrap.BootstrapButton hintButton;
    private WindowManager.LayoutParams lp;
    private ArrayAdapter areaAdapter;//c查找范围
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private ExpandableListView expandableListView;

    private ExpandableListAdapter adapter;
    private Button countInfo;
    private Button pop_countInfo;
    private Button btn_search;//查找按钮
    private SimpleDateFormat format;
    private SimpleDateFormat format2;
    private String startTime;
    private String endTime;
    private String token;
    private String machine_id;
    private ProgressDialog pDialog;
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    private String com_longitude="1.1";//无人机飞机公司地址
    private String com_latitude="1.1";//

    private String sl_area="1000";//查找范围
    private boolean text_gps_flag = false;//GPS定位是否成功
    private List<FieldInfo> fieldInfos=new ArrayList<FieldInfo>();//农田信息
    private View parentView;//主View
    private View infoView;
    private PopupWindow btn_popup;
    private boolean btn_pop_flag=false;
    private String telephone;
    private double nav_longitude;
    private double nav_latitude;
    private double centre_longitude;//地图中间经度
    private double centre_latitude;//地图中间维度


    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int authBaseRequestCode = 1;
    private Gson gson;
    private String url=AppConfig.URL_searchFarmlands;//服务器地址，获取
    private AutoCompleteTextView autoQuery;//用户输入查找中心点
    private ArrayAdapter<String> sugAdapter = null;
    private List<SuggestionResult.SuggestionInfo> allSuggestions;
    private int load_Index = 0;
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    ////////////////////////地图变量//////////////////////////
//    private MapView mMapView = null;
    private MapView mMapView=null;
    private BaiduMap mBaiduMap = null;
    private boolean isFristLocation = true;
    private BitmapDescriptor centreBitmap;//中心覆盖物的图标
    private Marker centreMarker;
    private String weatherData="";
    private Marker pointmarker;
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
    ///////////////////////////////////日期选择器变量/////////////////////
    private TextView t_startDate;
    private TextView t_endDate;
    private CalendarPickerView calendarPickerView;
    private View dateView;//日期选择View
    private PopupWindow datePickerPop;//日期选择器弹出
    private boolean popup_flag=false;//日期选择器是否弹出的标志
    private ArrayList<Date> dates = new ArrayList<Date>();//选择的起始日期
    ///////////////////////////////////日期选择器变量/////////////////////
    private Date first_date;
    ////////////////////////////////////操作提示/////////////////////////
    private PopupWindow hintPopup;
    private View hintView;//操作提示View
    ////////////////////////////////////操作提示/////////////////////////

    private String centerName;
    private Spinner sp_area;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_2_query_requirement_1,container,false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.my_location).setOnClickListener(this);
        view.findViewById(R.id.btn_search).setOnClickListener(this);
        view.findViewById(R.id.jobDate).setOnClickListener(this);
        view.findViewById(R.id.arrange_button).setOnClickListener(this);

        sp_area=(Spinner)view.findViewById(R.id.area);
        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;

        //获取权限
        //requestAllPower();


        t_startDate=(TextView)view.findViewById(R.id.startDate);
        t_endDate=(TextView)view.findViewById(R.id.endDate);

        countInfo=(Button)view.findViewById(R.id.infos);
        countInfo.setOnClickListener(this);
        countInfo.setText("共找到"+String.valueOf(mainMenu.selectedFieldInfo.size())+"块农田，点击查看");

        format = new SimpleDateFormat("yyyy-MM-dd");
        format2 = new SimpleDateFormat("yyyy/MM/d");
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
        gson=new Gson();
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
                        convertView = LayoutInflater.from(mainMenu).inflate(R.layout.new_expandablelistview_query_parent, null);
                    }
                    LinearLayout textLayout = (LinearLayout) convertView.findViewById(R.id.expandParent_text);
                    TextView favor = (TextView) convertView.findViewById(R.id.field_favor);
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
                    favor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String flyer_id = SessionManager.getInstance().getUserId();
                            String farmland_id = fieldInfo.getFarm_id();
                            String opration = "收藏";
                            //定义一个url
                            String url = AppConfig.URL_GET_FAVORorADD+"?flyer_id="+flyer_id+"&farmland_id="+farmland_id+"&opration="+opration;
                            //定义一个StringRequest
                            StringRequest request = new StringRequest(Request.Method.GET, url, new
                                    Response.Listener<String>() {// 添加请求成功监听

                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(getContext(),response.contains("成功")?"收藏成功":(response.contains("已收藏")?"已收藏过":"收藏失败"),Toast.LENGTH_LONG).show();
                                        }
                                    }, new Response.ErrorListener() {// 添加请求失败监听

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(),error.toString(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                            // 设置请求的tag标签，便于在请求队列中寻找该请求
                            request.setTag("lhdGet");
                            // 添加到全局的请求队列
                            AppController.getHttpQueues().add(request);
                        }
                    });
                    tv4.setOnClickListener(new View.OnClickListener() {//拨打电话
                        @Override
                        public void onClick(View v) {
                            final String start_date = SessionManager.getInstance().getTime(true);
                            final String end_date = SessionManager.getInstance().getTime(false);
                            //POST网络请求
                            String url=AppConfig.URL_Weather;
                            //定义一个StringRequest
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {// 添加请求成功监听
                                @Override
                                public void onResponse(String response) {
                                    String weather = new String();
                                    //Toast.makeText(getContext(), response,Toast.LENGTH_LONG).show();
                                    //弹出天气提醒,解析天气接口的数据，并调用showWeather方法
                                    WeatherBean weatherBean = new Gson().fromJson(response, WeatherBean.class);

                                    for( int i = 0 ; i < weatherBean.getMessage().size() ; i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
                                        String s = weatherBean.getMessage().get(i).getDate() + "的天气情况：\n       【" + weatherBean.getMessage().get(i).getType()+"】  风力强度："+weatherBean.getMessage().get(i).getWindrage()+"级";
                                        weather += s+"\n";
                                    }

                                    showDialog(weather);
                                    Log.i("weather",weather);

                                }


                            }, new Response.ErrorListener() {// 添加请求失败监听
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "上传失败",Toast.LENGTH_LONG).show();
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> map = new HashMap<String,String>();
                                    map.put("farmID",farmId);
                                    if(startTime.compareToIgnoreCase(fieldInfo.getStart_time().replace('-','/'))<0)
                                        map.put("timeStart", fieldInfo.getStart_time());
                                    else
                                        map.put("timeStart", startTime);

                                    if(endTime.compareToIgnoreCase(fieldInfo.getEnd_time().replace('-','/'))>0)
                                        map.put("timeEnd", fieldInfo.getEnd_time());
                                    else
                                        map.put("timeEnd", endTime);
                                    map.put("token", token);
                                    Log.e("farmId",farmId+start_date+end_date);
                                    return map;
                                }
                            };
                            // 设置请求的tag标签，便于在请求队列中寻找该请求
                            request.setTag("post");
                            // 添加到全局的请求队列
                            AppController.getHttpQueues().add(request);


                        }
                        private void showDialog(String weather){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
                            final AlertDialog.Builder normalDialog =
                                    new AlertDialog.Builder(getContext());
                            //normalDialog.setIcon(R.drawable.);
                            normalDialog.setTitle("天气状况提醒：");
                            normalDialog.setMessage(weather);
                            normalDialog.setPositiveButton("继续",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //...To-do
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + fieldInfo.getUser_name()));
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    });
                            normalDialog.setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //...To-do
                                        }
                                    });
                            // 显示
                            normalDialog.show();
                        }
                    });

                    tv5.setOnClickListener(new View.OnClickListener() {//开始导航
                        @Override
                        public void onClick(View v) {//导航
                            //弹出天气提醒
                            final String start_date = SessionManager.getInstance().getTime(true);
                            final String end_date = SessionManager.getInstance().getTime(false);
                            //POST网络请求
                            String url=AppConfig.URL_Weather;
                            //定义一个StringRequest
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {// 添加请求成功监听
                                @Override
                                public void onResponse(String response) {
                                    String weather = new String();
                                    //Toast.makeText(getContext(), response,Toast.LENGTH_LONG).show();
                                    //弹出天气提醒,解析天气接口的数据，并调用showWeather方法
                                    WeatherBean weatherBean = new Gson().fromJson(response, WeatherBean.class);

                                    for( int i = 0 ; i < weatherBean.getMessage().size() ; i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
                                        String s = weatherBean.getMessage().get(i).getDate() + "的天气情况：\n       【" + weatherBean.getMessage().get(i).getType()+"】  风力强度："+weatherBean.getMessage().get(i).getWindrage()+"级";
                                        weather += s+"\n";
                                    }

                                    showDialog(weather);
                                    Log.i("weather",weather);


                                }


                            }, new Response.ErrorListener() {// 添加请求失败监听
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "上传失败",Toast.LENGTH_LONG).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> map = new HashMap<String,String>();
                                    map.put("token", token);
                                    map.put("farmID",farmId);

                                    if(startTime.compareToIgnoreCase(fieldInfo.getStart_time().replace('-','/'))<0)
                                        map.put("timeStart", fieldInfo.getStart_time());
                                    else
                                        map.put("timeStart", startTime);

                                    if(endTime.compareToIgnoreCase(fieldInfo.getEnd_time().replace('-','/'))>0)
                                        map.put("timeEnd", fieldInfo.getEnd_time());
                                    else
                                        map.put("timeEnd", endTime);
                                    Log.e("farmId",farmId+start_date+end_date);
                                    return map;
                                }
                            };
                            // 设置请求的tag标签，便于在请求队列中寻找该请求
                            request.setTag("post");
                            // 添加到全局的请求队列
                            AppController.getHttpQueues().add(request);

                            longtitude = fieldInfo.getLongitude();
                            latitude = fieldInfo.getLatitude();




                            Log.i("jjjjjjjjjjj",String.valueOf(longtitude)+"ggg"+String.valueOf(latitude));
                        }
                    });
                }else {
                    Log.e(TAG, "未知错误1：未获取到农田信息，请返回上一个界面重新选择！");
                }
                return convertView;
            }

            Double longtitude;
            Double latitude;
            private void showDialog(String weather){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(getContext());
                //normalDialog.setIcon(R.drawable.);
                normalDialog.setTitle("天气状况提醒：");
                normalDialog.setMessage(weather);
                normalDialog.setPositiveButton("继续",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //导航
                                GohereListener gohere =  new GohereListener(longtitude,latitude);
                                gohere.routeplanToNavi();
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                            }
                        });
                // 显示
                normalDialog.show();
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


        dateView = mainMenu.getLayoutInflater().inflate(R.layout.datepicker, null);
        dateInit();//日期选择初始化
        initDatePicker();//日历选择器监听
        dateView.findViewById(R.id.getBack).setOnClickListener(this);
        dateView.findViewById(R.id.getHelp).setOnClickListener(this);
        hintButton=(com.beardedhen.androidbootstrap.BootstrapButton)dateView.findViewById(R.id.hintButton);
        calendarPickerView.setOnDateSelectedListener(new clDateSelectedListener());//选一个范围的日期
        calendarPickerView.setCellClickInterceptor(new clCellClick());//选一天的日期

        hintView = mainMenu.getLayoutInflater().inflate(R.layout.intelligent_pop, null);
        initHintPopup();
        hintView.findViewById(R.id.get_start).setOnClickListener(this);
        hintPopup.setOnDismissListener(new hintPopDisListener());
        //////////////////////////地图代码////////////////////////////
        //获取农机并获取农机经纬度
        try {
            machine_id = sessionManager.getUserId();
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
        mBaiduMap.setOnMapStatusChangeListener(new centreMap());
        centreBitmap= BitmapDescriptorFactory.fromResource(R.drawable.ic_set_location);//构建中心覆盖物Marker图标

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

        //在地图上添加农田数据
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
        // 初始化搜索模块，注册搜索事件监听
        this.autoQuery= (AutoCompleteTextView) view.findViewById(R.id.query_query_autoCompleteTextView_2);
        this.autoQuery.setText(centerName);
        this.autoQuery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GPS_longitude=String.valueOf(allSuggestions.get(position).pt.longitude);//GPS经度
                GPS_latitude=String.valueOf(allSuggestions.get(position).pt.latitude);

            }
        });

        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener(){
            @Override
            public void onGetSuggestionResult(SuggestionResult res) {

                if (res == null || res.getAllSuggestions() == null) {
                    return;
                }
                sugAdapter.clear();
                allSuggestions=res.getAllSuggestions();
                for (SuggestionResult.SuggestionInfo info : allSuggestions) {
                    if (info.key != null) {
                        sugAdapter.add(info.key);

                    }

                }
                sugAdapter.notifyDataSetChanged();
            }
        });
        sugAdapter = new ArrayAdapter<String>(mainMenu,android.R.layout.simple_dropdown_item_1line);
        autoQuery.setAdapter(sugAdapter);
        autoQuery.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    return;
                }
                String city = "保定";
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(city));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //下拉菜单选择项初始化
        //范围
        areaAdapter= new SpinnerAdapter_up_white_1(mainMenu,getResources().getStringArray(R.array.area));
        sp_area.setAdapter(areaAdapter);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sl_area = sp_area.getSelectedItem().toString();
                Log.e(TAG, "选择的距离：" + sl_area);
                switch (sl_area)
                {
                    case "50公里":
                        sl_area="50";
                        //查找，刷新
                        initFieldInfo();
                        break;
                    case "80公里":
                        sl_area="80";
                        //查找，刷新
                        initFieldInfo();
                        break;
                    case "100公里":
                        sl_area="100";
                        //查找，刷新
                        initFieldInfo();
                        break;
                    case "全部":
                        sl_area="1000";
                        //查找，刷新
                        initFieldInfo();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    //起始日期，终止日期初始化
    private void dateInit()
    {

        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        Date defaultStartDate=calendar.getTime();
        calendar.add(calendar.DATE, 1);
        Date defaultEndDate=calendar.getTime();
        t_startDate.setText(getResources().getString(R.string.jobStart) + format.format(defaultStartDate));
        t_endDate.setText(getResources().getString(R.string.jobEnd) + format.format(defaultEndDate));//显示默认的起止年月日
        startTime=format2.format(defaultStartDate);
        endTime=format2.format(defaultEndDate);
        /*
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat format2= new SimpleDateFormat("yyyy-MM-dd");
        try {
            t_startDate.setText(getResources().getString(R.string.jobStart) + format.format(format2.parse(startTime)));
            t_endDate.setText(getResources().getString(R.string.jobEnd) + format.format(format2.parse(endTime)));//显示默认的起止年月日
        }
        catch(Exception e)
        {}
        */
    }

    //初始化日期选择器popupWindow
    private void initDatePicker() {
        Calendar maxDate=Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 2);
        calendarPickerView=(CalendarPickerView)dateView.findViewById(R.id.calendarDatePicker);
        calendarPickerView.init(new Date(), maxDate.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        datePickerPop = new PopupWindow(dateView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        datePickerPop.setAnimationStyle(R.style.slideAnimation_bottom);
        datePickerPop.setOutsideTouchable(true);
        datePickerPop.setOnDismissListener(new datePickerDismiss());

    }
    //popupWindow销毁时监听
    private class datePickerDismiss implements PopupWindow.OnDismissListener
    {
        @Override
        public void onDismiss() {
            initDatePicker();
        }
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
               // mainMenu.getSupportFragmentManager().popBackStack();
                mainMenu.finish();
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
                pop_countInfo.setText("共找到" + String.valueOf(mainMenu.selectedFieldInfo.size()) + "块农田，点击关闭");

                break;
            case R.id.pop_infos:
                btn_pop_flag=false;
                btn_popup.dismiss();
                break;
            case R.id.jobDate:
                popup_flag=true;
                datePickerPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_search:
                //查找，刷新
                initFieldInfo();
                LatLng llS = new LatLng(Double.parseDouble(GPS_latitude),
                        Double.parseDouble(GPS_longitude));
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llS);
                mBaiduMap.animateMapStatus(u);
                break;
            case R.id.arrange_button:
                LatLng ll = new LatLng(Double.parseDouble(com_latitude),
                        Double.parseDouble(com_longitude));
                MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u1);
                break;
        }
    }
    private void initFieldInfo()
    {
        String tag_string_req = "req_init";
        mainMenu.pDialog.setMessage("正在查询 ...");
        mainMenu.showDialog();

        if (!netUtil.checkNet(mainMenu)) {
            commonUtil.error_hint_short("网络连接错误");
            mainMenu.hideDialog();
        } else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new initSuccessListener(), mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("Search_range",sl_area);//需要按照实际范围变动
                    params.put("crops_kind", "F");//作业状态
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
                    commonUtil.error_hint_short("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                }else if(status==0){
                    mainMenu.clearFieldData();//清空缓存的农田数据
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
//                    try {
//                        mainMenu.fieldInfoPosts.clear();
//                    }catch (Exception e){}
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
                        commonUtil.error_hint_short("未查询到符合要求的农田信息，请重新查询！");
                        countInfo.setText("共找到" + 0 + "块农田");
                        //清楚覆盖物Marker,重新加载
                        mBaiduMap.clear();
                    }else{
                        //mainMenu.addBackFragment(new item_query_requirement_1());
                        markRepairStation(mainMenu.selectedFieldInfo);
                        countInfo.setText("共找到" + String.valueOf(mainMenu.selectedFieldInfo.size()) + "块农田，点击查看");
                    }
//                    else
//                    {
//                        //存储到本地库
//                        saveFieldInfo(fieldInfoDao,fieldInfos);
//                        //按距离的排序好的农田
//                        arrangeField();
//                        mainMenu.addBackFragment(new item_query_requirement_1());//跳转执行查找
//                        commonUtil.error_hint_short("数据加载完成");
//                    }
                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("服务器数据错误1：response错误:" + errorMsg);
                }
            } catch (JSONException e) {
                // JSON error
                Log.e(TAG, "2 服务器数据错误：response错误:" + e.getMessage());
                commonUtil.error_hint_short("服务器数据错误2：response错误:" + e.getMessage());
            }
        }
    }
    ////////////////////////////////////获取天气数据////////////////////////////////////////////////
    private class initWeatherSuccessListener implements Response.Listener<String>//获取天气信息数据响应服务器成功
    {
        @Override
        public void onResponse(String response) {
            Log.e(TAG, "WeatherData Response: " + response);
            mainMenu.hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");

                if (status==1)
                {
                    String errorMsg = jObj.getString("message");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                }else if(status==0){


                    weatherData="";
                    JSONArray s_info=jObj.getJSONArray("message");
//                    Log.e(TAG, String.valueOf(s_post.length()));
                    for(int i=0;i<s_info.length();i++)
                    {
                        JSONObject obj = s_info.getJSONObject(i);
                        if(obj.getString("type").contains("雨") || obj.getString("type").contains("风") )
                        {
                            weatherData = weatherData+obj.getString("date").substring(5)+" "+obj.getString("type")+" 风力"+obj.getString("windrage")+"级\n";

                        }
                    }
                    if(weatherData.length()>4)
                        weatherData=weatherData.substring(0,weatherData.length()-1);
                    Log.e(TAG, String.valueOf(s_info.length()));
                    final FieldInfo fieldInfo = (FieldInfo) pointmarker.getExtraInfo().get("fieldInfo");
                    InfoWindow infoWindow;

                    //构造弹出layout
                    LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
                    View markerpopwindow = inflater.inflate(R.layout.markerpopwindow, null);

                    TextView tv = (TextView) markerpopwindow.findViewById(R.id.markinfo);
                    String markinfo = "位置:"+fieldInfo.getVillage()+"\n"+
                            "类型:"+commonUtil.transferCropKind(fieldInfo.getCrops_kind())+"\n"+
                            "电话:" + fieldInfo.getUser_name() + "\n" +
                            "面积:" + fieldInfo.getArea_num() + "\n" +
                            "单价:"+fieldInfo.getUnit_price()+"\n"+
                            "开始时间:" + fieldInfo.getStart_time()+"\n"+
                            "结束时间:"+fieldInfo.getEnd_time();
                    if(weatherData.equals("")==false)
                        markinfo=markinfo+"\n\n"+weatherData;
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

                    LatLng ll = pointmarker.getPosition();
                    //将marker所在的经纬度的信息转化成屏幕上的坐标
                    Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                    p.y -= -10;
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
                    MapStatusUpdate mapstatus = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.setMapStatus(mapstatus);

                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("服务器数据错误1：response错误:" + errorMsg);
                }
            } catch (JSONException e) {
                // JSON error
                Log.e(TAG, "2 服务器数据错误：response错误:" + e.getMessage());
                commonUtil.error_hint_short("服务器数据错误2：response错误:" + e.getMessage());
            }
        }
    }


    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////
    public void gps_MachineLocation(final String machine_id) {
        String tag_string_req = "req_GPS";
        //服务器请求
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_findFlyComByUser, new locationSuccessListener(), new locationErrorListener()) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("fm_id", machine_id);
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
                    com_longitude=GPS_longitude;//无人机飞机公司地址
                    com_latitude=GPS_latitude;
                    autoQuery.setText(location.getJSONObject(0).getString("com_name"));

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
    private class weatherErrorListener implements  Response.ErrorListener//定位服务器响应失败
    {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "ConnectService Error: " + error.getMessage());
            netUtil.testVolley(error);

        }
    }

    //定位成功或者失败后的响应
    private void isGetLocation() {
        if (!text_gps_flag) {
            Log.e(TAG, "GPS自动定位成功");
            LatLng ll = new LatLng(Double.valueOf(GPS_latitude),Double.valueOf(GPS_longitude));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
            commonUtil.error_hint_short("自动定位成功");
            //查找，刷新
            initFieldInfo();
        } else{
            Log.e(TAG, "GPS自动定位失败,开启百度定位！");
            try {
                GPS_latitude = String.valueOf(curlocation.getLatitude());
                GPS_longitude = String.valueOf(curlocation.getLongitude());
                LatLng ll = new LatLng(curlocation.getLatitude(),
                        curlocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                commonUtil.error_hint_short("自动定位成功");
                //查找，刷新
                initFieldInfo();
            }catch (Exception e)
            {
                commonUtil.error_hint_short("自动定位失败，请重试！");
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
            if("设为中心点".equals(marker.getTitle()))
            {
                GPS_longitude=String.valueOf(marker.getPosition().longitude);//GPS经度
                GPS_latitude=String.valueOf(marker.getPosition().latitude);

                //查找，刷新
                initFieldInfo();
                return false;
            }
            pointmarker=marker;
            //从服务器请求天气数据
            String tag_string_req = "req_weather_data";
            final FieldInfo fieldInfo = (FieldInfo) marker.getExtraInfo().get("fieldInfo");
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_WeatherData, new initWeatherSuccessListener(), new weatherErrorListener()) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("farmID", fieldInfo.getFarm_id());

                    if(startTime.compareToIgnoreCase(fieldInfo.getStart_time().replace('-','/'))<0)
                        params.put("timeStart", fieldInfo.getStart_time());
                    else
                        params.put("timeStart", startTime);

                    if(endTime.compareToIgnoreCase(fieldInfo.getEnd_time().replace('-','/'))>0)
                        params.put("timeEnd", fieldInfo.getEnd_time());
                    else
                        params.put("timeEnd", endTime);
                    //params.put("token", token);

                    return netUtil.checkParams(params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
    public class GohereListener implements View.OnClickListener {
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
    /**
     * 内部TTS播报状态回传handler
     */


    ////////////////////////////地图代码结束//////////////////////////
    //////////////////////////////////////////////////////////////
    //监听地图的改变，改变中心的坐标
    private class centreMap implements BaiduMap.OnMapStatusChangeListener{
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
//            updateMapState(mapStatus);
        }
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

        }
        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
//            updateMapState(mapStatus);
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            updateMapState(mapStatus);
        }
    }

    //获取MapStatus的经纬度
    private void updateMapState(MapStatus status) {
        LatLng mCenterLatLng = status.target;
        /**获取经纬度*/
        centre_latitude = mCenterLatLng.latitude;
        centre_longitude = mCenterLatLng.longitude;
        try{
            //
           // around_strReq.cancel();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }

      //  this.GPS_longitude=String.valueOf(centre_longitude);//GPS经度
        //this.GPS_latitude=String.valueOf(centre_latitude);//GPS纬度
        //查找，刷新
        //initFieldInfo();
        if(centreMarker!=null) {
            centreMarker.remove();
        }
        showMaker(centre_latitude, centre_longitude, centreBitmap);
    }

    //在（latitude,longitude）坐标处显示id为id_maker_icon的覆盖物
    private void showMaker(double latitude,double longitude,BitmapDescriptor bitmap)
    {
        centreMarker=null;
        LatLng point = new LatLng(latitude, longitude);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        centreMarker = (Marker) mBaiduMap.addOverlay(option);
        centreMarker.setTitle("设为中心点");
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
                Log.e(TAG, "first_date清空了");
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


    //发布按钮弹出时监听dismiss后背景变回原样
    private class hintPopDisListener implements PopupWindow.OnDismissListener
    {
        @Override
        public void onDismiss() {
            mainMenu.hintPopup_flag=false;
            lp.alpha = 1f;
            mainMenu.getWindow().setAttributes(lp);
        }
    }

    private void initHintPopup()//初始化提示信息弹窗
    {
        hintPopup = new PopupWindow(hintView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        hintPopup.setAnimationStyle(R.style.popWindow_fade);
        hintPopup.setOutsideTouchable(false);
        hintPopup.setBackgroundDrawable(new ColorDrawable(0x55000000));
    }





}
