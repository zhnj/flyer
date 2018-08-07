package com.njdp.njdp_drivers.changeDefault;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.njdp.njdp_drivers.items.jikai.Item_editor_machine;

import java.util.List;

import com.njdp.njdp_drivers.items.jikai.bean.DroneBean;


public class MyRefAdapter extends RecyclerView.Adapter<MyRefAdapter.ViewHolder> {
    private List<DroneBean.ResultBean> list;
    private Context context;
    ViewGroup parent;
    public MyRefAdapter(Context context, List<DroneBean.ResultBean> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //给Adapter添加布局，bq把这个view传递给HoldView，让HoldView找到空间
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drone_item, null);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    String phone;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //position为Adapter的位置，数据从list里面可以拿出来。
        phone = "13018100000" + position;
        String str = "无人机编号：" + list.get(position).getMachineId()
                + "\n无人机型号：" + list.get(position).getScheduleUavNorm().getBrandId()
                + "\n作业能力：" + list.get(position).getWorkingAbility()+"亩/分钟"
                + "\n备注：" + list.get(position).getSchMachineRemark();
        holder.textView.setText(str);
//        holder.bt1.setOnClickListener(new View.OnClickListener() {
//            //查看 ， 跳转到一个新的页面进行信息查看
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        holder.bt2.setOnClickListener(new View.OnClickListener() {
            //修改 , 跳转到编辑页面进行修改
            @Override
            public void onClick(View v) {

                Log.i("position", String.valueOf(position));
                Intent intent = new Intent(context, Item_editor_machine.class);
                intent.putExtra("bianhao", list.get(position).getMachineId());
                intent.putExtra("nengli", String.valueOf(list.get(position).getWorkingAbility()));
                intent.putExtra("beizhu", list.get(position).getSchMachineRemark());
                intent.putExtra("xinghao", list.get(position).getScheduleUavNorm().getId());
                intent.putExtra("id", list.get(position).getId());

                Log.i("display","bianhao"+list.get(position).getMachineId()+" nengli"+list.get(position).getWorkingAbility()
                        +" beizhu"+list.get(position).getSchMachineRemark()+" xinghao"+list.get(position).getScheduleUavNorm().getId()
                        +" id"+list.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.bt3.setOnClickListener(new View.OnClickListener() {
            //删除
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(parent.getContext());
                //normalDialog.setIcon(R.drawable.);
                normalDialog.setTitle("删除确认：");
                normalDialog.setMessage("您是否确定要删除此设备？");
                normalDialog.setPositiveButton("继续",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除
                                String url;
                                int id = list.get(position).getId();
                                //int norm = list.get(position).getScheduleUavNorm().getId();
                                url = AppConfig.DRONE_DELETE;
                                url = url + "?id=" + id;
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




                                //
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                            }
                        });
                // 显示
                normalDialog.show();

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

        public ViewHolder(View itemView) {
            super(itemView);
            //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
            textView = (TextView) itemView.findViewById(R.id.drone_tv);
            bt2 = (Button) itemView.findViewById(R.id.drone_bt2);
            bt3 = (Button) itemView.findViewById(R.id.drone_bt3);
        }
    }


    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<DroneBean.ResultBean> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<DroneBean.ResultBean> newList) {
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


