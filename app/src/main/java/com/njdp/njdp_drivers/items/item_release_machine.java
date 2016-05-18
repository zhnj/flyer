package com.njdp.njdp_drivers.items;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SpinnerAdapter_white;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.login;
import com.njdp.njdp_drivers.mainpages;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class item_release_machine extends Fragment implements View.OnClickListener {

    private slidingMenu mainMenu;
    private DrawerLayout menu;
    private Spinner sp_type;
    private Spinner sp_time;
    private Spinner sp_number;
    private com.beardedhen.androidbootstrap.BootstrapButton hintButton;
    private TextView selectedDate;
    private ImageButton btn_addRemark;
    private View parentView;//主View
    private int selectedNumber;//暂留

    /////////////////////日期选择弹窗//////////////////////////
//    private CalendarPickerView calendarPickerView;
//    private View dateView;//日期选择View
//    private PopupWindow datePickerPop;//日期选择器弹出
//    private boolean popup_flag = false;//日期选择器是否弹出的标志
//    private ArrayList<Date> dates = new ArrayList<Date>();//选择的起始日期
    /////////////////////日期选择弹窗//////////////////////////

    private ArrayAdapter adapter_type;
    private ArrayAdapter adapter_number;
    private ArrayAdapter adapter_time;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private NetUtil netUtil;
    private CommonUtil commonUtil;
    private String url;//发布信息服务器地址
    private String token;
    private String machine_number;//发布农机数量/暂留
    private String machine_id;
    private String workTime = "24";
    private StringBuilder machine_type;
    private String s_machine_type;
    private StringBuilder machine_cropType;
    private StringBuilder sb_crop;
    private String s_machine_cropType;
    private String remark;
    private int selectedType;
    private String startTime;//作业开始时间
    private String endTime;//作业结束时间
    private Date first_date;
    private EditText text_machine_id;
    private EditText text_type;
    private EditText text_crop;
    private LinearLayout ll_crop;
    private LinearLayout ll_gd;
//    private EditText text_number;

    /////////////////////确认发布信息弹窗///////////////////////////
    private View releaseView;
    private PopupWindow btn_popup;
    private boolean btn_pop_flag=false;
    private TextView tv_machineId;
    private TextView tv_machineType;
    private TextView tv_cropType;
    private TextView tv_workTime;
    private TextView tv_remark;
    private TextView showType;
    private WindowManager.LayoutParams lp;
    /////////////////////确认发布信息弹窗///////////////////////////

    private TextView tvr_machineType;
    private TextView tvr_cropType;
    private TextView tvr_workTime;
    private TextView tvr_remark;
    private TextView noRelease;
    private LinearLayout ll_history;
    private LinearLayout ll_hint;
    private EditText text_remark;
    private CheckBox ck_wheat;
    private CheckBox ck_corn;
    private CheckBox ck_soybean;
    private CheckBox ck_rice;
    private CheckBox ck_SS;
    private CheckBox ck_PD;
    private boolean ck_wheat_flag=false;
    private boolean ck_corn_flag=false;
    private boolean ck_soybean_flag=false;
    private boolean ck_rice_flag=false;
    private boolean ck_SS_flag=false;
    private boolean ck_PD_flag=false;
    private List<String> crop_type=new ArrayList<String>();
    private List<String> gd_type=new ArrayList<String>();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = item_release_machine.class.getSimpleName();
    private AwesomeValidation machine_id_Validation = new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_0_release_machine, container,
                false);

        view.findViewById(R.id.getback).setOnClickListener(this);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.release_button).setOnClickListener(this);
        view.findViewById(R.id.add_remark).setOnClickListener(this);
        view.findViewById(R.id.clear_hint).setOnClickListener(this);

        this.text_type = (EditText) view.findViewById(R.id.text_machine_type);
        this.text_crop=(EditText) view.findViewById(R.id.text_cropType);
        this.text_machine_id = (EditText) view.findViewById(R.id.input_machine_id);
        this.sp_type = (Spinner) view.findViewById(R.id.machine_type);
        this.sp_time = (Spinner) view.findViewById(R.id.work_time);
        this.selectedDate = (TextView) view.findViewById(R.id.date);//点击弹出日历选择器
        this.text_remark = (EditText) view.findViewById(R.id.remark);
        this.btn_addRemark = (ImageButton) view.findViewById(R.id.add_remark);
        this.ck_wheat=(CheckBox)view.findViewById(R.id.selectWheat);
        this.ck_corn=(CheckBox)view.findViewById(R.id.selectCorn);
        this.ck_soybean=(CheckBox)view.findViewById(R.id.selectSoybean);
        this.ck_rice=(CheckBox)view.findViewById(R.id.selectRice);
        this.ck_SS=(CheckBox)view.findViewById(R.id.selectSS);
        this.ck_PD=(CheckBox)view.findViewById(R.id.selectPD);
        this.ll_crop=(LinearLayout)view.findViewById(R.id.ck_crop);
        this.ll_gd=(LinearLayout)view .findViewById(R.id.ck_arableLand);
        this.ll_hint=(LinearLayout)view.findViewById(R.id.hint);
        this.ll_history=(LinearLayout)view.findViewById(R.id.release_history);
        this.noRelease=(TextView)view.findViewById(R.id.noRelease);
        this.tvr_machineType=(TextView)view.findViewById(R.id.tvr_machine_type);
        this.tvr_cropType=(TextView)view.findViewById(R.id.tvr_cropType);
        this.tvr_workTime=(TextView)view.findViewById(R.id.tvr_workTime);
        this.tv_remark=(TextView)view.findViewById(R.id.tvr_remark);
        ck_wheat.setOnCheckedChangeListener(new ckWheatListener());
        ck_corn.setOnCheckedChangeListener(new ckCornListener());
        ck_soybean.setOnCheckedChangeListener(new ckSoybeanListener());
        ck_rice.setOnCheckedChangeListener(new ckRiceListener());
        ck_SS.setOnCheckedChangeListener(new ckSSListener());
        ck_PD.setOnCheckedChangeListener(new ckPDListener());

        mainMenu = (slidingMenu) getActivity();
        menu = mainMenu.drawer;
        lp = mainMenu.getWindow().getAttributes();
        showReleaseHistory();//查看是否有发布历史

        machine_id_Validation.addValidation(text_machine_id, "\\d{12}|\\d{18}", getResources().getString(R.string.error_machineId));
        pDialog = new ProgressDialog(mainMenu);
        pDialog.setCancelable(false);

        url = AppConfig.URL_RELEASE;
        sessionManager = new SessionManager(mainMenu);
        token = sessionManager.getToken();
        commonUtil = new CommonUtil(mainMenu);
        netUtil = new NetUtil(mainMenu);

        parentView = LayoutInflater.from(mainMenu).inflate(R.layout.activity_0_release_machine, null);
        releaseView = mainMenu.getLayoutInflater().inflate(R.layout.release_popup, null);
        initBtnPopup();
        btn_popup.setOnDismissListener(new releasePopDisListener());
        releaseView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        releaseView.findViewById(R.id.btn_release).setOnClickListener(this);

//        dateView = mainMenu.getLayoutInflater().inflate(R.layout.datepicker, null);
//        dateInit();//日期选择初始化
//        initDatePicker();//日历选择器监听
//        dateView.findViewById(R.id.getBack).setOnClickListener(this);
//        dateView.findViewById(R.id.getHelp).setOnClickListener(this);
//        hintButton = (com.beardedhen.androidbootstrap.BootstrapButton) dateView.findViewById(R.id.hintButton);
//        calendarPickerView.setOnDateSelectedListener(new clDateSelectedListener());//选一个范围的日期
//        calendarPickerView.setCellClickInterceptor(new clCellClick());//选一天的日期

        //自动输入machine_id
        try {
            machine_id = new DriverDao(mainMenu).getDriver(1).getMachine_id();
            Log.e(TAG,machine_id);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        text_machine_id.setText(machine_id);

        //发布农机类型下拉
        adapter_type=new SpinnerAdapter_white(mainMenu,  getResources().getStringArray(R.array.machine_type));
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapter_type);
        sp_type.setOnItemSelectedListener(new typeSelectedListener());
//        //发布农机数量下拉选择项/暂留
//        adapter_number = ArrayAdapter.createFromResource(mainMenu, R.array.machine_number, android.R.layout.simple_spinner_item);
//        adapter_number.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_number.setAdapter(adapter_number);
//        sp_number.setOnItemSelectedListener(new numberSelectedListener());//发布农机数量监听/暂留
        //发布农机工作时间下拉
        adapter_time=new SpinnerAdapter_white(mainMenu,  getResources().getStringArray(R.array.workTime));
        adapter_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_time.setAdapter(adapter_time);
        sp_time.setOnItemSelectedListener(new workTimeSelectedListener());

        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback:
                mainMenu.finish();
                break;
            case R.id.menu:
                menu.openDrawer(Gravity.LEFT);
                break;
            case R.id.release_button:
                checkRelease();
                break;
            case R.id.clear_hint:
                ll_hint.setVisibility(View.GONE);
                break;
            case R.id.add_remark:
                showTextRemark();
                break;
            case R.id.btn_cancel:
                btn_pop_flag=false;
                btn_popup.dismiss();
                break;
            case R.id.btn_release:
                btn_pop_flag=false;
                btn_popup.dismiss();
                releaseInfo();
                break;
//            case R.id.date:
//                popup_flag = true;
//                datePickerPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//                break;
//            case R.id.getBack:
//                popup_flag = false;
//                datePickerPop.dismiss();
//                break;
//            case R.id.getHelp:
//                commonUtil.error_hint("请选择作业起止日期！");
//                break;
            default:
                break;
        }
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
//                    if (popup_flag) {//关闭日期选择弹窗
//                        popup_flag = false;
//                        datePickerPop.dismiss();
//                        return true;
//                    }else
                    if(btn_pop_flag){//关闭确认信息弹窗
                        btn_pop_flag=false;
                        btn_popup.dismiss();
                        return true;
                    }else {
                        mainMenu.finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void showReleaseHistory()//查看是否有发布历史
    {
        if(sessionManager.getReleaseFlag())
        {
            ll_history.setVisibility(View.VISIBLE);
            noRelease.setVisibility(View.GONE);
            tvr_cropType.setText(sessionManager.getCropType());
            tvr_machineType.setText(sessionManager.getMachineType());
            tvr_workTime.setText(sessionManager.getWorkTime());
            tvr_remark.setText(sessionManager.getRemark());
        }else
        {
            ll_history.setVisibility(View.GONE);
            noRelease.setVisibility(View.VISIBLE);
        }
    }

    //发布信息到数据库
    private void releaseInfo() {

        String tag_string_req = "req_njdp_release";
        pDialog.setMessage("正在发布中......");
        showDialog();

        if (netUtil.checkNet(mainMenu) == false) {
            commonUtil.error_hint("网络连接错误");
            hideDialog();
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, releaeSuccessListener, releaseErrorListener) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("machine_id", machine_id);
                    params.put("Sch_Machine_type", s_machine_cropType);
                    params.put("Sch_Machine_worktime", workTime);
                    params.put("Sch_Machine_remark", remark);
                    return netUtil.checkParams(params);
                }
            };

            strReq.setRetryPolicy(new DefaultRetryPolicy(5*1000,1,1.0f));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    //发布信息响应成功
    private Response.Listener<String> releaeSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "ReleaseInfo Response: " + response.toString());
            hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                int status = jObj.getInt("status");
                if (status == 1) {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint("密钥失效，请重新登录");
                    //清空数据，重新登录
                    netUtil.clearSession(mainMenu);
                    Intent intent = new Intent(mainMenu, login.class);
                    startActivity(intent);
                    mainMenu.finish();
                } else if (status == 0) {
                    commonUtil.error_hint("农机信息发布成功");
                    ll_history.setVisibility(View.VISIBLE);
                    noRelease.setVisibility(View.GONE);
                    tvr_cropType.setText(s_machine_cropType);
                    tvr_machineType.setText(transfer_s__machine_cropType(s_machine_type));
                    tvr_workTime.setText(workTime);
                    tvr_remark.setText(remark);
                } else {
                    String errorMsg = jObj.getString("result");
                    Log.e(TAG, "Json error：response错误:" + errorMsg);
                    commonUtil.error_hint(errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "ReleaseInfo Error1: " + e.getMessage());
                commonUtil.error_hint2_short(R.string.connect_error);
            }
        }
    };

    //发布服务器响应失败
    private Response.ErrorListener releaseErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideDialog();
            Log.e(TAG, "ConnectService Error: " + error.getMessage());
            commonUtil.error_hint2_short(R.string.connect_error);
        }
    };


    private void checkRelease()//检查发布信息
    {
        if (commonUtil.isempty(text_machine_id)) {
            commonUtil.error_hint2_short(R.string.error_machineId2);
        } else if (selectedType == 0) {
            commonUtil.error_hint2_short(R.string.error_machineId3);
        } else {
            checkByType(selectedType);
        }
//        暂留验证农机数量功能
//        else if (selectedNumber==0)
//        {
//            commonUtil.error_hint2(mainMenu, R.string.error_machineId4);
//        } else if ((selectedNumber==11)&&commonUtil.isempty(text_number))
//        {
//            commonUtil.error_hint2(mainMenu, R.string.error_machineId6);
//        }
    }

    private void checkByType(int selectedType)//根据农机服务类型,不同验证，验证合格弹出提示信息，发布
    {
        switch (selectedType)
        {
            case 1:
                if(!(ck_wheat_flag||ck_corn_flag||ck_soybean_flag||ck_rice_flag))
                {
                    commonUtil.error_hint2_short(R.string.err_cropCheck);
                }else if (machine_id_Validation.validate()) {//符合要求弹出确认弹窗
                    releasePopShow();
                }
                break;
            case 2:
                if(!(ck_SS_flag||ck_PD_flag))
                {
                    commonUtil.error_hint2_short(R.string.err_cropCheck);
                }else if (machine_id_Validation.validate()) {//符合要求弹出确认弹窗
                    releasePopShow();
                }
                break;
            case 3:
                if(!(ck_wheat_flag||ck_corn_flag||ck_soybean_flag||ck_rice_flag))
                {
                    commonUtil.error_hint2_short(R.string.err_cropCheck);
                }else if (machine_id_Validation.validate()) {//符合要求弹出确认弹窗
                    releasePopShow();
                }
                break;
//            case 4:
//                if(commonUtil.isempty(text_type)||commonUtil.isempty(text_crop)){
//                    commonUtil.error_hint2_short(R.string.error_machineId5);
//                }else if (machine_id_Validation.validate()) {//符合要求弹出确认弹窗
//                    releasePopShow();
//                }
//                break;
            default:
                break;

        }
    }

//    private void inputPost()//选择了其它类型手动输入的机器类型和作物或者根底类型
//    {
//        String[] machine_types=text_type.getText().toString().trim().split(",");
//        String[] crops=text_crop.getText().toString().trim().split(",");
//        machine_type=new StringBuilder(machine_types.length*3);//农机服务类型
//        sb_crop=new StringBuilder(crops.length*3);//作物或者耕地类型
//        for(int i=0;i<machine_types.length;i++)
//        {
//            if(i>0)
//            {
//                machine_type.append("&");
//            }
//            machine_type.append(machine_types[i]);
//        }
//        for(int i=0;i<crops.length;i++)
//        {
//            if(i>0)
//            {
//                sb_crop.append("&");
//            }
//            sb_crop.append(crops[i]);
//        }
//        s_machine_type=machine_type.toString();
//        s_machine_cropType=sb_crop.toString();
//        Log.e(TAG,"输入的多个农机类型："+s_machine_type);
//        Log.e(TAG,"输入的作物或者耕地类型："+s_machine_cropType);
//    }

    private void releasePopShow()//先为release PopupWindow布局中的textview赋值，再弹出release PopupWindow
    {
        machine_id = text_machine_id.getText().toString().trim();//农机Id
        tv_machineId.setText(machine_id);

        if(text_remark.getText().toString().trim().isEmpty())//描述
        {
            remark="无";
        }
        remark=text_remark.getText().toString().trim();
        tv_remark.setText(remark);

        switch (selectedType)
        {
            case 1:
                machine_cropType=new StringBuilder(crop_type.size()*3);//收获作物类型
                for(int i=0;i<crop_type.size();i++){
                    if(i>0)
                    {
                        machine_cropType.append("&");
                    }
                    machine_cropType.append("H"+crop_type.get(i));
                }
                s_machine_cropType=machine_cropType.toString();
                break;
            case 2:
                machine_cropType=new StringBuilder(gd_type.size()*3);//耕地类型
                for(int i=0;i<gd_type.size();i++){
                    if(i>0)
                    {
                        machine_cropType.append("&");
                    }
                    machine_cropType.append("C"+gd_type.get(i));
                }
                s_machine_cropType=machine_cropType.toString();
                break;
            case 3:
                machine_cropType=new StringBuilder(crop_type.size()*3);//播种作物类型
                for(int i=0;i<crop_type.size();i++){
                    if(i>0)
                    {
                        machine_cropType.append("&");
                    }
                    machine_cropType.append("S"+crop_type.get(i));
                }
                s_machine_cropType=machine_cropType.toString();
                break;
//            case 4://如果农机类型选择了其他，手动输入
//                inputPost();
//                break;
            default:
                break;
        }

        tv_machineType.setText(s_machine_type);
        tv_cropType.setText(transfer_s__machine_cropType(s_machine_type));

        Log.e(TAG,token);
        Log.e(TAG,machine_id);
        Log.e(TAG, s_machine_type);
        Log.e(TAG, s_machine_cropType);
        Log.e(TAG, workTime);
        Log.e(TAG, remark);

        btn_popup.showAtLocation(parentView, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        lp.alpha = 0.7f;
        mainMenu.getWindow().setAttributes(lp);
    }

    private String transfer_s__machine_cropType(final String s__machine_cropType)//将s__machine_cropType编码转换成汉字
    {
        String[] s_machine_cropTypes=s_machine_cropType.split("&");
        StringBuilder st_sb_crop=new StringBuilder(s_machine_cropTypes.length*3);
        for(int i=0;i<s_machine_cropTypes.length;i++)
        {
            if(i>0) {
                st_sb_crop.append("&");
            }
            st_sb_crop.append(commonUtil.transferCropKind(s_machine_cropTypes[i]));
        }
        return st_sb_crop.toString();
    }

    //控制描述输入框的显示和隐藏
    private void showTextRemark() {
        if (text_remark.getVisibility() == View.VISIBLE) {
            //隐藏
            btn_addRemark.setImageResource(R.drawable.ic_add_box_white_36dp);
            text_remark.setVisibility(View.GONE);
        } else {
            //显示
            btn_addRemark.setImageResource(R.drawable.ic_add_box_grey600_36dp);
            text_remark.setVisibility(View.VISIBLE);
            text_remark.setFocusable(true);
            text_remark.requestFocus();
        }
    }

//    //起始日期，终止日期初始化
//    private void dateInit() {
//        Calendar calendar = Calendar.getInstance();
//        Date defaultStartDate = calendar.getTime();
//        calendar.add(calendar.DATE, 1);
//        Date defaultEndDate = calendar.getTime();
//        selectedDate.setText(format.format(defaultStartDate) + "----" + format.format(defaultEndDate));//显示默认的起止年月日
//    }
//
//    //初始化日期选择器popupWindow
//    private void initDatePicker() {
//        Calendar maxDate = Calendar.getInstance();
//        maxDate.add(Calendar.MONTH, 2);
//        calendarPickerView = (CalendarPickerView) dateView.findViewById(R.id.calendarDatePicker);
//        calendarPickerView.init(new Date(), maxDate.getTime())
//                .inMode(CalendarPickerView.SelectionMode.RANGE);
//        datePickerPop = new PopupWindow(dateView, ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        datePickerPop.setAnimationStyle(R.style.slideAnimation_bottom);
//        datePickerPop.setOutsideTouchable(true);
//    }

    //ProgressDialog的显示与隐藏
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void initBtnPopup()//初始化确认信息弹窗
    {
        btn_popup = new PopupWindow(releaseView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_popup.setAnimationStyle(R.style.popWindow_fade);
        btn_popup.setOutsideTouchable(true);
        btn_popup.setFocusable(true);
        btn_popup.setBackgroundDrawable(new ColorDrawable(0x55000000));
        tv_machineId=(TextView)releaseView.findViewById(R.id.tv_machine_id);
        tv_machineType=(TextView)releaseView.findViewById(R.id.tv_machine_type);
        tv_cropType=(TextView)releaseView.findViewById(R.id.tv_cropType);
        tv_workTime=(TextView)releaseView.findViewById(R.id.tv_workTime);
        tv_remark=(TextView)releaseView.findViewById(R.id.tv_remark);
        showType=(TextView)releaseView.findViewById(R.id.showType);
        tv_workTime.setText("16小时");
    }

//    private class clCellClick implements CalendarPickerView.CellClickInterceptor//日期选择单天监听
//    {
//        @Override
//        public boolean onCellClicked(Date date) {
//            int size = (calendarPickerView.getSelectedDates()).size();
//            if (size == 1) {
//                hintButton.setText("请选择作业结束日期");
//            }
//            if (first_date == null) {
//                hintButton.setText("请选择作业结束日期");
//            }
//            if (date == first_date) {
//                String first_dateTime = format.format(date);
//                selectedDate.setText(first_dateTime + "----" + first_dateTime);
//                startTime = format2.format(date);
//                endTime = format2.format(date);
//                dates.clear();
//                popup_flag = false;
//                first_date = null;
//                hintButton.setText("请选择作业开始日期");
//                datePickerPop.dismiss();
//            }
//            return false;
//        }
//    }
//
//    private class clDateSelectedListener implements CalendarPickerView.OnDateSelectedListener//日期选择器范围时间监听
//    {
//        @Override
//        public void onDateSelected(Date date) {
//            first_date = date;
//            int size = (calendarPickerView.getSelectedDates()).size();
//            if (size >= 2) {
//                dates.addAll(calendarPickerView.getSelectedDates());
//                selectedDate.setText(format.format(dates.get(0)) + "---" + format.format(dates.get(size - 1)));
//                startTime = format2.format(dates.get(0));
//                endTime = format2.format(dates.get(size - 1));
//                dates.clear();
//                popup_flag = false;
//                first_date = null;
//                hintButton.setText("请选择作业开始日期");
//                datePickerPop.dismiss();
//            }
//        }
//
//        @Override
//        public void onDateUnselected(Date date) {
//
//        }
//    }

    private class typeSelectedListener implements AdapterView.OnItemSelectedListener//农机类型下拉菜单监听
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedType = position;
//            if (position == 4) {
//                text_type.setVisibility(View.VISIBLE);
//                text_crop.setVisibility(View.VISIBLE);
//                ll_crop.setVisibility(View.GONE);
//                ll_gd.setVisibility(View.GONE);
//                showType.setText("类型：");
//            } else {
//                text_type.setVisibility(View.GONE);
//                text_crop.setVisibility(View.GONE);
//            }
            s_machine_type = sp_type.getSelectedItem().toString();
            switch (position) {
                case 1:
                    ll_crop.setVisibility(View.VISIBLE);
                    ll_gd.setVisibility(View.GONE);
                    showType.setText("作物类型：");
                    break;
                case 2:
                    ll_crop.setVisibility(View.GONE);
                    ll_gd.setVisibility(View.VISIBLE);
                    showType.setText("耕地类型：");
                    break;
                case 3:
                    ll_crop.setVisibility(View.VISIBLE);
                    ll_gd.setVisibility(View.GONE);
                    showType.setText("播种类型：");
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ckWheatListener implements CompoundButton.OnCheckedChangeListener//小麦选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_wheat_flag=isChecked;
            if(isChecked){
                crop_type.add("WH");
            }else{
                crop_type.remove("WH");
            }
        }
    }

    private class ckCornListener implements CompoundButton.OnCheckedChangeListener//玉米选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_corn_flag=isChecked;
            if(isChecked){
                crop_type.add("CO");
            }else{
                crop_type.remove("CO");
            }
        }
    }

    private class ckSoybeanListener implements CompoundButton.OnCheckedChangeListener//谷子选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_soybean_flag=isChecked;
            if(isChecked){
                crop_type.add("GR");
            }else{
                crop_type.remove("GR");
            }
        }
    }

    private class ckRiceListener implements CompoundButton.OnCheckedChangeListener//水稻选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_rice_flag = isChecked;
            if (isChecked) {
                crop_type.add("RC");
            } else {
                crop_type.remove("RC");
            }
        }
    }

    private class ckSSListener implements CompoundButton.OnCheckedChangeListener//深松选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_SS_flag = isChecked;
            if (isChecked) {
                gd_type.add("SS");
            } else {
                gd_type.remove("SS");
            }
        }
    }

    private class ckPDListener implements CompoundButton.OnCheckedChangeListener//平地面选择监听
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ck_PD_flag = isChecked;
            if (isChecked) {
                gd_type.add("HA");
            } else {
                gd_type.remove("HA");
            }
        }
    }

    private class workTimeSelectedListener implements AdapterView.OnItemSelectedListener//作业时长下拉监听
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(position==0){
                workTime="24";
                tv_workTime.setText("24小时");
            }else {
                workTime = String.valueOf(position);
                tv_workTime.setText(String.valueOf(position)+"小时");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class releasePopDisListener implements PopupWindow.OnDismissListener//发布按钮弹出时监听dismiss后背景变回原样
    {
        @Override
        public void onDismiss() {
            btn_pop_flag=false;
            lp.alpha = 1f;
            mainMenu.getWindow().setAttributes(lp);
        }
    }

//    private class numberSelectedListener implements AdapterView.OnItemSelectedListener//农机数量下拉菜单监听/暂留
//    {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            selectedNumber = position;
//            if (position == 11) {
//                text_number.setVisibility(View.VISIBLE);
//            } else {
//                text_number.setVisibility(View.GONE);
//                machine_number = sp_number.getSelectedItem().toString();
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    }
    //农机工作时间下拉菜单监听

}