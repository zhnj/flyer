package com.njdp.njdp_drivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.njdp.njdp_drivers.db.SQLiteHandler;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends Activity {
    private EditText text_user_name=null;
    private EditText text_user_telephone=null;
    private EditText text_user_machine_id=null;
    private EditText text_verification_code=null;
    private EditText text_user_password=null;
    private TextView text_user_address=null;
    private Button btn_verification_code=null;
    private ImageButton btn_back=null;
    private com.beardedhen.androidbootstrap.BootstrapButton btn_register_next=null;
    private AwesomeValidation verification_code_Validation=new AwesomeValidation(ValidationStyle.BASIC);
    private AwesomeValidation mValidation=new AwesomeValidation(ValidationStyle.BASIC);
    private static final String TAG = register.class.getSimpleName();
    private ProgressDialog pDialog;
    private NetUtil netUtil;
    private SessionManager session;
    private String name;
    private String telephone;
    private String machine_id;
    private String password;
    private String address;
    private String t_verify_code;
    private String verify_code;
    private int address_select_flag=0;
    private int CODE_SELECT_SITE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.btn_verification_code=(Button) super.findViewById(R.id.register_get_verification_code);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        netUtil=new NetUtil(register.this);
        // Session manager
        session = new SessionManager(getApplicationContext());

        text_user_name = (EditText) super.findViewById(R.id.user_name);
        text_user_telephone = (EditText) super.findViewById(R.id.user_telephone);
        text_user_machine_id= (EditText) super.findViewById(R.id.user_machine_id);
        text_verification_code = (EditText) super.findViewById(R.id.verification_code);
        text_user_password = (EditText) super.findViewById(R.id.user_password);
        text_user_address=(TextView) super.findViewById(R.id.user_site);
        btn_register_next=(com.beardedhen.androidbootstrap.BootstrapButton) super.findViewById(R.id.register_next);
        btn_back=(ImageButton) super.findViewById(R.id.getback);
        btn_back.setOnClickListener(new backClickListener());
        btn_register_next.setEnabled(false);
        btn_register_next.setClickable(false);

        editTextIsNull();

        form_verification(register.this);
        verification_code_Validation.addValidation(register.this, R.id.user_telephone, "^1[3-9]\\d{9}+$", R.string.err_phone);

        //点击按钮时验证注册表单
        findViewById(R.id.register_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isempty(R.id.user_telephone)) {
                    empty_hint(R.string.err_phone2);
                } else if (isempty(R.id.verification_code)) {
                    empty_hint(R.string.err_verification_code2);
                } else if (mValidation.validate() == true) {
                    name = text_user_name.getText().toString().trim();
                    telephone = text_user_telephone.getText().toString().trim();
                    machine_id = text_user_machine_id.getText().toString().trim();
                    password = text_user_password.getText().toString().trim();
                    t_verify_code = text_verification_code.getText().toString().trim();
                    if (verify_code.equals(t_verify_code)) {
                        register_next();
                    } else {
                        error_hint("验证码错误！");
                    }
                }
            }
        });

        //点击获取验证码按钮,，没填手机号提示，填了以后，发送短信，按钮60s倒计时，禁用60s
        btn_verification_code.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isempty(R.id.user_telephone)) {
                    empty_hint(R.string.err_phone2);
                } else if(verification_code_Validation.validate()==true){
                    get_VerifyCode();
                    //按钮60s倒计时，禁用60s
                    TimeCount time_CountDown = new TimeCount(register.this,60000,1000,btn_verification_code);
                    time_CountDown.start();
                    empty_hint(R.string.vertify_hint);
                }
            }
        });

        //点击选择地址
        text_user_address.setOnClickListener(new select_site_clickListener());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_SELECT_SITE&&resultCode==RESULT_OK){
            String select_result=data.getStringExtra("select_site");
            if(select_result.equals("1")) {
                address_select_flag = 1;
                text_user_address.setText(select_result);
                if (!TextUtils.isEmpty(text_user_name.getText()) &&!TextUtils.isEmpty(text_user_telephone.getText()) && !TextUtils.isEmpty(text_user_password.getText())
                        && !TextUtils.isEmpty(text_verification_code.getText()) && !TextUtils.isEmpty(text_user_machine_id.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }else
            {
                address_select_flag=0;
                btn_register_next.setEnabled(false);
                btn_register_next.setClickable(false);
            }
        }else {
            address_select_flag=0;
            btn_register_next.setEnabled(false);
            btn_register_next.setClickable(false);
        }
    }

    //EditText输入是否为空
    private boolean isempty(int id){
        EditText editText=(EditText) super.findViewById(id);
        boolean bl= TextUtils.isEmpty(editText.getText());
        return bl;
    }

    //信息未输入提示
    private void empty_hint(int in){
        Toast toast = Toast.makeText(register.this, getResources().getString(in), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,-50);
        toast.show();
    }

    //错误信息提示
    private void error_hint(String str){
        Toast toast = Toast.makeText(register.this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,-50);
        toast.show();
    }

    //获取验证码
    private void get_VerifyCode(){

        String tag_string_req = "req_register_driver_VerifyCode";

        if(netUtil.checkNet(register.this)==false){
            error_hint("网络连接错误");
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.URL_REGISTER, vertifySuccessListener, mErrorListener) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("telephone", telephone);
                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private void register_next()
    {
        // 跳转设置头像页面
        Intent intent = new Intent(register.this, register_image.class);
        Bundle driver_bundle = new Bundle();
        driver_bundle.putString("name", text_user_name.getText().toString());
        driver_bundle.putString("password", text_user_password.getText().toString());
        driver_bundle.putString("telephone", text_user_telephone.getText().toString());
        driver_bundle.putString("machine_id", text_user_machine_id.getText().toString());
        driver_bundle.putString("address", text_user_machine_id.getText().toString());
        intent.putExtra("driver_register", driver_bundle);
        startActivity(intent);
    }

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
                    empty_hint(R.string.vertify_error1);
                    // Error occurred in registration. Get the error
                    // message
                    String errorMsg = jObj.getString("error_msg");
                    Log.e(TAG, errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                empty_hint(R.string.vertify_error2);
            }

        }
    };

    //响应服务器失败
    private Response.ErrorListener mErrorListener= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Register Error: " + error.getMessage());
            error_hint(error.getMessage());
            hideDialog();
        }
    };

    //ProgressDialog显示与隐藏
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //注册表单验证
    private void form_verification(final Activity activity){
        mValidation.addValidation(activity, R.id.user_name, "^[\\u4e00-\\u9fa5]+$", R.string.err_name);
        mValidation.addValidation(activity, R.id.user_telephone,"^1[3-9]\\d{9}+$", R.string.err_phone);
        //农机牌照
        mValidation.addValidation(activity, R.id.user_machine_id,"/[\\u4e00-\\u9fa5]{1}[A-Z]{1}(?:(?![a-zA-Z]{5})[0-9a-zA-z]){5}/", R.string.err_license_plate);
//        mValidation.addValidation(activity, R.id.user_id_card,"\\d{15}|\\d{18}", R.string.err_id_card); //身份证
        mValidation.addValidation(activity, R.id.verification_code,"\\d{6}+$", R.string.err_verification_code);
        mValidation.addValidation(activity, R.id.user_password, "^[A-Za-z0-9_]{5,15}+$", R.string.err_password);
    }


    //输入是否为空，判断是否禁用按钮
    private void editTextIsNull(){

        text_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((address_select_flag!=0)&&(s.length() > 0) && !TextUtils.isEmpty(text_user_telephone.getText()) && !TextUtils.isEmpty(text_user_password.getText())
                        && !TextUtils.isEmpty(text_verification_code.getText())&& !TextUtils.isEmpty(text_user_machine_id.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        text_user_telephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((address_select_flag!=0)&&(s.length() > 0) && !TextUtils.isEmpty(text_user_name.getText()) && !TextUtils.isEmpty(text_user_password.getText())
                        && !TextUtils.isEmpty(text_verification_code.getText())&& !TextUtils.isEmpty(text_user_machine_id.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        text_user_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((address_select_flag!=0)&&(s.length() > 0) && !TextUtils.isEmpty(text_user_password.getText()) && !TextUtils.isEmpty(text_user_name.getText())
                        && !TextUtils.isEmpty(text_verification_code.getText())&& !TextUtils.isEmpty(text_user_machine_id.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        text_verification_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((address_select_flag!=0)&&(s.length() > 0) && !TextUtils.isEmpty(text_user_password.getText()) && !TextUtils.isEmpty(text_user_name.getText())
                        && !TextUtils.isEmpty(text_user_telephone.getText())&& !TextUtils.isEmpty(text_user_machine_id.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        text_user_machine_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((address_select_flag!=0)&&(s.length() > 0) && !TextUtils.isEmpty(text_user_password.getText()) && !TextUtils.isEmpty(text_user_name.getText())
                        && !TextUtils.isEmpty(text_user_telephone.getText())  && !TextUtils.isEmpty(text_verification_code.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });
    }

    //省市县选择监听
    private class select_site_clickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(register.this, select_province.class);
            startActivityForResult(intent, CODE_SELECT_SITE);
        }
    }

    //省市县选择监听
    private class backClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}