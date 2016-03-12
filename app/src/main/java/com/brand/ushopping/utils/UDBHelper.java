package com.brand.ushopping.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/2/29.
 */
public class UDBHelper extends SQLiteOpenHelper {
    public UDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UDBHelper(Context context, String name){
        this(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists goods_view_history("
                + "id integer auto_increment primary key,"
                + "goods_id integer,"
                + "goods_name varchar(128),"
                + "logopic varchar(256),"
                + "price double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
