package com.njdp.njdp_drivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.LruBitmapCache;
import com.njdp.njdp_drivers.db.SQLiteHandler;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.NetUtil;
import com.njdp.njdp_drivers.util.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import bean.Driver;

public class login extends Activity {

    private EditText text_username=null;
    private EditText text_password=null;
    private ImageButton password_reveal=null;
    private ImageButton password_show=null;
    private ImageButton login_check=null;
    private static final String TAG = login.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private DriverDao driverDao;
    private LruBitmapCache loadImage=new LruBitmapCache();
    private Driver driver;
    private String url;
    private File tempFile;
    private String path;//用户头像路径
    private NetUtil netUtil;
    private CommonUtil commonUtil;
    private int first_KeyBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_login);

        this.text_username=(EditText) super.findViewById(R.id.user_username);
        this.text_password=(EditText) super.findViewById(R.id.user_password);
        this.password_reveal=(ImageButton) super.findViewById(R.id.reveal_button);
        this.password_show=(ImageButton) super.findViewById(R.id.show_button);
        this.login_check=(ImageButton) super.findViewById(R.id.loginin_button);

        //根据输入框是否为空判断是否禁用按钮
        login_check.setEnabled(false);
        login_check.setClickable(false);
        editTextIsNull();
        url=AppConfig.URL_LOGIN;

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        netUtil=new NetUtil(login.this);
        commonUtil =new CommonUtil(login.this);
        session = new SessionManager(getApplicationContext());
        driverDao=new DriverDao(login.this);
        driver=new Driver();

        //设置头像本地存储路径
        tempFile=commonUtil.getPath();
        path=tempFile.getAbsolutePath()+"/temp/njdp_user_image.png";

        // Check if user is already logged in or not
        if (session.isLoginIn()) {
            Intent intent = new Intent(login.this, mainpages.class);
            startActivity(intent);
            finish();
        }

        login_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                driver.setMachine_id(text_username.getText().toString());
                driver.setPassword(text_password.getText().toString());
                checkLogin(driver);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Log.d(TAG,"登录界面禁止返回,只能退出");
            Timer tExit = null;
            if(first_KeyBack ==0) {
                first_KeyBack = 1; // 准备退出
                commonUtil.error_hint2_long(R.string.exit_notice);
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        first_KeyBack = 0; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }

    //点击眼睛按钮，设置密码显示或者隐藏
    public void showClick(View v) {
        password_reveal.setVisibility(View.VISIBLE);
        password_show.setVisibility(View.GONE);
        login.this.text_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    public void revealClick(View v) {
        password_reveal.setVisibility(View.GONE);
        password_show.setVisibility(View.VISIBLE);
        login.this.text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    // register_user 跳转到注册界面
    public void register_user(View v){
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    //找回密码
    public void get_password(View v){
        Intent intent = new Intent(this, get_password.class);
        startActivity(intent);
    }

    //checkLogin 验证帐号密码
    public void checkLogin(final Driver driver) {

        String tag_string_req = "req_login";

        pDialog.setMessage("正在登录 ...");
        showDialog();

        if (netUtil.checkNet(login.this) == false) {
            hideDialog();
            error_hint("网络连接错误");
            return;
        } else {

            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, mSuccessListener, mErrorListener) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("fm_username", driver.getMachine_id());
                    params.put("fm_password", driver.getPassword());
                    params.put("fm_tag", "M");

                    return netUtil.checkParams(params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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

    //响应服务器成功
    private Response.Listener<String> mSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.i("tagconvertstr", "["+response+"]");
            Log.d(TAG, "Login Response: " + response.toString());
            hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");

                // Check for error node in json
                if (status==0) {
                    // Create Login session
                    String token=jObj.getString("result");
                    session.setLogin(true,token);
                    driver.setId(1);
                    driverDao.add(driver);

                    //缓存用户信息
//                    JSONObject user = jObj.getJSONObject("User");
//                    String imageurl= user.getString("imageurl");
//                    Bitmap bitmap = loadImage.getBitmap(imageurl);
//                    nutil.saveBitmap(login.this, bitmap);
//                    driver.setImage_url(path);

                    error_hint("登录成功！");
                    Intent intent = new Intent(login.this, mainpages.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint(errorMsg);
                }
            } catch (JSONException e) {
                empty_hint(R.string.connect_error);
                // JSON error
                e.printStackTrace();
                Log.e(TAG,"Json error：response错误！" + e.getMessage());
            }
        }
    };

    //响应服务器失败
    private Response.ErrorListener mErrorListener = new Response.ErrorListener()  {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Login Error: " + error.getMessage());
            error_hint("服务器连接失败");
            hideDialog();
        }
    };

    //错误信息提示1
    private void error_hint(String str){
        Toast toast = Toast.makeText(login.this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,-50);
        toast.show();
    }

    //错误信息提示2
    private void empty_hint(int in){
        Toast toast = Toast.makeText(login.this, getResources().getString(in), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,-50);
        toast.show();
    }

    //输入是否为空，判断是否禁用按钮
    private void editTextIsNull(){
        text_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0)&& !TextUtils.isEmpty(text_password.getText())) {
                    login_check.setClickable(true);
                    login_check.setEnabled(true);
                } else {
                    login_check.setEnabled(false);
                    login_check.setClickable(false);
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
                if ((s.length() > 0)&& !TextUtils.isEmpty(text_username.getText())) {
                    login_check.setClickable(true);
                    login_check.setEnabled(true);
                } else {
                    login_check.setEnabled(false);
                    login_check.setClickable(false);
                }
            }
        });
    }
}