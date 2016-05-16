package com.njdp.njdp_drivers.items;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.LruBitmapCache;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Driver;
import bean.FieldInfo;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

import static com.njdp.njdp_drivers.util.NetUtil.TAG;


public class item_personalInformation extends Fragment implements View.OnClickListener {

    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private View parentView;//主View
    ///////////////////////////////////////用户修改信息弹窗////////////////////////////////////////
    private PopupWindow fix_popup;
    private View fixView;
    private boolean fix_popup_flag=false;
    private EditText edt_fix_input;
    private TextView t_fix_hint;
    private TextView t_fix_title;
    private com.beardedhen.androidbootstrap.BootstrapButton btn_fix_save;
    private int fix_info_flag;
    private AwesomeValidation fixValidation=new AwesomeValidation(ValidationStyle.BASIC);
    ///////////////////////////////////////用户修改信息弹窗////////////////////////////////////////
    private ImageView title_Image;
    private LinearLayout l_name;
    private LinearLayout l_machine_id;
    private LinearLayout l_telephone;
    private LinearLayout l_weixin;
    private LinearLayout l_qq;
    private LinearLayout l_region;
    private TextView t_name;
    private TextView t_machine_id;
    private TextView t_telephone;
    private TextView t_weixin;
    private TextView t_qq;
    private TextView t_region;
    private LruBitmapCache loadImage;
    private DriverDao driverDao;
    private NetUtil netUtil;
    private Driver driver;
    private CommonUtil commonUtil;
    private Gson gson;
    private SessionManager sessionManager;
    private File tempFile;
    private String path;
    private String token;
    private String netImageUrl;
    private String fix_info_title;
    private String fix_info;//需要修改的个人信息
    private Map<String,String> fix_params=new HashMap<String,String>();

    //////////////////////////////////////照片裁剪//////////////////////////////////////////////////
    private int crop = 300;// 裁剪大小
    private static final int REQUEST_IMAGE=001;
    private static final int CROP_PHOTO_CODE = 002;
    private ArrayList<String> defaultDataArray;
    private Uri imageUri;
    private String Url_Image;
    private String Url;
    //////////////////////////////////////照片裁剪//////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_5_personalinformation, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.driver_name).setOnClickListener(this);
        view.findViewById(R.id.driver_machine_id).setOnClickListener(this);
        view.findViewById(R.id.driver_telephone).setOnClickListener(this);
        view.findViewById(R.id.driver_weixin).setOnClickListener(this);
        view.findViewById(R.id.driver_qq).setOnClickListener(this);
        view.findViewById(R.id.driver_region).setOnClickListener(this);
        this.l_name=(LinearLayout)view.findViewById(R.id.driver_name);
        this.l_machine_id=(LinearLayout)view.findViewById(R.id.driver_machine_id);
        this.l_telephone=(LinearLayout)view.findViewById(R.id.driver_telephone);
        this.l_weixin=(LinearLayout)view.findViewById(R.id.driver_weixin);
        this.l_qq=(LinearLayout)view.findViewById(R.id.driver_qq);
        this.l_region=(LinearLayout)view.findViewById(R.id.driver_region);
        this.title_Image=(ImageView) view.findViewById(R.id.information_div_title_image);
        this.t_name=(TextView) view.findViewById(R.id.input_driver_name);
        this.t_machine_id =(TextView) view.findViewById(R.id.input_driver_licence_plate);
        this.t_telephone=(TextView) view.findViewById(R.id.input_driver_telephone);
        this.t_weixin =(TextView) view.findViewById(R.id.input_driver_weixin);
        this.t_qq =(TextView) view.findViewById(R.id.input_driver_qq);
        this.t_region=(TextView) view.findViewById(R.id.input_driver_region);
        title_Image.setOnClickListener(this);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        parentView = LayoutInflater.from(mainMenu).inflate(R.layout.activity_5_personalinformation, null);

        fixView = mainMenu.getLayoutInflater().inflate(R.layout.fix_personal_info_pop, null);
        initFixPopup();
        fixView.findViewById(R.id.fix_save_change).setOnClickListener(this);
        this.edt_fix_input =(EditText)fixView.findViewById(R.id.fix_input_info);
        this.t_fix_hint=(TextView)fixView.findViewById(R.id.fix_hint_info);
        this.t_fix_title=(TextView)fixView.findViewById(R.id.fix_title);
        this.btn_fix_save=(com.beardedhen.androidbootstrap.BootstrapButton)fixView.findViewById(R.id.fix_save_change);
        btn_fix_save.setClickable(false);
        btn_fix_save.setEnabled(false);
        btn_fix_save.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!commonUtil.isempty(edt_fix_input))
                {
                    btn_fix_save.setClickable(true);
                    btn_fix_save.setEnabled(true);
                }else
                {
                    btn_fix_save.setClickable(false);
                    btn_fix_save.setEnabled(false);
                }
            }
        });//监听输入内容，判断是否禁用保存按钮
        fixView.findViewById(R.id.fix_getback).setOnClickListener(this);
        fix_popup.setOnDismissListener(new fixPopDisListener());

        driverDao=new DriverDao(getActivity());
        sessionManager=new SessionManager(getActivity());
        token=sessionManager.getToken();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        loadImage=new LruBitmapCache();

        try {
            driver = driverDao.getDriver(1);
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
        if(driver!=null) {
            try {
                showDriverData(driver);
                path = driver.getImage_url();
                if (path != null)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    title_Image.setImageBitmap(bitmap);
                }else {
                    intiData(driver);
                }
            }catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG,e.toString());
            }
        }else {
            intiData(driver);
        }
        return view;
    }

    public void onClick(View v) {
        fix_params.clear();
        fixValidation.clear();
        fix_params.put("token", token);
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.information_div_title_image:
                selectImage();
                break;
            case R.id.driver_name:
                fix_info_flag=1;
                t_fix_title.setText("修改姓名");
                t_fix_hint.setText("请输入姓名");
                fixValidation.addValidation(edt_fix_input, "^[\\u4e00-\\u9fa5]+$", getResources().getString(R.string.err_name));
                fix_popup.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                break;
            case R.id.driver_machine_id:
//                fix_info_title="修改农机编号";
//                fix_info="请输入农机编号";
                break;
            case R.id.driver_telephone:
                fix_info_flag=2;
//                fix_info_title="修改手机号";
//                fix_info="请输入新的手机号";
                break;
            case R.id.driver_weixin:
                fix_info_flag=3;
                t_fix_title.setText("修改微信号");
                t_fix_hint.setText("请输入微信号");
                fixValidation.addValidation(edt_fix_input, "^[a-zA-Z\\d_]{5,}$", "请输入正确的微信号");
                fix_popup.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                break;
            case R.id.driver_qq:
                fix_info_flag=4;
                t_fix_title.setText("修改QQ号");
                t_fix_hint.setText("请输入QQ号");
                fixValidation.addValidation(edt_fix_input, "[1-9][0-9]{4,14}", "请输入正确的QQ号");
                fix_popup.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                break;
            case R.id.driver_region:
                fix_params.put("person_photo", fix_info);
                break;
            case R.id.fix_getback:
                fix_popup.dismiss();
                break;
            case R.id.fix_save_change:
                fix_info=edt_fix_input.getText().toString().trim();
                switch (fix_info_flag)
                {
                    case 1:
                        fix_params.put("person_name", fix_info);
                        break;
                    case 3:
                        fix_params.put("person_weixin", fix_info);
                        break;
                    case 4:
                        fix_params.put("person_qq:", fix_info);
                        break;
                }
                check_fix_info();
                break;
            default:
                break;
        }
    }

    //监听返回按键
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (fix_popup_flag) {
                        fix_popup.dismiss();
                        return true;
                    } else {
                        mainMenu.finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    //初始化用户信息
    private void intiData(final Driver driver) {

        String tag_string_req = "req_login";

        mainMenu.pDialog.setMessage("正在载入数据 ...");
        mainMenu.showDialog();

        if (netUtil.checkNet(mainMenu) == false) {
            mainMenu.hideDialog();
            commonUtil.error_hint("网络连接错误");
            return;
        } else {

            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_QUERYPERSONINFO, new mSuccessListener(), mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);

                    return netUtil.checkParams(params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    //初始化用户信息响应服务器成功
    private class mSuccessListener implements Response.Listener<String>{

        @Override
        public void onResponse(String response) {
            Log.i("tagconvertstr", "[" + response + "]");
            Log.d(mainMenu.TAG, "PersonalInformation Response: " + response.toString());
            mainMenu.hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");

                if (!error) {

                    // Now store the user in SQLite
                    JSONObject s_driver = jObj.getJSONObject("result");
                    netImageUrl=s_driver.getString("person_photo");
                    driver.setName(s_driver.getString("person_name"));
                    driver.setTelephone(s_driver.getString("person_phone"));
                    driver.setWechart(s_driver.getString("person_weixin"));
                    driver.setQQ(s_driver.getString("person_qq"));
                    driver.setSite(s_driver.getString("person_address"));
                    driver.setId(1);
                    tempFile= commonUtil.getPath();
                    path=tempFile.getAbsolutePath()+"/temp/njdp_user_image.png";
                    driver.setImage_url(path);//设置头像本地存储路径

                    driverDao.add(driver);
                    showDriverData(driver);

                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    commonUtil.error_hint(errorMsg);
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                commonUtil.error_hint("Json error：response错误！" + e.getMessage());
            }
        }
    };

    private void check_fix_info()
    {
        if(commonUtil.isempty(edt_fix_input)){
            commonUtil.error_hint("请输入完整的信息");
        }else if (fixValidation.validate())
        {
            uploadInfo();
        }
    }

    private void uploadInfo()//上传修改用户信息
    {
        String tag_string_req = "req_fix_info";
        if (netUtil.checkNet(mainMenu) == false) {
            mainMenu.hideDialog();
            commonUtil.error_hint("网络连接错误");
            return;
        } else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_FIXPERSONINFO, new fixSuccessListener(), mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {
                    return netUtil.checkParams(fix_params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private class fixSuccessListener implements  Response.Listener<String>//信息修改成功响应
    {

        @Override
        public void onResponse(String response) {
            try {
                Log.e("PersonalInfo fix_back",response);
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");

                if (status==1) {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    Intent intent = new Intent(mainMenu, login.class);
                    startActivity(intent);mainMenu.finish();
                } else if(status==0){

                    String errorMsg=jObj.getString("result");
                    Log.e(TAG,"修改结果："+errorMsg);
                    switch (fix_info_flag)
                    {
                        case 1:
                            t_name.setText(fix_info);
                            break;
                        case 3:
                            t_weixin.setText(fix_info);
                            break;
                        case 4:
                            t_qq.setText(fix_info);
                            break;
                    }
                    fix_popup.dismiss();
                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("保存失败请重试");
                }
            } catch (JSONException e) {
                Log.e(TAG, "2 Json error：response错误" + e.getMessage());
                commonUtil.error_hint("保存失败重试" );
            }
        }
    }

    private void showDriverData(Driver driver)//显示用户数据
    {
        Bitmap bitmap = loadImage.getBitmap(netImageUrl);
        Bitmap zooBitmap=commonUtil.zoomBitmap(bitmap,300,300);
        commonUtil.saveBitmap(mainMenu, bitmap);
        title_Image.setImageBitmap(zooBitmap);
        t_name.setText(driver.getName());
        t_machine_id.setText(driver.getMachine_id());
        t_telephone.setText(driver.getTelephone());
        t_weixin.setText(driver.getWechart());
        t_qq.setText(driver.getQQ());
        t_region.setText(driver.getSite());
    }

    private class fixPopDisListener implements PopupWindow.OnDismissListener//发布按钮弹出时监听dismiss后背景变回原样
    {
        @Override
        public void onDismiss() {
            fix_popup_flag=false;
        }
    }

    private void initFixPopup()//初始化修改信息弹窗
    {
        fix_popup = new PopupWindow(fixView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        fix_popup.setAnimationStyle(R.style.slideAnimation_bottom);
        fix_popup.setFocusable(true);
        fix_popup.setBackgroundDrawable(new ColorDrawable(0x55000000));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1)
            return;

        switch (requestCode) {
            case REQUEST_IMAGE:
                if(resultCode == mainMenu.RESULT_OK){
                    // 获取返回的图片列表
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    path=paths.get(0);
                    File file=new File(path);
                    imageUri= Uri.fromFile(file);
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
        Intent intent = new Intent(mainMenu, MultiImageSelectorActivity.class);
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
            boolean tag= commonUtil.saveBitmap(mainMenu, mBitmap);
            if(tag)
            {
                title_Image.setImageBitmap(mBitmap);
                fix_iamge(new File(path));
                //上传头像
            }
        }
    }

    private void fix_iamge(File file)
    {
        OkHttpUtils.post()
                .url( AppConfig.URL_FIXPERSONINFO)
                .params(fix_params)
                .addFile("person_photo", "njdp_user_image.png",file)
                .addHeader("content-disposition","form-data")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, "3 Connect Error: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e(TAG, "UploadImage:" + response);
                            JSONObject jObj = new JSONObject(response);
                            int status = jObj.getInt("status");
                            if (status == 0) {
                                String msg=jObj.getString("result");
                                Log.e(TAG, "UploadImage response：" + msg);
                            } else {
                                String errorMsg = jObj.getString("error_msg");
                                Log.e(TAG, "1 Json error：response错误：" + errorMsg);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "2 Json error：response错误： " + e.getMessage());
                        }
                    }
                });
    }
}