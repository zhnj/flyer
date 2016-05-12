package com.njdp.njdp_drivers.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import bean.Driver;
import bean.FieldInfo;

/**
 * Created by USER-PC on 2016/4/24.
 */
public class FieldInfoDao{
    private Context context;
    private Dao<FieldInfo, Integer> fieldInfoDaoOpe;
    private DatabaseHelper helper;

    public FieldInfoDao(Context context)
    {
        this.context = context;
        try
        {
            helper = DatabaseHelper.getHelper(context);
            fieldInfoDaoOpe = helper.getDao(FieldInfo.class);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 增加一个用户
     * @param fieldInfo
     */
    public void add(FieldInfo fieldInfo)
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
     * 删除一条信息
     */
    public void delete(FieldInfo fieldInfo)
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
     * 清空用户信息，记得setId
     */
    public void clear()
    {
        try
        {
            fieldInfoDaoOpe.queryRaw("drop table tb_fieldInfo");
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 更新信息、
     * 每次全部获取修改，再更新,记得setId=1
     */
    public void update(FieldInfo fieldInfo)
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
     * 根据序号id获取FieldInfo信息
     * 每次获取设置id
     */
    public FieldInfo getFieldInfo(int id)
    {
        try
        {
            return fieldInfoDaoOpe.queryBuilder().where().eq("id", id)
                    .query().get(0);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
        catch (Exception e)
        {
            Log.e("DriverDao Error", e.toString());
        }
        return null;
    }

    /**
     * 根据farm_id获取FieldInfo信息
     * fieldId
     */
    public FieldInfo getFieldInfoByFieldId(String farm_id)
    {
        try
        {
            return fieldInfoDaoOpe.queryBuilder().where().ge("farm_id", farm_id)
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
    public List<FieldInfo> allFieldInfo()
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