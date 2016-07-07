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
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

public class register_2 extends Activity implements View.OnClickListener{
    private EditText text_user_name=null;
    private TextView text_user_address=null;
    private EditText text_user_qq=null;
    private EditText text_user_weixin=null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        netUtil=new NetUtil(this);
        commonUtil=new CommonUtil(this);
        // Session manager
        session = new SessionManager();
        token=session.getToken();

        text_user_name = (EditText) super.findViewById(R.id.user_name);
        text_user_address=(TextView) super.findViewById(R.id.user_site);
        text_user_qq=(EditText) super.findViewById(R.id.user_qq);
        text_user_weixin=(EditText) super.findViewById(R.id.user_weixin);
        btn_register_next=(com.beardedhen.androidbootstrap.BootstrapButton) super.findViewById(R.id.register_next);
        btn_back=(ImageButton) super.findViewById(R.id.getback);
        btn_register_next.setEnabled(false);
        btn_register_next.setClickable(false);
        btn_back.setOnClickListener(this);
        btn_register_next.setOnClickListener(this);

        editTextIsNull();
        form_verification(register_2.this);

        //点击选择地址
        text_user_address.setOnClickListener(new select_site_clickListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.getback:
//                finish();
                break;
            case R.id.register_next:
                checkInfo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_SELECT_SITE&&resultCode==RESULT_OK){
            Bundle _bundle=data.getExtras();
            String select_result=_bundle.getString("select_site");
            if(!select_result.equals("0")) {
                address_select_flag = 1;
                text_user_address.setText(select_result);
                if (!TextUtils.isEmpty(text_user_name.getText()) && !TextUtils.isEmpty(text_user_qq.getText())
                        && !TextUtils.isEmpty(text_user_weixin.getText())) {
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

    //注册表单验证
    private void form_verification(final Activity activity){
        mValidation.addValidation(activity, R.id.user_name, "^[\\u4e00-\\u9fa5]+$", R.string.err_name);
        mValidation.addValidation(activity, R.id.user_qq,"[1-9][0-9]{4,14}",R.string.err_qq);
        mValidation.addValidation(activity, R.id.user_weixin, "^[a-zA-Z\\d_]{5,}$", R.string.err_weixin);
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
                if ((address_select_flag==1)&&(s.length() > 0)
                        && !TextUtils.isEmpty(text_user_qq.getText())
                        && !TextUtils.isEmpty(text_user_weixin.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        text_user_qq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((address_select_flag == 1) && (s.length() > 0)
                        && !TextUtils.isEmpty(text_user_name.getText())
                        && !TextUtils.isEmpty(text_user_weixin.getText())) {
                    btn_register_next.setClickable(true);
                    btn_register_next.setEnabled(true);
                } else {
                    btn_register_next.setEnabled(false);
                    btn_register_next.setClickable(false);
                }
            }
        });

        text_user_weixin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((address_select_flag == 1) && (s.length() > 0)
                        && !TextUtils.isEmpty(text_user_name.getText())
                        && !TextUtils.isEmpty(text_user_qq.getText())) {
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
            intent.setClass(register_2.this, select_province.class);
            startActivityForResult(intent, CODE_SELECT_SITE);
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
        if (isempty(R.id.user_name)) {
            empty_hint(R.string.err_name2);
        } else if (isempty(R.id.user_qq)) {
            empty_hint(R.string.err_qq);
        } else if (isempty(R.id.user_weixin)) {
            empty_hint(R.string.err_weixin);
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

        String tag_string_req = "req_register_driver";
        pDialog.setMessage("正在提交个人信息，请等待......");
        showDialog();
        if(netUtil.checkNet(register_2.this)==false){
            error_hint("网络连接错误");
            hideDialog();
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_FIXPERSONINFO, mSuccessListener, mErrorListener) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("person_qq", qq);
                    params.put("person_weixin", weixn);
                    params.put("person_name", name);
                    params.put("person_address", address);
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
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG,errorMsg);
                    Intent intent = new Intent(register_2.this, register_image.class);
                    startActivity(intent);
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

}
