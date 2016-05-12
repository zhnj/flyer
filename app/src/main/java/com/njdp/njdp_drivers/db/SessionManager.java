package com.njdp.njdp_drivers.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "NjdpLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoginIn";

    private static final String TOKEN_TAG = "token";

    private static final String DEPLOY_ID = "deploy_id";

    private static final String NORM_ID = "norm_id";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //缓存登录状态信息
    public void setLogin(boolean isLoginIn,String token) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoginIn);
        editor.putString(TOKEN_TAG, token);
        editor.commit();

        Log.d(TAG, "User LoginState session modified!");
    }

    //缓存状态信息
    public void setUserTag(String deploy_id,String norm_id) {

        editor.putString(DEPLOY_ID, deploy_id);
        editor.putString(NORM_ID, norm_id);
        editor.commit();

        Log.d(TAG, "User d_n_id session modified!");
    }

    public boolean isLoginIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getToken(){
        return pref.getString(TOKEN_TAG, "");
    }

    public String getDeployId(){
        return pref.getString(DEPLOY_ID, "");
    }

    public String getNormId(){
        return pref.getString(NORM_ID, "");
    }

}