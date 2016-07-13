package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.JobHistory;

public class item_job_history extends Fragment implements View.OnClickListener{
    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private TextView no_item;
    public ProgressDialog pDialog;
    private SessionManager sessionManager;
    private CommonUtil commonUtil;
    private NetUtil netUtil;
    private Gson gson;
    private String token;
    private String url;
    private String machine_id;
    private String start_date;
    private String end_date;
    private List<Map<String, Object>> listItem=new ArrayList<Map<String, Object>>();
    private List<JobHistory> listData=new ArrayList<JobHistory>();
    private String TAG=item_job_history.class.getSimpleName();
    private ListView historyLIst;
    private TextView t_startDate;
    private TextView t_endDate;
    private View dateView;//日期选择View
    private View parentView;//主View
    private PopupWindow datePickerPop;//日期选择器弹出
    private boolean popup_flag=false;//日期选择器是否弹出的标志
    private CalendarPickerView calendarPickerView;
    private SimpleDateFormat format;
    private SimpleDateFormat format2;
    private Date first_date;
    private com.beardedhen.androidbootstrap.BootstrapButton hintButton;
    private ArrayList<Date> dates = new ArrayList<Date>();//选择的起始日期
    private SimpleAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_3_job_history, container,
                false);
        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.selectDate).setOnClickListener(this);
        view.findViewById(R.id.search).setOnClickListener(this);
        this.historyLIst=(ListView)view.findViewById(R.id.show_job_history);
        this.t_startDate=(TextView)view.findViewById(R.id.job_start);
        this.t_endDate=(TextView)view.findViewById(R.id.job_end);
        this.no_item=(TextView)view.findViewById(R.id.noItem);

        mainMenu=(slidingMenu)getActivity();
        menu=mainMenu.drawer;

        format = new SimpleDateFormat("yyyy年M月d日");
        format2= new SimpleDateFormat("yyyy-MM-dd");
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);
        sessionManager=new SessionManager();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();
        token=sessionManager.getToken();
        url= AppConfig.URL_JOBHISTORY;
        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            Log.e(TAG, machine_id);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        parentView = LayoutInflater.from(mainMenu).inflate(R.layout.activity_3_job_history, null);
        dateView = mainMenu.getLayoutInflater().inflate(R.layout.datepicker, null);
        dateInit();
        initDatePicker();
        hintButton=(com.beardedhen.androidbootstrap.BootstrapButton)dateView.findViewById(R.id.hintButton);
        dateView.findViewById(R.id.getBack).setOnClickListener(this);
        dateView.findViewById(R.id.getHelp).setOnClickListener(this);
        calendarPickerView.setOnDateSelectedListener(new clDateSelectedListener());//选一个范围的日期
        calendarPickerView.setCellClickInterceptor(new clCellClick());//选一天的日期

        getJobHistory();
        return view;
    }

    //监听返回按键
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if(popup_flag)
                    {
                        popup_flag=false;
                        datePickerPop.dismiss();
                        return true;
                    }else
                    {
                        mainMenu.finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.getback:
                mainMenu.finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.selectDate:
                popup_flag=true;
                datePickerPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.search:
                getJobHistory();
                break;
            case R.id.getBack:
                popup_flag=false;
                datePickerPop.dismiss();
                break;
            case R.id.getHelp:
                commonUtil.error_hint_short("请选择作业起止日期！");
                break;
        }
    }

    //获取作业历史信息
    private void getJobHistory()
    {
        Log.e(TAG,start_date);
        Log.e(TAG,end_date);
        Log.e(TAG,machine_id);
        Log.e(TAG,token);
        String tag_string_req = "req_JobHistory";
        if (!netUtil.checkNet(mainMenu)) {
            commonUtil.error_hint_short("网络连接错误");
            hideDialog();
        } else {
            listData.clear();
            listItem.clear();//重新加载，清空集合数据
            pDialog.setMessage("正在加载数据 ...");
            showDialog();
            //服务器请求
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url,new jobHistorySuccessListener(), new jobHistoryErrorListener()) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("machine_id", machine_id);
                    params.put("token", token);
                    params.put("start_date", start_date);
                    params.put("end_date", end_date);
                    return netUtil.checkParams(params);
                }
            };

            strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        }

    private class jobHistorySuccessListener implements Response.Listener<String>//查询历史响应成功
    {
        @Override
        public void onResponse(String response) {
            hideDialog();
            try {
                Log.e(TAG, "JobHistory: " + response);
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");
                if (status == 1) {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Token 错误:" + errorMsg);
                    commonUtil.error_hint_short("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    mainMenu.backLogin();
                    SysCloseActivity.getInstance().exit();
                } else if (status == 0) {

                    String result = jObj.getString("result");
                    Log.e(TAG, result);
                    commonUtil.error_hint_short("数据加载完成");
                    listData=gson.fromJson(result,new TypeToken<ArrayList<JobHistory>>() {}.getType());
                    if(listData.size()>=1){no_item.setVisibility(View.GONE);}//数据条数为0,提示作业历史
                    for(int i=0;i<listData.size();i++)
                    {
                        JobHistory jobHistory=listData.get(i);
                        Map<String, Object> dataJobHistory = new HashMap<String, Object>();
                        dataJobHistory.put("id",String.valueOf(i+1));
                        dataJobHistory.put("address",jobHistory.getAddress());
                        dataJobHistory.put("phone",jobHistory.getPhone());
                        dataJobHistory.put("start_time",jobHistory.getStart_time());
                        dataJobHistory.put("end_time",jobHistory.getEnd_time());
                        dataJobHistory.put("area",jobHistory.getArea());
                        listItem.add(dataJobHistory);
                    }
                    ////////////////////////////////////////////为ListView添加数据///////////////////////////////////////
                    listAdapter=new SimpleAdapter(mainMenu,listItem,R.layout.show_job_history,
                            new String[]{"id","address","phone","start_time","end_time","area",},
                            new int[]{R.id.job_id,R.id.job_site,R.id.job_telephone,R.id.job_startDate,
                                    R.id.job_endDate,R.id.job_fields});
                    historyLIst.setAdapter(listAdapter);
                    ////////////////////////////////////////////为ListView添加数据///////////////////////////////////////

                } else {

                    no_item.setVisibility(View.VISIBLE);
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "1 Json error：response错误:" + errorMsg);
                    commonUtil.error_hint_short("服务器数据错误1：response错误:" + errorMsg);
                }
            } catch (JSONException e) {

                no_item.setVisibility(View.VISIBLE);
                Log.e(TAG, "2 服务器数据错误：response错误:" + e.getMessage());
                commonUtil.error_hint_short("服务器数据错误2：response错误:" + e.getMessage());
            } catch (Exception e) {

                no_item.setVisibility(View.VISIBLE);
                Log.e(TAG, "3 服务器数据错误：response错误:" + e.getMessage());
                commonUtil.error_hint_short("服务器数据错误3：response错误:" + e.getMessage());
            }

        }
    }

    private class jobHistoryErrorListener implements Response.ErrorListener//查询历史响应失败
    {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            no_item.setVisibility(View.VISIBLE);
            hideDialog();
            commonUtil.error_hint_short("服务器连接失败！");
            netUtil.testVolley(volleyError);
            Log.e(TAG, "4 ConncectService Error错误!");
            Log.e("GET-ERROR", volleyError.getMessage(), volleyError);
            try{
                byte[] htmlBodyBytes = volleyError.networkResponse.data;
                Log.e("GET-ERROR", new String(htmlBodyBytes), volleyError);
            } catch (Exception e){}
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //起始日期，终止日期初始化
    private void dateInit()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.MONTH, 1);
        Date defaultEndDate=calendar.getTime();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date defaultStartDate=calendar.getTime();
        t_startDate.setText(getResources().getString(R.string.jobStart1)+ format.format(defaultStartDate));
        t_endDate.setText(getResources().getString(R.string.jobEnd1) + format.format(defaultEndDate));//显示默认的起止年月日
        Calendar getDate = Calendar.getInstance();
        getDate.setTime(defaultEndDate);//结束日期加一天
        getDate.add(Calendar.DAY_OF_MONTH, 1);
        start_date=format2.format(defaultStartDate)+" 00:00:00";
        end_date=format2.format(getDate.getTime())+" 00:00:00";
    }

    //初始化日期选择器popupWindow
    private void initDatePicker() {
        Calendar today=Calendar.getInstance();
        Calendar maxDate=Calendar.getInstance();
        Calendar minDate=Calendar.getInstance();
        maxDate.add(Calendar.YEAR, 1);
        minDate.add(Calendar.YEAR,-1);
        calendarPickerView=(CalendarPickerView)dateView.findViewById(R.id.calendarDatePicker);
        calendarPickerView.init(minDate.getTime(), maxDate.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        calendarPickerView.scrollToDate(today.getTime());//滚动到当前日期
        datePickerPop = new PopupWindow(dateView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        datePickerPop.setAnimationStyle(R.style.slideAnimation_bottom);
        datePickerPop.setOutsideTouchable(true);
        datePickerPop.setOnDismissListener(new datePickerDismiss());
    }

    //日期选择单天监听
    private class clCellClick implements CalendarPickerView.CellClickInterceptor
    {
        @Override
        public boolean onCellClicked(Date date) {
            int size = (calendarPickerView.getSelectedDates()).size();
            if (size == 1) {
                hintButton.setText("请选择作业结束日期");
            }
            if (first_date == null) {
                hintButton.setText("请选择作业结束日期");
            }
            if(date==first_date)
            {
                String first_dateTime = format.format(date);
                t_startDate.setText(getResources().getString(R.string.jobStart1) + first_dateTime);
                t_endDate.setText(getResources().getString(R.string.jobEnd1) + first_dateTime);
                start_date = format2.format(date)+" 00:00:00";
                Calendar getDate = Calendar.getInstance();
                getDate.setTime(date);//结束日期加一天
                getDate.add(Calendar.DAY_OF_MONTH, 1);
                end_date=format2.format(getDate)+" 00:00:00";
                dates.clear();
                popup_flag=false;
                first_date= null;
                hintButton.setText("请选择作业开始日期");
                datePickerPop.dismiss();
            }
            return false;
        }
    }

    //日期选择器范围时间监听
    private class clDateSelectedListener implements CalendarPickerView.OnDateSelectedListener
    {
        @Override
        public void onDateSelected(Date date) {
            first_date=date;
            int size = (calendarPickerView.getSelectedDates()).size();
            if (size >= 2) {
                dates.addAll(calendarPickerView.getSelectedDates());
                t_startDate.setText(getResources().getString(R.string.jobStart1) + format.format(dates.get(0)));
                t_endDate.setText(getResources().getString(R.string.jobEnd1) + format.format(dates.get(size - 1)));
                start_date = format2.format(dates.get(0))+" 00:00:00";
                Calendar getDate = Calendar.getInstance();
                getDate.setTime(dates.get(size - 1));//结束日期加一天
                getDate.add(Calendar.DAY_OF_MONTH, 1);
                end_date=format2.format(getDate.getTime())+" 00:00:00";
                dates.clear();
                popup_flag=false;
                first_date=null;
                hintButton.setText("请选择作业开始日期");
                datePickerPop.dismiss();
            }
        }

        @Override
        public void onDateUnselected(Date date) {

        }
    }

    private class datePickerDismiss implements PopupWindow.OnDismissListener
    {
        @Override
        public void onDismiss() {
            initDatePicker();
        }
    }

}
