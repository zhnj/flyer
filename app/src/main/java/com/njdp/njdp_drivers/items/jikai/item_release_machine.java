package com.njdp.njdp_drivers.items.jikai;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.MyRefAdapter;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import com.njdp.njdp_drivers.items.jikai.bean.DroneBean;

/*
* 我的无人机修改页面
* */
public class item_release_machine extends Fragment implements View.OnClickListener {
    RefreshLayout refreshLayout;
    DroneBean droneBean;
    Gson gson = new Gson();
    List<DroneBean.ResultBean> resultBeanList;
    View view;
    private slidingMenu mainMenu;
    RecyclerView rv;
    LinearLayoutManager layoutManager;//纵向线性布局
    int curr;
    String url;
    MyRefAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.drone_list, container, false);
            refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
            try {
                initData();
            }catch (Exception e){
                Toast.makeText(getContext(),"连接服务器失败",Toast.LENGTH_SHORT).show();
            }
            view.findViewById(R.id.drone_add).setOnClickListener(this);
            view.findViewById(R.id.drone_back).setOnClickListener(this);
            view.findViewById(R.id.drone_set).setOnClickListener(this);
            setPullRefresher();
            mainMenu=(slidingMenu)getActivity();
        }

        return view;
    }

    private void setPullRefresher() {
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                curr = 1;
                try {
                    getData(true);
                }catch (Exception e){
                    Toast.makeText(getContext(),"连接服务器失败",Toast.LENGTH_SHORT).show();
                }

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //模拟网络请求到的数据
                curr += 1;
                try {
                    getData(false);
                }catch (Exception e){
                    Toast.makeText(getContext(),"连接服务器失败",Toast.LENGTH_SHORT).show();
                }
                //在这里执行下拉加载时的具体操作(网络请求、更新UI等)

            }
        });
    }

    //此方法作用于初始化页面和下拉刷新数据
    private void initData() {
        curr = 1;//当前页数为1
        url = AppConfig.DRONE_DISPALY;
        url = url + "?pageNo=" + curr + "&limit=3&fmId="+fmId;
        Log.i("url",url);
        //定义一个StringRequest
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
            @Override
            public void onResponse(String response) {
                rv = (RecyclerView) view.findViewById(R.id.recyclerView);
                layoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(layoutManager);
                droneBean = gson.fromJson(response, DroneBean.class);
                if(droneBean.isStates()){
                    resultBeanList = droneBean.getResult();
                    Log.i("url", "resultBeanList"+resultBeanList.get(0).getMachineId());
                    adapter = new MyRefAdapter(getContext(),resultBeanList);
                    rv.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(),droneBean.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {// 添加请求失败监听
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "连接失败", Toast.LENGTH_LONG).show();
            }

        });
        // 设置请求的tag标签，便于在请求队列中寻找该请求
        request.setTag("get");
        // 添加到全局的请求队列
        AppController.getHttpQueues().add(request);
    }

    String fmId = SessionManager.getInstance().getUserId();
    //String fmId = "9";
    private void getData(final boolean isRefresh) {
        url = AppConfig.DRONE_DISPALY;
        url = url + "?pageNo=" + curr + "&limit=3&fmId="+fmId;
        //Log.i("url", url);
        //定义一个StringRequest
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
            @Override
            public void onResponse(String response) {
                droneBean = gson.fromJson(response, DroneBean.class);
                if(droneBean.isStates()){
                    resultBeanList = droneBean.getResult();
                    Log.i("url", "before"+resultBeanList.get(0).getMachineId());
                    if (isRefresh) {
                        Log.i("url", "after"+resultBeanList.get(0).getMachineId());
                        adapter.refresh(resultBeanList);
                        refreshLayout.finishRefresh(/*,false*/);
                        //不传时间则立即停止刷新    传入false表示刷新失败
                    } else {
                        adapter.add(resultBeanList);
                        refreshLayout.finishLoadMore(/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
                    }
                }else{
                    Toast.makeText(getContext(),droneBean.getMsg(),Toast.LENGTH_SHORT).show();
                    if (isRefresh) {
                        refreshLayout.finishRefresh(/*,false*/);
                    }else{
                        refreshLayout.finishLoadMore(/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
                    }
                }
            }
        }, new Response.ErrorListener() {// 添加请求失败监听
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "连接失败", Toast.LENGTH_LONG).show();
                Log.i("url", error.toString());
                if (isRefresh) {
                    refreshLayout.finishRefresh(/*,false*/);
                    //不传时间则立即停止刷新    传入false表示刷新失败
                } else {
                    refreshLayout.finishLoadMore(/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
                }
            }

        });
        // 设置请求的tag标签，便于在请求队列中寻找该请求
        request.setTag("get");
        // 添加到全局的请求队列
        AppController.getHttpQueues().add(request);
    }
    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.drone_add:
                    Intent intent = new Intent(getContext(), Item_editor_machine.class);
                    getContext().startActivity(intent);
                case R.id.drone_back:
                    //mainMenu.finish();
                    break;
                case R.id.drone_set:
                    //Intent intent1 = new Intent(getContext(), Job_preferencesActivity.class);
                    //getContext().startActivity(intent1);
                    break;
                default:
                    break;
            }
    }
}