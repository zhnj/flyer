package com.njdp.njdp_drivers.changeDefault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njdp.njdp_drivers.R;

import java.util.Map;

/**
 * Created by SC on 2016/6/22.
 */
public class ListViewAdapter_select_sites extends BaseAdapter {
    private Context mContext;
    private String[] mSites;
    private Map<String,Object> mListItems;


    public ListViewAdapter_select_sites(Context context, String[] sites, Map<String, Object> listItems)
    {
        mContext=context;
        mSites=sites;
        mListItems=listItems;
    }

    @Override
    public int getCount() {
//        Log.e("该级列表项的数量：", String.valueOf(mListItems.size()));
        return mListItems.size();
    }

    @Override
    public long getItemId(int position) {
        if (position < mListItems.size()) {
            return 01000010+position;
        } else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if (position < mListItems.size()) {
            return mListItems.get(position);
        } else
            return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.select_site_item, parent, false);
        }
        TextView textView_site=(TextView)convertView.findViewById(R.id.site);
        TextView textView_site_state=(TextView)convertView.findViewById(R.id.site_state);
        textView_site.setText(mSites[position]);
        String site_select_flag=mListItems.get(mSites[position]).toString();
        if(site_select_flag.equals("1")){
            textView_site_state.setText("已选择");
        }else
        {
            textView_site_state.setText("");
        }
        return convertView;
    }

    //刷新列表
    private void refreshList()
    {
        try {
            mListItems.clear();
        }catch (Exception e){}
        for (int i = 0; i < mSites.length; i++) {
            mListItems.put(mSites[i], "0");
        }
    }
}
