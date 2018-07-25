package com.njdp.njdp_drivers.items;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
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
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import bean.BasicDeployRes;
import bean.FieldInfo;
import bean.FieldInfoList;
import bean.FieldInfoPost;

public class item_intelligent_resolution_1 extends Fragment implements View.OnClickListener {

    private static final String TAG = item_intelligent_resolution_1.class.getSimpleName();
    private String deploy_url;//获取智能调度方案服务器地址
    private static int FLAG_GETDEPLOY=11101009;
    private com.yolanda.nohttp.rest.Request<JSONObject> getDeploy_strReq;
    private ExpandableListView expandableListView;
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private int size;//周围农田的数量
    private ExpandableListAdapter adapter;
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private Gson gson;
    private FieldInfoDao fieldInfoDao;
    private String token;
    private String deploy_id;
    private String norm_id;
    private String machine_id;
    private int deploySize;
    private List<FieldInfoPost> allFieldInfoPost=new ArrayList<FieldInfoPost>();
    private List<FieldInfoPost> selectedFieldInfoPost=new ArrayList<FieldInfoPost>();
    private SimpleDateFormat format;
    private SimpleDateFormat format2;
    private StringBuffer[] s_deploy_1;//方案1路线
    private StringBuffer[] s_deploy_2;;//方案2路线
    private StringBuffer[] s_deploy_3;;//方案3路线
    private StringBuffer[] s_deploy_4;;//方案4路线
    private StringBuffer[] s_deploy_5;;//方案5路线
    private HashMap<Integer, Boolean> checkState = new HashMap<Integer,Boolean>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_1_intelligent_resolution_1, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.arrange_button).setOnClickListener(this);
        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;

        sessionManager=new SessionManager();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        fieldInfoDao=new FieldInfoDao(mainMenu);
        token=sessionManager.getToken();
        deploy_id=sessionManager.getDeployId();
//        norm_id=sessionManager.getNormId();
        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            Log.e(TAG, machine_id);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        deploy_url=AppConfig.URL_BASICDEPLOY;

        for(int i=0;i< mainMenu.selectedFieldInfo.size();i++)
        {
            checkState.put(i,true);
        }

        try{
            allFieldInfoPost.clear();
            selectedFieldInfoPost.clear();
        }catch (Exception e) {}
        setSelectedFieldInfoPost();


        ///////////////////////////////////////测试mainMenu.selectedFieldInfo是否有数据////////////
//        Log.e(TAG,"农田总数："+String.valueOf(mainMenu.selectedFieldInfo.size()));
//        Log.e(TAG,"路线总数："+String.valueOf(selectedFieldInfoPost.size()));
//        for(int i=0;i<mainMenu.selectedFieldInfo.size();i++)
//        {
//            Log.e(TAG,"农田："+mainMenu.selectedFieldInfo.get(i).getFarm_id());
//            Log.e(TAG,"路线："+selectedFieldInfoPost.get(i).getFarm_id());
//        }
        ///////////////////////////////////////测试mainMenu.selectedFieldInfo是否有数据////////////


        format = new SimpleDateFormat("yyyy-MM-dd");
        format2 = new SimpleDateFormat("yyyy年MM月dd日");

        size=fieldInfoDao.countOfField();//农田总数

        expandableListView = (ExpandableListView)view.findViewById(R.id.intelligent_arrange_div_expand);
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
                return fieldInfoDao.getFieldInfoByFieldId(allFieldInfoPost.get(groupPosition).getFarm_id());
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return fieldInfoDao.getFieldInfoByFieldId(allFieldInfoPost.get(groupPosition).getFarm_id());
            }

            @Override
            public long getGroupId(int groupPosition) {
                return 2000+groupPosition*10;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return 2000+groupPosition*10+childPosition;
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
                if (allFieldInfoPost.size() >= 1) {
                    fieldInfoPost = allFieldInfoPost.get(groupPosition);
//                    Log.e(TAG, "parent序号1:" + fieldInfoPost.getFarm_id());
                    fieldInfo = mainMenu.selectedFieldInfo.get(groupPosition);
                    farmId=fieldInfo.getFarm_id();
//                    Log.e(TAG, "paren序号2:" + farmId);
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mainMenu).inflate(R.layout.expandablelistview_parent, null);
                    }

                    final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.selected);
                    checkBox.setChecked(checkState.get(groupPosition) == null ? false: true);

                    LinearLayout textLayout = (LinearLayout) convertView.findViewById(R.id.expandParent_text);
                    TextView tv1 = (TextView) convertView.findViewById(R.id.id);
                    tv1.setText(String.valueOf(groupPosition + 1));
                    try {
                        TextView tv2 = (TextView) convertView.findViewById(R.id.site);
                        tv2.setText(fieldInfo.getCropLand_site());
//                        TextView tv3 = (TextView) convertView.findViewById(R.id.field_number);
//                        tv3.setText(fieldInfo.getArea_num() + "亩");
                    } catch (Exception e) {
                        Log.e(TAG, "Error" + e.toString());
                    }

                    textLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isExpanded) {
                                expandableListView.collapseGroup(groupPosition);
                            } else {
                                expandableListView.expandGroup(groupPosition);
                            }
                        }
                    });
                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isChecked=checkBox.isChecked();
                            //////////////////////////临时存储选择的农田信息/////////////////////////
                            if (isChecked) {
                                checkState.put(groupPosition, isChecked);
                                selectedFieldInfoPost.add(fieldInfoPost);
                                int lp = selectedFieldInfoPost.size();
                                Log.e(mainMenu.TAG, "路线总数：" + lp + "：+l");
                            } else {
                                checkState.remove(groupPosition);
                                int length = allFieldInfoPost.size();
                                for (int i = 0; i < length; i++) {
                                    FieldInfoPost fieldInfoPost1 = allFieldInfoPost.get(i);
                                    if (fieldInfoPost1.getFarm_id().equals(farmId)) {
                                        selectedFieldInfoPost.remove(fieldInfoPost1);
                                        int lp = selectedFieldInfoPost.size();
                                        Log.e(mainMenu.TAG, "路线总数：" + lp + "：-l");
                                    }
                                }
                            }
                            ////////////////////////////////临时存储选择的农田信息/////////////////////////
                        }
                    });

                }else {
                    commonUtil.error_hint_short("未获取到农田信息，请返回上一个界面重新选择！");
                }
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                FieldInfoPost fieldInfoPost = allFieldInfoPost.get(groupPosition);
                Log.e(TAG, "child序号1:" + fieldInfoPost.getFarm_id());
                FieldInfo fieldInfo = mainMenu.selectedFieldInfo.get(groupPosition);
                String farmId=fieldInfo.getFarm_id();
                Log.e(TAG, "child序号2:" + farmId);
                if (convertView == null) {
                    convertView = LayoutInflater.from(mainMenu).inflate(R.layout.expandablelistview_child, null);
                }
                TextView tv1=(TextView)convertView.findViewById(R.id.text1);
                tv1.setText("农田面积："+fieldInfo.getArea_num()+"亩");
                TextView tv2=(TextView)convertView.findViewById(R.id.text2);
                tv2.setText("作物类型："+commonUtil.transferCropKind(fieldInfo.getCrops_kind()));
                TextView tv3=(TextView)convertView.findViewById(R.id.text3);
                tv3.setText("单价："+fieldInfo.getUnit_price()+"元/亩");
                TextView tv4=(TextView)convertView.findViewById(R.id.text4);
                tv4.setText("地块类型："+fieldInfo.getBlock_type());
                Date date1=new Date();
                Date date2=new Date();
                try {
                    date1 = format.parse(fieldInfo.getStart_time());
                    date2 = format.parse(fieldInfo.getEnd_time());
                }catch (Exception e)
                {
                    Log.e(TAG,e.toString());
                }
                TextView tv5=(TextView)convertView.findViewById(R.id.text5);
                tv5.setText("起止日期：" + format2.format(date1) + "--" + format2.format(date2));
                TextView tv6=(TextView)convertView.findViewById(R.id.text6);
                tv6.setText("联系方式："+fieldInfo.getUser_name());
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {

                return true;
            }
        };
        expandableListView.setAdapter(adapter);
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback:
                selectedFieldInfoPost.clear();
                mainMenu.clearFieldData();//清空缓存的农田数据
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.arrange_button:
                if(selectedFieldInfoPost.size()>=1)
                {
                    //////////////////////////////////////////////测试方案数据/////////////////////////
//                    mainMenu.basicDeployRess.clear();
//                    BasicDeployRes test1=new BasicDeployRes();
//                    test1.setCost("1000元");
//                    test1.setEarnings("800元");
//                    test1.setWorkTime("2016/5/1--2016/6/6");
//                    test1.setId(String.valueOf(1));
//                    test1.setRoute(new StringBuffer[]{new StringBuffer("1"),new StringBuffer("5"),new StringBuffer("6"),new StringBuffer("4")});
//                    mainMenu.basicDeployRess.add(test1);
//                    BasicDeployRes test2=new BasicDeployRes();
//                    test2.setCost("1000元");
//                    test2.setEarnings("800元");
//                    test2.setWorkTime("2016/5/1--2016/6/6");
//                    test2.setId(String.valueOf(2));
//                    test2.setRoute(new StringBuffer[]{new StringBuffer("2"),new StringBuffer("1"),new StringBuffer("6"),new StringBuffer("5")});
//                    mainMenu.basicDeployRess.add(test2);
//                    BasicDeployRes test3=new BasicDeployRes();
//                    test3.setCost("1000元");
//                    test3.setEarnings("800元");
//                    test3.setWorkTime("2016/5/1--2016/6/6");
//                    test3.setId(String.valueOf(2));
//                    test3.setRoute(new StringBuffer[]{new StringBuffer("6"),new StringBuffer("1"),new StringBuffer("4"),new StringBuffer("2")});
//                    test3.setId(String.valueOf(3));
//                    test3.setRoute(new StringBuffer[]{new StringBuffer("2"), new StringBuffer("1"), new StringBuffer("6"), new StringBuffer("5")});
//                    mainMenu.basicDeployRess.add(test3);
//                    BasicDeployRes test4=new BasicDeployRes();
//                    test4.setCost("1000元");
//                    test4.setEarnings("800元");
//                    test4.setWorkTime("2016/5/1--2016/6/6");
//                    test4.setId(String.valueOf(2));
//                    test4.setRoute(new StringBuffer[]{new StringBuffer("6"), new StringBuffer("4"), new StringBuffer("2"), new StringBuffer("5")});
//                    test4.setId(String.valueOf(4));
//                    test4.setRoute(new StringBuffer[]{new StringBuffer("3"), new StringBuffer("5"), new StringBuffer("2"), new StringBuffer("1")});
//                    mainMenu.basicDeployRess.add(test4);
//                    BasicDeployRes test5=new BasicDeployRes();
//                    test5.setCost("1000元");
//                    test5.setEarnings("800元");
//                    test5.setWorkTime("2016/5/1--2016/6/6");
//                    test5.setId(String.valueOf(5));
//                    test5.setRoute(new StringBuffer[]{new StringBuffer("1"), new StringBuffer("6"), new StringBuffer("3"), new StringBuffer("2")});
//                    mainMenu.basicDeployRess.add(test5);
//                    int deploySize=mainMenu.basicDeployRess.size();
//                    saveDeploy(deploySize);

//                    mainMenu.addBackFragment(new item_intelligent_resolution_2());//无数据测试跳转
                    //////////////////////////////////////////////测试数据/////////////////////////

                    initDeployInfo();
                }else {
                    commonUtil.error_hint_short("您还没有选择作业地点，请选择！");
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

    //获取智能调度方案
    private void initDeployInfo()
    {
        Map<String, String> params = new HashMap<String, String>();
        for(int i=0;i<selectedFieldInfoPost.size();i++)
        {
            params.put("farm_distance["+selectedFieldInfoPost.get(i).getFarm_id()+"]", selectedFieldInfoPost.get(i).getDistance());
            Log.e(TAG, gson.toJson(selectedFieldInfoPost.get(i)));
        }
        params.put("deploy_id",deploy_id);
        params.put("machine_id",machine_id);
//        params.put("norm_id",norm_id);
        params.put("token", token);
        Log.e(TAG, "获取方案发送的数据：" + gson.toJson(params));

        getDeploy_strReq= NoHttp.createJsonObjectRequest(deploy_url, RequestMethod.POST);
        getDeploy_strReq.add(params); //添加参数   wangchao 20180725
        getDeploy_strReq.addHeader("Content-Type", "application/x-www-form-urlencoded");
        getDeploy_strReq.setConnectTimeout(15 * 1000);
        RequestQueue requestQueue = NoHttp.newRequestQueue();

        if (!netUtil.checkNet(mainMenu)) {
            commonUtil.error_hint_short("网络连接错误");
            mainMenu.hideDialog();
        } else {
            requestQueue.add(FLAG_GETDEPLOY, getDeploy_strReq, new getDeploy_request());
            //        String tag_string_req = "req_initDeploy";
//            //服务器请求
//            StringRequest strReq = new StringRequest(Request.Method.POST,
//                    deploy_url, initSuccessListener, mainMenu.mErrorListener) {
//
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//                    for(int i=0;i<selectedFieldInfoPost.size();i++)
//                    {
//                        params.put("farm_distance["+selectedFieldInfoPost.get(i).getFarm_id()+"]", selectedFieldInfoPost.get(i).getDistance());
//                        Log.e(TAG, gson.toJson(selectedFieldInfoPost.get(i)));
//                    }
//                    params.put("deploy_id",deploy_id);
//                    params.put("norm_id",norm_id);
//                    params.put("token", token);
//                    Log.e(TAG, "获取方案发送的数据：" + gson.toJson(params));
//                    return netUtil.checkParams(params);
//                }
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> headers=new HashMap<String,String>();
//                    headers.put("Content-Type", "application/x-www-form-urlencoded");
////                    return super.getHeaders();
//                    return headers;
//                }
//            };
//
//            strReq.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
//            // Adding request to request queue
//            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    //获取智能调度方案访问服务器监听
    private class getDeploy_request implements OnResponseListener<JSONObject>
    {
        @Override
        public void onStart(int what) {
            mainMenu.pDialog.setMessage("正在计算，请稍候 ......");
            mainMenu.showDialog();
        }

        @Override
        public void onSucceed(int what, com.yolanda.nohttp.rest.Response<JSONObject> response) {

            try {
                JSONObject jObj=response.get();
                Log.e(TAG,"获取智能调度方案接收的数据："+jObj.toString());
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
                    String deployInfo = jObj.getString("result");//方案JSON

                    ////////////////////方案放入集合，暂时存储/////////////////////////////
                    mainMenu.basicDeployRess.clear();//清空存储方案集合
                    mainMenu.basicDeployRess = gson.fromJson(deployInfo, new TypeToken<List<BasicDeployRes>>() {
                    }.getType());
                    deploySize = mainMenu.basicDeployRess.size();
                    Log.e(TAG,String.valueOf(deploySize));
                    ////////////////////方案放入集合，暂时存储////////////////////////////

                    saveDeploy(deploySize);//数据存入临时数据集合deploy1,deploy2,deploy3,deploy4,deploy5

                    //跳转选择方案界面
                    mainMenu.addBackFragment(new item_intelligent_resolution_2());
                } else {

                    String errorMsg = jObj.getString("error_msg");
                    Log.e(TAG, "Json error:json返回的数据有错误：" + errorMsg);
                    commonUtil.error_hint_short(errorMsg);
                }
            } catch (JSONException e) {

                e.printStackTrace();
                Log.e(TAG, "Json error:json解析错误：" + e.getMessage());
                commonUtil.error_hint2_short(R.string.connect_error);
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "服务器连接失败Around Machine Error: " + exception.getMessage());
            commonUtil.error_hint2_short(R.string.connect_error);
        }

        @Override
        public void onFinish(int what) {
            mainMenu.hideDialog();
        }
    }

//   //获取方案数据响应服务器成功
//    private Response.Listener<String> initSuccessListener = new Response.Listener<String>() {
//
//        @Override
//        public void onResponse(String response) {
//
//            mainMenu.hideDialog();
//            try {
//                Log.e(TAG, "DeployInit Response: " + response);
//                JSONObject jObj = new JSONObject(response);
//                int status = jObj.getInt("status");
//
//                if (status == 1) {
//                    String errorMsg = jObj.getString("result");
//                    Log.e(TAG, "Json error：response错误:" + errorMsg);
//                    commonUtil.error_hint_short("密钥失效，请重新登录");
//                    //清空数据，重新登录
//                    netUtil.clearSession(mainMenu);
//                    mainMenu.backLogin();
//                    SysCloseActivity.getInstance().exit();
//                } else if (status == 0) {
//
//                    String deployInfo = jObj.getString("result");//方案JSON
//
//                    ////////////////////方案放入集合，暂时存储/////////////////////////////
//                    mainMenu.basicDeployRess.clear();//清空存储方案集合
//                    mainMenu.basicDeployRess = gson.fromJson(deployInfo, new TypeToken<List<BasicDeployRes>>() {
//                    }.getType());
//                    deploySize = mainMenu.basicDeployRess.size();
//                    Log.e(TAG,String.valueOf(deploySize));
//                    ////////////////////方案放入集合，暂时存储////////////////////////////
//
//                    saveDeploy(deploySize);//数据存入临时数据集合deploy1,deploy2,deploy3,deploy4,deploy5
//
//                    //跳转选择方案界面
//                    mainMenu.addBackFragment(new item_intelligent_resolution_2());
//                } else {
//
//                    String errorMsg = jObj.getString("error_msg");
//                    Log.e(TAG, "Json error:json错误：" + errorMsg);
//                    commonUtil.error_hint_short(errorMsg);
//                }
//            } catch (JSONException e) {
//
//                //SON error
//                e.printStackTrace();
//                Log.e(TAG, "Json error:json错误：" + e.getMessage());
//                commonUtil.error_hint2_short(R.string.connect_error);
//            }
//        }
//    };
    ////////////////////////////获取智能调度方案//////////////////////////////////////


    /**
     *
     * 数据存入临时数据集合deploy1,deploy2,deploy3,deploy4,deploy5
     *
     */
    private void saveDeploy(int deploySize)
    {
        mainMenu.deploy_1.clear();
        mainMenu.deploy_2.clear();
        mainMenu.deploy_3.clear();
        mainMenu.deploy_4.clear();
        mainMenu.deploy_5.clear();
        if (deploySize==1)
        {
            s_deploy_1 = mainMenu.basicDeployRess.get(0).getRoute();
            for (int i = 0; i < s_deploy_1.length; i++) {
                mainMenu.deploy_1.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_1[i].toString()));
            }
        }
        else if (deploySize==2) {
            s_deploy_1 = mainMenu.basicDeployRess.get(0).getRoute();
            for (int i = 0; i < s_deploy_1.length; i++) {
                mainMenu.deploy_1.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_1[i].toString()));
            }
            s_deploy_2 = mainMenu.basicDeployRess.get(1).getRoute();
            for (int i = 0; i < s_deploy_2.length; i++) {
                mainMenu.deploy_2.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_2[i].toString()));
            }
        }
        else if (deploySize==3) {
            s_deploy_1 = mainMenu.basicDeployRess.get(0).getRoute();
            for (int i = 0; i < s_deploy_1.length; i++) {
                mainMenu.deploy_1.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_1[i].toString()));
            }
            s_deploy_2 = mainMenu.basicDeployRess.get(1).getRoute();
            for (int i = 0; i < s_deploy_2.length; i++) {
                mainMenu.deploy_2.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_2[i].toString()));
            }
            s_deploy_3 = mainMenu.basicDeployRess.get(2).getRoute();
            for (int i = 0; i < s_deploy_3.length; i++) {
                mainMenu.deploy_3.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_3[i].toString()));
            }
        } else if (deploySize==4) {
            s_deploy_1 = mainMenu.basicDeployRess.get(0).getRoute();
            for (int i = 0; i < s_deploy_1.length; i++) {
                mainMenu.deploy_1.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_1[i].toString()));
            }
            s_deploy_2 = mainMenu.basicDeployRess.get(1).getRoute();
            for (int i = 0; i < s_deploy_2.length; i++) {
                mainMenu.deploy_2.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_2[i].toString()));
            }
            s_deploy_3 = mainMenu.basicDeployRess.get(2).getRoute();
            for (int i = 0; i < s_deploy_3.length; i++) {
                mainMenu.deploy_3.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_3[i].toString()));
            }
            s_deploy_4 = mainMenu.basicDeployRess.get(3).getRoute();
            for (int i = 0; i < s_deploy_4.length; i++) {
                mainMenu.deploy_4.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_4[i].toString()));
            }
        } else if (deploySize==5) {
            s_deploy_1 = mainMenu.basicDeployRess.get(0).getRoute();
            Log.e("数组测试", s_deploy_1[0].toString());
            Log.e("数组测试", String.valueOf(s_deploy_1.length));
            for (int i = 0; i < s_deploy_1.length; i++) {
                mainMenu.deploy_1.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_1[i].toString()));
            }
            s_deploy_2 = mainMenu.basicDeployRess.get(1).getRoute();
            Log.e("数组测试", s_deploy_2[0].toString());
            Log.e("数组测试", String.valueOf(s_deploy_2.length));
            for (int i = 0; i < s_deploy_2.length; i++) {
                mainMenu.deploy_2.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_2[i].toString()));
            }
            s_deploy_3 = mainMenu.basicDeployRess.get(2).getRoute();
            Log.e("数组测试", s_deploy_3[0].toString());
            Log.e("数组测试", String.valueOf(s_deploy_3.length));
            for (int i = 0; i < s_deploy_3.length; i++) {
                mainMenu.deploy_3.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_3[i].toString()));
            }
            s_deploy_4 = mainMenu.basicDeployRess.get(3).getRoute();
            Log.e("数组测试", s_deploy_4[0].toString());
            Log.e("数组测试", String.valueOf(s_deploy_4.length));
            for (int i = 0; i < s_deploy_4.length; i++) {
                mainMenu.deploy_4.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_4[i].toString()));
            }
            s_deploy_5 = mainMenu.basicDeployRess.get(4).getRoute();
            Log.e("数组测试", s_deploy_5[0].toString());
            Log.e("数组测试", String.valueOf(s_deploy_5.length));
            for (int i = 0; i < s_deploy_5.length; i++) {
                mainMenu.deploy_5.add(fieldInfoDao.getFieldInfoByFieldId(s_deploy_5[i].toString()));
            }
        }
    }

    private void setSelectedFieldInfoPost()//默认所有的村庄路径全选存入selectedFieldInfoPost
    {
        int length1=mainMenu.selectedFieldInfo.size();
        Log.e(TAG, "农田数量：" + length1);
        int length2 = mainMenu.fieldInfoPosts.size();
        Log.e(TAG, "路径数量：" + length2);
        for(int i=0;i<length1;i++)
        {
            String farmId=mainMenu.selectedFieldInfo.get(i).getFarm_id();
            for (int j = 0; j< length2; j++) {
                FieldInfoPost fieldInfoPost1 = mainMenu.fieldInfoPosts.get(j);
                if (fieldInfoPost1.getFarm_id().equals(farmId)) {
                    selectedFieldInfoPost.add(fieldInfoPost1);
                    allFieldInfoPost.add(fieldInfoPost1);
                    int alp = allFieldInfoPost.size();
                    int lp = selectedFieldInfoPost.size();
                    Log.e(TAG, "全部路线总数：" + alp + "：+l");
                    Log.e(TAG, "post路线总数：" + lp + "：+l");
                    break;
                }
            }
        }
    }



}