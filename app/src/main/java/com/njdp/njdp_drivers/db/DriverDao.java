package com.njdp.njdp_drivers.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import bean.Driver;

/**
 * Created by USER-PC on 2016/4/24.
 */
public class DriverDao {
    private Context context;
    private Dao<Driver, Integer> driverDaoOpe;
    private DatabaseHelper helper;

    public DriverDao(Context context)
    {
        this.context = context;
        try
        {
            helper = DatabaseHelper.getHelper(context);
            driverDaoOpe = helper.getDao(Driver.class);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }catch (Exception e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 增加一个用户
     * @param driver
     */
    public void add(Driver driver)
    {
        try
        {
            driverDaoOpe.create(driver);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 清空用户信息，记得setId=1
     */
    public void delete(Driver driver)
    {
        try
        {
            driverDaoOpe.delete(driver);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 更新信息、
     * 每次全部获取修改，再更新,记得setId=1
     */
    public void update(Driver driver)
    {
        try
        {
            driverDaoOpe.update(driver);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取driver信息
     * 每次获取把id设为1
     */
    public Driver getDriver(int driver_id)
    {
        try
        {
            return driverDaoOpe.queryBuilder().where().eq("id", driver_id)
                    .query().get(0);
        } catch (Exception e)
        {
            Log.e("DriverDao Error", e.toString());
        }
        return null;
    }

}