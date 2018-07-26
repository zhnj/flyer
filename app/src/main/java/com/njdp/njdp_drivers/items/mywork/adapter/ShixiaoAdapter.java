package com.njdp.njdp_drivers.items.mywork.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.items.mywork.bean.CommentBean;
import com.njdp.njdp_drivers.items.mywork.bean.ShixiaoBean;

import java.util.List;

public class ShixiaoAdapter extends RecyclerView.Adapter<ShixiaoAdapter.ViewHolder> {
    private List<ShixiaoBean.ResultBean> list;
    private Context context;

    public ShixiaoAdapter(Context context, List<ShixiaoBean.ResultBean> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //给Adapter添加布局，bq把这个view传递给HoldView，让HoldView找到空间
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_yishixiao_work, null);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    String phone;
    String address;
    String area;
    String price;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //position为Adapter的位置，数据从list里面可以拿出来。
        try {
            address = "地址：" + list.get(position).getFarmlandsInfo().getFarmlandsProvince() + list.get(position).getFarmlandsInfo().getFarmlandsCity() + list.get(position).getFarmlandsInfo().getFarmlandsCounty() + list.get(position).getFarmlandsInfo().getFarmlandsVillage();
        }catch (Exception e){
            address = "地址：[null]";
        }
        try {
            area = "\n面积：" + list.get(position).getFarmlandsInfo().getFarmlandsArea();
        }catch (Exception e){
            area = "\n面积：[null]";
        }
        try {
            price = "\n单价：" + list.get(position).getFarmlandsInfo().getFarmlandsUnitPrice();
        }catch (Exception e){
            price = "地址：[null]";
        }
        try {
            phone = "\n电话：" + list.get(position).getFarmlandsInfo().getPersonInfoFarmerMachine().getPersonPhone();
        }catch (Exception e){
            phone = "\n电话：[无效数据控制]";
        }
        String str = address+area+price+phone;
        holder.textView.setText(str);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "当前点击的条目的地址为：" + list.get(position).getFarmlandsInfo().getFarmlandsVillage(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
            }
        });
        holder.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                int flyer_id = list.get(position).getFlyerId();
                final int farmland_id = list.get(position).getFarmlandsInfo().getId();
                String opration = "完成";
                url = AppConfig.URL_MYJOB_PART_COMMENT;
                url = url + "?flyer_id=" + flyer_id + "&farmlandId=" + farmland_id;
                Log.i("delurl", url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        CommentBean cb = new Gson().fromJson(response,CommentBean.class);
                        Toast.makeText(context, farmland_id+":"+cb.getFarmlandsPingjia(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "连接失败,检查网络", Toast.LENGTH_LONG).show();
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
                String url;
                int flyer_id = list.get(position).getFlyerId();
                int farmland_id = list.get(position).getFarmlandsInfo().getId();
                String opration = "完成";
                url = AppConfig.URL_MYJOB_PART_CHECK;
                url = url + "?flyer_id=" + flyer_id + "&farmland_id=" + farmland_id + "&opration=" + opration;
                Log.i("delurl", url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "确认完成", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "连接失败,检查网络", Toast.LENGTH_LONG).show();
                    }

                });
                // 设置请求的tag标签，便于在请求队列中寻找该请求
                request.setTag("get");
                // 添加到全局的请求队列
                AppController.getHttpQueues().add(request);
            }
        });
        holder.bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                int flyer_id = list.get(position).getFlyerId();
                int farmland_id = list.get(position).getFarmlandsInfo().getId();
                url = AppConfig.URL_MYJOB_PART_DELETE;
                url = url + "?flyer_id=" + flyer_id + "&farmland_id=" + farmland_id;
                Log.i("delurl", url);
                //定义一个StringRequest
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {// 添加请求成功监听
                    @Override
                    public void onResponse(String response) {
                        delete(position);
                        Toast.makeText(context, "删除完成", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {// 添加请求失败监听
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "连接失败,检查网络", Toast.LENGTH_LONG).show();
                        delete(position);
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
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button bt1;
        Button bt2;
        Button bt3;
        Button bt4;

        public ViewHolder(View itemView) {
            super(itemView);
            //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
            textView = (TextView) itemView.findViewById(R.id.tv_adapter);
            bt1 = (Button) itemView.findViewById(R.id.fw_bt1);
            bt2 = (Button) itemView.findViewById(R.id.fw_bt2);
            bt3 = (Button) itemView.findViewById(R.id.fw_bt3);
            bt4 = (Button) itemView.findViewById(R.id.fw_bt4);
        }
    }


    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<ShixiaoBean.ResultBean> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<ShixiaoBean.ResultBean> newList) {
        //刷新数据
        list.clear();
        //list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    // 删除数据
    public void delete(int index) {
        Log.i("size", "即将删除的条目：" + String.valueOf(index));
        list.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(0, list.size());
    }

}


