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

    private static final String HINT_FLAG="hint_flag";


    private static final String RELEASE_FLAG="release_flag";
    private static final String MACHINE_TYPE="machine_type";
    private static final String CROP_TYPE="crop_type";
    private static final String WORK_TIME="work_time";
    private static final String REMARK="remark";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
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
    public void setUserTag(String deploy_id,String norm_id) {

        editor.putString(DEPLOY_ID, deploy_id);
        editor.putString(NORM_ID, norm_id);
        editor.putBoolean(HINT_FLAG, false); //智能调度不再出现提示
        editor.commit();

        Log.d(TAG, "User d_n_id session modified!");
    }

    //缓存发布历史
    public void setReleaseHistory(final String machine_type,final String crop_type,final String work_time,final String remark) {

        editor.putBoolean(RELEASE_FLAG, true);
        editor.putString(MACHINE_TYPE, machine_type);
        editor.putString(CROP_TYPE, crop_type);
        editor.putString(WORK_TIME, work_time);
        editor.putString(REMARK, remark);
        editor.commit();

        Log.d(TAG, "User ReleaseHistory session modified!");
    }

    //清空发布历史
    public void clearReleaseHistory()
    {
        editor.putBoolean(RELEASE_FLAG, false);
    }

    public boolean getHintFlag()
    {
        return pref.getBoolean(HINT_FLAG, true);
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

    public boolean getReleaseFlag() {
        return pref.getBoolean(RELEASE_FLAG, false);
    }

}