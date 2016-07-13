package com.njdp.njdp_drivers.items;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.Gson;
import com.njdp.njdp_drivers.R;
import com.njdp.njdp_drivers.changeDefault.SysCloseActivity;
import com.njdp.njdp_drivers.db.AppConfig;
import com.njdp.njdp_drivers.db.AppController;
import com.njdp.njdp_drivers.db.DriverDao;
import com.njdp.njdp_drivers.db.SessionManager;
import com.njdp.njdp_drivers.slidingMenu;
import com.njdp.njdp_drivers.util.CommonUtil;
import com.njdp.njdp_drivers.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bean.Driver;

import static com.njdp.njdp_drivers.util.NetUtil.TAG;

public class item_personalinformation_setting extends Fragment implements View.OnClickListener {
    private slidingMenu mainMenu;
    private com.beardedhen.androidbootstrap.BootstrapButton btn_fix_save;
    private Map<String,String> fix_params=new HashMap<String,String>();
    public AwesomeValidation fixValidation=new AwesomeValidation(ValidationStyle.BASIC);
    private NetUtil netUtil;
    private CommonUtil commonUtil;
    private Gson gson;
    private SessionManager sessionManager;
    private String token;
    private DriverDao driverDao;
    private Driver driver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_5_personalinformation_setting, container,
                false);

        view.findViewById(R.id.getBack).setOnClickListener(this);
        view.findViewById(R.id.fix_user_pwd).setOnClickListener(this);
        view.findViewById(R.id.exit_app).setOnClickListener(this);

        mainMenu=(slidingMenu)getActivity();

        sessionManager=new SessionManager();
        token=sessionManager.getToken();
        commonUtil=new CommonUtil(mainMenu);
        netUtil=new NetUtil(mainMenu);
        gson=new Gson();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.getBack:
                mainMenu.getSupportFragmentManager().popBackStack();
                break;
            case R.id.fix_user_pwd:
                mainMenu.addBackFragment(new item_personalinformation_2_fix_password());
                break;
            case R.id.exit_app:
                Log.e(TAG, "退出登录");
                netUtil.clearSession(mainMenu);
                mainMenu.backLogin();
                SysCloseActivity.getInstance().exit();
                break;

        }
    }

}
