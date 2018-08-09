package com.njdp.njdp_drivers.items;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Driver;
import bean.UAVCompany;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static com.njdp.njdp_drivers.util.NetUtil.TAG;
//import okhttp3.Call;



public class item_personalInformation extends Fragment implements View.OnClickListener {

    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private ProgressDialog pDialog;
    private String TAG=item_personalInformation.class.getSimpleName();
    private View parentView;//主View
    private com.njdp.njdp_drivers.CircleMenu.CircleImageView title_Image;
    private LinearLayout l_name;
    private LinearLayout l_machine_id;
    private LinearLayout l_telephone;
    private LinearLayout l_weixin;
    private LinearLayout l_qq;
    private LinearLayout l_region;
    //新添加
    private LinearLayout l_sex;
    private LinearLayout l_personsfzh;
    private LinearLayout l_personcomid;
    private LinearLayout l_manager_name;
    //private LinearLayout l_populationnum;
    //private LinearLayout l_farmlandarea;

    private TextView t_name;
    private TextView t_machine_id;
    private TextView t_telephone;
    private TextView t_weixin;
    private TextView t_qq;
    private TextView t_region;
    private TextView btn_setting;
    //新添加
    private TextView t_sex;
    private TextView t_personsfzh;
    private TextView input_manager_name;
    private  TextView input_con_indro;
    private TextView t_personcomid=null;
    private String[] uavInfo;

    private DriverDao driverDao;
    private NetUtil netUtil;
    private Driver driver;
    private CommonUtil commonUtil;
    private Gson gson;
    private SessionManager sessionManager;
    //    private ImageLoader imageLoader;
//    private ImageLoader.ImageListener imageListener;
    private String path;
    private String path_post;
    private String token;
    private String netImageUrl;
    private Bitmap userImageBitmap;
    private static int FLAG_USERIMAGE=11102002;
    private static int FLAG_GETINFO=11102000;
    private static int FLAG_FIXINFO=11102001;
    private String getInfo_url;//获取个人信息服务器地址
    private String fixInfo_url;//修改个人信息服务器地址
    private com.yolanda.nohttp.rest.Request<JSONObject> getInfo_strReq;
    private com.yolanda.nohttp.rest.Request<Bitmap> getUserImage_strReq;
    private com.yolanda.nohttp.rest.Request<JSONObject> fixInfo_strReq;

    //////////////////////////////////////照片裁剪//////////////////////////////////////////////////
    private int crop = 300;// 裁剪大小
    private static final int REQUEST_IMAGE=001;
    private static final int CROP_PHOTO_CODE = 002;
    private ArrayList<String> defaultDataArray;
    private Uri imageUri;
    UAVCompany company;
    //////////////////////////////////////照片裁剪//////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_5_personalinformation, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.driver_name).setOnClickListener(this);//公司名称
        view.findViewById(R.id.driver_machine_id).setOnClickListener(this);
        view.findViewById(R.id.driver_telephone).setOnClickListener(this);
        view.findViewById(R.id.driver_weixin).setOnClickListener(this);
        view.findViewById(R.id.driver_qq).setOnClickListener(this);
        view.findViewById(R.id.driver_region).setOnClickListener(this);
//        view.findViewById(R.id.login_out).setOnClickListener(this);
        view.findViewById(R.id.settings).setOnClickListener(this);
        //新添加
        view.findViewById(R.id.driver_sex).setOnClickListener(this);
        view.findViewById(R.id.manager_name).setOnClickListener(this);
        //view.findViewById(R.id.driver_personsfzh).setOnClickListener(this);
       // view.findViewById(R.id.driver_personcomid).setOnClickListener(this);


        this.l_name=(LinearLayout)view.findViewById(R.id.driver_name);
        this.l_machine_id=(LinearLayout)view.findViewById(R.id.driver_machine_id);
        this.l_telephone=(LinearLayout)view.findViewById(R.id.driver_telephone);
        this.l_weixin=(LinearLayout)view.findViewById(R.id.driver_weixin);
        this.l_qq=(LinearLayout)view.findViewById(R.id.driver_qq);
        this.l_region=(LinearLayout)view.findViewById(R.id.driver_region);
        //新添加
        this.l_sex=(LinearLayout)view.findViewById(R.id.driver_sex);
        this.l_personsfzh=(LinearLayout)view.findViewById(R.id.driver_personsfzh);
        this.l_manager_name=(LinearLayout)view.findViewById(R.id.manager_name);
        //this.l_personcomid=(LinearLayout)view.findViewById(R.id.driver_personcomid);
        this.input_con_indro=(TextView) view.findViewById(R.id.input_con_indro);

        this.title_Image=(com.njdp.njdp_drivers.CircleMenu.CircleImageView) view.findViewById(R.id.information_div_title_image);
        this.t_name=(TextView) view.findViewById(R.id.input_driver_name);
        this.t_machine_id =(TextView) view.findViewById(R.id.input_driver_licence_plate);
        this.t_telephone=(TextView) view.findViewById(R.id.input_driver_telephone);
        this.t_weixin =(TextView) view.findViewById(R.id.input_driver_weixin);
        this.t_qq =(TextView) view.findViewById(R.id.input_driver_qq);
        this.t_region=(TextView) view.findViewById(R.id.input_driver_region);
        title_Image.setOnClickListener(this);
        //新添加
        this.t_sex=(TextView) view.findViewById(R.id.input_driver_sex);
        this.t_personsfzh=(TextView) view.findViewById(R.id.input_person_sfzh);
        this.input_manager_name=(TextView)view.findViewById(R.id.input_manager_name);
       // this.t_personcomid= (TextView) view.findViewById(R.id.sp_person_comid);



        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        parentView = LayoutInflater.from(mainMenu).inflate(R.layout.activity_5_personalinformation, null);
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);

        driverDao=new DriverDao(getActivity());
        sessionManager=new SessionManager();
        token=sessionManager.getToken();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        try {
            driver = driverDao.getDriver(1);
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }

        getUserInfo();
        //getUAVInfor();////从服务器获取飞机服务公司信息
        setTextFix();//修改后，显示修改后的信息
        return view;
    }


    private class fixUploadSuccessListener implements  Response.Listener<String>//信息修改成功响应
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
                    commonUtil.error_hint_short("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                } else if(status==0){
                    String errorMsg=jObj.getString("result");
                    Log.e(TAG, "修改结果：" + errorMsg);
                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("保存失败,请重试");
                }
            } catch (JSONException e) {
                Log.e(TAG, "2 Json error：response错误" + e.getMessage());
                commonUtil.error_hint_short("保存失败,请重试" );
            }
        }
    }

    public void onClick(View v) {
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
                mainMenu.fix_info_title="修改公司名称";
                mainMenu.fix_info_flag=1;
                mainMenu.t_fix_title="修改公司名称";
                mainMenu.t_fix_hint="请输入公司名称";
                mainMenu.addBackFragment(new item_personalinformation_1_fix_info());
                break;
            case R.id.driver_machine_id:;
                break;
            case R.id.driver_telephone:
                mainMenu.fix_info_flag=2;
                break;
            case R.id.driver_weixin:
                mainMenu.fix_info_title="修改微信号";
                mainMenu.fix_info_flag=3;
                mainMenu.t_fix_title="修改微信号";
                mainMenu.t_fix_hint="请输入微信号";
                mainMenu.addBackFragment(new item_personalinformation_1_fix_info());
                break;
            case R.id.driver_qq:
                mainMenu.fix_info_title="修改QQ号";
                mainMenu.fix_info_flag=4;
                mainMenu.t_fix_title="修改QQ号";
                mainMenu.t_fix_hint="请输入QQ号";
                mainMenu.addBackFragment(new item_personalinformation_1_fix_info());
                break;
            case R.id.driver_region:
                mainMenu.fix_info_title="修改公司地址";
                mainMenu.fix_info_flag=7;
                mainMenu.t_fix_title="修改公司地址";
                mainMenu.t_fix_hint="请输入公司地址";
                mainMenu.addBackFragment(new item_personalinformation_1_fix_info());
                break;
//            case R.id.login_out:
//                Log.e(TAG, "退出登录");
//                netUtil.clearSession(mainMenu);
//                mainMenu.backLogin();
//                SysCloseActivity.getInstance().exit();
//                break;
            case R.id.settings:
                mainMenu.addBackFragment(new item_personalinformation_setting());
                break;
            //新添加//员工总数
            case R.id.driver_sex:
                mainMenu.fix_info_title="修改员工总数";
                mainMenu.fix_info_flag=5;
                mainMenu.t_fix_title="修改员工总数";
                mainMenu.t_fix_hint="请输入员工总数";
                mainMenu.addBackFragment(new item_personalinformation_1_fix_info());
                break;
            case R.id.driver_personsfzh:
                mainMenu.fix_info_title="修改身份证号";
                mainMenu.fix_info_flag=6;
                mainMenu.t_fix_title="修改身份证号";
                mainMenu.t_fix_hint="请输入身份证号";
                mainMenu.addBackFragment(new item_personalinformation_1_fix_info());
                break;
            default:
                break;
        }
    }



//    //监听返回按键
//    @Override
//    public void onResume() {
//        super.onResume();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                    if (fix_popup_flag) {
//                        fix_popup.dismiss();
//                        return true;
//                    } else {
//                        mainMenu.finish();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//    }

//    //初始化用户信息
//    private void intiData(final Driver driver) {
//
//        String tag_string_req = "req_login";
//
//        mainMenu.pDialog.setMessage("正在载入数据 ...");
//        mainMenu.showDialog();
//
//        if (netUtil.checkNet(mainMenu) == false) {
//            mainMenu.hideDialog();
//            commonUtil.error_hint_short("网络连接错误");
//            return;
//        } else {
//
//            //服务器请求
//            StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
//                    AppConfig.URL_QUERYPERSONINFO, new mSuccessListener(), mainMenu.mErrorListener) {
//
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("token", token);
//
//                    return netUtil.checkParams(params);
//                }
//            };
//
//            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//        }
//    }
//
//    //初始化用户信息响应服务器成功
//    private class mSuccessListener implements Response.Listener<String>{
//
//        @Override
//        public void onResponse(String response) {
//            mainMenu.hideDialog();
//
//            try {
//                Log.e(TAG, "PersonalInformation Response: " + response.toString());
//                JSONObject jObj = new JSONObject(response);
//                int status = jObj.getInt("status");
//
//                if (status==1) {
//
//                    String errorMsg = jObj.getString("result");
//                    Log.e(TAG, "Json error：response错误:" + errorMsg);
//                    commonUtil.error_hint_short("密钥失效，请重新登录");
//                    //清空数据，重新登录
//                    netUtil.clearSession(mainMenu);
//                    Intent intent = new Intent(mainMenu, login.class);
//                    startActivity(intent);
//                    mainMenu.finish();
//                } else if(status==0){
//
//                    JSONObject s_driver = jObj.getJSONObject("result");
//                    netImageUrl=s_driver.getString("person_photo");
//                    driver.setName(s_driver.getString("person_name"));
//                    driver.setTelephone(s_driver.getString("person_phone"));
//                    driver.setWechart(s_driver.getString("person_weixin"));
//                    driver.setQQ(s_driver.getString("person_qq"));
//                    driver.setSite(commonUtil.transferSite(s_driver.getString("person_address")));
//                    driver.setId(1);
//                    path=null;
//                    path=commonUtil.imageTempFile();
//                    driver.setImage_url(path);//设置头像本地存储路径
//                    getImage(AppConfig.URL_IP + netImageUrl);
//
//                } else {
//
//                    String errorMsg = jObj.getString("result");
//                    Log.e(TAG, "1 Json error：获取用户数据失败-response错误:" + errorMsg);
//                }
//            } catch (JSONException e) {
//                Log.e(TAG, "2 Json error：获取用户数据失败-response错误" + e.getMessage());
//            }
//        }
//    };


//    private void initFixPopup()//初始化修改信息弹窗
//    {
//        fix_popup = new PopupWindow(fixView, ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        fix_popup.setAnimationStyle(R.style.slideAnimation_bottom);
//        fix_popup.setFocusable(true);
//        fix_popup.setBackgroundDrawable(new ColorDrawable(0x55000000));
//    }


    @Override
    public void onDestroy() {
        commonUtil.destroyBitmap(userImageBitmap);//销毁bitmap
        super.onDestroy();
    }

    private void showDriverInfo(Driver driver)//只显示用户基本信息
    {
        t_name.setText(driver.getName());
        t_machine_id.setText(driver.getMachine_id());
        t_telephone.setText(driver.getTelephone());
        t_weixin.setText(driver.getWechart());
        t_qq.setText(driver.getQQ());
        t_region.setText(driver.getSite_0());
        //新添加
        t_sex.setText(driver.getSex());
        t_personsfzh.setText(driver.getSfzh());



    }

    private void showDriverData(UAVCompany driver)//显示用户基本信息和头像
    {
        //driverDao.update(driver);
        t_name.setText(driver.getCom_name());//公司名称
        t_machine_id.setText(driver.getUav_num());//无人机数量
        t_telephone.setText(driver.getPerson_phone());
        t_weixin.setText(driver.getPersion_weixin());
        t_qq.setText(driver.getPerson_qq());
        t_region.setText(driver.getCom_addr());
        //新添加
        t_sex.setText(driver.getStaff_num());//员工总数
        t_personsfzh.setText(driver.getPerson_sfzh());
        input_con_indro.setText(driver.getCom_memo());
        input_manager_name.setText(driver.getCom_charge());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1)
            return;

        switch (requestCode) {
            case REQUEST_IMAGE:
                if(resultCode == mainMenu.RESULT_OK){
                    path_post=null;
                    // 获取返回的图片列表
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    path_post=paths.get(0);
                    File file=new File(path_post);
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

    private void selectImage()//选择照片
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

    private void cropPhoto()//裁剪图片
    {
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

    private void setCropImg(Intent picdata)//保存裁剪的照片
    {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            boolean tag= commonUtil.saveBitmap(mBitmap);
            if(tag)
            {
                title_Image.setImageBitmap(mBitmap);
                fix_iamge(new File(path_post));
                //上传头像
            }else {
                title_Image.setImageDrawable(ContextCompat.getDrawable(mainMenu,R.drawable.turnplate_center));
                //上传失败，默认头像
            }
        }
    }

    private void getUserInfo()//获取用户基本信息--无人机公司
    {
        pDialog.setMessage("正在载入数据 ...");
        getInfo_strReq= NoHttp.createJsonObjectRequest(AppConfig.URL_findFlyComByUser, RequestMethod.POST);
        //getInfo_strReq.add("token", token);
        getInfo_strReq.add("fm_id", sessionManager.getUserId());
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(mainMenu) == false) {
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_GETINFO, getInfo_strReq, new getUserInfo_request());
        }
    }

    private class getUserInfo_request implements OnResponseListener<JSONObject>//获取用户基本信息访问服务器监听
    {
        @Override
        public void onStart(int what) {
            showDialog();
        }

        @Override
        public void onSucceed(int what, com.yolanda.nohttp.rest.Response<JSONObject> response) {

            //{"fm_id":"9",
            // "person_qq":"888888",
            // "person_weixin":"zhihuinongji1",
            // "com_name":"天鹏航空2",
            // "com_addr":"保定市高新区大学科技园",
            // "staff_num":"66",
            // "com_memo":"全商用服务、航模、无人机科普技能教育培训、无人机驾驶专业技术教育培训",
            // "com_longitude":"115.4709990134",
            // "com_latitude":"38.9233110455",
            // "person_sfzh":"99812345678DX",
            // "uav_num",10}

            try {
                JSONObject jObj=response.get();
                Log.e(TAG, "获取用户基本信息-接收的数据：" + jObj.toString());
                int status = jObj.getInt("status");
                if (status==0) {
                    JSONArray location = jObj.getJSONArray("result");
                    JSONObject  s_driver=location.getJSONObject(0);
                    company = new UAVCompany();
                    netImageUrl=s_driver.getString("person_photo");
                    company.setCom_name(s_driver.getString("com_name"));
                    company.setPerson_sfzh(s_driver.getString("person_sfzh"));
                    company.setCom_addr(s_driver.getString("com_addr"));
                    company.setStaff_num(s_driver.getString("staff_num"));
                    company.setPersion_weixin(s_driver.getString("person_weixin"));
                   company.setUav_num(s_driver.getString("uav_num"));
                    company.setPerson_qq(s_driver.getString("person_qq"));
                    company.setPerson_phone(s_driver.getString("person_phone"));
                    company.setCom_charge(s_driver.getString("com_charge"));
                    company.setCom_memo(s_driver.getString("com_memo"));
                    showDriverData(company);//显示个人信息
                    path=null;
                    path=commonUtil.imageTempFile();
                    driver.setImage_url(path);//设置头像本地存储路径
                    getImage(AppConfig.URL_IP + netImageUrl);//从服务器获取个人头像图片
                } else if(status==1){
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, getResources().getString(R.string.connect_service_key_err1) + errorMsg);
                    commonUtil.error_hint2_short(R.string.connect_service_key_err2);
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                }else{
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Get UserInfo Error："+getResources().getString(R.string.connect_service_err1)+ errorMsg);
                    commonUtil.error_hint_short(getResources().getString(R.string.connect_service_err1) + errorMsg);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Get UserInfo Error：" + getResources().getString(R.string.connect_service_err2) + e.getMessage());
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

    private void getImage(String url)//获取用户头像
    {
//        imageLoader.get(url, imageListener, 300, 300);
//        title_Image.setDrawingCacheEnabled(true);
//        Bitmap mbitmap=Bitmap.createBitmap(title_Image.getDrawingCache());
//        title_Image.setDrawingCacheEnabled(false);
//        title_Image.setImageBitmap(mbitmap);
//        showDriverData(driver);
        getUserImage_strReq = NoHttp.createImageRequest(url);
        getUserImage_strReq.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(mainMenu) == false) {
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_USERIMAGE, getUserImage_strReq, new getUserImage_request());
        }
    }

    private class getUserImage_request implements OnResponseListener<Bitmap> //获取用户头像访问服务器监听
    {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, com.yolanda.nohttp.rest.Response<Bitmap> response) {
            Log.e(TAG, getResources().getString(R.string.user_image_success));
            userImageBitmap = commonUtil.zoomBitmap(response.get(), 300, 300);//裁剪
            title_Image.setImageBitmap(userImageBitmap);//显示头像图片
            commonUtil.saveBitmap_noCrop(userImageBitmap);//保存到本地
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e(TAG, "Get UserImage Error："+getResources().getString(R.string.user_image_err) + exception.getMessage());
            commonUtil.error_hint_short(getResources().getString(R.string.user_image_err) + exception.getMessage());
        }

        @Override
        public void onFinish(int what) {

        }
    }

    private void fix_iamge(File file)//上传用户头像
    {
        fixInfo_strReq= NoHttp.createJsonObjectRequest(fixInfo_url, RequestMethod.POST);
        fixInfo_strReq.add("token", token);
        fixInfo_strReq.add("person_photo",new FileBinary(file));
        fixInfo_strReq.addHeader("content-disposition","form-data");
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        if (netUtil.checkNet(mainMenu) == false) {
            commonUtil.error_hint_short("网络连接错误");
            return;
        } else {
            requestQueue.add(FLAG_FIXINFO, fixInfo_strReq, new fixUserImage_request());
        }
//        OkHttpUtils.post()
//                .url( AppConfig.URL_FIXPERSONINFO)
//                .addParams("token", token)
//                .addFile("person_photo", "userimage.png",file)
//                .addHeader("content-disposition","form-data")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        Log.e(TAG, "3 Connect Error: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.e(TAG, "UploadImage:" + response);
//                            JSONObject jObj = new JSONObject(response);
//                            int status = jObj.getInt("status");
//                            if (status == 0) {
//                                String msg=jObj.getString("result");
//                                Log.e(TAG, "UploadImage response：" + msg);
//                            } else {
//                                String errorMsg = jObj.getString("result");
//                                Log.e(TAG, "1 Json error：response错误：" + errorMsg);
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, "2 Json error：response错误： " + e.getMessage());
//                        }
//                    }
//                });
    }

    private class fixUserImage_request implements OnResponseListener<JSONObject>//上传用户头像访问服务器监听
    {
        @Override
        public void onStart(int what) {
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
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
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

        }
    }

    private void setTextFix()//完成修改后，显示到界面
    {
        if(mainMenu.change_info_flag)
        {
            switch (mainMenu.fix_info_flag)
            {
                case 1:
                    t_name.setText(mainMenu.fix_info);
                    break;
                case 3:
                    t_weixin.setText(mainMenu.fix_info);
                    break;
                case 4:
                    t_qq.setText(mainMenu.fix_info);
                    break;
                case 5:
                    t_sex.setText(mainMenu.fix_info);
                    break;
                case 6:
                    t_personsfzh.setText(mainMenu.fix_info);
                    break;
            }
            mainMenu.change_info_flag=false;
        }
    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}