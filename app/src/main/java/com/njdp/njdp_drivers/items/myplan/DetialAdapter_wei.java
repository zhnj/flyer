package com.njdp.njdp_drivers.items.myplan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.items.BNDGuideActivity;
import com.njdp.njdp_drivers.util.NormalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.njdp.njdp_drivers.items.item_query_requirement_1.activityList;

/**
 * Created by Rock on 2018/7/28.
 */

public class DetialAdapter_wei extends RecyclerView.Adapter<DetialAdapter_wei.ViewHolder> {
    private List<PlanDetailBean.ResultBean> list;
    ViewGroup parent;
    String str;
    SessionManager sessionManager=new SessionManager();
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int authBaseRequestCode = 1;
    public DetialAdapter_wei(List<PlanDetailBean.ResultBean> list) {
        this.list = list;
    }

    @Override
    public DetialAdapter_wei.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_detail, null);
        ViewHolder holder = new ViewHolder(view);
        gps_MachineLocation();
        //initNavi();

        return holder;
    }
    ////////////////////////////////////从服务器获取农机经纬度///////////////////////////////////////
    public void gps_MachineLocation() {
        String tag_string_req = "req_GPS";
        //服务器请求
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_findFlyComByUser, new locationSuccessListener(), new locationErrorListener()) {

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
    @Override
    public void onBindViewHolder(DetialAdapter_wei.ViewHolder holder, final int position) {
        String id = "作业地块"+(position+1);
        String address = "\n地址："+list.get(position).getFarm_province()
                +list.get(position).getFarm_city()
                +list.get(position).getFarm_county()
                +list.get(position).getFarm_town()
                +list.get(position).getFarm_village();
        String income = "\n收入："+list.get(position).getIncome()+"元";
        String area = "\n面积："+list.get(position).getArea_num()+"亩";
        final String phone = "\n电话："+list.get(position).getFarmer_phone();
        String result = "\n住宿建议：";//+list.get(position).getReturnStatus());
        switch (list.get(position).getReturnStatus()){
            case "1":
                result+="当地住宿";
                break;
            case "2":
                result+="返程";
                break;
            case "3":
                result+="当地住宿";
                break;
            case "4":
                result+="返程";
                break;
            default:
                result="";
                break;
        }
        String date = "\n日期："+list.get(position).getBeginTime()+" 至 "+list.get(position).getEndTime();
        if (list.get(position).getModifyStatus().equals("1")){
            date+="(已为您避开有雨天气)";
        }
        //str = id+address+income+area+phone+result+date;
        str = id+address+income+area+result+date;
        Log.i("info","str:"+str);
        holder.tv.setText(str);

        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        //导航
        holder.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double longtitude = Double.valueOf(list.get(position).getLongitude());
                Double latitude = Double.valueOf(list.get(position).getLatitude());
                //添加导航代码
                GohereListener gohere =  new GohereListener(longtitude,latitude);
                gohere.routeplanToNavi();
            }
        });
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
                BaiduNaviManager.getInstance().launchNavigator((Activity) parent.getContext(), list, 2, true, new GohereRoutePlanListener(sNode));

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

    private boolean hasBasePhoneAuth() {
        PackageManager pm = parent.getContext().getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, parent.getContext().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        Activity activity= (Activity) parent.getContext();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                activity.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(activity,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(parent.getContext(), result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(parent.getContext(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(parent.getContext(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();

                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                        Toast.makeText(parent.getContext(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void initTTS()
    {
        BaiduNaviManagerFactory.getTTSManager().initTTS(parent.getContext(),
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
    @Override
    public int getItemCount() {
        return list!= null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        Button bt;
        Button bt2;

        public ViewHolder(View itemView) {
            super(itemView);
            //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
            tv = (TextView) itemView.findViewById(R.id.detail_tv);
            bt = (Button) itemView.findViewById(R.id.detail_bt);
            bt2 = (Button) itemView.findViewById(R.id.detail_bt2);
        }
    }
}