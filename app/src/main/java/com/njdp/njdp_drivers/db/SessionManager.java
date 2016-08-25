package com.njdp.njdp_drivers.db;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static SessionManager sessionManager;
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    /**
     * 是否是测试环境.
     */
    public static final boolean DEBUG = false;

    // Shared Preferences
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
//    private static Context _context;

    // Shared pref mode
//    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "NjdpSession";

    private static final String KEY_IS_LOGGEDIN = "isLoginIn";

    private static final String TOKEN_TAG = "token";

    private static final String DEPLOY_ID = "deploy_id";

    private static final String NORM_ID = "norm_id";

    private static final String HINT_FLAG="hint_flag";


    private static final String RELEASE_FLAG="release_flag";
    private static final String MACHINE_TYPE="machine_type";
    private static final String CROP_TYPE="crop_type";
    private static final String WORK_TIME="work_time";
    private static final String REMARK="remark";

    public SessionManager() {
        pref = AppController.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    //缓存登录状态信息
    public void setLogin(boolean isLoginIn,String token) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoginIn);
        editor.putString(TOKEN_TAG, token);
        editor.putBoolean(HINT_FLAG, true); //智能调度出现提示
        editor.commit();

        Log.d(TAG, "User LoginState session modified!");
    }

    //缓存状态信息
    public void setUserTag(String deploy_id) {

        editor.putString(DEPLOY_ID, deploy_id);
//        editor.putString(NORM_ID, norm_id);
        editor.putBoolean(HINT_FLAG, false); //智能调度不再出现提示
        editor.commit();

        Log.d(TAG, "User d_n_id session modified!");
    }

    //缓存发布历史
    public void setReleaseHistory(boolean isReleased,final String machine_type,final String crop_type,final String work_time,final String remark) {

        editor.putBoolean(RELEASE_FLAG, isReleased);
        editor.putString(MACHINE_TYPE, machine_type);
        editor.putString(CROP_TYPE, crop_type);
        editor.putString(WORK_TIME, work_time);
        editor.putString(REMARK, remark);
        editor.commit();

        Log.d(TAG, "User ReleaseHistory session modified!");
    }

    //清空发布历史
    public void clearReleaseHistory() {
        editor.putBoolean(RELEASE_FLAG, false);
    }

    public boolean getHintFlag()
    {
        return pref.getBoolean(HINT_FLAG, true);
    }

    public boolean isLoginIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getToken() {
        return pref.getString(TOKEN_TAG, "");
    }

    public String getDeployId(){
        return pref.getString(DEPLOY_ID, "");
    }

    public String getNormId(){
        return pref.getString(NORM_ID, "");
    }

    public String getRemark() {
        return pref.getString(REMARK, "");
    }

    public String getWorkTime() {
        return pref.getString(WORK_TIME, "");
    }

    public String getCropType() {
        return pref.getString(CROP_TYPE, "");
    }

    public String getMachineType() {
        return pref.getString(MACHINE_TYPE, "");
    }

    public static SessionManager getInstance() {
        if (sessionManager == null)
            sessionManager = new SessionManager();
        return sessionManager;
    }

    public boolean getReleaseFlag() {
        return pref.getBoolean(RELEASE_FLAG, false);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

}