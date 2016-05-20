package com.njdp.njdp_drivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.FieldInfoDao;
import com.njdp.njdp_drivers.db.LruBitmapCache;
import com.njdp.njdp_drivers.db.SavedFieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.items.item_intelligent_resolution;
import com.njdp.njdp_drivers.items.item_intelligent_resolution_3;
import com.njdp.njdp_drivers.items.item_job_history;
import com.njdp.njdp_drivers.items.item_personalInformation;
import com.njdp.njdp_drivers.items.item_query_requirement;
import com.njdp.njdp_drivers.items.item_release_machine;
import com.njdp.njdp_drivers.items.item_repair_station;
import com.njdp.njdp_drivers.items.item_service_object;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bean.BasicDeployRes;
import bean.Driver;
import bean.FieldInfo;
import bean.FieldInfoPost;

import static com.njdp.njdp_drivers.util.NetUtil.TAG;

public class slidingMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ProgressDialog pDialog;
    public SessionManager session;
    private DriverDao driverDao;
    private SavedFieldInfoDao savedFieldInfoDao;
    private int count;
    public static final String TAG =slidingMenu.class.getSimpleName();
    public DrawerLayout drawer;
    private Fragment fragment,mContent,item1,item2,item3,item4,item5,item6,item7,item8;
    private DrawerLayout menu;
    private com.njdp.njdp_drivers.CircleMenu.CircleImageView title_Image;
    private int index;
    private String token;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private String path;//用户头像路径
    private Driver driver;
    private Bitmap bitmap;
    private Bitmap zooBitmap;
    private ImageLoader imageLoader;
    private ImageLoader.ImageListener imageListener;
    private String netImageUrl;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);

        title_Image=(com.njdp.njdp_drivers.CircleMenu.CircleImageView)findViewById(R.id.user_image);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        driverDao=new DriverDao(getApplicationContext());
        savedFieldInfoDao=new SavedFieldInfoDao(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        commonUtil=new CommonUtil(slidingMenu.this);
        netUtil=new NetUtil(slidingMenu.this);

        fragment = null;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_sliding_menu, null);
        navigationView.addHeaderView(headerView);
        title_Image=(com.njdp.njdp_drivers.CircleMenu.CircleImageView)headerView.findViewById(R.id.user_image);//用户头像ImageView
        imageLoader=AppController.getInstance().getImageLoader();
        imageListener=ImageLoader.getImageListener(title_Image, R.drawable.turnplate_center,R.drawable.turnplate_center);
        token=session.getToken();
        initUserInfo();
        intiData(driver);

        Bundle index_Bundle = this.getIntent().getBundleExtra("index_bundle");
        index=index_Bundle.getInt("index");
        interfaceJump(index);

    }

    public List<FieldInfo> fieldInfos=new ArrayList<FieldInfo>();//需求查询用到的农田信息

    ////////////////////////////////////智能调度农田信息//////////////////////////////////////
    public List<FieldInfoPost> fieldInfoPosts=new ArrayList<FieldInfoPost>();//返回农田距离排序
    public List<FieldInfo> selectedFieldInfo=new ArrayList<FieldInfo>();//排好序列的农田,默认全选
    public int deploy_tag;//选择的导航方案的标志1，2，3，4，5
    public List<BasicDeployRes> basicDeployRess=new ArrayList<BasicDeployRes>();//调配方案
    public List<FieldInfo> deploy_1=new ArrayList<FieldInfo>();//方案1
    public List<FieldInfo> deploy_2=new ArrayList<FieldInfo>();//方案2
    public List<FieldInfo> deploy_3=new ArrayList<FieldInfo>();//方案3
    public List<FieldInfo> deploy_4=new ArrayList<FieldInfo>();//方案4
    public List<FieldInfo> deploy_5=new ArrayList<FieldInfo>();//方案5
    ////////////////////////////////////智能调度农田信息//////////////////////////////////////

    ////////////////////////////////////需求查询//////////////////////////////////////////////
    public String s_query_machinceType;
    public String s_query_cropType;
    public String s_query_area;
    public String s_startDate;
    public String s_endDate;
    ////////////////////////////////////需求查询//////////////////////////////////////////////
    public int fix_info_flag;//确定信息修改的内容类别
    public String fix_info_title;
    public String fix_info;//需要修改的个人信息
    public String t_fix_title;
    public String t_fix_hint;
    public boolean change_info_flag=false;//是否修改信息的标志

    public boolean hintPopup_flag=true;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sliding_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.i("ccccccccc",String.valueOf(id)+"ssssss");
        if (id == R.id.item_1) {
            fragment = new item_release_machine();
            addOrShowFragment(fragment);
        } else if (id == R.id.item_2) {
            fragment = new item_intelligent_resolution();
            addOrShowFragment(fragment);
        } else if (id == R.id.item_3) {
            fragment = new item_query_requirement();
            addOrShowFragment(fragment);
        } else if (id == R.id.item_4) {
            fragment = new item_service_object();
            addOrShowFragment(fragment);
        } else if (id == R.id.item_5) {
            fragment = new item_job_history();
            addOrShowFragment(fragment);
        } else if (id == R.id.item_6) {
            //
        } else if (id == R.id.item_7) {

        }else if (id == R.id.item_8) {
//            fragment = new item_personalInformation();
//            addOrShowFragment(fragment);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        /**
         *
         * 备注：销毁Bitmap
         *
         */
        if (null != bitmap && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        if (null != bitmap && !bitmap.isRecycled()){
            zooBitmap.recycle();
            zooBitmap = null;
        }
        super.onDestroy();
    }

    //加载用户头像
    private void initUserInfo()
    {
        try {
            driver = driverDao.getDriver(1);
        }catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        if(driver.getTelephone()!=null) {
            try {
                path = driver.getImage_url();
                if (path != null)
                {
                    bitmap = BitmapFactory.decodeFile(path);
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
    }

    //界面跳转
    private void interfaceJump(int index)
    {
        switch (index) {
            case 0:
                item1 = new item_release_machine();
                addOrShowFragment(item1);
                break;
            case 1:
                count=savedFieldInfoDao.countOfField();
                if(count>=1)
                {
                    item2 = new item_intelligent_resolution_3();
                }
                else
                {
                    item2 = new item_intelligent_resolution();
                }
                addOrShowFragment(item2);
                break;
            case 2:
                item3 = new item_query_requirement();
                addOrShowFragment(item3);
                break;
            case 3:
                item4 = new item_service_object();
                addOrShowFragment(item4);
                break;
            case 4:
                item5 = new item_job_history();
                addOrShowFragment(item5);
                break;
            case 5:
                item6 = new item_personalInformation();
                addOrShowFragment(item6);
                break;
            case 6:
                item6 = new item_repair_station();
                addOrShowFragment(item6);
                break;
            case 7:
                //加油站
                break;
        }
    }

    //切换Fragment
    public void addOrShowFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenu, fragment).commit();
        mContent = fragment;
    }
    //切换Fragment,可以返回
    public void addBackFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenu, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
        mContent = fragment;
    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //初始化用户信息
    private void intiData(final Driver driver) {

        String tag_string_req = "req_login";

        pDialog.setMessage("正在载入数据 ...");
        showDialog();

        if (netUtil.checkNet(slidingMenu.this) == false) {
            hideDialog();
            commonUtil.error_hint("网络连接错误");
            return;
        } else {

            //服务器请求
            StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                    AppConfig.URL_QUERYPERSONINFO, new mSuccessListener(), mErrorListener) {

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
            hideDialog();

            try {
                Log.e(TAG, "PersonalInformation Response: " + response.toString());
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");

                if (status==1) {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(slidingMenu.this);
                    Intent intent = new Intent(slidingMenu.this, login.class);
                    startActivity(intent);
                    slidingMenu.this.finish();
                } else if(status==0){

                    JSONObject s_driver = jObj.getJSONObject("result");
                    netImageUrl=s_driver.getString("person_photo");
                    driver.setName(s_driver.getString("person_name"));
                    driver.setTelephone(s_driver.getString("person_phone"));
                    driver.setWechart(s_driver.getString("person_weixin"));
                    driver.setQQ(s_driver.getString("person_qq"));
                    driver.setSite(commonUtil.transferSite(s_driver.getString("person_address")));
                    driver.setId(1);
                    path=commonUtil.imageTempFile();
                    driver.setImage_url(path);//设置头像本地存储路径
                    try{//有问题，ImageView设置
                    getImage(AppConfig.URL_IP + netImageUrl);
                    }catch (Exception e){
                        e.printStackTrace();}

                } else {

                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：获取用户数据失败-response错误:" + errorMsg);
                }
            } catch (JSONException e) {
                Log.e(TAG, "2 Json error：获取用户数据失败-response错误" + e.getMessage());
            }
        }
    };

    private void getImage(String url)//获取用户头像
    {
        imageLoader.get(url, imageListener, 300, 300);
        title_Image.setDrawingCacheEnabled(true);
        Bitmap mbitmap=Bitmap.createBitmap(title_Image.getDrawingCache());
        title_Image.setDrawingCacheEnabled(false);
        showDriverData(driver,mbitmap);
    }

    private void showDriverData(Driver driver,Bitmap mbitmap)//显示用户基本信息和头像
    {
        commonUtil.saveBitmap_noCrop(slidingMenu.this, mbitmap);//保存到本地
        title_Image.setImageBitmap(commonUtil.zoomBitmap(mbitmap, 300, 300));//裁剪
        driverDao.update(driver);
        commonUtil.destroyBitmap(mbitmap);
    }


    //响应服务器失败
    public Response.ErrorListener mErrorListener = new Response.ErrorListener()  {

        @Override
        public void onErrorResponse(VolleyError error) {
            netUtil.testVolley(error);
            Log.e(TAG, "3 ConncectService Error错误!");
            Log.e("GET-ERROR", error.getMessage(), error);
            try{
                byte[] htmlBodyBytes = error.networkResponse.data;
                Log.e("GET-ERROR", new String(htmlBodyBytes), error);
            } catch (Exception e){}
            commonUtil.error_hint("服务器连接失败！");
            hideDialog();
        }
    };


    //清空缓存农田数据
    public void clearFieldData()
    {
        try {
            selectedFieldInfo.clear();
        }catch (Exception e){}
        try {
            fieldInfoPosts.clear();
        }catch (Exception e){}
    }

}