package com.njdp.njdp_drivers.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import bean.FieldInfo;
import bean.SavedFiledInfo;

/**
 * Created by USER-PC on 2016/4/29.
 */
public class SavedFieldInfoDao {
    private Context context;
    private Dao<SavedFiledInfo, Integer> fieldInfoDaoOpe;
    private DatabaseHelper helper;

    public SavedFieldInfoDao(Context context)
    {
        this.context = context;
        try
        {
            helper = DatabaseHelper.getHelper(context);
            fieldInfoDaoOpe = helper.getDao(SavedFiledInfo.class);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 增加一个用户
     * @param fieldInfo
     */
    public void add(SavedFiledInfo fieldInfo)
    {
        try
        {
            fieldInfoDaoOpe.create(fieldInfo);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 删除一条信息，记得setId
     */
    public void delete(SavedFiledInfo fieldInfo)
    {
        try
        {
            fieldInfoDaoOpe.delete(fieldInfo);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }


    /**
     * 更新信息、
     * 每次全部获取修改，再更新,记得setId=1
     */
    public void update(SavedFiledInfo fieldInfo)
    {
        try
        {
            fieldInfoDaoOpe.update(fieldInfo);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 根据id获取FieldInfo信息
     * 每次获取设置id
     */
    public SavedFiledInfo getFieldInfo(int fieldInfo_id)
    {
        try
        {
            return fieldInfoDaoOpe.queryBuilder().where().eq("id", fieldInfo_id)
                    .query().get(0);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
        return null;
    }

    /**
     * 根据farm_id获取FieldInfo信息
     * 每次获取设置farm_id
     */
    public SavedFiledInfo getFieldInfo(String farm_id)
    {
        try
        {
            return fieldInfoDaoOpe.queryBuilder().where().eq("farm_id", farm_id)
                    .query().get(0);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
        return null;
    }

    /**
     * 获取表中全部的FieldInfo
     */
    public List<SavedFiledInfo> allFieldInfo()
    {
        try
        {
            fieldInfoDaoOpe.countOf();
            return fieldInfoDaoOpe.queryForAll();
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
        return null;
    }

    /**
     * 获取表的行数
     */
    public int countOfField()
    {
        try
        {
            return (int)fieldInfoDaoOpe.countOf();
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());;
        }
        return 0;
    }
}
