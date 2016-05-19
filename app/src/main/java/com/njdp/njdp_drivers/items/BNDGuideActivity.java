package com.njdp.njdp_drivers.items;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;
import com.baidu.navisdk.hudsdk.BNRemoteConstants;
import com.baidu.navisdk.hudsdk.BNRemoteMessage;
import com.baidu.navisdk.hudsdk.client.BNRemoteVistor;
import com.baidu.navisdk.hudsdk.client.HUDConstants;
import com.baidu.navisdk.hudsdk.client.HUDSDkEventCallback;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.util.StringUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class BNDGuideActivity extends Activity {

    private final String TAG = BNDGuideActivity.class.getName();
    private BNRoutePlanNode mBNRoutePlanNode = null;
    private BaiduNaviCommonModule mBaiduNaviCommonModule = null;

    /*
     * 对于导航模块有两种方式来实现发起导航。 1：使用通用接口来实现 2：使用传统接口来实现
     */
    // 是否使用通用接口
    private boolean useCommonInterface = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        item_intelligent_resolution_3.activityList.add(this);
        createHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        }
        View view = null;
        if (useCommonInterface) {
            //使用通用接口
            mBaiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(
                    NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE,this,
                    BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE,mOnNavigationListener);
            if(mBaiduNaviCommonModule!=null){
                mBaiduNaviCommonModule.onCreate();
                view = mBaiduNaviCommonModule.getView();
            }
        }else{
            //使用传统接口
            view = BNRouteGuideManager.getInstance().onCreate(this,mOnNavigationListener);
        }

        if(view!=null){
            setContentView(view);
        }

        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
                mBNRoutePlanNode = (BNRoutePlanNode)bundle.getSerializable(item_intelligent_resolution_3.ROUTE_PLAN_NODE);
            }
        }

        //开始导航后调用导航HUD
        /*******************************************导航 hud *****************/
        initViews();
        //Log.e(BNRemoteConstants.MODULE_TAG, "onCreate..................");
        mMainHandler = new Handler(getMainLooper());
        BNRemoteVistor.getInstance().init(getApplicationContext(), getPackageName(),
                getAppVersionName(BNDGuideActivity.this, getPackageName()), mRGEventCallback, mConnectCallback);
        BNRemoteVistor.getInstance().setShowLog(true);
        BNRemoteVistor.getInstance().open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(useCommonInterface){
            if(mBaiduNaviCommonModule!=null){
                mBaiduNaviCommonModule.onResume();
            }
        }else{
            BNRouteGuideManager.getInstance().onResume();
        }

        if(hd!=null){
            hd.sendEmptyMessageAtTime(MSG_SHOW, 2000);
        }
    }

    protected void onPause() {
        super.onPause();

        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onPause();
            }
        } else {
            BNRouteGuideManager.getInstance().onPause();
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onDestroy();
            }
        } else {
            BNRouteGuideManager.getInstance().onDestroy();
        }
        item_intelligent_resolution_3.activityList.remove(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onStop();
            }
        } else {
            BNRouteGuideManager.getInstance().onStop();
        }

    }

    @Override
    public void onBackPressed() {
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onBackPressed(false);
            }
        } else {
            BNRouteGuideManager.getInstance().onBackPressed(false);
        }
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onConfigurationChanged(newConfig);
            }
        } else {
            BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
        }

    };

    private void addCustomizedLayerItems(){
        List<BNRouteGuideManager.CustomizedLayerItem> items = new ArrayList<BNRouteGuideManager.CustomizedLayerItem>();
        BNRouteGuideManager.CustomizedLayerItem item1 = null;
        if(mBNRoutePlanNode!=null){
            item1 = new BNRouteGuideManager.CustomizedLayerItem(mBNRoutePlanNode.getLongitude(),mBNRoutePlanNode.getLatitude(),
                    mBNRoutePlanNode.getCoordinateType(),getResources().getDrawable(R.drawable.ic_launcher),
                    BNRouteGuideManager.CustomizedLayerItem.ALIGN_CENTER);
            items.add(item1);
            BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
        }
        BNRouteGuideManager.getInstance().showCustomizedLayer(true);

    }

    private void createHandler() {
        if (hd == null) {
            hd = new Handler(getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==MSG_SHOW){
                        addCustomizedLayerItems();
                    }else if (msg.what == MSG_HIDE) {
                        BNRouteGuideManager.getInstance().showCustomizedLayer(false);
                    } else if (msg.what == MSG_RESET_NODE) {
                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(
                                mBNRoutePlanNode);
                    }
                }
            };
        }
    }


    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;
    private Handler hd = null;

    private BNRouteGuideManager.OnNavigationListener mOnNavigationListener = new BNRouteGuideManager.OnNavigationListener() {

        @Override
        public void onNaviGuideEnd() {
            finish();
        }

        @Override
        public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {

            if (actionType == 0) {
                Log.i(TAG, "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
            }

            Log.i(TAG, "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
        }

    };


    /*******************************
     * baidu daohang hud
     *********************/
    private final static String MODULE_TAG = "BNEVENT";

    private View mSimpleRGLayout;
    private View mNaviAfterView = null;
    private ImageView mTurnIcon = null;
    private TextView mAfterMetersInfoTV = null;
    private TextView mAfterLabelInfoTV = null;
    private TextView mGoLabelTV = null;
    private TextView mGoWhereInfoTV = null;

    private View mAlongRoadView = null;
    private TextView mCurRoadNameTV = null;
    private TextView mCurRoadRemainDistTV = null;

    private TextView mNewCurRoadTv = null;

    private View mRemainInfoLayout;
    private TextView mTotalDistTV = null;
    private TextView mArriveTimeTV = null;

    private TextView mNavilogTv = null;
    private EditText mServerIPTv = null;
    //private Button mConnectBtn = null;
    //private Button mCloseBtn = null;

    private Handler mMainHandler = null;
    private ProgressDialog mProgressDialog;

    private View mEnlargeRoadMapView = null;
    private ImageView mEnlargeImageView = null;
    private TextView mRemainDistTV = null;
    private TextView mNextRoadTV = null;
    private ProgressBar mProgressBar = null;
    private View mCarPosLayout;
    private View bnav_rg_enlarge_image_mask;
    private ImageView mCarPosImgView;

    private int mEnlargeType;
    private String mRoadName;
    private int mTotalDist;
    private int mRemDist;
    private int mProgress;
    private boolean mbUpdateRasterInfo;

    private int mCarPosX = 0;
    private int mCarPosY = 0;

    private Matrix mRotateMatrix;
    private int mCarRotate;

    private boolean mForceRefreshImage = false;
    private HUDSDkEventCallback.OnConnectCallback mConnectCallback = new HUDSDkEventCallback.OnConnectCallback() {

        @Override
        public void onReConnected() {
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mSimpleRGLayout.setVisibility(View.VISIBLE);
                    mRemainInfoLayout.setVisibility(View.VISIBLE);
                    mNavilogTv.setText("重新连接到百度导航");
                }
            });

        }

        @Override
        public void onConnected() {
            //Log.e(BNRemoteConstants.TAG, "connect to server success");
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    //mProgressDialog.dismiss();
                    mSimpleRGLayout.setVisibility(View.VISIBLE);
                    mRemainInfoLayout.setVisibility(View.VISIBLE);
                    mNavilogTv.setText("成功连接到百度导航");
                }
            });
        }

        @Override
        public void onClose(int arg0, String arg1) {
            //Log.e(BNRemoteConstants.MODULE_TAG, "MainActivity.................onClose()  disconnect, reason = " + arg1);
            final String reason = arg1;
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    //mProgressDialog.dismiss();
                    //mSimpleRGLayout.setVisibility(View.GONE);
                    //mRemainInfoLayout.setVisibility(View.GONE);
                    //mNavilogTv.setText("连接断开, " + reason);
                }
            });
        }

        @Override
        public void onAuth(BNRemoteMessage.BNRGAuthSuccess arg0) {
            if (arg0 != null) {
                Log.d(BNRemoteConstants.MODULE_TAG, "auth success, serverVer = " + arg0.getServerVersion());
                final String serverVer = arg0.getServerVersion();
                mMainHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mNavilogTv.setText("认证成功， 服务器版本: " + serverVer);
                    }
                });
            }
        }

        @Override
        public void onStartLBSAuth() {

        }

        @Override
        public void onEndLBSAuth(int result, String reason) {
            // TODO Auto-generated method stub
        }
    };
    private HUDSDkEventCallback.OnRGInfoEventCallback mRGEventCallback = new HUDSDkEventCallback.OnRGInfoEventCallback() {

        @Override
        public void onAssistant(BNRemoteMessage.BNRGAssistant arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onAssistant......distance = " + arg0.getAssistantDistance() + ", type = " + arg0.getAssistantType());

            String assistantTips = "";
            String assistantTypeS = "合流";
            if (arg0.getAssistantDistance() > 0) {
                switch (arg0.getAssistantType()) {
                    case HUDConstants.AssistantType.JOINT:
                        assistantTypeS = "合流";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.TUNNEL:
                        assistantTypeS = "隧道";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.BRIDGE:
                        assistantTypeS = "桥梁";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.RAILWAY:
                        assistantTypeS = "铁路";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.BLIND_BEND:
                        assistantTypeS = "急转弯";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.BLIND_SLOPE:
                        assistantTypeS = "陡坡";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.ROCKFALL:
                        assistantTypeS = "落石";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.ACCIDENT:
                        assistantTypeS = "事故多发区";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.SPEED_CAMERA:
                        assistantTypeS = "测速摄像";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS + "限速： " + arg0.getAssistantLimitedSpeed();
                        break;
                    case HUDConstants.AssistantType.TRAFFIC_LIGHT_CAMERA:
                        assistantTypeS = "交通信号灯摄像";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.INTERVAL_CAMERA:
                        assistantTypeS = "区间测速";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.CHILDREN:
                        assistantTypeS = "注意儿童";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.UNEVEN:
                        assistantTypeS = "路面不平";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.NARROW:
                        assistantTypeS = "道路变窄";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.VILLAGE:
                        assistantTypeS = "前面村庄";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.SLIP:
                        assistantTypeS = "路面易滑";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.OVER_TAKE_FORBIDEN:
                        assistantTypeS = "禁止超车";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    case HUDConstants.AssistantType.HONK:
                        assistantTypeS = "请铭喇叭";
                        assistantTips = "前方" + getFormatAfterMeters(arg0.getAssistantDistance()) + assistantTypeS;
                        break;
                    default:
                        break;
                }
            }

            final String assistantTipstr = assistantTips;
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText(assistantTipstr);
                }
            });
        }

        @Override
        public void onCruiseEnd(BNRemoteMessage.BNRGCruiseEnd arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "cruise end");
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("关闭电子狗");
                }
            });
        }

        @Override
        public void onCruiseStart(BNRemoteMessage.BNRGCruiseStart arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "cruise start");
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("开启电子狗");
                }
            });
        }

        @Override
        public void onCurrentRoad(BNRemoteMessage.BNRGCurrentRoad arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onCurrentRoad...........curRoadName = " + arg0.getCurrentRoadName());

            final String curRoadName = arg0.getCurrentRoadName();
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNewCurRoadTv.setText("当前道路: " + curRoadName);
                    mNewCurRoadTv.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onGPSLost(BNRemoteMessage.BNRGGPSLost arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onGPSLost....");
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("GPS信号丢失");
                }
            });
        }

        @Override
        public void onGPSNormal(BNRemoteMessage.BNRGGPSNormal arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onGPSNormal....");

            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("GPS信号正常");
                }
            });
        }

        @Override
        public void onManeuver(BNRemoteMessage.BNRGManeuver arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onManeuver...........name = " + arg0.getManeuverName() + ", distance = " +
            //arg0.getManeuverDistance() + ",nextRoadName = " + arg0.getNextRoadName());

            final String afterMeterS = getFormatAfterMeters(arg0.getManeuverDistance());
            final String nextRoadName = arg0.getNextRoadName();
            final boolean isAlong = arg0.mIsStraight;
            String turnName = arg0.name;
            int turnIconResId = SimpleGuideModle.gTurnIconID[0];

            if (turnName != null && !"".equalsIgnoreCase(turnName)) {
                turnName = turnName + ".png";
            }

            if (turnName != null && !"".equalsIgnoreCase(turnName)) {
                turnIconResId = SimpleGuideModle.getInstance().getTurnIconResId(turnName);
            }

            final int turnIcon = turnIconResId;

            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mAfterMetersInfoTV.setText(afterMeterS);
                    mGoWhereInfoTV.setText(nextRoadName);

                    mNaviAfterView.setVisibility(View.VISIBLE);
                    mAlongRoadView.setVisibility(View.GONE);
                    mTurnIcon.setImageDrawable(getResources().getDrawable(turnIcon));
                }
            });
        }

        @Override
        public void onNaviEnd(BNRemoteMessage.BNRGNaviEnd arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onNaviEnd...........");

            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("导航结束");
                }
            });
        }

        @Override
        public void onNaviStart(BNRemoteMessage.BNRGNaviStart arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onNaviStart...........");
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("导航开始");
                }
            });
        }

        @Override
        public void onNextRoad(BNRemoteMessage.BNRGNextRoad arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onNextRoad...........nextRoadName = " + arg0.getNextRoadName());
        }

        @Override
        public void onRemainInfo(BNRemoteMessage.BNRGRemainInfo arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onRemainInfo.............distance = " + arg0.getRemainDistance() + ", time = " + arg0.getRemainTime());

            final String remainDistance = calculateTotalRemainDistString(arg0.getRemainDistance());
            final String remainTime = calculateArriveTime(arg0.getRemainTime());
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mTotalDistTV.setText(remainDistance);
                    mArriveTimeTV.setText(remainTime);
                }
            });
        }

        @Override
        public void onRoutePlanYawComplete(BNRemoteMessage.BNRGRPYawComplete arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onRoutePlanYawComplete............");

            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("偏航算路完成");
                }
            });
        }

        @Override
        public void onRoutePlanYawing(BNRemoteMessage.BNRGRPYawing arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onRoutePlanYawing............");

            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText("偏航中...");
                }
            });
        }

        @Override
        public void onServiceArea(BNRemoteMessage.BNRGServiceArea arg0) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onServiceArea............name = " + arg0.getServiceAreaName() + ", distance = " + arg0.getServiceAreaDistance());

            final String serviceAreaTips = getFormatAfterMeters(arg0.getServiceAreaDistance()) + " " + arg0.getServiceAreaName();
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mNavilogTv.setText(serviceAreaTips);
                }
            });
        }

        @Override
        public void onEnlargeRoad(BNRemoteMessage.BNEnlargeRoad enlargeRoad) {
            //Log.d(BNRemoteConstants.MODULE_TAG, "onEnlargeRoad......enlargeRoad = " + enlargeRoad);

            final BNRemoteMessage.BNEnlargeRoad enlargeInfo = enlargeRoad;
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    handleEnlargeRoad(enlargeInfo);
                }
            });
        }

        @Override
        public void onCarFreeStatus(BNRemoteMessage.BNRGCarFreeStatus arg0) {
            // TODO Auto-generated method stub
            //Log.d(BNRemoteConstants.MODULE_TAG, "onCarFreeStatus...... ");

        }

        @Override
        public void onCarInfo(BNRemoteMessage.BNRGCarInfo arg0) {
            // TODO Auto-generated method stub
            //Log.d(BNRemoteConstants.MODULE_TAG, "onCarInfo...... ");

        }

        @Override
        public void onCarTunelInfo(BNRemoteMessage.BNRGCarTunelInfo arg0) {
            // TODO Auto-generated method stub
            //Log.d(BNRemoteConstants.MODULE_TAG, "onCarTunelInfo...... ");
        }

        @Override
        public void onCurShapeIndexUpdate(BNRemoteMessage.BNRGCurShapeIndexUpdate arg0) {
            // TODO Auto-generated method stub
            //Log.d(BNRemoteConstants.MODULE_TAG, "onCurShapeIndexUpdate...... ");
        }

        @Override
        public void onDestInfo(BNRemoteMessage.BNRGDestInfo arg0) {
            // TODO Auto-generated method stub
            Log.d(BNRemoteConstants.MODULE_TAG, "onDestInfo...... ");
        }

        @Override
        public void onNearByCamera(BNRemoteMessage.BNRGNearByCameraInfo arg0) {
            // TODO Auto-generated method stub
            //Log.d(BNRemoteConstants.MODULE_TAG, "onNearByCamera...... ");
        }

        @Override
        public void onRouteInfo(BNRemoteMessage.BNRGRouteInfo arg0) {
            // TODO Auto-generated method stub
            //Log.d(BNRemoteConstants.MODULE_TAG, "onRouteInfo...... ");
        }

    };

    private void handleEnlargeRoad(BNRemoteMessage.BNEnlargeRoad enlargeRoad) {
        int enlargeType = enlargeRoad.getEnlargeRoadType();
        int enlargeState = enlargeRoad.getEnlargeRoadState();

        if (enlargeState == HUDConstants.EnlargeMapState.EXPAND_MAP_STATE_HIDE) {
            if (mEnlargeRoadMapView != null) {
                mEnlargeRoadMapView.setVisibility(View.GONE);
            }
        } else {

            if (mEnlargeRoadMapView != null) {
                if (enlargeState == HUDConstants.EnlargeMapState.EXPAND_MAP_STATE_UPDATE &&
                        mEnlargeRoadMapView.getVisibility() != View.VISIBLE) {
                    return;
                }
                mEnlargeRoadMapView.setVisibility(View.VISIBLE);
            }

            mEnlargeType = enlargeType;
            if (enlargeState == HUDConstants.EnlargeMapState.EXPAND_MAP_STATE_SHOW) {
                mbUpdateRasterInfo = false;
            } else {
                mbUpdateRasterInfo = true;
            }

            Bitmap basicImage = enlargeRoad.getBasicImage();
            Bitmap arrowImage = enlargeRoad.getArrowImage();

            String roadName = enlargeRoad.getRoadName();
            mTotalDist = enlargeRoad.getTotalDist();
            mRemDist = enlargeRoad.getRemainDist();
            if (!TextUtils.isEmpty(roadName)) {
                mRoadName = roadName;
            }

            mProgress = 0;
            if (mRemDist <= 0 || mTotalDist <= 0) {
                mProgress = 100;
            } else {
                mProgress = (int) (mTotalDist - mRemDist) * 100 / mTotalDist;
            }

            if (enlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_VECTOR) {
                mCarPosX = enlargeRoad.getCarPosX();
                mCarPosY = enlargeRoad.getCarPosY();
                mCarRotate = enlargeRoad.getCarPosRotate();
                mCarRotate = -mCarRotate;
            } else if (null != mCarPosImgView) {
                mCarPosImgView.setVisibility(View.INVISIBLE);
            }

            if (enlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_DEST_STREET_VIEW) {

                if (null != bnav_rg_enlarge_image_mask) {
                    bnav_rg_enlarge_image_mask.setVisibility(View.INVISIBLE);
                }
            }

            updateEnlargeRoadView(basicImage, arrowImage);
        }
    }

    private void updateEnlargeRoadView(Bitmap baseicImage, Bitmap arrawImage) {
        if (!mbUpdateRasterInfo || mForceRefreshImage) {
            mForceRefreshImage = false;
            if (mEnlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_DIRECT_BOARD) {
                updateDirectBoardView(baseicImage, arrawImage);

            } else if (mEnlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_RASTER) {
                updateSimpleModelView(baseicImage, arrawImage);

            } else if (mEnlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_VECTOR) {
                updateVectorMapView(baseicImage, arrawImage);

            } else if (mEnlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_DEST_STREET_VIEW) {
                updateStreetView(baseicImage, arrawImage);
            }
        }
        updateProgress(baseicImage, arrawImage);
    }

    private void updateProgress(Bitmap baseicImage, Bitmap arrawImage) {
        // 更新剩余距离和进度条
        StringBuffer distance = new StringBuffer();
        StringUtils.formatDistance(mRemDist, StringUtils.UnitLangEnum.ZH, distance);
        mRemainDistTV.setText(distance.toString());
        mProgressBar.setProgress(mProgress);

        if (mEnlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_VECTOR) {
            updateVectorMapCarPos(baseicImage, arrawImage);
        } else if (mEnlargeType == HUDConstants.EnlargeMapType.EXPAND_MAP_DEST_STREET_VIEW) {
            if (null != mCarPosImgView) {
                mCarPosImgView.setVisibility(View.INVISIBLE);
            }
        } else if (null != mCarPosImgView) {
            mCarPosImgView.setVisibility(View.INVISIBLE);
        }
    }

    private void updateVectorMapCarPos(Bitmap baseicImage, Bitmap arrawImage) {
    }

    private synchronized void updateDirectBoardView(Bitmap baseicImage, Bitmap arrawImage) {
        if (null == mEnlargeImageView || null == mNextRoadTV) {
            return;
        }

        releaseImageView(mEnlargeImageView);
        if (!TextUtils.isEmpty(mRoadName)) {
            mNextRoadTV.setText(mRoadName);
            mNextRoadTV.setVisibility(View.VISIBLE);
        } else {
            mNextRoadTV.setVisibility(View.INVISIBLE);
        }

        if (baseicImage != null && arrawImage != null) {
            mEnlargeImageView.setImageBitmap(arrawImage);
            mEnlargeImageView.setBackgroundDrawable(new BitmapDrawable(baseicImage));
        }
        mEnlargeImageView.setVisibility(View.VISIBLE);
    }

    private void updateSimpleModelView(Bitmap baseicImage, Bitmap arrawImage) {
        if (null == mEnlargeImageView || null == mNextRoadTV) {
            return;
        }

        releaseImageView(mEnlargeImageView);

        if (!TextUtils.isEmpty(mRoadName)) {
            mNextRoadTV.setText(mRoadName);
            mNextRoadTV.setVisibility(View.VISIBLE);
        } else {
            mNextRoadTV.setVisibility(View.INVISIBLE);
        }

        if (arrawImage != null && baseicImage != null) {
            mEnlargeImageView.setImageBitmap(arrawImage);
            mEnlargeImageView.setBackgroundDrawable(new BitmapDrawable(baseicImage));
        }

        mEnlargeImageView.setVisibility(View.VISIBLE);
    }

    private void updateVectorMapView(Bitmap baseicImage, Bitmap arrawImage) {
        if (null == mEnlargeImageView || null == mNextRoadTV) {
            return;
        }

        releaseImageView(mEnlargeImageView);

        if (!TextUtils.isEmpty(mRoadName)) {
            mNextRoadTV.setText(mRoadName);
            mNextRoadTV.setVisibility(View.VISIBLE);
        } else {
            mNextRoadTV.setVisibility(View.INVISIBLE);
        }

        if (baseicImage != null) {
            mEnlargeImageView.setImageBitmap(baseicImage);
            mEnlargeImageView.setBackgroundResource(android.R.color.transparent);
        }
        mEnlargeImageView.setVisibility(View.VISIBLE);
    }

    private void updateStreetView(Bitmap baseicImage, Bitmap arrawImage) {
        if (null == mEnlargeImageView || null == mNextRoadTV) {
            return;
        }
        releaseImageView(mEnlargeImageView);

        mNextRoadTV.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mRoadName)) {
            mNextRoadTV.setText("距离" + mRoadName);
        } else {
            mNextRoadTV.setVisibility(View.INVISIBLE);
        }

        if (baseicImage != null) {
            mEnlargeImageView.setImageBitmap(baseicImage);
            mEnlargeImageView.setBackgroundResource(android.R.color.transparent);
        }

        mEnlargeImageView.setVisibility(View.VISIBLE);
    }

    /**
     * 释放 ImageView
     *
     * @param iv
     */
    public static void releaseImageView(ImageView iv) {
        if (null != iv) {
            iv.setImageBitmap(null);
            iv.setBackgroundResource(android.R.color.transparent);
            iv.setBackgroundDrawable(null);
            iv = null;
        }
    }

    private void initViews() {

        mSimpleRGLayout = findViewById(R.id.simple_route_guide);
        mNaviAfterView = findViewById(R.id.bnavi_rg_after_layout);
        mTurnIcon = (ImageView) findViewById(R.id.bnav_rg_sg_turn_icon);
        mAfterMetersInfoTV = (TextView) findViewById(R.id.bnav_rg_sg_after_meters_info);
        mAfterLabelInfoTV = (TextView) findViewById(R.id.bnav_rg_sg_after_label_info);
        mGoLabelTV = (TextView) findViewById(R.id.bnav_rg_sg_go_label_tv);
        mGoWhereInfoTV = (TextView) findViewById(R.id.bnav_rg_sg_go_where_info);

        mAlongRoadView = findViewById(R.id.bnav_rg_sg_along_layout);
        mCurRoadNameTV = (TextView) findViewById(R.id.bnav_rg_sg_cur_road_name_tv);
        mCurRoadRemainDistTV = (TextView) findViewById(R.id.bnav_rg_sg_cur_road_remain_dist_tv);

        mNewCurRoadTv = (TextView) findViewById(R.id.cur_road_name_tv);

        mRemainInfoLayout = findViewById(R.id.remain_info);
        mTotalDistTV = (TextView) findViewById(R.id.bnav_rg_cp_total_dist);
        mArriveTimeTV = (TextView) findViewById(R.id.bnav_rg_cp_arrive_time);

        mNavilogTv = (TextView) findViewById(R.id.hud_log_tv);
        mServerIPTv = (EditText) findViewById(R.id.server_ip_tv);

        mEnlargeRoadMapView = findViewById(R.id.bnav_rg_enlarge_road_map);
        mEnlargeImageView = (ImageView) findViewById(R.id.bnav_rg_enlarge_image);

        mRemainDistTV = (TextView) findViewById(R.id.bnav_rg_enlarge_remain_dist);
        mNextRoadTV = (TextView) findViewById(R.id.bnav_rg_enlarge_next_road);
        mProgressBar = (ProgressBar) findViewById(R.id.bnav_rg_enlarge_progress);

        mCarPosLayout = findViewById(R.id.bnav_rg_enlarge_carpos_layout);
        mCarPosImgView = (ImageView) findViewById(R.id.bnav_rg_enlarge_carpos_image);

        bnav_rg_enlarge_image_mask = findViewById(R.id.bnav_rg_enlarge_image_mask);
        //mProgressDialog = ProgressDialog.show(DiaoPeiActivity.this, "", "正在连接中...");
        /*mConnectBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String ip = mServerIPTv.getText().toString();
                //String ip = getLocalIpAddress();
                mProgressDialog = ProgressDialog.show(BNDGuideActivity.this, "", "正在连接中...");
                BNRemoteVistor.getInstance().setServerIPAddr(ip);
                BNRemoteVistor.getInstance().open();
            }
        });*/

        /*mCloseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BNRemoteVistor.getInstance().close(HUDSDkEventCallback.OnConnectCallback.CLOSE_NORMAL, "user closed");;
            }
        });*/
    }

    private String getAppVersionName(Context context, String appName) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(appName, 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 根据剩余距离获取格式化的字符串，如  200米后
     *
     * @param nextRemainDist
     * @return
     */
    private String getFormatAfterMeters(int nextRemainDist) {
        StringBuffer distance = new StringBuffer();
        StringUtils.formatDistance(nextRemainDist, StringUtils.UnitLangEnum.ZH, distance);
        return getResources().getString(R.string.nsdk_string_rg_sg_after_meters, distance);
    }

    private String calculateTotalRemainDistString(int nDist) {
        StringBuffer builder = new StringBuffer();
        StringUtils.formatDistance(nDist, StringUtils.UnitLangEnum.ZH, builder);
        String totalRemainDistS = builder.toString();

        return totalRemainDistS;
    }

    private String calculateArriveTime(int remainTime) {
        long mArriveTime = System.currentTimeMillis();
        Date curDate = new Date(mArriveTime);
        mArriveTime += (remainTime * 1000);
        Date arriveDate = new Date(mArriveTime);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String mArriveTimeS = sdf.format(arriveDate);

        GregorianCalendar curCal = new GregorianCalendar();
        curCal.setTime(curDate);
        GregorianCalendar arriveCal = new GregorianCalendar();
        arriveCal.setTime(arriveDate);

        if (curCal.get(GregorianCalendar.DAY_OF_MONTH) == arriveCal.get(GregorianCalendar.DAY_OF_MONTH)) {
            if (0 == arriveCal.get(GregorianCalendar.HOUR_OF_DAY)) {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time_at_wee), mArriveTimeS);
            } else {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time), mArriveTimeS);
            }
        } else {
            int interval = getIntervalDays(curDate, arriveDate);
            if (interval == 1) {
                if (arriveCal.get(GregorianCalendar.HOUR_OF_DAY) >= 0 && arriveCal.get(GregorianCalendar.HOUR_OF_DAY) < 4) {
                    mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time),
                            getString(R.string.nsdk_string_rg_wee_hours));
                } else {
                    mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time),
                            getString(R.string.nsdk_string_rg_tomorrow));
                }
            } else if (interval == 2) {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time),
                        getString(R.string.nsdk_string_rg_the_day_after_tomorrow));
            } else if (interval > 2) {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time_after_day), "" + interval);
            } else {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time), mArriveTimeS);
            }
        }

        return mArriveTimeS;
    }

    /*
     * 两个日期之间相隔的天数
     *
     * @param fDate
     * @param oDate
     * @return
     */
    private static int getIntervalDays(Date fDate, Date oDate) {
        if (null == fDate || null == oDate) {
            return 0;
        }

        long intervalMilli = oDate.getTime() - fDate.getTime();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fDate = (Date) sdf.parse(sdf.format(fDate));
            oDate = (Date) sdf.parse(sdf.format(oDate));
            intervalMilli = oDate.getTime() - fDate.getTime();
        } catch (Exception e) {

        }

        return (int) (intervalMilli / (24 * 60 * 60 * 1000));


    }

}
