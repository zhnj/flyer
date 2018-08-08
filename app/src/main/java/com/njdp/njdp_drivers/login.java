package com.njdp.njdp_drivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.LruBitmapCache;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.NetUtil;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

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
    private static int FLAG_LOGIN=11101000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置沉浸模式
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

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
        session = new SessionManager();
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
        // Intent intent = new Intent(this, register_2.class);
        startActivity(intent);
    }

    //找回密码
    public void get_password(View v){
        Intent intent = new Intent(this, get_password.class);
        startActivity(intent);
    }


    //登录
    private void checkLogin(final Driver driver)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fm_username", driver.getMachine_id());
        params.put("fm_password", driver.getPassword());
        //params.put("fm_tag", "M");
        params.put("fm_tag", "FLYER");
        com.yolanda.nohttp.rest.Request<JSONObject> strReq= NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
        strReq.add(params);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(login.this) == false) {
            hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_LOGIN, strReq, new login_request());
        }
    }

    //登录访问服务器监听
    private class login_request implements OnResponseListener<JSONObject> {
        @Override
        public void onStart(int what) {
            showDialog();
        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            try {
                JSONObject jObj=response.get();
                int status = jObj.getInt("status");

                if (status==0) {

                    String token=jObj.getString("result");
                    session.setLogin(true,token);

                    session.setID(jObj.getString("fm_id"));
                    String fm_ID=session.getUserId();

                    driver.setId(1);
                    driverDao.add(driver);

                    commonUtil.error_hint_short("登录成功！");
                    Intent intent = new Intent(login.this, mainpages.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short(errorMsg);
                }
            } catch (JSONException e) {
                commonUtil.error_hint2_short(R.string.connect_error);
                // JSON error
                e.printStackTrace();
                Log.e(TAG,"Json error：response错误！" + e.getMessage());
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
        text_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0) && !TextUtils.isEmpty(text_password.getText())) {
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
                if ((s.length() > 0) && !TextUtils.isEmpty(text_username.getText())) {
                    login_check.setClickable(true);
                    login_check.setEnabled(true);
                } else {
                    login_check.setEnabled(false);
                    login_check.setClickable(false);
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
}