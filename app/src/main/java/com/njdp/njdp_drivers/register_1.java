package com.njdp.njdp_drivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.Driver;

public class register_1 extends Activity implements View.OnClickListener{
    private EditText text_user_machine_id=null;
    private EditText text_user_password=null;
    private EditText text_verification_code=null;
    private ImageButton btn_back=null;
    private com.beardedhen.androidbootstrap.BootstrapButton btn_register_next=null;
    private AwesomeValidation mValidation=new AwesomeValidation(ValidationStyle.BASIC);
    private static final String TAG = register_1.class.getSimpleName();
    private ProgressDialog pDialog;
    private NetUtil netUtil;
    private CommonUtil commonUtil;
    private DriverDao driverDao;
    private SessionManager session;
    private Driver driver;
    private String telephone;
    private String machine_id;
    private String password;
    private String verify_code;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);

        driver=new Driver();
        Bundle driver_bundle=this.getIntent().getBundleExtra("driver_register");
        if (driver_bundle!=null)
        {
            telephone=driver_bundle.getString("telephone");
            driver.setTelephone(telephone);
        }else
        {
            error_hint("程序错误！请联系管理员！");
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        driverDao=new DriverDao(register_1.this);
        commonUtil=new CommonUtil(register_1.this);
        netUtil=new NetUtil(register_1.this);
        session = new SessionManager(getApplicationContext());

        text_user_machine_id= (EditText) super.findViewById(R.id.user_machine_id);
        text_user_password = (EditText) super.findViewById(R.id.user_password);
        text_verification_code = (EditText) super.findViewById(R.id.verification_code);
        btn_register_next=(com.beardedhen.androidbootstrap.BootstrapButton) super.findViewById(R.id.register_next);
        btn_back=(ImageButton) super.findViewById(R.id.getback);
        btn_back.setOnClickListener(new backClickListener());
        btn_register_next.setEnabled(false);
        btn_register_next.setClickable(false);
        btn_back.setOnClickListener(this);
        btn_register_next.setOnClickListener(this);

        editTextIsNull();
        form_verification(register_1.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.getback:
                finish();
                break;
            case R.id.register_next:
                checkInfo();
                break;
        }
    }

    //返回监听
    private class backClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    //注册表单验证
    private void form_verification(final Activity activity) {
        mValidation.addValidation(activity, R.id.user_machine_id, "\\d{11}+$", R.string.err_machine_id);
        mValidation.addValidation(activity, R.id.user_password, "^[A-Za-z0-9_]{5,15}+$", R.string.err_password);
    }

    //输入是否为空，判断是否禁用按钮
    private void editTextIsNull(){

        text_user_machine_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_user_password.getText())
                        && !TextUtils.isEmpty(text_verification_code.getText())) {
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
                if ((s.length() > 0) && !TextUtils.isEmpty(text_user_machine_id.getText())
                        && !TextUtils.isEmpty(text_verification_code.getText())) {
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
                if ((s.length() > 0) && !TextUtils.isEmpty(text_user_machine_id.getText())
                        && !TextUtils.isEmpty(text_user_password.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

    }

    //EditText输入是否为空
    private boolean isempty(int id){
        EditText editText=(EditText) super.findViewById(id);
        boolean bl= TextUtils.isEmpty(editText.getText());
        return bl;
    }

    //信息未输入提示
    private void empty_hint(int in){
        Toast toast = Toast.makeText(register_1.this, getResources().getString(in), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

    //错误信息提示
    private void error_hint(String str){
        Toast toast = Toast.makeText(register_1.this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

    //ProgressDialog显示与隐藏
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //验证表单输入信息
    private void checkInfo()
    {
        if (isempty(R.id.user_machine_id)) {
            empty_hint(R.string.err_machine_id_2);
        } else if (isempty(R.id.verification_code)) {
            empty_hint(R.string.err_verification_code2);
        }  else if(isempty(R.id.user_password)){
            empty_hint(R.string.err_password2);
        } else if (mValidation.validate() == true) {
            machine_id = text_user_machine_id.getText().toString().trim();
            password = text_user_password.getText().toString().trim();
            verify_code=text_verification_code.getText().toString().trim();
            verify_machine_id();//验证农机id，并上传服务器注册
        }
    }

    //注册
    private void register_user(){

        String tag_string_req = "req_register_driver";
        if(netUtil.checkNet(register_1.this)==false){
            error_hint("网络连接错误");
            hideDialog();
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_REGISTER, mSuccessListener, mErrorListener) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone", telephone);
                    params.put("machine_id", machine_id);
                    params.put("password", password);
                    params.put("code", verify_code);
                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }


    //响应服务器成功
    private Response.Listener<String> mSuccessListener =new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            hideDialog();
            Log.e(TAG, "Register Response: " + response.toString());

            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");
                if (status==0) {
                    token=jObj.getString("result");
                    session.setLogin(true,token);
                    driver.setId(1);
                    driver.setMachine_id(machine_id);
                    driver.setPassword(password);
                    driverDao.add(driver);
                    Intent intent = new Intent(register_1.this, register_2.class);
                    startActivity(intent);
                    finish();
                } else {
                    String err_status = jObj.getString("status");
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "错误代码："+err_status+"---"+errorMsg);
                    commonUtil.error_hint(errorMsg);
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
            hideDialog();
            Log.e(TAG, "Register UserInfo Error: " + error.getMessage());
            error_hint(error.getMessage());
        }
    };

    //验证农机id
    private void verify_machine_id(){

        String tag_string_req = "req_register_driver";
        pDialog.setMessage("正在提交注册申请，请等待......");
        showDialog();
        if(netUtil.checkNet(register_1.this)==false){
            error_hint("网络连接错误");
            hideDialog();
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_VERIFY_MACHINE_ID, verifySuccessListener, mErrorListener) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("machine_id", machine_id);
                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }


    //验证农机响应服务器成功
    private Response.Listener<String> verifySuccessListener =new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            hideDialog();
            Log.e(TAG, "Register Response: " + response.toString());

            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");
                if (status==0) {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, errorMsg);
                    register_user();
                } else {
                    String err_status = jObj.getString("status");
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "错误代码："+err_status+"---"+errorMsg);
                    commonUtil.error_hint(errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                empty_hint(R.string.vertify_error2);
            }

        }
    };

}
