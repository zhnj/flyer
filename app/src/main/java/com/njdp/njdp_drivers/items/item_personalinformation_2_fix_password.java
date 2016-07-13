package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class item_personalinformation_2_fix_password extends Fragment implements View.OnClickListener {
    private slidingMenu mainMenu;
    public AwesomeValidation fixValidation=new AwesomeValidation(ValidationStyle.BASIC);
    private ProgressDialog pDialog;
    private NetUtil netUtil;
    private CommonUtil commonUtil;
    private Gson gson;
    private SessionManager sessionManager;
    private String token;
    private EditText oldPwd_edit;
    private EditText newPwd_edit;
    private EditText newPwd2_edit;
    private com.beardedhen.androidbootstrap.BootstrapButton btn_fixPwd;
    private static int FLAG_FIXPWD=11101004 ;
    private String url;
    private String TAG=item_personalinformation_2_fix_password.class.getSimpleName();
    private ImageButton password_reveal;
    private ImageButton password_show;
    private ImageButton password_reveal2;
    private ImageButton password_show2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_5_personalinformation_fix_password, container,
                false);

        mainMenu=(slidingMenu)getActivity();

        sessionManager=new SessionManager();
        token=sessionManager.getToken();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);
        url= AppConfig.URL_FIXPASSWORD;

        oldPwd_edit=(EditText)view.findViewById(R.id.user_old_password);
        newPwd_edit=(EditText)view.findViewById(R.id.user_new_password);
        newPwd2_edit=(EditText)view.findViewById(R.id.user_new_password2);
        btn_fixPwd=(com.beardedhen.androidbootstrap.BootstrapButton)view.findViewById(R.id.finish);
        password_reveal=(ImageButton)view.findViewById(R.id.reveal_button);
        password_show=(ImageButton)view.findViewById(R.id.show_button);
        password_reveal2=(ImageButton)view.findViewById(R.id.reveal_button2);
        password_show2=(ImageButton)view.findViewById(R.id.show_button2);

        try{
            fixValidation.clear();
        }catch (Exception e){}
        fixValidation.addValidation(oldPwd_edit, "^[A-Za-z0-9_]{5,15}+$", getResources().getString(R.string.err_password));
        fixValidation.addValidation(newPwd_edit, "^[A-Za-z0-9_]{5,15}+$", getResources().getString(R.string.err_password));
        fixValidation.addValidation(newPwd2_edit, "^[A-Za-z0-9_]{5,15}+$", getResources().getString(R.string.err_password));

        btn_fixPwd.setOnClickListener(this);
        view.findViewById(R.id.fix_getback).setOnClickListener(this);
        view.findViewById(R.id.show_button).setOnClickListener(this);
        view.findViewById(R.id.reveal_button).setOnClickListener(this);
        view.findViewById(R.id.show_button2).setOnClickListener(this);
        view.findViewById(R.id.reveal_button2).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fix_getback:
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
            case R.id.reveal_button:
                revealClick();
                break;
            case R.id.show_button:
                showClick();
                break;
            case R.id.reveal_button2:
                revealClick2();
                break;
            case R.id.show_button2:
                showClick2();
                break;
            case R.id.finish:
                if (commonUtil.isempty(oldPwd_edit)) {
                    commonUtil.error_hint_short("请输入旧的密码");
                } else if (commonUtil.isempty(newPwd_edit)) {
                    commonUtil.error_hint_short("请输入新的密码");
                }else if (commonUtil.isempty(newPwd2_edit)) {
                    commonUtil.error_hint_short("请再次输入新的密码");
                }else if (!newPwd_edit.getText().toString().trim().equals(newPwd2_edit.getText().toString().trim())){
                    commonUtil.error_hint_long("两次输入的新密码不一致");
                }else if (fixValidation.validate() == true) {
                    String oldPassword=oldPwd_edit.getText().toString().trim();
                    String nPassword = newPwd2_edit.getText().toString().trim();
                    fixPassword(oldPassword,nPassword, token);
                }
                break;
        }
    }

    //修改密码
    private void fixPassword(final String oldPwd,final String newPwd,final String token)
    {
        pDialog.setMessage("正在修改，请等待.......");
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldPassword", oldPwd);
        params.put("password", newPwd);
        params.put("token", token);
        com.yolanda.nohttp.rest.Request<JSONObject> req_get_verifyCode = NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
        req_get_verifyCode.add(params);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(mainMenu) == false) {
            hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_FIXPWD, req_get_verifyCode, new fix_password_request());
        }
    }

    //修改密码访问服务器监听
    private class fix_password_request implements OnResponseListener<JSONObject> {
        @Override
        public void onStart(int what) {
            showDialog();
        }

        @Override
        public void onSucceed(int what, com.yolanda.nohttp.rest.Response<JSONObject> response) {

            try {
                JSONObject jObj=response.get();
                int status = jObj.getInt("status");

                if (status==0) {
                    String errorMsg=jObj.getString("result");
                    Log.e(TAG, "修改密码成功：" + errorMsg);
                    commonUtil.error_hint_short(errorMsg);
                    mainMenu.getSupportFragmentManager().popBackStack();
                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误1:" + errorMsg);
                    commonUtil.error_hint_short(errorMsg);
                }
            } catch (JSONException e) {
                commonUtil.error_hint2_short(R.string.connect_error);
                // JSON error
                e.printStackTrace();
                Log.e(TAG,"Json error：response错误2" + e.getMessage());
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "Fix Password Error: " + exception.getMessage());
            commonUtil.error_hint_short("服务器连接失败");
        }

        @Override
        public void onFinish(int what) {
            hideDialog();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //点击眼睛1按钮，设置密码显示或者隐藏
    public void showClick() {
        password_reveal.setVisibility(View.VISIBLE);
        password_show.setVisibility(View.GONE);
        item_personalinformation_2_fix_password.this.newPwd_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    public void revealClick() {
        password_reveal.setVisibility(View.GONE);
        password_show.setVisibility(View.VISIBLE);
        item_personalinformation_2_fix_password.this.newPwd_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }
    //点击眼睛2按钮，设置密码显示或者隐藏
    public void showClick2() {
        password_reveal2.setVisibility(View.VISIBLE);
        password_show2.setVisibility(View.GONE);
        item_personalinformation_2_fix_password.this.newPwd2_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    public void revealClick2() {
        password_reveal2.setVisibility(View.GONE);
        password_show2.setVisibility(View.VISIBLE);
        item_personalinformation_2_fix_password.this.newPwd2_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }
}
