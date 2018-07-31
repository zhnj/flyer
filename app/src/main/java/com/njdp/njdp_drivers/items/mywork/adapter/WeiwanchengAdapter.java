package com.njdp.njdp_drivers.items.mywork.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.items.BNDGuideActivity;
import com.njdp.njdp_drivers.items.item_query_requirement_1;
import com.njdp.njdp_drivers.items.jikai.bean.WeatherBean;
import com.njdp.njdp_drivers.items.mywork.bean.WorkBean;

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

public class WeiwanchengAdapter extends RecyclerView.Adapter<WeiwanchengAdapter.ViewHolder> {
    private List<WorkBean.ResultBean> list;
    private Context context;
    ViewGroup parent;
    Double longitude;
    Double latitude;
    public static List<Activity> activityList = new LinkedList<Activity>();
    SessionManager sessionManager=new SessionManager();
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    public WeiwanchengAdapter(Context context, List<WorkBean.ResultBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        //给Adapter添加布局，bq把这个view传递给HoldView，让HoldView找到空间
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_weiwancheng_work, null);
        final ViewHolder holder = new ViewHolder(view);
        gps_MachineLocation();
        initNavi();
        return holder;
    }
    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////
    public void gps_MachineLocation() {
        String tag_string_req = "req_GPS";
        //服务器请求
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_findFlyComByUser, new WeiwanchengAdapter.locationSuccessListener(), new WeiwanchengAdapter.locationErrorListener()) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("fm_id", sessionManager.getUserId());
                //params.put("token", token);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private class locationSuccessListener implements Response.Listener<String>//获取农机位置响应服务器成功
    {
        @Override
        public void onResponse(String response) {
            Log.d("导航", "AreaInit Response: " + response);
            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");

                if (status == 1) {
                    String errorMsg = jObj.getString("result");
                    Log.e("导航", "Json error：response错误:" + errorMsg);

                } else if (status == 0) {

                    ///////////////////////////获取服务器农机，经纬度/////////////////////
                    JSONArray location = jObj.getJSONArray("result");
                    GPS_longitude = location.getJSONObject(0).getString("com_longitude");
                    GPS_latitude = location.getJSONObject(0).getString("com_latitude");

                    ///////////////////////////获取服务器无人机公司，经纬度/////////////////////


                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e("导航", "GPSLocation Error:" + errorMsg);

                }
            } catch (JSONException e) {
                // JSON error
                Log.e("导航", "GPSLocation Error,Json error：response错误:" + e.getMessage());

            }
        }
    }

    private class locationErrorListener implements  Response.ErrorListener//定位服务器响应失败
    {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("导航", "ConnectService Error: " + error.getMessage());


        }
    }
    String phone;
    String address;
    String area;
    String price;
    String date;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //position为Adapter的位置，数据从list里面可以拿出来。
        try {
            address = "地址：" + list.get(position).getFarmlandsInfo().getFarmlandsProvince() + list.get(position).getFarmlandsInfo().getFarmlandsCity() + list.get(position).getFarmlandsInfo().getFarmlandsCounty() + list.get(position).getFarmlandsInfo().getFarmlandsVillage();
        } catch (Exception e) {
            address = "地址：[null]";
        }
        try {
            area = "\n面积：" + list.get(position).getFarmlandsInfo().getFarmlandsArea();
        } catch (Exception e) {
            area = "\n面积：[null]";
        }
        try {
            price = "\n单价：" + list.get(position).getFarmlandsInfo().getFarmlandsUnitPrice();
        } catch (Exception e) {
            price = "地址：[null]";
        }
        try {
            phone = "\n电话：" + list.get(position).getFarmlandsInfo().getPersonInfoFarmerMachine().getPersonPhone();
        } catch (Exception e) {
            phone = "\n电话：[null]";
        }
        try {
            date = "\n日期：" + stampToDate(list.get(position).getFarmlandsInfo().getFarmlandsStartTime()) + "  至  " + stampToDate(list.get(position).getFarmlandsInfo().getFarmlandsEndTime());
        } catch (Exception e) {
            date = "\n日期：[null]";
        }

        String str = address + area + price + phone + date;
        holder.textView.setText(str);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "当前点击的条目的地址为：" + list.get(position).getFarmlandsInfo().getFarmlandsVillage(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(parent.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                parent.getContext().startActivity(intent);
            }
        });
        holder.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //POST网络请求
                String url=AppConfig.URL_Weather;
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        String weather = new String();
                        //Toast.makeText(parent.getContext(), response,Toast.LENGTH_LONG).show();
                        //弹出天气提醒,解析天气接口的数据，并调用showWeather方法
                        WeatherBean weatherBean = new Gson().fromJson(response, WeatherBean.class);

                        for( int i = 0 ; i < weatherBean.getMessage().size() ; i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
                            String s = weatherBean.getMessage().get(i).getDate() + "的天气情况：\n       【" + weatherBean.getMessage().get(i).getType()+"】  风力强度："+weatherBean.getMessage().get(i).getWindrage()+"级";
                            weather += s+"\n";
                        }
                        longitude = Double.valueOf(list.get(position).getFarmlandsInfo().getFarmlandsLongitude());
                        latitude = Double.valueOf(list.get(position).getFarmlandsInfo().getFarmlandsLatitude());

                        showDialog(weather);
                        Log.i("weather",weather);

                    }


                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(parent.getContext(), "获取天气失败",Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        long startTime = list.get(position).getFarmlandsInfo().getFarmlandsStartTime();
                        long endTime = list.get(position).getFarmlandsInfo().getFarmlandsEndTime();
                        String farmId = String.valueOf(list.get(position).getFarmlandsInfo().getId());
                        String startT = stampToDate(startTime);
                        String endT = addDay(startT);

                        Map<String,String> map = new HashMap<String,String>();
                        map.put("farmID",farmId);
                        map.put("timeStart", startT);
                        map.put("timeEnd", endT);
                        Log.e("farmId",startT+":"+endT);
                        return map;
                    }
                };
                // 设置请求的tag标签，便于在请求队列中寻找该请求
                request.setTag("post");
                // 添加到全局的请求队列
                AppController.getHttpQueues().add(request);





            }
        });
        holder.bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                int flyer_id = list.get(position).getFlyerId();
                int farmland_id = list.get(position).getFarmlandsInfo().getId();
                String opration = "完成";
                url = AppConfig.URL_MYJOB_PART_CHECK;
                url = url + "?flyer_id=" + flyer_id + "&farmland_id=" + farmland_id + "&opration=" + opration;
                Log.i("delurl", url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "确认完成", Toast.LENGTH_SHORT).show();
                        delete(position);
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "连接失败,检查网络", Toast.LENGTH_LONG).show();
                    }

                });
                // 设置请求的tag标签，便于在请求队列中寻找该请求
                request.setTag("get");
                // 添加到全局的请求队列
                AppController.getHttpQueues().add(request);
            }
        });
        holder.bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                int flyer_id = list.get(position).getFlyerId();
                int farmland_id = list.get(position).getFarmlandsInfo().getId();
                url = AppConfig.URL_MYJOB_PART_DELETE;
                url = url + "?flyer_id=" + flyer_id + "&farmland_id=" + farmland_id;
                Log.i("delurl", url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        delete(position);
                        Toast.makeText(context, "删除完成", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "连接失败,检查网络", Toast.LENGTH_LONG).show();
                        //delete(position);
                    }

                });
                // 设置请求的tag标签，便于在请求队列中寻找该请求
                request.setTag("get");
                // 添加到全局的请求队列
                AppController.getHttpQueues().add(request);
            }
        });
    }
    private void showDialog(String weather){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(parent.getContext());
        //normalDialog.setIcon(R.drawable.);
        normalDialog.setTitle("天气状况提醒：");
        normalDialog.setMessage(weather);
        normalDialog.setPositiveButton("继续",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //导航
                        GohereListener gohere =  new GohereListener(longitude,latitude);
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
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button bt1;
        Button bt2;
        Button bt3;
        Button bt4;

        public ViewHolder(View itemView) {
            super(itemView);
            //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
            textView = (TextView) itemView.findViewById(R.id.tv_adapter);
            bt1 = (Button) itemView.findViewById(R.id.fw_bt1);
            bt2 = (Button) itemView.findViewById(R.id.fw_bt2);
            bt3 = (Button) itemView.findViewById(R.id.fw_bt3);
            bt4 = (Button) itemView.findViewById(R.id.fw_bt4);
        }
    }


    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<WorkBean.ResultBean> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<WorkBean.ResultBean> newList) {
        //刷新数据
        list.clear();
        //list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    // 删除数据
    public void delete(int index) {
        Log.i("size", "即将删除的条目：" + String.valueOf(index));
        list.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(0, list.size());
    }

    public static String stampToDate(long lt){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
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
                BaiduNaviManager.getInstance().launchNavigator((Activity) parent.getContext(), list, 2, true, new WeiwanchengAdapter.GohereRoutePlanListener(sNode));

            }
        }
    }
    private static final String APP_FOLDER_NAME = "BNSDK";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private String mSDCardPath = null;
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

            Intent intent = new Intent(parent.getContext(), BNDGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            parent.getContext().startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(parent.getContext(), "算路失败", Toast.LENGTH_SHORT).show();
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
        BaiduNaviManager.getInstance().init((Activity)parent.getContext(), mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int i, String s) {
                if (0 == i) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + s;
                }

            }

            @Override
            public void initStart() {
                //Toast.makeText(, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void initSuccess() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();

            }

            @Override
            public void initFailed() {
                //Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();

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

                    //showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
//                    Toast.makeText(getActivity(), "Handler : TTS play end", Toast.LENGTH_SHORT).show();

                    //showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };
    //监听地图的改变，改变中心的坐标
    private class centreMap implements BaiduMap.OnMapStatusChangeListener{
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
//            updateMapState(mapStatus);
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
    private double centre_longitude;//地图中间经度
    private double centre_latitude;//地图中间维度
    private BitmapDescriptor centreBitmap;//中心覆盖物的图标
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
    private Marker centreMarker;
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
    private BaiduMap mBaiduMap = null;

    //实现日期加3天的方法
    public static String addDay(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, 2);//增加一天
            //cd.add(Calendar.MONTH, n);//增加一个月
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

}




