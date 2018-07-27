package com.njdp.njdp_drivers.items.myplan.Weiwancheng;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.njdp.njdp_drivers.items.myplan.PlanDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rock on 2018/7/27.
 */

public class WwcAdapter extends RecyclerView.Adapter<WwcAdapter.ViewHolder> {
    private List<PlanBean.ResultBean> list;
    ViewGroup parent;
    String str;
    public WwcAdapter(List<PlanBean.ResultBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_weiwancheng, null);
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
                Intent intent = new Intent(parent.getContext(), PlanDetail.class);
                parent.getContext().startActivity(intent);
            }
        });
        holder.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成
                String url;
                String flyer_id = "";
                String farmland_id = "";
                String opration = "完成";
                url = AppConfig.URL_MYJOB_PART_CHECK;
                url = url + "?flyer_id=" + flyer_id + "&farmland_id=" + farmland_id + "&opration=" + opration;
                Log.i("delurl", url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(parent.getContext(), "确认完成", Toast.LENGTH_SHORT).show();
                        delete(position);
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
        holder.bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                //POST网络请求
                String url= AppConfig.URL_MYJOB_PLAN_DELETE;
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        delete(position);
                        Toast.makeText(parent.getContext(), "删除完成", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(parent.getContext(), "连接失败,检查网络", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("user_id",SessionManager.getInstance().getUserId());
                        map.put("plan_id",list.get(position).getPlan_id());
                        return map;
                    }
                };
                // 设置请求的tag标签，便于在请求队列中寻找该请求
                request.setTag("post");
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
        Button bt2;
        Button bt3;

        public ViewHolder(View itemView) {
            super(itemView);
            //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
            tv = (TextView) itemView.findViewById(R.id.pw_tv);
            bt1 = (Button) itemView.findViewById(R.id.pw_bt1);
            bt2 = (Button) itemView.findViewById(R.id.pw_bt2);
            bt3 = (Button) itemView.findViewById(R.id.pw_bt3);
        }
    }
    // 删除数据
    public void delete(int index) {
        Log.i("size", "即将删除的条目：" + String.valueOf(index));
        list.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(0, list.size());
    }

}
