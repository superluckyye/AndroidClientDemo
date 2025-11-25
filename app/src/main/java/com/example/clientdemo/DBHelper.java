package com.example.clientdemo;
//  这里要和你项目里 MainActivity.java 顶部的包名一致

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * 作用：
 *  1. 第一次使用时自动创建数据库和数据表
 *  2. 在表里预埋一条固定账号：admin / 123456
 */
public class DBHelper extends SQLiteOpenHelper {

    // 数据库名字，可以随便取
    private static final String DB_NAME = "user.db";
    // 数据库版本号，以后如果要升级，可以改这个数字
    private static final int DB_VERSION = 1;

    // 表名
    public static final String TABLE_USER = "user";

    public DBHelper(Context context) {
        // 调用父类构造方法
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 当数据库第一次创建时会自动执行这个方法
     * 在这里建表 + 预埋一条账号数据
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. 创建用户表（id 自增，username、password 文本）
        String createTableSql = "CREATE TABLE " + TABLE_USER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "password TEXT)";
        db.execSQL(createTableSql);

        // 2. 预埋一条账号：用户名 admin，密码 123456
        String insertUserSql = "INSERT INTO " + TABLE_USER +
                "(username, password) VALUES('admin', '123456')";
        db.execSQL(insertUserSql);
    }

    /**
     * 数据库升级时调用，这里为了简单，直接删表重建
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 删除旧表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // 重新创建
        onCreate(db);
    }
}
