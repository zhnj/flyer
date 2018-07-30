package com.njdp.njdp_drivers.items.myplan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.items.BNDGuideActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.njdp.njdp_drivers.items.item_query_requirement_1.activityList;

/**
 * Created by Rock on 2018/7/28.
 */

public class DetialAdapter extends RecyclerView.Adapter<DetialAdapter.ViewHolder> {
    private List<PlanDetailBean.ResultBean> list;
    ViewGroup parent;
    String str;
    private String GPS_longitude="1.1";//GPS经度
    private String GPS_latitude="1.1";//GPS纬度
    public DetialAdapter(List<PlanDetailBean.ResultBean> list) {
        this.list = list;
    }

    @Override
    public DetialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_detail, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DetialAdapter.ViewHolder holder, final int position) {
        String id = "作业地块"+(position+1);
        String address = "\n地址："+list.get(position).getFarm_province()
                +list.get(position).getFarm_city()
                +list.get(position).getFarm_county()
                +list.get(position).getFarm_town()
                +list.get(position).getFarm_village();
        String income = "\n收入："+list.get(position).getIncome();
        String area = "\n面积："+list.get(position).getArea_num()+"亩";
        final String phone = "\n电话："+list.get(position).getFarmer_phone();
        String result = "\n是否住宿：";//+list.get(position).getReturnStatus());
        switch (list.get(position).getReturnStatus()){
            case "1":
                result+="是";
                break;
            case "2":
                result+="否";
                break;
            case "3":
                result+="是";
                break;
            case "4":
                result+="否";
                break;
            default:
                result="";
                break;
        }
        String date = "\n日期："+list.get(position).getBeginTime()+" 至 "+list.get(position).getEndTime();


        str = id+address+income+area+phone+result+date;
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
//                Toast.makeText(getActivity(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void initSuccess() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();

            }

            @Override
            public void initFailed() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();

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
