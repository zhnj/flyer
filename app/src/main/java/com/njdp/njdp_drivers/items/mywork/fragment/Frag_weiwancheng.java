package com.njdp.njdp_drivers.items.mywork.fragment;

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
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.items.mywork.adapter.WeiwanchengAdapter;
import com.njdp.njdp_drivers.items.mywork.bean.WorkBean;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;


public class Frag_weiwancheng extends Fragment {
    WeiwanchengAdapter adapter;
    RefreshLayout refreshLayout;
    View view;
    WorkBean workBean;
    Gson gson = new Gson();
    List<WorkBean.ResultBean> resultBeanList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.new_fragment_weiwancheng, container, false);
            refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
            initData();
            setPullRefresher();
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
                getData(true);

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //模拟网络请求到的数据
                curr += 1;
                getData(false);
                //在这里执行下拉加载时的具体操作(网络请求、更新UI等)

            }
        });
    }

    RecyclerView rv;
    LinearLayoutManager layoutManager;//纵向线性布局
    int curr;
    String url;

    //此方法作用于初始化页面和下拉刷新数据
    private void initData() {
        curr = 1;//当前页数为1
        url = AppConfig.URL_MYJOB_PART;
        url = url + "?pageNo=" + curr + "&limit=3&flyer_id="+flyer_id+"&work_state=1";
        Log.i("url",url);
        //定义一个StringRequest
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
            @Override
            public void onResponse(String response) {
                rv = (RecyclerView) view.findViewById(R.id.recyclerView);
                layoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(layoutManager);
                workBean = gson.fromJson(response, WorkBean.class);
                if(workBean.isStates()){
                    resultBeanList = workBean.getResult();
                    adapter = new WeiwanchengAdapter(getContext(),resultBeanList);
                    rv.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(),workBean.getMsg(), Toast.LENGTH_SHORT).show();
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

    String flyer_id = SessionManager.getInstance().getUserId();
    private void getData(final boolean isRefresh) {
        url = AppConfig.URL_MYJOB_PART;
        url = url + "?pageNo=" + curr + "&limit=3&flyer_id="+flyer_id+"&work_state=1";
        Log.i("url", url);
        //定义一个StringRequest
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
            @Override
            public void onResponse(String response) {
                workBean = gson.fromJson(response, WorkBean.class);
                if(workBean.isStates()){
                    resultBeanList = workBean.getResult();
                    if (isRefresh) {
                        adapter.refresh(resultBeanList);
                        refreshLayout.finishRefresh(/*,false*/);
                        //不传时间则立即停止刷新    传入false表示刷新失败
                    } else {
                        adapter.add(resultBeanList);
                        refreshLayout.finishLoadMore(/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
                    }
                }else{
                    Toast.makeText(getContext(),workBean.getMsg(), Toast.LENGTH_SHORT).show();
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
}