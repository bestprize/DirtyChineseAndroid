package com.fengxingshifang.dirtychineseandroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fengxingshifang.dirtychineseandroid.utils.DateUtils;

import java.util.Date;

/**
 * Created by git on 2017/12/4.
 */

public class MyOpenHelper  extends SQLiteOpenHelper {
    private final static String DB_NAME = "info.db";// 数据库文件名
    private final static int DB_VERSION = 1;// 数据库版本

    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建info表
        db.execSQL("create table db_info(" +
                        "infoid varchar primary key," +
                "infoorcomm varchar, " +
                "title varchar, " +
                "digest varchar, " +
                "content varchar, " +
                "publisher varchar, " +
                "phoneidpublisher varchar, " +
                "fatherinfoid varchar, " +
                "fathertitle varchar, " +
                "createtime datetime, " +
                "lastupdatetime datetime " +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
