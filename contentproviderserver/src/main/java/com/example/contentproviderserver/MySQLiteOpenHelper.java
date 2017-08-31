package com.example.contentproviderserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Description:
 * Author: qiubing
 * Date: 2017-08-28 17:08
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "person.db";
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "student";
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE " + TABLE_NAME
            + "(_id INTEGER PRIMARY KEY,"
            + "name VARCHAR(20) NOT NULL,"
            + "age INTEGER)";

    public MySQLiteOpenHelper(Context context){
        this(context,DB_NAME,null,VERSION);
    }

    public MySQLiteOpenHelper(Context context, String s, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, s, cursorFactory, i);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO update the database
    }
}
