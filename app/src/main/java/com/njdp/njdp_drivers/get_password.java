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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.njdp.njdp_drivers.changeDefault.TimeCount;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

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
    private static final String TAG = register.class.getSimpleName();
    private ProgressDialog pDialog;
    private CommonUtil commonUtil ;
    private NetUtil netUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);

        verification_code_Validation.addValidation(get_password.this, R.id.user_telephone, "^1[3-9]\\d{9}+$", R.string.err_phone);

        this.getPassword_next =(com.beardedhen.androidbootstrap.BootstrapButton) super.findViewById(R.id.btn_getPassword_next);
        this. btn_verification_code=(Button) super.findViewById(R.id.btn_get_verificationCode);
        this.text_telephone =(EditText) super.findViewById(R.id.user_telephone);
        this.text_user_license_plater =(EditText) super.findViewById(R.id.user_username);
        this.text_VerificationCcode =(EditText) super.findViewById(R.id.user_VerifyCode);

        getPassword_next.setEnabled(false);
        getPassword_next.setClickable(false);

        editTextIsNull();

        commonUtil =new CommonUtil(get_password.this);
        netUtil=new NetUtil(get_password.this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //点击获取验证码按钮,，没填手机号提示，填了以后，发送短信，按钮60s倒计时，禁用60s
        btn_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonUtil.isempty(text_telephone)) {
                    commonUtil.error_hint("手机号不能为空");
                } else if (verification_code_Validation.validate() ==true) {
                    String username= text_telephone.getText().toString().trim();
                    String telephone= text_user_license_plater.getText().toString().trim();
                    //按钮60s倒计时，禁用60s
                    TimeCount time_CountDown = new TimeCount(get_password.this, 60000, 1000, btn_verification_code);
                    time_CountDown.start();
                    empty_hint(R.string.vertify_hint);
                }
            }
        });

    }

    //获取验证码
    private void get_VerifyCode(){

        String tag_string_req = "req_register_driver_VerifyCode";

        if(netUtil.checkNet(get_password.this) == false) {
            commonUtil.error_hint( "网络连接错误");
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.URL_REGISTER, vertifySuccessListener, mErrorListener) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("license_plater", text_user_license_plater.getText().toString().trim());
                    params.put("telephone", text_telephone.getText().toString().trim());
                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    //响应服务器失败
    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Register Error: " + error.getMessage());
            commonUtil.error_hint( "服务器连接失败");
            hideDialog();
        }
    };

    //验证码响应服务器成功
    private Response.Listener<String> vertifySuccessListener =new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d(TAG, "Register Response: " + response.toString());
            hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    //服务器返回的验证码
                    verify_code=jObj.getString("verify_code");
                } else {
                    // Error occurred in registration. Get the error
                    String errorMsg = jObj.getString("error_msg");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint( errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                empty_hint(R.string.vertify_error2);
            }

        }
    };

    //跳转到设置新密码页面
    public void getPassword_next(View v){
        String t_verify_code= text_VerificationCcode.getText().toString().trim();
        if((verify_code!=null)&&(verify_code!=""))
        {
            if (verify_code.equals(t_verify_code)) {
                Intent intent = new Intent(get_password.this, get_password2.class);
                Bundle get_driverr_bundle = new Bundle();
                get_driverr_bundle.putString("license_plater", text_user_license_plater.getText().toString());
                get_driverr_bundle.putString("telephone", text_telephone.getText().toString());
                get_driverr_bundle.putBoolean("isDriver", true);
                intent.putExtra("driver_access", get_driverr_bundle);
                startActivity(intent);
            } else {
                commonUtil.error_hint( "验证码错误！");
            }
        }else
        {
            commonUtil.error_hint("请验证手机号，获取验证码！");
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
                if ((s.length() > 0) && !TextUtils.isEmpty(text_user_license_plater.getText()) && !TextUtils.isEmpty(text_VerificationCcode.getText())) {
                    getPassword_next.setClickable(true);
                    getPassword_next.setEnabled(true);
                } else {
                    getPassword_next.setEnabled(false);
                    getPassword_next.setClickable(false);
                }
            }
        });

        text_user_license_plater.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_telephone.getText()) && !TextUtils.isEmpty(text_VerificationCcode.getText())) {
                    getPassword_next.setClickable(true);
                    getPassword_next.setEnabled(true);
                } else {
                    getPassword_next.setEnabled(false);
                    getPassword_next.setClickable(false);
                }
            }
        });

        text_VerificationCcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_telephone.getText()) && !TextUtils.isEmpty(text_user_license_plater.getText())) {
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

    //信息未输入提示
    private void empty_hint(int in){
        Toast toast = Toast.makeText(get_password.this, getResources().getString(in), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

}