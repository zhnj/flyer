package com.njdp.njdp_drivers.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.Driver;
import bean.UAVCompany;

import static com.njdp.njdp_drivers.util.NetUtil.TAG;

public class item_personalinformation_1_fix_info extends Fragment implements View.OnClickListener {
    private slidingMenu mainMenu;
    private EditText edt_fix_input;
    private TextView t_fix_hint;
    private TextView t_fix_title;
    private com.beardedhen.androidbootstrap.BootstrapButton btn_fix_save;
    private Map<String,String> fix_params=new HashMap<String,String>();
    public AwesomeValidation fixValidation=new AwesomeValidation(ValidationStyle.BASIC);
    private NetUtil netUtil;
    private CommonUtil commonUtil;
    private Gson gson;
    private SessionManager sessionManager;
    private String token;
    private DriverDao driverDao;
    private Driver driver;

    GeoCoder mSearch;
    GeoCodeResult geoCodeResult1;
    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(mainMenu,"不能根据地址获取经纬度信息，请准确输入地址！",Toast.LENGTH_LONG).show();
                return;
            }
            //获取地理编码结果
            geoCodeResult1=geoCodeResult;
            Log.e("位置位置",String.valueOf(geoCodeResult1.getLocation().latitude));
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            }
            //获取反向地理编码结果
         }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_5_personalinformation_fix_info, container,
                false);
        this.edt_fix_input =(EditText)view.findViewById(R.id.fix_input_info);
        this.t_fix_hint=(TextView)view.findViewById(R.id.fix_hint_info);
        this.t_fix_title=(TextView)view.findViewById(R.id.fix_title);
        this.btn_fix_save=(com.beardedhen.androidbootstrap.BootstrapButton)view.findViewById(R.id.fix_save_change);
        btn_fix_save.setClickable(false);
        btn_fix_save.setEnabled(false);
        edt_fix_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(edt_fix_input.getText())) {
                    btn_fix_save.setClickable(true);
                    btn_fix_save.setEnabled(true);
                    if(mainMenu.fix_info_flag==7)
                    {
                        mSearch.geocode(new GeoCodeOption().city("北京").address(s.toString()));
                    }
                } else {
                    btn_fix_save.setClickable(false);
                    btn_fix_save.setEnabled(false);
                }
            }
        });//监听输入内容，判断是否禁用保存按钮
        view.findViewById(R.id.fix_getback).setOnClickListener(this);
        view.findViewById(R.id.fix_save_change).setOnClickListener(this);

        mainMenu=(slidingMenu)getActivity();

        sessionManager=new SessionManager();
        token=sessionManager.getToken();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();

        t_fix_hint.setText(mainMenu.t_fix_hint);
        t_fix_title.setText(mainMenu.t_fix_title);

        try{
            fixValidation.clear();
        }catch (Exception e){}
        switch (mainMenu.fix_info_flag)
        {
            case 1:
                fixValidation.addValidation(edt_fix_input, "^[\\u4e00-\\u9fa5]+$", getResources().getString(R.string.err_name));
                break;
            case 3:
                fixValidation.addValidation(edt_fix_input,"^[a-zA-Z\\d_]{5,}$",getResources().getString(R.string.err_weixin));
                break;
            case 4:
                fixValidation.addValidation(edt_fix_input, "[1-9][0-9]{4,14}", getResources().getString(R.string.err_qq));
                break;
            case 5:
                fixValidation.addValidation(edt_fix_input, "^[男女]$", getResources().getString(R.string.err_sex));
                break;
            case 6:
                fixValidation.addValidation(edt_fix_input, "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)", getResources().getString(R.string.err_sfzh));
                break;
            case 7:
                //点击选择地址
                //text_user_address.setOnClickListener(new select_site_clickListener());
                mSearch= GeoCoder.newInstance();
                mSearch.setOnGetGeoCodeResultListener(listener);
                break;
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fix_save_change:
                mainMenu.fix_info=edt_fix_input.getText().toString().trim();
                try {
                    fix_params.clear();
                }catch (Exception e){}
                UAVCompany com = new UAVCompany();
                com.setFm_id(sessionManager.getUserId());
                Gson gson2=new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

               // fix_params.put("fm_id",sessionManager.getUserId());
                switch (mainMenu.fix_info_flag)
                {
                    case 1:
                        com.setCom_name(mainMenu.fix_info);
                        break;
                    case 3:

                        com.setPersion_weixin(mainMenu.fix_info);
                        break;
                    case 4:
                       // fix_params.put("person_qq", mainMenu.fix_info);
                       com.setPerson_qq(mainMenu.fix_info);
                        break;
                    case 5:
                        //fix_params.put("staff_num", mainMenu.fix_info);
                       com.setStaff_num(mainMenu.fix_info);
                        break;
                    case 6:
                        //fix_params.put("person_sfzh", mainMenu.fix_info);
                        com.setPerson_sfzh( mainMenu.fix_info);
                        break;
                    case 7:
                        //fix_params.put("com_addr", mainMenu.fix_info);
                        //fix_params.put("com_latitude",String.valueOf(geoCodeResult1.getLocation().latitude));
                        //fix_params.put("com_longitude","(String.valueOf(geoCodeResult1.getLocation().longitude)");

                       com.setCom_addr( mainMenu.fix_info);
                        com.setCom_latitude(String.valueOf(geoCodeResult1.getLocation().latitude));
                        com.setCom_longitude(String.valueOf(geoCodeResult1.getLocation().longitude));
                        break;

                }
                //check_fix_info();
                String obj2=gson2.toJson(com);
                fix_params.put("comJson", obj2);
                Log.e(TAG,"公司名称:"+fix_params.get("comJson"));
                uploadInfo();
                break;
            case R.id.fix_getback:
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
        }
    }

    private void check_fix_info()//检测输入的信息
    {
        if(commonUtil.isempty(edt_fix_input)){
            commonUtil.error_hint_short("请输入完整的信息");
        }else {
            if (fixValidation.validate()) {
                Log.e(TAG, "检测通过："+mainMenu.fix_info);
                uploadInfo();
            }
        }
    }

    private void uploadInfo()//上传修改用户信息
    {
        Log.e(TAG,"token"+fix_params.get("token"));
        String tag_string_req = "req_fix_info";
        if (netUtil.checkNet(mainMenu) == false) {
            mainMenu.hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_UAV_MODIFY_COM, new fixSuccessListener(), mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {
                    Log.e(TAG,gson.toJson(netUtil.checkParams(fix_params)));

                    return netUtil.checkParams(fix_params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private class fixSuccessListener implements  Response.Listener<String>//信息修改成功响应
    {
        @Override
        public void onResponse(String response) {
            try {
                Log.e("PersonalInfo fix_back",response);
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");

                if (status==1) {

                    String errorMsg = jObj.getString("msg");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                } else if(status==0){

                    String errorMsg=jObj.getString("msg");
                    Log.e(TAG, "修改结果：" + errorMsg);
                    mainMenu.change_info_flag=true;
                    driverDao=new DriverDao(mainMenu);
                    driver=driverDao.getDriver(1);
                    switch (mainMenu.fix_info_flag)
                    {
                        case 1:
                            driver.setName(mainMenu.fix_info);
                            break;
                        case 3:
                            driver.setWechart(mainMenu.fix_info);
                            break;
                        case 4:
                            driver.setQQ(mainMenu.fix_info);
                            break;
                        case 5:
                            driver.setSex(mainMenu.fix_info);
                            break;
                        case 6:
                            driver.setSfzh(mainMenu.fix_info);
                            break;
                        case 7:
                            //driver.setPersoncomid(mainMenu.fix_info);
                            break;

                    }
                    driverDao.update(driver);
                    mainMenu.getSupportFragmentManager().popBackStack();
                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("保存失败,请重试");
                }
            } catch (JSONException e) {
                Log.e(TAG, "2 Json error：response错误" + e.getMessage());
                commonUtil.error_hint_short("保存失败,请重试" );
            }
        }
    }

    private void postParamsClear()
    {
        fix_params.put("person_name", mainMenu.fix_info);
        fix_params.put("person_name", mainMenu.fix_info);
        fix_params.put("person_name", mainMenu.fix_info);
        fix_params.put("person_name", mainMenu.fix_info);
        fix_params.put("person_name", mainMenu.fix_info);
        fix_params.put("person_name", mainMenu.fix_info);
        fix_params.put("person_name", mainMenu.fix_info);
    }
}