package com.njdp.njdp_drivers.items.myplan.Yiwancheng;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.items.myplan.PlanBean;
import com.njdp.njdp_drivers.items.myplan.PlanDetail_wei;
import com.njdp.njdp_drivers.items.myplan.PlanDetail_yiwancheng;

import java.util.List;

/**
 * Created by Rock on 2018/7/27.
 */

public class YwcAdapter extends RecyclerView.Adapter<YwcAdapter.ViewHolder> {
    private List<PlanBean.ResultBean> list;
    ViewGroup parent;
    String str;
    public YwcAdapter(List<PlanBean.ResultBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_yiwancheng, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String planid = "方案"+(position+1);
        String income = "\n估计收益："+list.get(position).getIncome();
        String cost = "\n估计成本："+list.get(position).getCost();
        String time = "\n作业时段："+list.get(position).getBeginDate()+" 至 "+list.get(position).getEndDate();
        holder.tv.setText(planid+income+cost+time);

        holder.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //请求详细地块
                String url;
                String plan_id = list.get(position).getPlan_id();
                url = AppConfig.URL_DEPLOY_DETAIL;
                url = url + "?plan_id=" + plan_id;
                Log.i("url", url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        Log.i("info", "详细方案信息"+response);
                        Intent intent = new Intent(parent.getContext(), PlanDetail_yiwancheng.class);
                        intent.putExtra("detail",response);
                        parent.getContext().startActivity(intent);
                        //PlanDetailBean planDetailBean = new Gson().fromJson(response,PlanDetailBean.class);
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(parent.getContext(), "连接失败,检查网络", Toast.LENGTH_LONG).show();
                    }

                });
                // 设置请求的tag标签，便于在请求队列中寻找该请求
                request.setTag("get");
                // 添加到全局的请求队列
                AppController.getHttpQueues().add(request);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list!= null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        Button bt1;

        public ViewHolder(View itemView) {
            super(itemView);
            //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
            tv = (TextView) itemView.findViewById(R.id.py_tv);
            bt1 = (Button) itemView.findViewById(R.id.py_bt1);
        }
    }

}
