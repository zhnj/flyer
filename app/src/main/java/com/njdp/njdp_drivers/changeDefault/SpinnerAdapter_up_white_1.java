package com.njdp.njdp_drivers.changeDefault;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.njdp.njdp_drivers.R;

/**
 * Created by SC on 2016/4/18.
 */
public class SpinnerAdapter_up_white_1 extends ArrayAdapter<String> {

    private Context mContext;
    private String [] mStringArray;

    public SpinnerAdapter_up_white_1(Context context, String[] stringArray)
    {
        super(context, android.R.layout.simple_spinner_item, stringArray);
        mContext= context;
        mStringArray=stringArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
        }
        //修改Spinner展开后的字体
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray[position]);
        tv.setTextSize(16f);//改变字体大小
        tv.setTextColor(ContextCompat.getColor(mContext, R.color.whiteFont));//改变颜色
        parent.setBackgroundResource(R.drawable.spinner_bg);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        // 修改Spinner选择后结果的字体
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray[position]);
        tv.setTextSize(16f);
        tv.setTextColor(ContextCompat.getColor(mContext, R.color.whiteFont));
        parent.setBackgroundResource(R.drawable.area_arrow_up);
        return convertView;
    }
}
