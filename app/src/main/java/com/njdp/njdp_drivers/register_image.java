package com.njdp.njdp_drivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.changeDefault.NewClickableSpan;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Driver;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

public class register_image extends AppCompatActivity {

    private Button finish;
    private ImageButton getback;
    private TextView notice=null;
    private TextView setImage=null;
    private ImageView userImage=null;
    private Driver driver;
    private String s1="服务条款";
    private String s2="隐私协议";
    private ProgressDialog pDialog;
    private NetUtil netUtil;
    private SessionManager session;
    private DriverDao driverDao;
    private Uri imageUri;
    private String url;
    private String token;
    private int crop = 300;// 裁剪大小
    private static final int REQUEST_IMAGE=001;
    private static final int CROP_PHOTO_CODE = 002;
    private ArrayList<String> defaultDataArray;
    public boolean IsSetImage=false;
    private CommonUtil commonUtil;
    private static final String TAG = register.class.getSimpleName();
    private File tempFile;
    private String path;//用户头像路径
    private Gson gson;
    private boolean imageError;
    private boolean error;
    private static int FLAG_FIXINFO=11102000;
    private String fixInfo_url;//修改个人信息服务器地址
    private com.yolanda.nohttp.rest.Request<JSONObject>fixInfo_strReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_image);

        fixInfo_url=AppConfig.URL_FIXPERSONINFO;
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager();// Session manager
        driverDao=new DriverDao(register_image.this);
        commonUtil=new CommonUtil(register_image.this);
        netUtil=new NetUtil(register_image.this);
        gson = new Gson();
        token=session.getToken();

        this.notice = (TextView) super.findViewById(R.id.regiser_termsOfService);
        this.setImage = (TextView) super.findViewById(R.id.set_user_image);
        this.getback=(ImageButton) super.findViewById(R.id.getback);
        this.finish=(Button) super.findViewById(R.id.btn_registerFinish);
        this.userImage = (ImageView) super.findViewById(R.id.user_image);

        Bundle driver_bundle=this.getIntent().getBundleExtra("driver_register");
        if (driver_bundle!=null)
        {
            driver.setName(driver_bundle.getString("name"));
            driver.setPassword(driver_bundle.getString("password"));
            driver.setTelephone(driver_bundle.getString("telephone"));
            driver.setMachine_id(driver_bundle.getString("machine_id"));
            driver.setSite(driver_bundle.getString("address"));
        }else
        {
            commonUtil.error_hint_short("程序错误！请联系管理员！");
        }

        //服务条款
        Intent intent = new Intent(this, register_TermsofService.class);
        SpannableString span1 = new SpannableString(s1);
        SpannableString span2 = new SpannableString(s2);
        ClickableSpan clickableSpan1 = new NewClickableSpan(ContextCompat.getColor(this, R.color.colorDefault), this, intent);
        ClickableSpan clickableSpan2 = new NewClickableSpan(ContextCompat.getColor(this, R.color.colorDefault), this, intent);
        span1.setSpan(clickableSpan1, 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span2.setSpan(clickableSpan2, 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        notice.setText("注册即意味着您同意");
        notice.append(span1);
        notice.append("和");
        notice.append(span2);
        notice.setMovementMethod(LinkMovementMethod.getInstance());
        //设置Textview超链接高亮背景色为透明色
        notice.setHighlightColor(00000000);

        //设置头像本地存储路径
        tempFile= commonUtil.getPath();
        path=tempFile.getAbsolutePath()+"/temp/njdp_user_image.png";
        driver.setImage_url(path);

        //完成注册
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsSetImage==true)
                {
                    //上传头像并跳转主页面
                    register_uploadImage(url, path);
                } else {
                    // 跳转到主页面
                    Intent intent = new Intent(register_image.this, mainpages.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //返回上个界面
        getback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //弹出图片选择菜单
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1)
            return;

        switch (requestCode) {
            case REQUEST_IMAGE:
                if(resultCode == RESULT_OK){
                    // 获取返回的图片列表
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    path=paths.get(0);
                    File file=new File(path);
                    imageUri=Uri.fromFile(file);
                    cropPhoto();
                }
                break;
            case CROP_PHOTO_CODE:
                if (null != data)
                {
                    setCropImg(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //选择照片
    private void selectImage()
    {
        Intent intent = new Intent(register_image.this, MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        // 默认选择图片,回填选项(支持String ArrayList)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    //裁剪图片
    private void cropPhoto() {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", crop);
        intent.putExtra("outputY", crop);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO_CODE);
    }
    //保存裁剪的照片
    private void setCropImg(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            boolean tag= commonUtil.saveBitmap(register_image.this, mBitmap);
            if(tag)
            {
                userImage.setImageBitmap(mBitmap);
                IsSetImage=true;
            }
        }
    }

    //上传头像并注册
    private void register_uploadImage(String url,String path) {

        pDialog.setMessage("即将完成注册 ...");
        File file=new File(path);

        if (!file.exists()) {
            hideDialog();
            commonUtil.error_hint_short("头像图片不存在!请重新选择！");
            IsSetImage=false;
            return;
        } else if (!netUtil.checkNet(register_image.this)) {
            hideDialog();
            commonUtil.error_hint_short("网络连接错误");
            return;
        }else{
            fix_iamge(file);
        }
    }

    //上传用户头像
    private void fix_iamge(File file)
    {
        fixInfo_strReq= NoHttp.createJsonObjectRequest(fixInfo_url, RequestMethod.POST);
        fixInfo_strReq.add("token", token);
        fixInfo_strReq.add("person_photo",new FileBinary(file));
        fixInfo_strReq.addHeader("content-disposition","form-data");
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(this) == false) {
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_FIXINFO, fixInfo_strReq, new fixUserImage_request());
        }
    }

    //上传用户头像访问服务器监听
    private class fixUserImage_request implements OnResponseListener<JSONObject>
    {
        @Override
        public void onStart(int what) {
            showDialog();
        }

        @Override
        public void onSucceed(int what, com.yolanda.nohttp.rest.Response<JSONObject> response) {

            try {
                JSONObject jObj=response.get();
                Log.e(TAG, "上传用户头像-接收的数据：" + jObj.toString());
                int status = jObj.getInt("status");
                if (status==0) {
                    String msg=jObj.getString("result");
                    Log.e(TAG, "上传用户头像成功：" + msg);
                } else if(status==1){
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, getResources().getString(R.string.connect_service_key_err1) + errorMsg);
                    commonUtil.error_hint2_short(R.string.connect_service_key_err2);
                    //清空数据，重新登录
                    netUtil.clearSession(register_image.this);
                    Intent intent = new Intent(register_image.this, login.class);
                    finish();
                    startActivity(intent);
                    SysCloseActivity.getInstance().exit();
                }else{
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Upload UserImage Error："+getResources().getString(R.string.connect_service_err1)+ errorMsg);
                    commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err1) + errorMsg);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Upload UserImage Error：" + getResources().getString(R.string.connect_service_err2) + e.getMessage());
                commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err2) + e.getMessage());
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "Upload UserImage Error："+getResources().getString(R.string.connect_service_err3) + exception.getMessage());
            commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err3) + exception.getMessage());
        }

        @Override
        public void onFinish(int what) {
            hideDialog();
        }
    }

//    //不上传头像注册注册
//    private void register_finish(String url) {
//
//        // Tag used to cancel the request
//        String tag_string_req = "req_register_image";
//
//        pDialog.setMessage("即将完成注册 ...");
//        showDialog();
//
//        if (netUtil.checkNet(register_image.this) == false) {
//            error_hint_short("网络连接错误");
//            return;
//        }else {
//            StringRequest strReq = new StringRequest(Request.Method.POST,
//                    url,mSuccessListener,mErrorListener) {
//
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("machine_id", driver.getMachine_id());
//                    params.put("password", driver.getPassword());
//                    params.put("telephone", driver.getTelephone());
//                    return params;
//                }
//            };
//
//            // Adding request to request queue
//            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//        }
//    }

//    //注册响应服务器成功
//    private Response.Listener<String> mSuccessListener =new Response.Listener<String>() {
//
//        @Override
//        public void onResponse(String response) {
//            Log.d(TAG, "Register Response: " + response.toString());
//            hideDialog();
//
//            try {
//                JSONObject jObj = new JSONObject(response);
//                boolean error = jObj.getBoolean("error");
//                if(!error) {
//                    String token=jObj.getString("token");
//                    session.setLogin(true,token);
//                    driver.setId(1);
//                    driverDao.add(driver);
//
//                    // 跳转到主页面
//                    empty_hint(R.string.register_success);
//                    Intent intent = new Intent(register_image.this, mainpages.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    // Error occurred in registration. Get the error
//                    String errorMsg = jObj.getString("error_msg");
//                    Log.e(TAG, "Json error：response错误:" + errorMsg);
//                    commonUtil.error_hint_short( errorMsg);
//                }
//            } catch (JSONException e) {
//                empty_hint(R.string.register_error2);
//                e.printStackTrace();
//                Log.e(TAG, "RegisterError: " + e.getMessage());
//            }
//        }
//    };
//
//    //响应服务器失败
//    private Response.ErrorListener mErrorListener= new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Log.e(TAG, "RegisterError: " + error.getMessage());
//            empty_hint(R.string.connect_error);
//            hideDialog();
//        }
//    };

    //ProgressDialog显示与隐藏
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}