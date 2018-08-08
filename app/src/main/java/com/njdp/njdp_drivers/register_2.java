package com.njdp.njdp_drivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
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
import java.util.zip.CheckedOutputStream;

import bean.UAVCompany;

public class register_2 extends Activity implements View.OnClickListener{
    private EditText text_user_name=null;//法人
    private EditText text_user_address=null;
    private EditText text_user_qq=null;
    private EditText text_user_weixin=null;
    private EditText text_com_name=null;//公司名称
    private EditText text_uav_num=null;//无人机数量
    private EditText text_company_num=null;//员工数量
    private EditText text_company_indro=null;//公司简介
    private ImageButton btn_back=null;
    private com.beardedhen.androidbootstrap.BootstrapButton btn_register_next=null;
    private AwesomeValidation mValidation=new AwesomeValidation(ValidationStyle.BASIC);
    private static final String TAG = register_2.class.getSimpleName();
    private ProgressDialog pDialog;
    private NetUtil netUtil;
    private CommonUtil commonUtil;
    private SessionManager session;
    private String name;
    private String address;
    private String weixn;
    private String qq;
    private String token;
    private int address_select_flag=0;
    private int CODE_SELECT_SITE=101;
    private static int FLAG_REGISTER_INFO=11101005;
    private String fixInfo_url;//修改个人信息服务器地址
    private com.yolanda.nohttp.rest.Request<JSONObject> fixInfo_strReq;
    GeoCoder mSearch;
    GeoCodeResult geoCodeResult1;
    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(register_2.this,"不能根据地址获取经纬度信息，请准确输入地址！",Toast.LENGTH_LONG).show();
                return;
            }
            //获取地理编码结果
            geoCodeResult1=geoCodeResult;
            Log.e("位置位置",String.valueOf(geoCodeResult1.getLocation().latitude));
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                //Toast.makeText(register_2.this,"不能根据地址获取经纬度信息，请准确输入地址！",Toast.LENGTH_LONG).show();
                //return;
            }
            //获取反向地理编码结果
           // geoCodeResult=reverseGeoCodeResult;
        }


    };
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

        setContentView(R.layout.activity_register_2);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        netUtil=new NetUtil(this);
        commonUtil=new CommonUtil(this);
        // Session manager
        session = new SessionManager();
        token=session.getToken();
        fixInfo_url=AppConfig.URL_UAV_MODIFY_COM;//修改飞服公司信息


        text_user_name = (EditText) super.findViewById(R.id.user_name);
        text_user_address=(EditText) super.findViewById(R.id.user_site);
        text_user_qq=(EditText) super.findViewById(R.id.user_qq);
        text_user_weixin=(EditText) super.findViewById(R.id.user_weixin);
        btn_register_next=(com.beardedhen.androidbootstrap.BootstrapButton) super.findViewById(R.id.register_next);
        text_com_name=(EditText)super.findViewById(R.id.com_name);//公司名称
        text_uav_num=(EditText)super.findViewById(R.id.uav_num);;//无人机数量
        text_company_num=(EditText)super.findViewById(R.id.company_num);;//员工数量
        text_company_indro=(EditText)super.findViewById(R.id.company_indro);;//公司简介



        btn_back=(ImageButton) super.findViewById(R.id.getback);
       // btn_register_next.setEnabled(false);
        //btn_register_next.setClickable(false);
        btn_back.setOnClickListener(this);
        btn_register_next.setOnClickListener(this);

        editTextIsNull();
        form_verification(register_2.this);

        //点击选择地址
        //text_user_address.setOnClickListener(new select_site_clickListener());
        mSearch= GeoCoder.newInstance();

        mSearch.setOnGetGeoCodeResultListener(listener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.getback:
//                finish();
                break;
            case R.id.register_next:
                //mSearch.geocode(new GeoCodeOption().city("北京").address(text_user_address.getText().toString()));
                checkInfo();
                break;
        }
    }


    //注册表单验证
    private void form_verification(final Activity activity){
       // mValidation.addValidation(activity, R.id.user_name, "^[\\u4e00-\\u9fa5]+$", R.string.err_name);
       // mValidation.addValidation(activity, R.id.user_qq,"[1-9][0-9]{4,14}",R.string.err_qq);
      //  mValidation.addValidation(activity, R.id.user_weixin, "^[a-zA-Z\\d_]{5,}$", R.string.err_weixin);
    }

    //输入是否为空，判断是否禁用按钮
    private void editTextIsNull(){

        text_com_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0))
                {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                }
            }
        });
        text_user_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.length() > 0))
                {
                    mSearch.geocode(new GeoCodeOption().city("北京").address(s.toString()));
                    btn_register_next.setEnabled(true);
                    btn_register_next.setClickable(true);
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
        Toast toast = Toast.makeText(register_2.this, getResources().getString(in), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

    //错误信息提示
    private void error_hint(String str){
        Toast toast = Toast.makeText(register_2.this, str, Toast.LENGTH_LONG);
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
        if (isempty(R.id.com_name)) {
            Toast.makeText(this,"请输入公司名称",Toast.LENGTH_LONG).show();
            return;
        } else if (isempty(R.id.user_site)) {
            Toast.makeText(this,"请输入公司地址",Toast.LENGTH_LONG).show();
            return;
        } else if (mValidation.validate() == true) {
            name = text_user_name.getText().toString().trim();
            qq = text_user_qq.getText().toString().trim();
            weixn = text_user_weixin.getText().toString().trim();
            address = text_user_address.getText().toString().trim();
            register_user_info();
        }
    }

    //提交个人信息
    private void register_user_info(){

        pDialog.setMessage("正在上传个人信息.......");
        Map<String, String> params = new HashMap<String, String>();
        UAVCompany com = new UAVCompany();
        com.setFm_id(session.getUserId());
        //com.setFm_id("50");
        com.setPersion_weixin(weixn);
        com.setCom_addr(address);
        com.setPerson_qq(qq);
        com.setUav_num(text_uav_num.getText().toString());
        com.setCom_name(text_com_name.getText().toString());
        com.setStaff_num(text_company_num.getText().toString());
        com.setCom_memo(text_company_indro.getText().toString());
        if(geoCodeResult1!=null) {
            com.setCom_latitude(String.valueOf(geoCodeResult1.getLocation().latitude));
            com.setCom_longitude(String.valueOf(geoCodeResult1.getLocation().longitude));
        }
        Gson gson2=new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        String obj2=gson2.toJson(com);

        params.put("comJson", obj2);
        /*
        params.put("person_qq", qq);
        params.put("person_weixin", weixn);
        params.put("person_name", name);
        params.put("person_address", address);
        */
        fixInfo_strReq= NoHttp.createJsonObjectRequest(fixInfo_url, RequestMethod.POST);
        fixInfo_strReq.add(params);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(register_2.this) == false) {
            hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_REGISTER_INFO, fixInfo_strReq, new register_info_request());
        }
    }

    //注册访问服务器监听
    private class register_info_request implements OnResponseListener<JSONObject> {
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
                    String errorMsg = jObj.getString("msg");
                    Log.e(TAG, "Register2 用户信息上传成功："+errorMsg);
                    Toast.makeText(register_2.this,"用户信息注册完成，请登录！",Toast.LENGTH_LONG).show();
                   //Intent intent = new Intent(register_2.this, register_image.class);
                    //startActivity(intent);
                    //Intent intent = new Intent(register_2.this, login.class);
                    //startActivity(intent);
                    finish();
                } else {
                    String errorMsg = jObj.getString("msg");
                    Log.e(TAG, "错误代码：" + status + "---" + errorMsg);
                    Log.e(TAG, "Register2 Error："+getResources().getString(R.string.connect_service_err1)+
                            "错误代码" + String.valueOf(status)+ "," +errorMsg);
                    commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err1) + errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Register2 Error：" + getResources().getString(R.string.connect_service_err2) + e.getMessage());
                commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err2) + e.getMessage());
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "Get UserInfo Error："+getResources().getString(R.string.connect_service_err3) + exception.getMessage());
            commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err3) + exception.getMessage());
        }

        @Override
        public void onFinish(int what) {
            hideDialog();
        }
    }

//    //响应服务器成功
//    private Response.Listener<String> mSuccessListener =new Response.Listener<String>() {
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
//                    Log.e(TAG,errorMsg);
//                    Intent intent = new Intent(register_2.this, register_image.class);
//                    startActivity(intent);
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

//    //响应服务器失败
//    private Response.ErrorListener mErrorListener= new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            hideDialog();
//            Log.e(TAG, "Register UserInfo Error: " + error.getMessage());
//            error_hint(error.getMessage());
//        }
//    };

}
