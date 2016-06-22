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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.Gson;
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

import static com.njdp.njdp_drivers.util.NetUtil.TAG;

public class item_personalinformation_1_fix_info extends Fragment implements View.OnClickListener {
    private slidingMenu mainMenu;
    private DrawerLayout menu;
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
                } else {
                    btn_fix_save.setClickable(false);
                    btn_fix_save.setEnabled(false);
                }
            }
        });//监听输入内容，判断是否禁用保存按钮
        view.findViewById(R.id.fix_getback).setOnClickListener(this);
        view.findViewById(R.id.fix_save_change).setOnClickListener(this);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;

        sessionManager=new SessionManager(getActivity());
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
                fix_params.put("token", token);
                switch (mainMenu.fix_info_flag)
                {
                    case 1:
                        fix_params.put("person_name", mainMenu.fix_info);
                        Log.e(TAG,"name:"+fix_params.get("person_name"));
                        break;
                    case 3:
                        fix_params.put("person_weixin", mainMenu.fix_info);
                        Log.e(TAG, "weixin:" +fix_params.get("person_weixin"));
                        break;
                    case 4:
                        fix_params.put("person_qq:", mainMenu.fix_info);
                        Log.e(TAG, "qq:" + fix_params.get("person_qq"));
                        break;
                }
                check_fix_info();
                break;
            case R.id.fix_getback:
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
        }
    }

    private void check_fix_info()//检测输入的信息
    {
        if(commonUtil.isempty(edt_fix_input)){
            commonUtil.error_hint("请输入完整的信息");
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
            commonUtil.error_hint("网络连接错误");
            return;
        } else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_FIXPERSONINFO, new fixSuccessListener(), mainMenu.mErrorListener) {

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

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                } else if(status==0){

                    String errorMsg=jObj.getString("result");
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
                    }
                    driverDao.update(driver);
                    mainMenu.getSupportFragmentManager().popBackStack();
                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("保存失败,请重试");
                }
            } catch (JSONException e) {
                Log.e(TAG, "2 Json error：response错误" + e.getMessage());
                commonUtil.error_hint("保存失败,请重试" );
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
