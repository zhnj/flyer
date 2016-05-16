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
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

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
    private String Url_Image;
    private String Url;
    private int crop = 300;// 裁剪大小
    private static final int REQUEST_IMAGE=001;
    private static final int CROP_PHOTO_CODE = 002;
    private ArrayList<String> defaultDataArray;
    public boolean IsSetImage=false;
    private CommonUtil commonUtil;
    private static final String TAG = register.class.getSimpleName();
    private File tempFile;
    private String path;//用户头像路径
    private Gson gson = new Gson();
    private boolean imageError;
    private boolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_image);

        //修改
        Url=AppConfig.URL_REGISTER;
        Url_Image=AppConfig.URL_GETPASSWORD2;
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());// Session manager
//        db = new SQLiteHandler(getApplicationContext());
        driverDao=new DriverDao(register_image.this);
        commonUtil=new CommonUtil(register_image.this);
        netUtil=new NetUtil(register_image.this);

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
        }else
        {
            error_hint("程序错误！请联系管理员！");
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
                    //上传头像.并注册
                    register_uploadImage(Url_Image, path);
                } else {
                    //默认头像，注册
                    register_finish(Url);
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
        showDialog();

        File file=new File(path);
        Map<String, String> params = new HashMap<String, String>();
        params.put("machine_id", driver.getMachine_id());
        params.put("password", driver.getPassword());
        params.put("telephone", driver.getTelephone());
        params.put("imageurl", driver.getImage_url());
        params.put("setImage", "true");
        params.put("tag", "M");

        if (!file.exists()) {
            hideDialog();
            Toast.makeText(register_image.this, "头像图片不存在!请重新选择！", Toast.LENGTH_SHORT).show();
            IsSetImage=false;
            return;
        } else if (!netUtil.checkNet(register_image.this)) {
            hideDialog();
            error_hint("网络连接错误");
            return;
        }else {
            OkHttpUtils.post()
                    .addFile("imageFile", "njdp_user_image.png", file)
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new Callback() {

                        //xml格式数据解析
                        @Override
                        public Object parseNetworkResponse(okhttp3.Response response) throws Exception {
                            return null;
                        }

                        //连接服务器错误
                        @Override
                        public void onError(Call call, Exception e) {

                            Log.e(TAG, "Register Connect Error: " + e.getMessage());
                            empty_hint(R.string.connect_error);
                            hideDialog();
                        }
                        //Json数据解析
                        @Override
                        public void onResponse(Object response) {

                            try {
                                String result=String.valueOf(response);
                                JSONObject jObj = new JSONObject(result);
                                boolean error = jObj.getBoolean("error");
                                boolean imageError = jObj.getBoolean("imageError");
                                if (!imageError) {
                                    Log.e(TAG, "Register UploadImage Error: " + response);
                                    empty_hint(R.string.register_error3);
                                    hideDialog();
                                } else if (!error) {
                                    String errorMsg = jObj.getString("error_msg");
                                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                                    commonUtil.error_hint(errorMsg);
                                    hideDialog();
                                } else {
                                    String token=jObj.getString("token");
                                    session.setLogin(true,token);
                                    driver.setId(1);
                                    driverDao.add(driver);

                                    empty_hint(R.string.register_success);
                                    // 跳转到主页面
                                    Intent intent = new Intent(register_image.this, mainpages.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }catch (Exception e)
                            {
                                empty_hint(R.string.register_error2);
                                e.printStackTrace();
                                Log.e(TAG, "Register Error: " + e.getMessage());
                            }
                        }
                    });
        }
    }


    //不上传头像注册注册
    private void register_finish(String url) {

        // Tag used to cancel the request
        String tag_string_req = "req_register_image";

        pDialog.setMessage("即将完成注册 ...");
        showDialog();

        if (netUtil.checkNet(register_image.this) == false) {
            error_hint("网络连接错误");
            return;
        }else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url,mSuccessListener,mErrorListener) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("machine_id", driver.getMachine_id());
                    params.put("password", driver.getPassword());
                    params.put("telephone", driver.getTelephone());
                    params.put("imageurl", driver.getImage_url());
                    params.put("setImage", "false");
                    params.put("tag", "M");
                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }


    //注册响应服务器成功
    private Response.Listener<String> mSuccessListener =new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d(TAG, "Register Response: " + response.toString());
            hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if(!error) {
                    String token=jObj.getString("token");
                    session.setLogin(true,token);
                    driver.setId(1);
                    driverDao.add(driver);

                    // 跳转到主页面
                    empty_hint(R.string.register_success);
                    Intent intent = new Intent(register_image.this, mainpages.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error occurred in registration. Get the error
                    String errorMsg = jObj.getString("error_msg");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint( errorMsg);
                }
            } catch (JSONException e) {
                empty_hint(R.string.register_error2);
                e.printStackTrace();
                Log.e(TAG, "RegisterError: " + e.getMessage());
            }
        }
    };

    //响应服务器失败
    private Response.ErrorListener mErrorListener= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "RegisterError: " + error.getMessage());
            empty_hint(R.string.connect_error);
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

    //信息未输入提示
    private void empty_hint(int in) {
        Toast toast = Toast.makeText(register_image.this, getResources().getString(in), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

    //错误信息提示
    private void error_hint(String str) {
        Toast toast = Toast.makeText(register_image.this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

}