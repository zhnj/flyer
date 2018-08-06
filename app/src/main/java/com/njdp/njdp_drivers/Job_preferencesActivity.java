package com.njdp.njdp_drivers;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;

import org.json.JSONObject;

import static com.njdp.njdp_drivers.R.id.id;
import static com.njdp.njdp_drivers.R.id.remark;
import static com.njdp.njdp_drivers.R.id.uavhotelcost;
import static com.njdp.njdp_drivers.R.id.uavlaborcost;
import static com.njdp.njdp_drivers.R.id.uavrunfuel;
import static com.njdp.njdp_drivers.R.id.uavrunvelocity;
import static com.njdp.njdp_drivers.R.id.uavworkcost;
import static com.njdp.njdp_drivers.R.id.uavworktime;

public class Job_preferencesActivity extends AppCompatActivity {

    private com.yolanda.nohttp.rest.Request<JSONObject> prefrence;
    private String pre_url;

    EditText text_uav_work_time;
    EditText text_uav_hotel_cost;
    EditText text_uav_labor_cost;
    EditText text_uav_run_fuel;
    EditText text_uav_work_cost;
    EditText text_uav_run_velocity;
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

        setContentView(R.layout.activity_job_preferences);
        initData();
        text_uav_work_time = (EditText)findViewById(uavworktime);
        text_uav_hotel_cost = (EditText)findViewById(R.id.uavhotelcost);
        text_uav_labor_cost = (EditText)findViewById(R.id.uavlaborcost);
        text_uav_run_fuel = (EditText)findViewById(R.id.uavrunfuel);
        text_uav_work_cost = (EditText)findViewById(R.id.uavworkcost);
        text_uav_run_velocity = (EditText)findViewById(R.id.uavrunvelocity);
        Button btn = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new BtnListener());
        pre_url=AppConfig.URL_PREFERE;
    }

    private void initData() {
        String url = AppConfig.URL_UAV_Preference;;
        url+= "?fmId="+SessionManager.getInstance().getUserId();
        //定义一个StringRequest
        Log.e("url", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {// 添加请求成功监听

                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int status = jObj.getInt("states");
                            if (status==1)
                            {

                                text_uav_work_time.setText(jObj.getJSONObject("result").getString("uavWorkTime"));
                                text_uav_hotel_cost.setText(jObj.getJSONObject("result").getString("uavHotelCost"));
                                text_uav_labor_cost.setText(jObj.getJSONObject("result").getString("uavLaborCost"));
                                text_uav_run_fuel.setText(jObj.getJSONObject("result").getString("uavRunFuel"));
                                text_uav_work_cost.setText(jObj.getJSONObject("result").getString("uavWorkCost"));
                                text_uav_run_velocity.setText(jObj.getJSONObject("result").getString("uavRunVelocity"));
                            }else {

                                String errorMsg = jObj.getString("result");
                                Log.e("重新登录", "Json error：response错误:" + errorMsg);

                                SysCloseActivity.getInstance().exit();


                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Job_preferencesActivity.this, error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
        // 设置请求的tag标签，便于在请求队列中寻找该请求
        request.setTag("lhdGet");
        // 添加到全局的请求队列
        AppController.getHttpQueues().add(request);
    }

    public class BtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
                //prefrence= NoHttp.createJsonObjectRequest(pre_url, RequestMethod.GET);
            String work_time = text_uav_work_time.getText().toString().trim();
            String hotel_cost = text_uav_hotel_cost.getText().toString().trim();
            String labor_cost = text_uav_labor_cost.getText().toString().trim();
            String run_fuel= text_uav_run_fuel.getText().toString().trim();
            String work_cost = text_uav_work_cost.getText().toString().trim();
            String run_velocity = text_uav_run_velocity.getText().toString().trim();
            //定义一个url
            SessionManager sessionManager = new SessionManager();
            String url = AppConfig.URL_UAV_ADD_OR_EDIT_Preference;
            url+= "?fmId="+SessionManager.getInstance().getUserId()+"&";
            url+= "uavRunVelocity="+run_velocity+"&";
            url+= "uavRunFuel="+run_fuel+"&";
            url+= "uavWorkTime="+work_time+"&";
            url+= "uavWorkCost="+work_cost+"&";
            url+= "uavLaborCost="+labor_cost+"&";
            url+= "uavHotelCost="+hotel_cost;
            //定义一个StringRequest
            StringRequest request = new StringRequest(Request.Method.GET, url, new
                    Response.Listener<String>() {// 添加请求成功监听

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj=new JSONObject(response);
                                if(obj.getBoolean("states")==true)
                                             Toast.makeText(Job_preferencesActivity.this, "偏好保存成功！",
                                        Toast.LENGTH_LONG).show();
                            }
                           catch (Exception e)
                           {
                               e.printStackTrace();
                           }


                        }
                    }, new Response.ErrorListener() {// 添加请求失败监听
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Job_preferencesActivity.this, error.toString(),
                            Toast.LENGTH_LONG).show();
                }
            });
            // 设置请求的tag标签，便于在请求队列中寻找该请求
            request.setTag("lhdGet");
            // 添加到全局的请求队列
            AppController.getHttpQueues().add(request);


        }
    }
}
