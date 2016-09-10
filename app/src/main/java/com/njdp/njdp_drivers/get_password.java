package com.njdp.njdp_drivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.njdp.njdp_drivers.changeDefault.TimeCount;
import com.njdp.njdp_drivers.db.AppConfig;
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

public class get_password extends AppCompatActivity {

    private String verify_code;
    private com.beardedhen.androidbootstrap.BootstrapButton getPassword_next =null;
    private AwesomeValidation verification_code_Validation=new AwesomeValidation(ValidationStyle.BASIC);
    private Button btn_verification_code=null;
    private EditText text_telephone =null;
    private EditText text_user_license_plater =null;
    private EditText text_VerificationCcode =null;
    private ImageButton btn_back=null;
    private static final String TAG = register.class.getSimpleName();
    private ProgressDialog pDialog;
    private CommonUtil commonUtil ;
    private NetUtil netUtil;
    private String url;
    private String url_code;
    private static int FLAG_GETPASSWORD1=11101002;
    private static int FLAG_GETVERIFYCODE=11101001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);
        url=AppConfig.URL_GETPASSWORD1;
        url_code=AppConfig.URL_GET_VERIFYCODE;

        verification_code_Validation.addValidation(get_password.this, R.id.user_telephone, "^1[3-9]\\d{9}+$", R.string.err_phone);

        this.getPassword_next =(com.beardedhen.androidbootstrap.BootstrapButton) super.findViewById(R.id.btn_getPassword_next);
        this. btn_verification_code=(Button) super.findViewById(R.id.btn_get_verificationCode);
        this.text_telephone =(EditText) super.findViewById(R.id.user_telephone);
//        this.text_user_license_plater =(EditText) super.findViewById(R.id.user_username);
        this.text_VerificationCcode =(EditText) super.findViewById(R.id.user_VerifyCode);
        this.btn_back=(ImageButton)super.findViewById(R.id.getBack);

        getPassword_next.setEnabled(false);
        getPassword_next.setClickable(false);

        editTextIsNull();

        commonUtil =new CommonUtil(get_password.this);
        netUtil=new NetUtil(get_password.this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //点击获取验证码按钮,，没填手机号提示，填了以后，发送短信，按钮60s倒计时，禁用60s
        btn_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonUtil.isempty(text_telephone)) {
                    commonUtil.error_hint_short("手机号不能为空");
                } else if (verification_code_Validation.validate() ==true) {
//                    String username= text_telephone.getText().toString().trim();
                    String telephone= text_telephone.getText().toString().trim();
                    get_VerifyCode(telephone);
                    //按钮60s倒计时，禁用60s
                    TimeCount time_CountDown = new TimeCount(60000, 1000, btn_verification_code,get_password.this);
                    time_CountDown.start();
                }
            }
        });

        btn_back.setOnClickListener(new backClickListener());
        getPassword_next.setOnClickListener(new nextClickLIstener());

    }

    //获取验证码
    private void get_VerifyCode(String telephone){
        com.yolanda.nohttp.rest.Request<JSONObject> req_get_verifyCode= NoHttp.createJsonObjectRequest(url_code, RequestMethod.POST);
        req_get_verifyCode.add("phone",telephone);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(get_password.this) == false) {
            hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_GETVERIFYCODE, req_get_verifyCode, new verifyCode_request());
        }
    }

    //获取验证码访问服务器监听
    private class verifyCode_request implements OnResponseListener<JSONObject> {
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
                    Log.e(TAG, "发送成功：" + errorMsg);
                    commonUtil.error_hint2_long(R.string.vertify_hint);
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
            Log.e(TAG, "Login Error: " + exception.getMessage());
            commonUtil.error_hint_short("服务器连接失败");
        }

        @Override
        public void onFinish(int what) {
            hideDialog();
        }
    }


    //输入是否为空，判断是否禁用按钮
    private void editTextIsNull(){

        text_telephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_VerificationCcode.getText())) {
                    getPassword_next.setClickable(true);
                    getPassword_next.setEnabled(true);
                } else {
                    getPassword_next.setEnabled(false);
                    getPassword_next.setClickable(false);
                }
            }
        });

//        text_user_license_plater.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if ((s.length() > 0) && !TextUtils.isEmpty(text_telephone.getText()) && !TextUtils.isEmpty(text_VerificationCcode.getText())) {
//                    getPassword_next.setClickable(true);
//                    getPassword_next.setEnabled(true);
//                } else {
//                    getPassword_next.setEnabled(false);
//                    getPassword_next.setClickable(false);
//                }
//            }
//        });

        text_VerificationCcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_telephone.getText())) {
                    getPassword_next.setClickable(true);
                    getPassword_next.setEnabled(true);
                } else {
                    getPassword_next.setEnabled(false);
                    getPassword_next.setClickable(false);
                }
            }
        });

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //返回监听
    private class backClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    //下一步按钮点击监听，验证验证码跳转到第二步
    private class nextClickLIstener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (!commonUtil.isempty(text_VerificationCcode)) {
                String telephone = text_telephone.getText().toString().trim();
                String verifyCode = text_VerificationCcode.getText().toString().trim();
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", telephone);
                params.put("code", verifyCode);
                com.yolanda.nohttp.rest.Request<JSONObject> req_get_verifyCode = NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
                req_get_verifyCode.add(params);
                RequestQueue requestQueue = NoHttp.newRequestQueue();
                if (netUtil.checkNet(get_password.this) == false) {
                    hideDialog();
                    commonUtil.error_hint_short("网络连接错误");
                    return;
                } else {
                    requestQueue.add(FLAG_GETPASSWORD1, req_get_verifyCode, new verify_code_request());
                }
            } else {
                commonUtil.error_hint_short("请填写验证码！");}
        }
    }

    //验证验证码访问服务器监听
    private class verify_code_request implements OnResponseListener<JSONObject> {
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
                    Log.d(TAG, "验证码验证成功：" + errorMsg);
                    Intent intent = new Intent(get_password.this, get_password2.class);
                    Bundle get_driverr_bundle = new Bundle();
                    get_driverr_bundle.putString("telephone", text_telephone.getText().toString());
                    intent.putExtra("driver_access", get_driverr_bundle);
                    startActivity(intent);
                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误1:" + errorMsg);
                    commonUtil.error_hint_short(errorMsg);
                }
            } catch (JSONException e) {
                commonUtil.error_hint2_short(R.string.connect_error);
                e.printStackTrace();
                Log.e(TAG,"Json error：response错误2:" + e.getMessage());
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "VerifyCode Error: " + exception.getMessage());
            commonUtil.error_hint_short("服务器连接失败");
        }

        @Override
        public void onFinish(int what) {
            hideDialog();
        }
    }


}