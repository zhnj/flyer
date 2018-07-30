package com.njdp.njdp_drivers.items.jikai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;

import java.util.ArrayList;
import java.util.List;

import com.njdp.njdp_drivers.items.jikai.bean.DroneBean;

/*
* 我的无人机修改页面
* */
public class Item_editor_machine extends AppCompatActivity {
    private String type;
    private TextView textView;
    //首先还是先声明这个Spinner控件
    private Spinner spinner;
    private List<DroneBean.ScheduleUavNormsBean> typeList = new ArrayList<>();
    //定义一个String类型的List数组作为数据源
    private List<String> dataList;

    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter;
    private boolean state = false;
    EditText id;
    EditText remark;
    EditText ability;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drone_editor);

        id = (EditText) findViewById(R.id.drone_et_id);
        remark = (EditText) findViewById(R.id.drone_et_remark);
        ability = (EditText) findViewById(R.id.drone_et_ability);
        spinner = (Spinner) findViewById(R.id.drone_spinner);
        textView = (TextView) findViewById(R.id.drone_editor_tv_display);

        isNull();
        initData();
    }
    DroneBean droneBean;
    Gson gson = new Gson();
    private void initData() {
        String url = AppConfig.DRONE_DISPALY;
        url = url + "?pageNo=" + 1 + "&limit=1&fmId="+SessionManager.getInstance().getUserId();

        Log.i("url",url);
        //定义一个StringRequest
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
            @Override
            public void onResponse(String response) {
                droneBean = gson.fromJson(response,DroneBean.class);
                typeList = droneBean.getScheduleUavNorms();
                Log.i("aaa","typeList"+typeList.size());
                initView();
            }
        }, new Response.ErrorListener() {// 添加请求失败监听
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Item_editor_machine.this, "连接失败", Toast.LENGTH_LONG).show();
            }
        });
        request.setTag("get");
        AppController.getHttpQueues().add(request);
    }
    private void initView() {



        //为dataList赋值，将下面这些数据添加到数据源中
        dataList = new ArrayList<String>();
        for(int i=0;i<typeList.size();i++){
            dataList.add(typeList.get(i).getBrandId());
        }


        /*为spinner定义适配器，也就是将数据源存入adapter，这里需要三个参数
        1. 第一个是Context（当前上下文），这里就是this
        2. 第二个是spinner的布局样式，这里用android系统提供的一个样式
        3. 第三个就是spinner的数据源，这里就是dataList*/
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList);

        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        spinner.setAdapter(adapter);

        if (state) {
            //设置spinner的默认值
            spinner.setSelection(xinghao - 1, true);
            type = String.valueOf(xinghao);
            textView.setText("无人机ID："+typeList.get(xinghao-1).getId()
                    +"\n无人机型号："+typeList.get(xinghao-1).getBrandId());
        }
        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = String.valueOf(position+1);
                textView.setText("无人机ID："+typeList.get(position).getId()
                        +"\n无人机型号："+typeList.get(position).getBrandId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //textView.setText("请选择您的");
            }
        });


        Button commit = (Button) findViewById(R.id.drone_editor_bt);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发布/修改 无人机
                String url = AppConfig.DRONE_ADDorUPDATE;
                String norm = type;
                String machineId = id.getText().toString().trim();
                String schMachineRemark = remark.getText().toString().trim();
                String workingAbility = ability.getText().toString().trim();
                String fmId = SessionManager.getInstance().getUserId();
                url += "?norm="+norm+"&machineId="+machineId+"&schMachineRemark="+schMachineRemark+"&workingAbility="+workingAbility+"&fmId="+fmId;
                if (state){
                    url += "&id="+data_id;
                }
                Log.i("url",state+url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("操作成功")){
                            Toast.makeText(Item_editor_machine.this, "上传成功", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Item_editor_machine.this, "连接失败", Toast.LENGTH_LONG).show();
                    }
                });
                request.setTag("get");
                AppController.getHttpQueues().add(request);
            }
        });
    }

    int data_id;
    int xinghao;
    public void isNull() {
            Intent intent=getIntent();
            String bianhao =intent.getStringExtra("bianhao");
        if (bianhao!=null){
            String nengli =intent.getStringExtra("nengli");
            String beizhu =intent.getStringExtra("beizhu");
            xinghao = intent.getIntExtra("xinghao",0);
            data_id = intent.getIntExtra("id",0);
            state = true;
            Log.i("display","editor:"+bianhao+nengli+beizhu+xinghao+data_id);
            id.setText(bianhao);
            remark.setText(beizhu);
            ability.setText(nengli);
        }
    }
}

