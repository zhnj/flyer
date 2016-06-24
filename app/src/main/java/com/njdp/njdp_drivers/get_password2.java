package com.njdp.njdp_drivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.LruBitmapCache;
import com.njdp.njdp_drivers.db.SQLiteHandler;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

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
    private String name;
    private String telephone;
    private String machine_id;
    private String tag;
    private AwesomeValidation password_Validation=new AwesomeValidation(ValidationStyle.BASIC);
    private AwesomeValidation password2_Validation=new AwesomeValidation(ValidationStyle.BASIC);
    private static final String TAG = register.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;

    private LruBitmapCache loadImage=new LruBitmapCache();
    private String url;
    private File tempFile;
    private String path;//用户头像路径
    private CommonUtil commonUtil;
    private NetUtil netUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password2);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        commonUtil =new CommonUtil(get_password2.this);
        netUtil=new NetUtil(get_password2.this);
        session = new SessionManager(getApplicationContext());
        url= AppConfig.URL_LOGIN;

        //设置头像本地存储路径
        tempFile=commonUtil.getPath();
        path=tempFile.getAbsolutePath()+"/temp/njdp_user_image.png";

        //获取forgetpassword输入的用户名、手机号.姓名
        Bundle s_bundle = this.getIntent().getBundleExtra("driver_access");
        if(s_bundle!=null)
        {
            machine_id=s_bundle.getString("license_plater");
            name=s_bundle.getString("name");
            telephone=s_bundle.getString("telephone");
            tag="M";
        }else
        {
            commonUtil.error_hint("程序错误！请联系管理员！");
        }

        password_Validation.addValidation(get_password2.this, R.id.user_password, "^[A-Za-z0-9_]{5,15}+$", R.string.err_password);
        password2_Validation.addValidation(get_password2.this, R.id.user_password2,"^[A-Za-z0-9_]{5,15}+$" , R.string.err_password);
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
                if(commonUtil.isempty(text_password)){
                    commonUtil.error_hint("请输入新的密码");
                }else if(password_Validation.validate()==true){
                    if(commonUtil.isempty(text_password2)){
                        commonUtil.error_hint("请再次输入密码");
                    }else if(password2_Validation.validate()==true){
                        String nPassword=text_password2.getText().toString().trim();
                        setNewPassword(nPassword,name,telephone);
                    }
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
        get_password2.this.text_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    public void revealClick2(View v) {
        password_reveal2.setVisibility(View.GONE);
        password_show2.setVisibility(View.VISIBLE);
        get_password2.this.text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    //设置新的密码到数据库
    public void setNewPassword(final String license_plater,final String password,final String telephone) {

        // Tag used to cancel the request
        String tag_string_req = "req_register_student";

        pDialog.setMessage("正在重置密码 ...");
        showDialog();

        if (netUtil.checkNet(get_password2.this) == false) {
            commonUtil.error_hint("网络连接错误");
            hideDialog();
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");;
                        if (!error) {
                            String token=jObj.getString("token");
                            session.setLogin(true,token);
                            JSONObject user = jObj.getJSONObject("User");
                            String imageurl= user.getString("imageurl");
                            Bitmap bitmap = loadImage.getBitmap(imageurl);
                            commonUtil.saveBitmap(get_password2.this, bitmap);
                            commonUtil.error_hint("重置密码成功");
                            // 跳转到Driver主页面
                            Intent intent = new Intent(get_password2.this, mainpages.class);
                            startActivity(intent);
                            finish();
                        }else {
                            // Error occurred in registration. Get the error
                            String errorMsg = jObj.getString("error_msg");
                            Log.e(TAG, "Json error：response错误:" + errorMsg);
                            commonUtil.error_hint(errorMsg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    commonUtil.error_hint(error.getMessage());
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("license_plater", license_plater);
                    params.put("telephone", telephone);
                    params.put("tag", "M");

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

    //返回监听
    private class backClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}