package com.njdp.njdp_drivers.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import bean.FieldInfo;
import bean.FieldInfoPost;

/**
 * Created by USER-PC on 2016/4/29.
 */
public class FieldInfoPostDao {

    private Context context;
    private Dao<FieldInfoPost, Integer> fieldInfoPostDaoOpe;
    private DatabaseHelper helper;

    public FieldInfoPostDao(Context context)
    {
        this.context = context;
        try
        {
            helper = DatabaseHelper.getHelper(context);
            fieldInfoPostDaoOpe = helper.getDao(FieldInfoPost.class);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 增加一个用户
     * @param fieldInfoPost
     */
    public void add(FieldInfoPost fieldInfoPost)
    {
        try
        {
            fieldInfoPostDaoOpe.create(fieldInfoPost);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 删除一条信息，记得setId
     */
    public void delete(FieldInfoPost fieldInfoPost)
    {
        try
        {
            fieldInfoPostDaoOpe.delete(fieldInfoPost);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }


    /**
     * 更新信息、
     * 每次全部获取修改，再更新,记得setId=1
     */
    public void update(FieldInfoPost fieldInfoPos)
    {
        try
        {
            fieldInfoPostDaoOpe.update(fieldInfoPos);
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());
        }
    }

    /**
     * 根据id获取FieldInfo信息
     * 每次获取设置id
     */
    public FieldInfoPost getFieldInfo(int fieldInfoPost_id)
    {
        try
        {
            return fieldInfoPostDaoOpe.queryBuilder().where().eq("id", fieldInfoPost_id)
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
    public List<FieldInfoPost> allFieldInfo()
    {
        try
        {
            fieldInfoPostDaoOpe.countOf();
            return fieldInfoPostDaoOpe.queryForAll();
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
            return (int)fieldInfoPostDaoOpe.countOf();
        } catch (SQLException e)
        {
            Log.e("DriverDao Error", e.toString());;
        }
        return 0;
    }

}
