package com.njdp.njdp_drivers.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import bean.Driver;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_njdp";

    // table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_DRIVER = "driver";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_LICENSE_PLATER= "license_plater";
    private static final String KEY_OWNER_TYPE= "owner_type";
    private static final String KEY_MACHINE_MODEL= "machine_model";
    private static final String KEY_REGION = "region";
    private static final String KEY_URL = "imageUrl";
    private Driver driver;

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LICENSE_PLATER + " TEXT,"
                + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_NAME +" TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_DRIVER_TABLE = "CREATE TABLE " + TABLE_DRIVER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_TELEPHONE +
                " TEXT,"+ KEY_LICENSE_PLATER + " TEXT," + KEY_OWNER_TYPE +
                " TEXT,"+ KEY_MACHINE_MODEL + " TEXT,"+ KEY_REGION+" TEXT" + ")";
        db.execSQL(CREATE_DRIVER_TABLE);

        Log.d(TAG, "Database tables created !");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * 登录用户的账号信息
     * */
    public void addUser(String name, String username, String password,String imageurl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LICENSE_PLATER, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_NAME, name);
        values.put(KEY_NAME, imageurl);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("username", cursor.getString(1));
            user.put("password", cursor.getString(2));
            user.put("name", cursor.getString(3));
            user.put("imageurl", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

//    /**
//     * 机主用户的信息
//     * */
//    public void addDriver(Driver driver) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, driver.getName());
//        values.put(KEY_TELEPHONE,driver.getTelephone());
//        values.put(KEY_LICENSE_PLATER, driver.getLicense_plater());
//        values.put(KEY_OWNER_TYPE, driver.getOwner_type());
//        values.put(KEY_MACHINE_MODEL, driver.getMachine_model());
//        values.put(KEY_REGION, driver.getRegion());
//        values.put(KEY_URL, driver.getRegion());
//        // Inserting Row
//        long id = db.insert(TABLE_DRIVER, null, values);
//        db.close(); // Closing database connection
//
//        Log.d(TAG, "New Driver inserted into sqlite: " + id);
//    }

//    public Driver getDriverDetails() {
////        HashMap<String, String> driver = new HashMap<String, String>();
//        String selectQuery = "SELECT  * FROM " + TABLE_DRIVER;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // Move to first row
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            driver.setName(cursor.getString(1));
//            driver.setTelephone(cursor.getString(3));
//            driver.setLicense_plater(cursor.getString(4));
//            driver.setOwner_type(cursor.getString(5));
//            driver.setMachine_model(cursor.getString(6));
//            driver.setGender(cursor.getString(7));
//            driver.setRegion(cursor.getString(8));
//            driver.setImageUrl(cursor.getString(9));
//        }
//        cursor.close();
//        db.close();
//
//        return driver;
//    }

    /**
     * 删除用户信息
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_DRIVER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}