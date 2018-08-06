package com.njdp.njdp_drivers;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.njdp.njdp_drivers.changeDefault.ListViewAdapter_select_sites;
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
    private static final String TAG = select_province.class.getSimpleName();
    private String mProvince;
    private String mCity;
    private String mCounty;
    private ImageButton btn_provinceBack = null;
    private ImageButton btn_cityBack = null;
    private ImageButton btn_countyBack = null;
    private List<ProvinceModel> provinceList;
    private List<CityModel> cityList;
    private List<DistrictModel> countyList;
    private String[] provinceDates;
    private String[]cityDates;
    private String[] countyDates;
    private LinearLayout linearLayout_list_province=null;
    private LinearLayout linearLayout_list_city=null;
    private LinearLayout linearLayout_list_county=null;
    private ListView list_province=null;
    private ListView list_city=null;
    private ListView list_county=null;
    private Map<String,Object> listProvinceItems=new HashMap<String,Object>();
    private Map<String,Object> listCityItems=new HashMap<String,Object>();;
    private Map<String,Object> listCountyItems=new HashMap<String,Object>();;
    private AssetManager asset;
    private TranslateAnimation slideLeftAction;
    private TranslateAnimation slideRightAction;
    private int select_rate=0;//0,1,2,3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置沉浸模式
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }


        setContentView(R.layout.activity_select_province);

        slideLeftAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slideLeftAction.setDuration(360);
        slideRightAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slideRightAction.setDuration(360);

        btn_provinceBack=(ImageButton) super.findViewById(R.id.select_site_getBack);
        btn_cityBack=(ImageButton) super.findViewById(R.id.select_site_getBack1);
        btn_countyBack=(ImageButton) super.findViewById(R.id.select_site_getBack2);
        btn_provinceBack.setOnClickListener(new provinceBack());
        btn_cityBack.setOnClickListener(new cityeBack());
        btn_countyBack.setOnClickListener(new countyBack());

        linearLayout_list_province=(LinearLayout) super.findViewById(R.id.site_select_province);
        linearLayout_list_city=(LinearLayout) super.findViewById(R.id.site_select_city);
        linearLayout_list_county=(LinearLayout) super.findViewById(R.id.site_select_county);
        list_province=(ListView) super.findViewById(R.id.list_province);
        list_city=(ListView) super.findViewById(R.id.list_city);
        list_county=(ListView) super.findViewById(R.id.list_county);
        initProvince();

        list_province.setOnItemClickListener(new provinceItemListener());
        list_city.setOnItemClickListener(new cityItemListener());
        list_county.setOnItemClickListener(new countyItemListener());
    }

    //初始化省份列表
    private void initProvince()
    {
        asset = select_province.this.getAssets();
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
            provinceList = handler.getDataList();
            provinceDates = new String[provinceList.size()];
//            Log.e(TAG, "共" + String.valueOf(provinceList.size()) + "个省");
            refreshProvinceList();
            list_province.setAdapter(new ListViewAdapter_select_sites(select_province.this,
                    provinceDates, listProvinceItems));
//            initCity(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } finally {

        }
    }

    //初始化市列表
    private void initCity(int position)
    {
        cityList=null;
        cityList=provinceList.get(position).getCityList();
//        Log.e(TAG,"省数量："+String.valueOf(provinceList.size()));
        cityDates=new String[cityList.size()];
//        Log.e(TAG, "城市数量：" + String.valueOf(cityList.size()));
        refreshCityList();
        list_city.setAdapter(new ListViewAdapter_select_sites(select_province.this,
                cityDates, listCityItems));
//        initCounty(0);
    }

    //初始化县区列表
    private void initCounty(int position)
    {
//        if (countyList != null && !countyList.isEmpty()) {
//            countyList.clear();
//        }
        countyList=null;
        countyList=cityList.get(position).getDistrictList();
        if(countyList.size()>0)
        {
            countyDates = new String[countyList.size()];
            refreshCountyList();
            list_county.setAdapter(new ListViewAdapter_select_sites(select_province.this,
                    countyDates, listCountyItems));
        }
    }

    //刷新省份列表
    private void refreshProvinceList()
    {
        try {
            listProvinceItems.clear();
        }catch (Exception e){}
        for (int i = 0; i < provinceList.size(); i++) {
            provinceDates[i] = provinceList.get(i).getName();
            listProvinceItems.put(provinceList.get(i).getName(), "0");
        }
    }

    //刷新城市列表
    private void refreshCityList()
    {
        try {
            listCityItems.clear();
        }catch (Exception e){}
        for (int i = 0; i < cityList.size(); i++) {
            cityDates[i] = cityList.get(i).getName();
            listCityItems.put(cityList.get(i).getName(), "0");
        }
    }

    //刷新县区列表
    private void refreshCountyList()
    {
        try {
            listCountyItems.clear();
        }catch (Exception e){}
        for (int i = 0; i < countyList.size(); i++) {
            countyDates[i] = countyList.get(i).getName();
            listCountyItems.put(countyList.get(i).getName(), "0");
        }
    }

    //省份选择监听
    private class provinceItemListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.e(TAG, "1 省份更新");
            refreshProvinceList();
            initCity(position);
            select_rate=1;
            mProvince=provinceDates[position];
            listProvinceItems.put(mProvince, "1");//选中了某个地区0变1作为标志
            list_province.setAdapter(new ListViewAdapter_select_sites(select_province.this,
                    provinceDates, listProvinceItems));//刷新列表
//            Log.e(TAG, "省"+String.valueOf(listProvinceItems.size()));
//            Log.e(TAG, "市"+String.valueOf(listCityItems.size()));
//            Log.e(TAG, "县" + String.valueOf(listCountyItems.size()));
//            for(int i=0;i<cityDates.length;i++)
//            {
//                Log.e(TAG, "市" +cityDates[i]);
//            }
//            for(int i=0;i<countyDates.length;i++)
//            {
//                Log.e(TAG, "县" +countyDates[i]);
//            }
            linearLayout_list_province.setVisibility(View.GONE);
            linearLayout_list_city.setVisibility(View.VISIBLE);
//            linearLayout_list_province.setAnimation(slideLeftAction);
//            linearLayout_list_city.setAnimation(slideLeftAction);
        }
    }

    //城市选择监听
    private class cityItemListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.e(TAG, "2 城市更新");
            refreshCityList();
            initCounty(position);
            select_rate=2;
            mCity = cityDates[position];
            listCityItems.put(mCity, "1");//选中了某个地区0变1作为标志
            list_city.setAdapter(new ListViewAdapter_select_sites(select_province.this,
                    cityDates, listCityItems));

            if(cityList.size()>0) {
                linearLayout_list_city.setVisibility(View.GONE);
                linearLayout_list_county.setVisibility(View.VISIBLE);
//                linearLayout_list_city.setAnimation(slideLeftAction);
//                linearLayout_list_county.setAnimation(slideLeftAction);
            }else{
                Intent intent = new Intent();
                Bundle _bundle=new Bundle();
                _bundle.putString("select_site",mProvince + mCity);
                intent.putExtras(_bundle);
                Log.e(TAG,"选择结果："+ mProvince + mCity);
                setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
                try {
                    listProvinceItems.clear();
                }catch (Exception e){}
                try {
                    listCityItems.clear();
                }catch (Exception e){}
                try {
                    listCountyItems.clear();
                }catch (Exception e){}
                finish();//返回选择的省市
            }
        }
    }

    //县区选择监听
    private class countyItemListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.e(TAG, "3 县区更新");
            refreshCountyList();
            select_rate=3;
            mCounty=countyDates[position];
            listCityItems.put(mCounty, "1");//选中了某个地区0变1作为标志
            list_county.setAdapter(new ListViewAdapter_select_sites(select_province.this,
                    countyDates, listCountyItems));
            Intent intent = new Intent();
            Bundle _bundle=new Bundle();
            _bundle.putString("select_site", mProvince + mCity + mCounty);
            intent.putExtras(_bundle);
            Log.e(TAG, "选择结果：" + mProvince + mCity + mCounty);
            setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
            try {
                listProvinceItems.clear();
            }catch (Exception e){}
            try {
                listCityItems.clear();
            }catch (Exception e){}
            try {
                listCountyItems.clear();
            }catch (Exception e){}
            finish();//返回选择的省市县
        }
    }

    //省份list返回
    private class provinceBack implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Bundle _bundle=new Bundle();
            _bundle.putString("select_site", "0");
            intent.putExtras(_bundle);
            Log.e(TAG, "选择结果：无");
            setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
            try {
                listProvinceItems.clear();
            }catch (Exception e){}
            try {
                listCityItems.clear();
            }catch (Exception e){}
            try {
                listCountyItems.clear();
            }catch (Exception e){}
            finish();//返回
        }
    }

    //城市list返回
    private class cityeBack implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            linearLayout_list_city.setVisibility(View.GONE);
            linearLayout_list_province.setVisibility(View.VISIBLE);
//            linearLayout_list_city.setAnimation(slideRightAction);
//            linearLayout_list_province.setAnimation(slideRightAction);
        }
    }

    //县区list返回
    private class countyBack implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            linearLayout_list_county.setVisibility(View.GONE);
            linearLayout_list_city.setVisibility(View.VISIBLE);
//            linearLayout_list_county.setAnimation(slideRightAction);
//            linearLayout_list_city.setAnimation(slideRightAction);

        }
    }

    @Override
    public void onBackPressed() {
        //物理返回键无效
//        super.onBackPressed();
    }

    //刷新列表
    private void refreshList(String[] listDates,Map<String,Object> listItems)
    {
        try {
            listItems.clear();
        }catch (Exception e){}
        for (int i = 0; i < listDates.length; i++) {
            listItems.put(listDates[i], "0");
        }
    }

    //    //切换Fragment,可以返回
//    public void addBackFragment(Fragment fragment)
//    {
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainMenu, fragment).
//                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
//    }

}
