package com.njdp.njdp_drivers.items.myplan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.njdp.njdp_drivers.R;

import java.util.List;

/**
 * Created by Rock on 2018/7/28.
 */

public class DetialAdapter extends RecyclerView.Adapter<DetialAdapter.ViewHolder> {
    private List<PlanDetailBean.ResultBean> list;
    ViewGroup parent;
    String str;
    public DetialAdapter(List<PlanDetailBean.ResultBean> list) {
        this.list = list;
    }

    @Override
    public DetialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_detail, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DetialAdapter.ViewHolder holder, int position) {
        String id = "作业地块"+(position+1);
        String address = "\n地址："+list.get(position).getFarm_province()
                +list.get(position).getFarm_city()
                +list.get(position).getFarm_county()
                +list.get(position).getFarm_town()
                +list.get(position).getFarm_village();
        String income = "\n收入："+list.get(position).getIncome();
        String area = "\n面积："+list.get(position).getArea_num()+"亩";
        final String phone = "\n电话："+list.get(position).getFarmer_phone();
        String result = "\n是否住宿：";//+list.get(position).getReturnStatus());
        switch (list.get(position).getReturnStatus()){
            case "1":
                result+="是";
                break;
            case "2":
                result+="否";
                break;
            case "3":
                result+="是";
                break;
            case "4":
                result+="否";
                break;
            default:
                result="";
                break;
        }
        String date = "\n日期："+list.get(position).getBeginTime()+" 至 "+list.get(position).getEndTime();


        str = id+address+income+area+phone+result+date;
        Log.i("info","str:"+str);
        holder.tv.setText(str);

        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(parent.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                parent.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list!= null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        Button bt;

        public ViewHolder(View itemView) {
            super(itemView);
            //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
            tv = (TextView) itemView.findViewById(R.id.detail_tv);
            bt = (Button) itemView.findViewById(R.id.detail_bt);
        }
    }
}
