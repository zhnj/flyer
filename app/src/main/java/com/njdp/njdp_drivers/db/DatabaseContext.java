package com.njdp.njdp_drivers.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by USER-PC on 2016/4/24.
 * 将SQLL数据库存入SD卡
 */
public class DatabaseContext extends ContextWrapper {

    private static final String ROOT_SDCARD = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private String dbDir;

    public DatabaseContext(Context base, String path) {
        super(base);
        dbDir = path;
    }

    @Override
    public File getDatabasePath(String name) {
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        if (!sdExist) {// 如果不存在,
            Log.e("Database error", "SD卡不存在");
            return null;
        }

        // 判断目录是否存在，不存在则创建该目录
        File dirFile = new File(dbDir);
        if (!dirFile.exists())
            dirFile.mkdirs();

        // 标记数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        String dbPath = dbDir + "/" + name;// 数据库路径
        File dbFile = new File(dbPath);
        // 如果数据库文件不存在则创建该文件
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            isFileCreateSuccess = true;

        // 返回数据库文件对象
        if (isFileCreateSuccess)
            return dbFile;
        else
            return null;

    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
        return result;
    }
}
