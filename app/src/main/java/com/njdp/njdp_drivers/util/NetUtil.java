package com.njdp.njdp_drivers.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.FieldInfoDao;
import com.njdp.njdp_drivers.db.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bean.Driver;
import bean.FieldInfo;

/**
 * 工具类，检查当前网络状态
 */
public class NetUtil {

    public static final String TAG = NetUtil.class.getSimpleName();
    private boolean ISCONNECTED;
    private Context context;
    private CommonUtil commonUtil=new CommonUtil(context);

    public NetUtil(Context context)
    {
        this.context = context;
    }

    //判断是否连接网络
    public boolean checkNet(Context context) {

        // 获取手机所以连接管理对象（包括wi-fi，net等连接的管理）
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            // 网络管理连接对象
            NetworkInfo info = conn.getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                // 判断当前网络是否连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    //判断是否连接wifi
    public boolean isWifi(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //判断是否连接服务器
    public  boolean isConnected() {
        this.context =context;
        String tag_string_req = "req_connect";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_IP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "[" + response + "]");
                        Log.d(TAG, "Login Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                ISCONNECTED = true;
                            } else {
                                ISCONNECTED = false;
                                String errorMsg = jObj.getString("error_msg");
                                Log.e(TAG,errorMsg);
                                commonUtil.error_hint_short( "服务器连接失败");
                            }
                        } catch (JSONException e) {
                            ISCONNECTED = false;
                            e.printStackTrace();
                            Log.e(TAG, "Json error：response错误！" + e.getMessage());
                            commonUtil.error_hint_short("服务器连接失败");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Connect Error: " + error.getMessage());
                commonUtil.error_hint_short("服务器连接失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Connect_Test","C");
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return ISCONNECTED;
    }

    //测试volley的响应失败的错误类型
    public void testVolley(VolleyError error)
    {
        if (error instanceof TimeoutError) {
            Log.e("Volley", "TimeoutError");
        }else if(error instanceof NoConnectionError){
            Log.e("Volley", "NoConnectionError");
        } else if (error instanceof AuthFailureError) {
            Log.e("Volley", "AuthFailureError");
        } else if (error instanceof ServerError) {
            Log.e("Volley", "ServerError");
        } else if (error instanceof NetworkError) {
            Log.e("Volley", "NetworkError");
        } else if (error instanceof ParseError) {
            Log.e("Volley", "ParseError");
        }
    }

    //清空用户数据
    public void clearSession(Context context)
    {
        //清空session数据
        new SessionManager().setLogin(false,"");
        //清空sqlliter数据
        DriverDao driverDao=new DriverDao(context);
        FieldInfoDao fieldInfoDao=new FieldInfoDao(context);
        try
        {
            Driver driver=driverDao.getDriver(1);
            driverDao.delete(driver);
        }catch (Exception e)
        {
            Log.e(TAG,"ormlite Error:"+e.getMessage());
        }
        try
        {
            List<FieldInfo> fieldInfoList=fieldInfoDao.allFieldInfo();
            for(int i=0;i<fieldInfoList.size();i++)
            {
                fieldInfoDao.delete(fieldInfoList.get(i));
            }
        }catch (Exception e)
        {
            Log.e(TAG,"ormlite Error:"+e.getMessage());
        }

    }

    /**
     *
     * 检测params，是否为null；
     * Volley post请求不能将数据中的null 转换成" " ,
     * 所以我们在使用过程中需要将null转换
     *
     */
    public Map<String, String> checkParams (Map<String, String> map)
    {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

}
