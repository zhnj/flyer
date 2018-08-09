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
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.Driver;

public class register_1 extends Activity implements View.OnClickListener{
    private EditText text_sfzh=null;
    private EditText text_password=null;
    private EditText text_code=null;
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
    //private String machine_id;
    private String sfzh;
    private String password;
    private String verify_code;
    private String token;
    private static int FLAG_REGISTER=11101005;
    private static int FLAG_MACHINEVERIFY=11101010;
    private String register_url;
    private String machineVerify_url;
    private com.yolanda.nohttp.rest.Request<JSONObject> register_strReq;
    private com.yolanda.nohttp.rest.Request<JSONObject> machineVerify_strReq;

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
        session = new SessionManager();
        register_url=AppConfig.URL_REGISTER;
        machineVerify_url=AppConfig.URL_VERIFY_MACHINE_ID;

        text_sfzh= (EditText) super.findViewById(R.id.sfzh);
        text_password = (EditText) super.findViewById(R.id.password);
        text_code = (EditText) super.findViewById(R.id.code);
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
       // mValidation.addValidation(activity, R.id.user_machine_id, "\\d{12}+$", "请输入正确的身份证号");
        //mValidation.addValidation(activity, R.id.user_password, "^[A-Za-z0-9_]{5,15}+$", R.string.err_password);
    }

    //输入是否为空，判断是否禁用按钮
    private void editTextIsNull(){

        text_sfzh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_password.getText())
                        && !TextUtils.isEmpty(text_code.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        text_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_sfzh.getText())
                        && !TextUtils.isEmpty(text_code.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        //验证码判断
        text_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_sfzh.getText())
                        && !TextUtils.isEmpty(text_password.getText())) {
                    if(s.toString().equals(AppConfig.VerifyCode)==false)
                    {
                        Toast.makeText(register_1.this,"验证码错误，请重新输入或获取！",Toast.LENGTH_LONG).show();

                    }
                    else {
                        btn_register_next.setClickable(true);
                        btn_register_next.setEnabled(true);
                    }
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
        if (isempty(R.id.sfzh)) {
            empty_hint(R.string.err_machine_id_2);
        } else if (isempty(R.id.code)) {
            empty_hint(R.string.err_verification_code2);
        }  else if(isempty(R.id.password)){
            empty_hint(R.string.err_password2);
        } else if (mValidation.validate() == true) {
            sfzh = text_sfzh.getText().toString().trim();
            password = text_password.getText().toString().trim();
            verify_code=text_code.getText().toString().trim();
            //verify_machine_id();//验证农机id，并上传服务器注册
            register_user();//注册用户
        }
    }

    //注册
    private void register_user(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", telephone);
        params.put("sfzh", sfzh);
        params.put("password", password);
        params.put("code", AppConfig.VerifyCode);
        register_strReq= NoHttp.createJsonObjectRequest(register_url, RequestMethod.POST);
        register_strReq.add(params);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(register_1.this) == false) {
            hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_REGISTER, register_strReq, new register_request());
        }
    }

    //注册访问服务器监听
    private class register_request implements OnResponseListener<JSONObject> {
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
                    token=jObj.getString("result");
                    session.setLogin(true,token);
                    String userid=jObj.getString("user_id");
                    session.setID(userid);
                    driver.setId(Integer.parseInt(userid));
                    //driver.setMachine_id(machine_id);
                    driver.setSfzh(sfzh);
                    driver.setTelephone(telephone);
                    driver.setPassword(password);
                    driverDao.add(driver);
                    Intent intent = new Intent(register_1.this, register_2.class);
                    intent.putExtra("phone",telephone);
                    startActivity(intent);
                    finish();
                } else {
                    String err_status = jObj.getString("status");
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "错误代码："+err_status+"---"+errorMsg);
                    commonUtil.error_hint_short(errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                empty_hint(R.string.vertify_error2);
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "Register1 Error: " + exception.getMessage());
            commonUtil.error_hint_short("服务器连接失败");
        }

        @Override
        public void onFinish(int what) {
            hideDialog();
        }
    }

    //验证农机id
    /*
    private void verify_machine_id(){
        pDialog.setMessage("正在提交注册申请，请等待......");
        machineVerify_strReq= NoHttp.createJsonObjectRequest(machineVerify_url, RequestMethod.POST);
        machineVerify_strReq.add("machine_id", machine_id);
        Log.e(TAG, "验证农机MachineID-接收的数据：" + machine_id);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(register_1.this) == false) {
            hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_MACHINEVERIFY, machineVerify_strReq, new machineVerify_request());
        }
    }


    //验证农机id访问服务器监听
    private class machineVerify_request implements OnResponseListener<JSONObject> {
        @Override
        public void onStart(int what) {
            showDialog();
        }

        @Override
        public void onSucceed(int what, com.yolanda.nohttp.rest.Response<JSONObject> response) {

            try {
                JSONObject jObj=response.get();
                Log.e(TAG, "验证农机MachineID-接收的数据：" + jObj.toString());
                int status = jObj.getInt("status");
                if (status==0) {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "验证MachineID成功："+errorMsg);
                    register_user();
                } else {
                    String err_status = jObj.getString("status");
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Register1 MachineVerify Error："+getResources().getString(R.string.connect_service_err1)+ String.valueOf(err_status)+" "+errorMsg);
                    commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err1) + errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Register1 MachineVerify Error：" + getResources().getString(R.string.connect_service_err2) + e.getMessage());
                commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err2) + e.getMessage());
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "Register1 MachineVerify Error："+getResources().getString(R.string.connect_service_err3) + exception.getMessage());
            commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err3) + exception.getMessage());
        }

        @Override
        public void onFinish(int what) {
            hideDialog();
        }
    }
*/

//    //验证农机响应服务器成功
//    private Response.Listener<String> verifySuccessListener =new Response.Listener<String>() {
//
//        @Override
//        public void onResponse(String response) {
//            hideDialog();
//            Log.e(TAG, "Register Response: " + response.toString());
//
//            try {
//                JSONObject jObj = new JSONObject(response);
//                int status = jObj.getInt("status");
//                if (status==0) {
//                    String errorMsg = jObj.getString("result");
//                    Log.e(TAG, errorMsg);
//                    register_user();
//                } else {
//                    String err_status = jObj.getString("status");
//                    String errorMsg = jObj.getString("result");
//                    Log.e(TAG, "错误代码："+err_status+"---"+errorMsg);
//                    commonUtil.error_hint_short(errorMsg);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                empty_hint(R.string.vertify_error2);
//            }
//
//        }
//    };

}
