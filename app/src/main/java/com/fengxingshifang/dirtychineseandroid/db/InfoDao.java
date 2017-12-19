package com.fengxingshifang.dirtychineseandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.fengxingshifang.dirtychineseandroid.domain.InfoListData.Info;
import com.fengxingshifang.dirtychineseandroid.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by git on 2017/12/4.
 */

public class InfoDao {

    private MyOpenHelper helper;

    public InfoDao(Context context) {
        helper = new MyOpenHelper(context);
    }

    /**
     * 查询所有笔记
     */
    public List<Info> queryInfosAll() {
        SQLiteDatabase db = helper.getWritableDatabase();

        List<Info> InfoList = new ArrayList<>();
        Info Info ;
        String sql ;
        Cursor cursor = null;
        try {
            sql = "select * from db_info " ;
            cursor = db.rawQuery(sql, null);
            //cursor = db.query("Info", null, null, null, null, null, "n_id desc");
            while (cursor.moveToNext()) {
                //循环获得展品信息
                Info = new Info();
                Info.setInfoid(cursor.getString(cursor.getColumnIndex("infoid")));
                Info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                Info.setContent(cursor.getString(cursor.getColumnIndex("content")));
                Info.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                Info.setLastupdatetime(cursor.getString(cursor.getColumnIndex("lastupdatetime")));
                InfoList.add(Info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return InfoList;
    }

    /**
     * 插入笔记
     */
    public long insertInfo(Info Info) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into db_Info(title,content," +
                "createtime,lastupdatetime) " +
                "values(?,?,?,?)";

        long ret = 0;
        //sql = "insert into ex_user(eu_login_name,eu_create_time,eu_update_time) values(?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        try {
            stat.bindString(1, Info.getTitle());
            stat.bindString(2, Info.getContent());
            stat.bindString(3, DateUtils.date2string(new Date()));
            stat.bindString(4, DateUtils.date2string(new Date()));
            ret = stat.executeInsert();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return ret;
    }

    /**
     * 更新笔记
     * @param Info
     */
    public void updateInfo(Info Info) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", Info.getTitle());
        values.put("content", Info.getContent());
        values.put("lastupdatetime", DateUtils.date2string(new Date()));
        db.update("db_Info", values, "infoid=?", new String[]{Info.getInfoid()+""});
        db.close();
    }

    /**
     * 删除笔记
     */
    public int deleteInfo(int InfoId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = 0;
        try {
            ret = db.delete("db_Info", "infoid=?", new String[]{InfoId + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }

    /**
     * 批量删除笔记
     *
     * @param mInfos
     */
    public int deleteInfo(List<Info> mInfos) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = 0;
        try {
            if (mInfos != null && mInfos.size() > 0) {
                db.beginTransaction();//开始事务
                try {
                    for (Info Info : mInfos) {
                        ret += db.delete("db_Info", "n_id=?", new String[]{Info.getInfoid() + ""});
                    }
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }
}
