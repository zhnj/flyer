package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okio.BufferedSource;

public class item_service_object extends Fragment implements View.OnClickListener {
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private EditText edit_telephone;
    public ProgressDialog pDialog;
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private String token;
    private String url;
    private String machine_id;
    private String telephone;
    private String TAG=item_service_object.class.getSimpleName();
    private AwesomeValidation telephoneValidation=new AwesomeValidation(ValidationStyle.BASIC);
    private RelativeLayout ll_hint;

    /////////////////////确认发布信息弹窗///////////////////////////
    private View parentView;//主View
    private View releaseView;//确认手机号view
    private PopupWindow btn_popup;
    private boolean btn_pop_flag=false;
    private TextView tv_telephone;
    private WindowManager.LayoutParams lp;
    private boolean popup_flag=false;
    /////////////////////确认发布信息弹窗///////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_6_service_object, container,
                false);
        view.findViewById(R.id.confirm).setOnClickListener(this);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.clear_hint).setOnClickListener(this);
        this.edit_telephone=(EditText)view.findViewById(R.id.server_telephone);
        this.ll_hint=(RelativeLayout)view.findViewById(R.id.hint);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        lp = mainMenu.getWindow().getAttributes();

        telephoneValidation.addValidation(edit_telephone, "^1[3-9]\\d{9}+$", "手机号格式不正确");
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);
        sessionManager=new SessionManager();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        token=sessionManager.getToken();
        url= AppConfig.URL_SERVEROBJECT;

        parentView = LayoutInflater.from(mainMenu).inflate(R.layout.activity_6_service_object, null);
        releaseView = mainMenu.getLayoutInflater().inflate(R.layout.server_object_popup, null);
        initBtnPopup();
        releaseView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        releaseView.findViewById(R.id.btn_confirm).setOnClickListener(this);
        tv_telephone=(TextView)releaseView.findViewById(R.id.tv_telephone);
        btn_popup.setOnDismissListener(new confirmPopDisListener());

        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            Log.e(TAG, machine_id);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return view;
    }

    //监听返回按键
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (popup_flag) {//关闭确认信息弹窗
                        popup_flag = false;
                        btn_popup.dismiss();
                        return true;
                    } else {
                        mainMenu.finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.clear_hint:
                ll_hint.setVisibility(View.GONE);
                break;
            case R.id.confirm:
                if(!commonUtil.isempty(edit_telephone)) {
                    if(telephoneValidation.validate()) {
                        confirmPopShow();
                    }
                }else{
                    commonUtil.error_hint_short("手机号不能为空!");
                }
                break;
            case R.id.btn_cancel:
                popup_flag=false;
                btn_popup.dismiss();
                break;
            case R.id.btn_confirm:
                btn_popup.dismiss();
                confirmServer();
            default:
                break;
        }
    }

    private void confirmServer()//确认服务对象
    {
        String tag_string_req = "req_ServerObject";
        if (!netUtil.checkNet(mainMenu)) {
            commonUtil.error_hint_short("网络连接错误");
            hideDialog();
        } else {

            Log.e(TAG, machine_id);
            Log.e(TAG, telephone);
            Log.e(TAG, token);
            pDialog.setMessage("请等待 ...");
            showDialog();
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new confirmSuccessListener(), new confirmErrorListener()) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("machine_id", machine_id);
                    params.put("token", token);
                    params.put("phone", telephone);

                    return netUtil.checkParams(params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private class confirmSuccessListener implements Response.Listener<String>//确认服务对象响应成功
    {
        @Override
        public void onResponse(String response) {
            hideDialog();
            try {
                Log.e(TAG, "Confirm: " + response);
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");
                if (status == 1) {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Token 错误:" + errorMsg);
                    commonUtil.error_hint_short("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                } else if (status == 0) {
                    String Msg = jObj.getString("result");
                    Log.e(TAG, Msg);
                    commonUtil.error_hint_short("服务对象设置成功");
                    mainMenu.finish();//设置成功后，退出界面
                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("服务器数据错误1：response错误:" + errorMsg);
                }
            } catch (JSONException e) {

                Log.e(TAG, "2 服务器数据错误：response错误:" + e.getMessage());
                commonUtil.error_hint_short("服务器数据错误2：response错误:" + e.getMessage());
            } catch (Exception e) {

                Log.e(TAG, "3 服务器数据错误：response错误:" + e.getMessage());
                commonUtil.error_hint_short("服务器数据错误3：response错误:" + e.getMessage());
            }
        }
    }

    private class confirmErrorListener implements Response.ErrorListener//响应服务器失败
    {
        @Override
        public void onErrorResponse(VolleyError error) {
            netUtil.testVolley(error);
            Log.e(TAG, "3 ConncectService Error错误!");
            Log.e("GET-ERROR", error.getMessage(), error);
            try{
                byte[] htmlBodyBytes = error.networkResponse.data;
                Log.e("GET-ERROR", new String(htmlBodyBytes), error);
            } catch (Exception e){}
            commonUtil.error_hint_short("服务器连接失败！");
            hideDialog();
        }
    };

    //控制pDialg的显示和隐藏
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void initBtnPopup()//初始化确认信息弹窗
    {
        btn_popup = new PopupWindow(releaseView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_popup.setAnimationStyle(R.style.popWindow_fade);
        btn_popup.setOutsideTouchable(true);
        btn_popup.setOutsideTouchable(true);
        btn_popup.setFocusable(true);
        btn_popup.setBackgroundDrawable(new ColorDrawable(0x55000000));
    }

    private void confirmPopShow()//confirm PopupWindow布局中的textview赋值，再弹出confirmPopupWindow
    {
        telephone=edit_telephone.getText().toString().trim();
        tv_telephone.setText(telephone);
        Log.e(TAG,telephone);
        btn_popup.showAtLocation(parentView, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        popup_flag=true;
        lp.alpha = 0.7f;
        mainMenu.getWindow().setAttributes(lp);
    }

    private class confirmPopDisListener implements PopupWindow.OnDismissListener//发布确认弹出时监听dismiss后背景变回原样
    {
        @Override
        public void onDismiss() {
            lp.alpha = 1f;
            mainMenu.getWindow().setAttributes(lp);
        }
    }

}
