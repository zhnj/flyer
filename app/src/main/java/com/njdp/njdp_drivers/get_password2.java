package com.njdp.njdp_drivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.LruBitmapCache;
import com.njdp.njdp_drivers.db.SQLiteHandler;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class get_password2 extends AppCompatActivity {
    private EditText text_password=null;
    private EditText text_password2=null;
    private ImageButton password_reveal=null;
    private ImageButton password_show = null;
    private ImageButton password_reveal2=null;
    private ImageButton password_show2=null;
    private ImageButton btn_back=null;
    private String telephone;
    private String machine_id;
    private AwesomeValidation password_Validation=new AwesomeValidation(ValidationStyle.BASIC);
    private static final String TAG = register.class.getSimpleName();
    private ProgressDialog pDialog;
//    private SessionManager session;

    private LruBitmapCache loadImage=new LruBitmapCache();
    private String url;
    private static int FLAG_SETNEWPASSWORD=11101003;
    private File tempFile;
    private String path;//用户头像路径
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private Gson gson;


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


        setContentView(R.layout.activity_get_password2);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        commonUtil =new CommonUtil(get_password2.this);
        netUtil=new NetUtil(get_password2.this);
//        session = new SessionManager();
        gson=new Gson();
        url= AppConfig.URL_GETPASSWORD2;

        //设置头像本地存储路径
        tempFile=commonUtil.getPath();
        path=tempFile.getAbsolutePath()+"/temp/njdp_user_image.png";

        //获取forgetpassword输入的手机号
        Bundle s_bundle = this.getIntent().getBundleExtra("driver_access");
        if(s_bundle!=null)
        {
//            machine_id=s_bundle.getString("license_plater");
            telephone=s_bundle.getString("telephone");
        }else
        {
            commonUtil.error_hint_short("程序错误！请联系管理员！");
        }

        password_Validation.addValidation(get_password2.this, R.id.user_password, "^[A-Za-z0-9_]{5,15}+$", R.string.err_password);
        password_Validation.addValidation(get_password2.this, R.id.user_password2, "^[A-Za-z0-9_]{5,15}+$", R.string.err_password);
        this.password_reveal=(ImageButton) super.findViewById(R.id.reveal_button);
        this.password_show=(ImageButton) super.findViewById(R.id.show_button);
        this.password_reveal2=(ImageButton) super.findViewById(R.id.reveal_button2);
        this.password_show2=(ImageButton) super.findViewById(R.id.show_button2);
        this.text_password=(EditText)super.findViewById(R.id.user_password);
        this.text_password2=(EditText)super.findViewById(R.id.user_password2);
        this.btn_back=(ImageButton)super.findViewById(R.id.getBack);

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonUtil.isempty(text_password)) {
                    commonUtil.error_hint_short("请输入新的密码");
                } else if (commonUtil.isempty(text_password2)) {
                    commonUtil.error_hint_short("请再次输入新的密码");
                }else if (!text_password2.getText().toString().trim().equals(text_password.getText().toString().trim())){
                    commonUtil.error_hint_long("两次输入的密码不一致");
                }else if (password_Validation.validate() == true) {
                    String nPassword = text_password2.getText().toString().trim();
                    setNewPassword(nPassword, telephone);
                }
            }
        });

        btn_back.setOnClickListener(new backClickListener());

    }

    //点击眼睛1按钮，设置密码显示或者隐藏
    public void showClick(View v) {
        password_reveal.setVisibility(View.VISIBLE);
        password_show.setVisibility(View.GONE);
        get_password2.this.text_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    public void revealClick(View v) {
        password_reveal.setVisibility(View.GONE);
        password_show.setVisibility(View.VISIBLE);
        get_password2.this.text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }
    //点击眼睛2按钮，设置密码显示或者隐藏
    public void showClick2(View v) {
        password_reveal2.setVisibility(View.VISIBLE);
        password_show2.setVisibility(View.GONE);
        get_password2.this.text_password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    public void revealClick2(View v) {
        password_reveal2.setVisibility(View.GONE);
        password_show2.setVisibility(View.VISIBLE);
        get_password2.this.text_password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    //设置新的密码到数据库
    public void setNewPassword(final String password,final String telephone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", telephone);
        params.put("password",password);
        Log.d(TAG,gson.toJson(params));
        com.yolanda.nohttp.rest.Request<JSONObject> req_setPassword = NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
        req_setPassword.add(params);
        RequestQueue requestQueue = NoHttp.newRequestQueue();

        pDialog.setMessage("正在重置密码，请等待...");

        if (netUtil.checkNet(get_password2.this) == false) {
            commonUtil.error_hint_short("网络连接错误");
            hideDialog();
            return;
        } else {
            requestQueue.add(FLAG_SETNEWPASSWORD, req_setPassword, new set_password_request());
        }
    }

    //设置新密码访问服务器监听
    private class set_password_request implements OnResponseListener<JSONObject> {
        @Override
        public void onStart(int what) {
            showDialog();
        }

        @Override
        public void onSucceed(int what, com.yolanda.nohttp.rest.Response<JSONObject> response) {

            try {
                JSONObject jObj=response.get();
                Log.d(TAG,gson.toJson(jObj));
                int status = jObj.getInt("status");

                if (status==0) {
                    String errorMsg=jObj.getString("result");
                    Log.e(TAG, "修改成功：" + errorMsg);
                    commonUtil.error_hint_short("重置密码成功，请重新登录");
                    Intent intent = new Intent(get_password2.this, login.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误1:" + errorMsg);
                    commonUtil.error_hint_short(errorMsg);
                }
            } catch (JSONException e) {
                commonUtil.error_hint2_short(R.string.connect_error);
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
}