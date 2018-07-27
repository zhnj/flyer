package com.njdp.njdp_drivers.items.myplan.Yiwancheng;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.items.myplan.PlanBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rock on 2018/7/27.
 */

public class PlanYiwancheng extends Fragment {
    View view;
    RecyclerView rv;
    PlanBean planBean;
    LinearLayoutManager layoutManager;//纵向线性布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.plan_weiwancheng, container, false);
            try {
                initData(); //加载数据
            }catch (Exception e){
                Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void initView() {
        rv = (RecyclerView) view.findViewById(R.id.plan_rv);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        YwcAdapter adapter = new YwcAdapter(planBean.getResult());
        rv.setAdapter(adapter);

    }

    private void initData() {
        //POST网络请求
        String url= AppConfig.URL_DEPLOY;
        //定义一个StringRequest
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {// 添加请求成功监听
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), response,Toast.LENGTH_LONG).show();
                try {
                    Log.i("info",response);
                    planBean = new Gson().fromJson(response, PlanBean.class);
                    Log.i("info", "size:"+String.valueOf(planBean.getResult().size()));
                    initView(); //加载布局
                }catch (Exception e){
                    Log.i("info",response);
                }
            }


        }, new Response.ErrorListener() {// 添加请求失败监听
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "连接失败",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("type","1");
                map.put("user_id", SessionManager.getInstance().getUserId());
                return map;
            }
        };
        // 设置请求的tag标签，便于在请求队列中寻找该请求
        request.setTag("post");
        // 添加到全局的请求队列
        AppController.getHttpQueues().add(request);
    }
}
