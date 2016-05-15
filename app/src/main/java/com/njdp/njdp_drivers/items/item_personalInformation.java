package com.njdp.njdp_drivers.items;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.LruBitmapCache;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.Driver;

import static com.njdp.njdp_drivers.util.NetUtil.TAG;


public class item_personalInformation extends Fragment implements View.OnClickListener {

    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private ImageView title_Image;
    private TextView title_name;
    private TextView t_name;
    private TextView t_licence_plate;
    private TextView t_telephone;
    private TextView t_weixin;
    private TextView t_qq;
    private TextView t_gender;
    private TextView t_region;
    private LruBitmapCache loadImage;
    //    private SQLiteHandler db;
    private DriverDao driverDao;
    private NetUtil netUtil;
    private Driver driver;
    private CommonUtil commonUtil;
    private Gson gson;
    private SessionManager sessionManager;
    private String path;
    private String token;
    private String machine_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_5_personalinformation, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        this.title_Image=(ImageView) view.findViewById(R.id.information_div_title_image);
        this.title_name=(TextView) view.findViewById(R.id.information_div_title_name);
        this.t_name=(TextView) view.findViewById(R.id.input_driver_name);
        this.t_licence_plate=(TextView) view.findViewById(R.id.input_driver_licence_plate);
        this.t_telephone=(TextView) view.findViewById(R.id.input_driver_telephone);
        this.t_weixin =(TextView) view.findViewById(R.id.input_driver_weixin);
        this.t_qq =(TextView) view.findViewById(R.id.input_driver_qq);
        this.t_gender=(TextView) view.findViewById(R.id.input_driver_gender);
        this.t_region=(TextView) view.findViewById(R.id.input_driver_region);
        title_Image.setOnClickListener(this);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;
        driverDao=new DriverDao(getActivity());
        sessionManager=new SessionManager(getActivity());
        token=sessionManager.getToken();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        loadImage=new LruBitmapCache();


        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            Log.e(TAG, machine_id);
        }catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }

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
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.information_div_title_image:
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
                    mainMenu.finish();
                    return true;
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
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    AppConfig.URL_LOGIN, mSuccessListener, mainMenu.mErrorListener) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("machine_id", driver.getMachine_id());
                    params.put("token", token);

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
            Log.i("tagconvertstr", "[" + response + "]");
            Log.d(mainMenu.TAG, "PersonalInformation Response: " + response.toString());
            mainMenu.hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");

                if (!error) {

                    // Now store the user in SQLite
                    JSONObject s_driver = jObj.getJSONObject("Driver");
                    driver.setName(s_driver.getString("Name"));
                    driver.setTelephone(s_driver.getString("Telephone"));
                    driver.setMachine_id(s_driver.getString("License_plater"));
                    driver.setWechart(s_driver.getString("Wechart"));
                    driver.setQQ(s_driver.getString("QQ"));
                    driver.setProvince(s_driver.getString("Province"));
                    driver.setCity(s_driver.getString("City"));
                    driver.setCounty(s_driver.getString("County"));
                    driver.setVillage(s_driver.getString("Village"));
                    driver.setId(1);

                    // Inserting row in users table
//                    db.addDriver(driver);
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

    //显示用户数据
    private void showDriverData(Driver driver)
    {
        Bitmap bitmap = loadImage.getBitmap(driver.getImage_url());
        Bitmap zooBitmap=commonUtil.zoomBitmap(bitmap,300,300);
        commonUtil.saveBitmap(mainMenu, bitmap);
        title_Image.setImageBitmap(zooBitmap);
        title_name.setText(driver.getName());
        t_name.setText(driver.getName());
        t_licence_plate.setText(driver.getMachine_id());
        t_telephone.setText(driver.getTelephone());
        t_gender.setText(driver.getTelephone());
        t_weixin.setText(driver.getWechart());
        t_qq.setText(driver.getQQ());
        t_region.setText(driver.getSite());
    }
}