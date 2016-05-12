package com.njdp.njdp_drivers;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
    private com.njdp.njdp_drivers.CircleMenu.CircleImageView user_image;
    private int index;
    private String token;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private LruBitmapCache loadImage;
    private String path;//用户头像路径
    private String userTempFile;
    private Driver driver;
    private Bitmap bitmap;
    private Bitmap zooBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        driverDao=new DriverDao(getApplicationContext());
        savedFieldInfoDao=new SavedFieldInfoDao(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        loadImage=new LruBitmapCache();
        commonUtil=new CommonUtil(slidingMenu.this);
        netUtil=new NetUtil(slidingMenu.this);
        user_image=(com.njdp.njdp_drivers.CircleMenu.CircleImageView)findViewById(R.id.user_image);//用户头像ImageView
        token=session.getToken();
        init();

        fragment = null;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
//            fragment = new item_repair_station();
//            addOrShowFragment(fragment);
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
    private void init()
    {
        try {
            driver = driverDao.getDriver(1);
        }catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }
        if(driver!=null) {
            path = driver.getImage_url();
            if (path != null) {
                try {
                    bitmap = BitmapFactory.decodeFile(path);
                    zooBitmap=commonUtil.zoomBitmap(bitmap, 300, 300);
                    user_image.setImageBitmap(zooBitmap);
                }catch (Exception e)
                {
                    Log.e(TAG,e.toString());
                }
            } else {
                path=commonUtil.imageTempFile();
                driver.setImage_url(path);
                driver.setId(1);
                driverDao.update(driver);
                getImage();
                try {
                    bitmap = loadImage.getBitmap(userTempFile);
                    zooBitmap = commonUtil.zoomBitmap(bitmap, 300, 300);
                    user_image.setImageBitmap(zooBitmap);
                    commonUtil.saveBitmap(slidingMenu.this, zooBitmap);
                }catch (Exception e)
                {
                    Log.e(TAG,e.toString());
                }
            }
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
//                item6 = new item_personalInformation();
//                addOrShowFragment(item5);
                break;
            case 6:
//                item7= new item_repair_station();
//                addOrShowFragment(item7);
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

    //获取数据库头像
    private void getImage() {

        String tag_string_req = "req_slidingMenu";

        if (netUtil.checkNet(slidingMenu.this) == false) {
            commonUtil.error_hint("网络连接错误");
            return;
        } else {
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_LOGIN, mSuccessListener,mErrorListener) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("machine_id", driver.getMachine_id());

                    return netUtil.checkParams(params);
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    //响应服务器成功
    private Response.Listener<String> mSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.e(TAG, "SlidingMenu Response: " + response.toString());
            hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");

                if (!error) {

                    userTempFile = jObj.getString("ImageUrl");
                    Log.e(TAG,"Storage Successful:"+userTempFile);
                } else {

                    String errorMsg = jObj.getString("error_msg");
                    Log.e(TAG,errorMsg);
                    commonUtil.error_hint(errorMsg);
                }
            } catch (JSONException e) {
                // JSON error
                Log.e(TAG, "ConnectService Error" + e.toString());
            }
        }
    };

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

}