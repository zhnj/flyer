package com.njdp.njdp_drivers;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.njdp.njdp_drivers.changeDefault.ListViewAdapter_select_site;
import com.njdp.njdp_drivers.changeDefault.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import bean.CityModel;
import bean.DistrictModel;
import bean.ProvinceModel;

public class select_province extends AppCompatActivity {
    private String mProvince;
    private String mCity;
    private String mCounty;
    private ImageButton btn_provinceBack = null;
    private ImageButton btn_cityBack = null;
    private ImageButton btn_countyBack = null;
    private List<ProvinceModel> provinceList = null;
    private List<CityModel> cityList = null;
    private List<DistrictModel> countyList = null;
    private String[] provinceDates;
    private String[]cityDates;
    private String[] countyDates;
    private ListView list_province=null;
    private ListView list_city=null;
    private ListView list_county=null;
    private String[] sites_province=null;
    private String[] sites_city;
    private String[] sites_county;
    private List<Map<String,Object>> listProvinceItems;
    private List<Map<String,Object>> listCityItems;
    private List<Map<String,Object>> listCountyItems;
    private AssetManager asset = getAssets();
    private TranslateAnimation showAction;
    private TranslateAnimation hideAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_province);

        showAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        showAction.setDuration(300);
        hideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAction.setDuration(300);

        btn_provinceBack=(ImageButton) super.findViewById(R.id.select_site_getBack);
        btn_cityBack=(ImageButton) super.findViewById(R.id.select_site_getBack1);
        btn_countyBack=(ImageButton) super.findViewById(R.id.select_site_getBack2);
        btn_provinceBack.setOnClickListener(new provinceBack());
        btn_cityBack.setOnClickListener(new cityeBack());
        btn_countyBack.setOnClickListener(new countyBack());

        list_province=(ListView) super.findViewById(R.id.list_province);
        list_city=(ListView) super.findViewById(R.id.list_city);
        list_county=(ListView) super.findViewById(R.id.list_county);
        initProvince();
        list_province.setAdapter(new ListViewAdapter_select_site(select_province.this,
                R.layout.select_site_item, sites_province, listProvinceItems));
        list_city.setAdapter(new ListViewAdapter_select_site(select_province.this,
                R.layout.select_site_item, sites_city, listCityItems));
        list_county.setAdapter(new ListViewAdapter_select_site(select_province.this,
                R.layout.select_site_item, sites_county, listCountyItems));
        list_province.setOnItemClickListener(new provinceItemListener());
        list_city.setOnItemClickListener(new cityItemListener());
        list_county.setOnItemClickListener(new countyItemListener());
    }

    //初始化省份列表
    private void initProvince()
    {
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            if (provinceList!= null && !provinceList.isEmpty()) {
                provinceList.clear();
            }
                provinceList = handler.getDataList();
                provinceDates = new String[provinceList.size()];
                refreshProvinceList();
                initCity(0);
        } catch (Exception e) {

        } finally {

        }
    }

    //初始化市列表
    private void initCity(int position)
    {
        if (cityList != null && !cityList.isEmpty()) {
            cityList.clear();
        }
        cityList=provinceList.get(position).getCityList();
        refreshCityList();
        initCounty(0);
    }

    //初始化县区列表
    private void initCounty(int position)
    {
        if (cityList != null && !cityList.isEmpty()) {
            cityList.clear();
        }
        countyList=cityList.get(position).getDistrictList();
        refreshCountyList();
    }

    //刷新省份列表
    private void refreshProvinceList()
    {
        for (int i = 0; i < provinceList.size(); i++) {
            provinceDates[i] = provinceList.get(i).getName();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(provinceList.get(i).getName(), "0");
            listProvinceItems.add(map);
        }
    }

    //刷新城市列表
    private void refreshCityList()
    {
        for (int i = 0; i < cityList.size(); i++) {
            cityDates[i] = cityList.get(i).getName();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(cityList.get(i).getName(), "0");
            listCityItems.add(map);
        }
    }

    //刷新县区列表
    private void refreshCountyList()
    {
        for (int i = 0; i < countyList.size(); i++) {
            countyDates[i] = countyList.get(i).getName();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(countyList.get(i).getName(), "0");
            listCountyItems.add(map);
        }
    }

    //省份选择监听
    private class provinceItemListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mProvince=provinceDates[position];
            listProvinceItems.get(position).put(mProvince, "1");//选中了某个地区0变1作为标志
            list_province.setAdapter(new ListViewAdapter_select_site(select_province.this,
                    R.layout.select_site_item, sites_province, listProvinceItems));//刷新列表
            initCity(position);
            list_province.startAnimation(showAction);
            list_province.setVisibility(View.GONE);
            list_city.startAnimation(hideAction);
            list_city.setVisibility(View.VISIBLE);
        }
    }

    //城市选择监听
    private class cityItemListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mCity=cityDates[position];
            listCityItems.get(position).put(mCity, "1");//选中了某个地区0变1作为标志
            list_city.setAdapter(new ListViewAdapter_select_site(select_province.this,
                    R.layout.select_site_item, sites_city, listCityItems));
            initCounty(position);
            list_city.startAnimation(showAction);
            list_city.setVisibility(View.GONE);
            list_county.startAnimation(hideAction);
            list_county.setVisibility(View.VISIBLE);
        }
    }

    //县区选择监听
    private class countyItemListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mCounty=countyDates[position];
            listCityItems.get(position).put(mCounty, "1");//选中了某个地区0变1作为标志
            list_county.setAdapter(new ListViewAdapter_select_site(select_province.this,
                    R.layout.select_site_item, sites_county, listCountyItems));
            Intent intent = new Intent();
            intent.putExtra("select_site", mProvince + mCity + mCounty);
            setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
            finish();//返回选择的省市县
        }
    }

    //省份list返回
    private class provinceBack implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("select_site","0");
            setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
            finish();//返回
        }
    }

    //城市list返回
    private class cityeBack implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            list_city.startAnimation(hideAction);
            list_city.setVisibility(View.GONE);
            list_province.startAnimation(showAction);
            list_province.setVisibility(View.VISIBLE);
        }
    }

    //县区list返回
    private class countyBack implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            list_county.startAnimation(hideAction);
            list_county.setVisibility(View.GONE);
            list_city.startAnimation(showAction);
            list_city.setVisibility(View.VISIBLE);
        }
    }
//    //切换Fragment,可以返回
//    public void addBackFragment(Fragment fragment)
//    {
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenu, fragment).
//                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
//    }

}
