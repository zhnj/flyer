package com.njdp.njdp_drivers.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.njdp.njdp_drivers.db.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;

import bean.FieldInfo;



public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();
    private Context context;

    public CommonUtil(Context context)
    {
        this.context = context;
    }

    //错误信息提示1
    public void error_hint_short(String str){
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

    //错误信息提示2
    public void error_hint_long(String str){
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }

    //信息未输入提示1
    public void error_hint2_short(int in){
        Toast toast = Toast.makeText(context, context.getResources().getString(in), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,-50);
        toast.show();
    }

    //信息未输入提示2
    public void error_hint2_long(int in){
        Toast toast = Toast.makeText(context, context.getResources().getString(in), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,-50);
        toast.show();
    }

    //EditText输入是否为空
    public static boolean isempty(EditText editText)
    {
        return TextUtils.isEmpty(editText.getText());
    }

    //是否存在Sd卡
    public boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    //返回存储路径
    public File getPath() {
        File savefile;
        if(ExistSDCard())
        {
            savefile= Environment.getExternalStorageDirectory();
            return savefile;
        }else {
            savefile=context.getCacheDir();
            return savefile;
        }
    }


    //设置头像本地存储路径
    public String imageTempFile()
    {
        File tempFile=getPath();
        String path=tempFile.getAbsolutePath()+"/njdpTemp/userimage.png";
        return path;
    }

    //ProgressDialog的显示与隐藏
    public void showDialog(ProgressDialog pDialog) {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    public void hideDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //Bitmap缩放
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    //保存照片到本地,不裁剪
    public boolean saveBitmap_noCrop(Bitmap mBitmap) {
        File file=getPath();
        File tempFile=null;
        FileOutputStream fOut;
        if (!file.exists()) {
            try {
                file.mkdirs();
                tempFile=new File(file.getAbsolutePath(),"/njdpTemp");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            tempFile=new File(file.getAbsolutePath(),"/njdpTemp");
        }
        try {
            File file1=new File(tempFile,"userimage"+".png");
            fOut = new FileOutputStream(tempFile);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 60, fOut);
            fOut.flush();
            Log.e(TAG, "保存成功path:" + file1.getAbsolutePath().toString());
            fOut.close();
        } catch (Exception e) {
            Log.e(TAG, "保存失败:" + e.getMessage());
            error_hint_short("头像保存失败！请重试");
            return false;
        }
        return true;
    }

    //保存照片到本地
    public boolean saveBitmap(Bitmap mBitmap) {
        File file=getPath();
        File tempFile=null;
        Bitmap bitmap = zoomBitmap(mBitmap, 400, 400);
        FileOutputStream fOut;
        if (!file.exists()) {
            try {
                file.mkdirs();
                tempFile=new File(file.getAbsolutePath(),"/njdpTemp");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            tempFile=new File(file.getAbsolutePath(),"/njdpTemp");
        }
        try {
            File file1=new File(tempFile,"userimage"+".png");
            fOut = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, fOut);
            fOut.flush();
            Log.e(TAG, "保存成功path:" + file1.getAbsolutePath().toString());
            fOut.close();
        } catch (Exception e) {
            Log.e(TAG, "保存失败:" + e.getMessage());
            error_hint_short("头像保存失败！请重试");
            return false;
        }
        return true;
    }

    /**
     * 根据下拉项的值value, 设置spinner默认选中:
     * @param spinner
     * @param value/position
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }
    public static void setSpinnerItemSelectedByPosition(Spinner spinner,int position){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if((apsAdapter.getItem(position).toString()).equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(position,true);// 默认选中项
                break;
            }
        }
    }

    public String transferCropKind(String cropKind)//将作物类型编码转换成汉字
    {
        switch (cropKind)
        {
            case "HWH":
                return "收割小麦";
            case "HCO":
                return "收割玉米";
            case "HRC":
                return "收割水稻";
            case "HGR":
                return "收割谷物";
            case "SWH":
                return "种植小麦";
            case "SCO":
                return "种植玉米";
            case "SRC":
                return "种植水稻";
            case "SGR":
                return "种植谷物";
            case "CSS":
                return "深松";
            case "CHA":
                return "平地";

            case "FWH":
                return "植保小麦";
            case "FCO":
                return "植保玉米";
            case "FRC":
                return "植保水稻";
            case "FGR":
                return "植保谷物";
            case "FFT":
                return "植保果树";
            case "FOT":
                return "植保其它";



        }
        return "类型错误";
    }

    public String transferSite(String site)//将地址中间的横线去掉
    {
        String[] s_site=site.split("-");
        StringBuilder st_site=new StringBuilder(s_site.length*3);
        for(int i=0;i<s_site.length;i++)
        {
            st_site.append((s_site[i]));
        }
        return st_site.toString();
    }

    public void destroyBitmap(Bitmap bitmap)//销毁Bitmap
    {
        if (null != bitmap && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }

   public void chenJin(Activity a){
       //设置沉浸模式
       a.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           Window window = a.getWindow();
           window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                   | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
           window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                   | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
           window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
           window.setStatusBarColor(Color.TRANSPARENT);
           window.setNavigationBarColor(Color.TRANSPARENT);
       }
   }
}