package com.njdp.njdp_drivers.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.FieldInfoDao;
import com.njdp.njdp_drivers.db.SavedFieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.BasicDeployRes;
import bean.FieldInfo;
import bean.FieldInfoPost;
import bean.SavedFiledInfo;

public class item_intelligent_resolution_2 extends Fragment implements View.OnClickListener {

    private static final String TAG = item_intelligent_resolution_2.class.getSimpleName();
    private ExpandableListView expandableListView;
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private int size;
    private int deploySize;
    private int list_tag=1;//不同农田信息集合对应的标志，1，2，3，4，5
    private ExpandableListAdapter adapter;
    private int EXPAND_TAG=-1;//groupView展开隐藏的标志，默认-1隐藏,打开时等于对应的groupPosition
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private Gson gson;
    private String token;
    private String url;//服务器地址
    private String plan_id;
    private SavedFieldInfoDao saveFieldInfoDao;
    private List<FieldInfo> deployFieldInfo=new ArrayList<FieldInfo>();//选择的调配方案对应的农田信息

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_1_intelligent_resolution_2, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        deploySize=mainMenu.basicDeployRess.size();
        Log.e(TAG, "总方案数：" + deploySize);

        sessionManager=new SessionManager(getActivity());
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        token=sessionManager.getToken();
        url= AppConfig.URL_UPLOADDEPLOY;

        saveFieldInfoDao=new SavedFieldInfoDao(getActivity());
        expandableListView = (ExpandableListView)view.findViewById(R.id.intelligent_deploy_expand);
        adapter=new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return deploySize;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return getCropLandCount(groupPosition);
            }

            @Override
            public Object getGroup(int groupPosition) {
                return mainMenu.basicDeployRess.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return  getFieldInfo(groupPosition).get(childPosition);
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
                BasicDeployRes basicDeployRes = mainMenu.basicDeployRess.get(groupPosition);
                if (convertView == null) {
                    convertView = LayoutInflater.from(mainMenu).inflate(R.layout.expandablelistview_deploy_parent, null);
                }

                TextView tv1=(TextView)convertView.findViewById(R.id.deploy_id);
                tv1.setText("方案" + String.valueOf(groupPosition + 1));
                TextView tv4=(TextView)convertView.findViewById(R.id.earnings);
                tv4.setText("估计收益：" + basicDeployRes.getEarnings()+"元");
                TextView tv5=(TextView)convertView.findViewById(R.id.cost);
                tv5.setText("估计成本：" + basicDeployRes.getCost()+"元");
                TextView tv6=(TextView)convertView.findViewById(R.id.timeFrame);
                tv6.setTextSize(16f);
                setDate(tv6, groupPosition);

                LinearLayout text_layout=(LinearLayout)convertView.findViewById(R.id.text_Layout);
                Button deploy_selecte=(Button)convertView.findViewById(R.id.deploy_selected);//选择方案按钮
                class textLayoutListener implements View.OnClickListener//expand区域监听
                {
                    @Override
                    public void onClick(View v) {
                        if (!isExpanded && (EXPAND_TAG == -1)) {
                            expandableListView.expandGroup(groupPosition);
                            expandableListView.setSelectedGroup(groupPosition);// 设置被选中的group置于顶端
                            EXPAND_TAG = groupPosition;
                        } else if (isExpanded && (EXPAND_TAG == groupPosition)) {
                            expandableListView.collapseGroup(EXPAND_TAG);
                            EXPAND_TAG = -1;
                        } else {
                            expandableListView.collapseGroup(EXPAND_TAG);
                            expandableListView.expandGroup(groupPosition);// 展开被选的group
                            expandableListView.setSelectedGroup(groupPosition);// 设置被选中的group置于顶端
                            EXPAND_TAG = groupPosition;
                        }
                    }
                }
                class deploySelecteListener implements View.OnClickListener//跳转到线路导航界面监听
                {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Get Navigation！");
                        mainMenu.deploy_tag=groupPosition+1;
                        //根据选择
                        try {
                            switch (groupPosition) {
                                case 0:
                                    deployFieldInfo.addAll(mainMenu.deploy_1);
                                    break;
                                case 1:
                                    deployFieldInfo.addAll(mainMenu.deploy_2);
                                    break;
                                case 2:
                                    deployFieldInfo.addAll(mainMenu.deploy_3);
                                    break;
                                case 3:
                                    deployFieldInfo.addAll(mainMenu.deploy_4);
                                    break;
                                case 4:
                                    deployFieldInfo.addAll(mainMenu.deploy_5);
                                    break;
                            }

                            //////////////////////////////////Log测试
                            Log.e(TAG, "一共" + String.valueOf(deployFieldInfo.size()) + "个地方！");
                            //////////////////////////////////Log测试

                            //////////////////////////////////存储选择的导航方案////////////////////////////
                            if(saveFieldInfoDao.countOfField()>0)
                            {
                                //本地库里是否存在农田信息，存在则删除
                                List<SavedFiledInfo> testFieldInfos=saveFieldInfoDao.allFieldInfo();
                                for (int i = 0; i < testFieldInfos.size(); i++) {
                                    SavedFiledInfo savedFiledInfo = testFieldInfos.get(i);
                                    saveFieldInfoDao.delete(savedFiledInfo);
                                    Log.e(TAG, "删除" +savedFiledInfo.getFarm_id()+ ":第"+String.valueOf(savedFiledInfo.getId()) + "条");
                                }
                            }
                            for (int i=0;i<deployFieldInfo.size();i++)
                            {
                                SavedFiledInfo savedFiledInfo=SavedFiledInfo.getSavedFiledInfo(deployFieldInfo.get(i));
                                savedFiledInfo.setId(i + 1);
                                saveFieldInfoDao.add(savedFiledInfo);//按顺序存入SqlLiter数据库
                                Log.e(TAG, "增加" + savedFiledInfo.getFarm_id() + ":第" + String.valueOf(savedFiledInfo.getId()) + "条");
                            }
                            deployFieldInfo.clear();
                            //////////////////////////////////存储选择的导航方案////////////////////////////

//                            mainMenu.addBackFragment(new item_intelligent_resolution_3());//跳转
                            /////////////////服务器测试需要改为注释的内容///////////////////////////////////////////
                            plan_id=mainMenu.basicDeployRess.get(groupPosition).getId();
                            Log.e(TAG,"方案ID："+plan_id);
                            uploadDeploy();
                            mainMenu.addBackFragment(new item_intelligent_resolution_3());
                            /////////////////服务器测试需要改为注释的内容///////////////////////////////////////////

                        }catch (Exception e)
                        {
                            Log.e(TAG, "导航失败，错误：" + e.toString() );
                            commonUtil.error_hint("请重新选择");
                        }
                    }

                }
                text_layout.setOnClickListener(new textLayoutListener());//点击expand区域监听
                deploy_selecte.setOnClickListener(new deploySelecteListener());//上传数据，跳转到线路导航界面监听

                return convertView;

            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mainMenu).inflate(R.layout.expandablelistview_deploy_child, null);
                }
                List<FieldInfo> fieldInfoList=getFieldInfo(groupPosition);
                FieldInfo fieldInfo=fieldInfoList.get(childPosition);
                if(fieldInfo!=null) {
                    TextView tv1 = (TextView) convertView.findViewById(R.id.name);
                    tv1.setText(fieldInfo.getName());
                    TextView tv2 = (TextView) convertView.findViewById(R.id.telephone);
                    tv2.setText("电话："+fieldInfo.getUser_name());
                    TextView tv3 = (TextView) convertView.findViewById(R.id.fieldNumber);
                    tv3.setText(fieldInfo.getArea_num()+"亩");
                    TextView tv4 = (TextView) convertView.findViewById(R.id.univalenc);
                    tv4.setText("单价：" + fieldInfo.getUnit_price()+"元/亩");
                    TextView tv5 = (TextView) convertView.findViewById(R.id.site);
                    tv5.setText("地址：" + fieldInfo.getCropLand_site());
                    TextView tv6 = (TextView) convertView.findViewById(R.id.cropLand_id);
                    tv6.setText("农田" + String.valueOf(childPosition + 1));
                }else{
                    Log.e(TAG,"error：方案显示错误!");
                }

                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {

                return true;
            }

            @Override
            public void onGroupExpanded(int groupPosition) {
                super.onGroupExpanded(groupPosition);
            }
        };
        expandableListView.setAdapter(adapter);

        return  view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
    }

    //根据groupPosition获取各个方案对应的农田Info集合
    private List<FieldInfo> getFieldInfo(int groupPosition)
    {
        if(groupPosition==0){
            Log.e(TAG,String.valueOf(mainMenu.deploy_1.size()));
            return mainMenu.deploy_1;
        }else if(groupPosition==1){
            return mainMenu.deploy_2;
        }else if(groupPosition==2){
            return mainMenu.deploy_3;
        }else if(groupPosition==3){
            return mainMenu.deploy_4;
        } else if(groupPosition==4){
            return mainMenu.deploy_5;
        }else {
            return null;
        }
    }

    //根据groupPosition获取各个方案对应的农田Info集合的农田数
    private int getCropLandCount(int groupPosition)
    {
        int childrenCount;
        switch (groupPosition)
        {
            case 0:
                childrenCount= mainMenu.deploy_1.size();
                return childrenCount;
            case 1:
                childrenCount= mainMenu.deploy_2.size();
                return childrenCount;
            case 2:
                childrenCount= mainMenu.deploy_3.size();
                return childrenCount;
            case 3:
                childrenCount= mainMenu.deploy_4.size();
                return childrenCount;
            case 4:
                childrenCount= mainMenu.deploy_5.size();
                return childrenCount;
        }
        return 0;
    }

    //根据groupPosition设置各个方案对应的作业起止日期
    private void setDate(TextView textView,int groupPosition){
        try {
            switch (groupPosition) {
                case 0:
                    textView.setText("作业时段：" + mainMenu.deploy_1.get(0).getStart_time() + "至"
                            + mainMenu.deploy_1.get(mainMenu.deploy_1.size() - 1).getEnd_time());
                    break;
                case 1:
                    textView.setText("作业时段：" + mainMenu.deploy_2.get(0).getStart_time() + "至"
                            + mainMenu.deploy_2.get(mainMenu.deploy_2.size() - 1).getEnd_time());
                    break;
                case 2:
                    textView.setText("作业时段：" + mainMenu.deploy_3.get(0).getStart_time() + "至"
                            + mainMenu.deploy_3.get(mainMenu.deploy_3.size() - 1).getEnd_time());
                    break;
                case 3:
                    textView.setText("作业时段：" + mainMenu.deploy_4.get(0).getStart_time() + "至"
                            + mainMenu.deploy_4.get(mainMenu.deploy_4.size() - 1).getEnd_time());
                    break;
                case 4:
                    textView.setText("作业时段：" + mainMenu.deploy_5.get(0).getStart_time() + "至"
                            + mainMenu.deploy_5.get(mainMenu.deploy_5.size() - 1).getEnd_time());
                    break;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
    }

    ////////////////////////////上传选择方案/////////////////////////////////////
    private void uploadDeploy()
    {
        String tag_string_req = "req_init";

        mainMenu.pDialog.setMessage("正在准备导航，请等待 ...");
        mainMenu.showDialog();

        if(!netUtil.checkNet(mainMenu)){
            commonUtil.error_hint("网络连接错误");
            mainMenu.hideDialog();
        }else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.PUT,
                    url, initSuccessListener,mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("plan_id", plan_id);

                    return netUtil.checkParams(params);
                }
            };

            strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    }

    //上传选择方案响应服务器成功
    private Response.Listener<String> initSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            mainMenu.hideDialog();
            try {

                Log.e(TAG, "AreaInit Response: " + response);
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
                    String msg=jObj.getString("result");
                    Log.e(TAG, msg);
                    commonUtil.error_hint("上传选择方案成功");

                } else {

                    String errorMsg = jObj.getString("error_msg");
                    Log.e(TAG, "服务器数据错误1：Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("服务器数据错误1"+errorMsg);
                }
            } catch (JSONException e) {
                // JSON error
                Log.e(TAG, "服务器数据错误2：" + e.getMessage());
                commonUtil.error_hint("服务器数据错误1" + e.getMessage());
            }
        }
    };
    ////////////////////////////上传选择方案///////////////////////////////////

}
