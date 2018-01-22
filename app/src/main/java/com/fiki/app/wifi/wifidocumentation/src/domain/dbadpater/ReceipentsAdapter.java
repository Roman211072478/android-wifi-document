package com.fiki.app.wifi.wifidocumentation.src.domain.dbadpater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 2017/11/23.
 */

public class ReceipentsAdapter extends SQLiteOpenHelper {

    private SQLiteOpenHelper sqLiteOpenHelper;
    //set table name
    public final static String TABLE_MYDATA = "receipentsTable";

    //set Columns
    public static final String EMAIL = "email";
    public static final String DATA_ID= "id";
    private static final String DATABASE_NAME = "receipents.db";
    private static final int DATABASE_VERSION = 1;

    //Constructor
    public ReceipentsAdapter(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    //DATABASE CREATION SQL
    private static final String CREATE_USER_TABLE = "create TABLE IF NOT EXISTS "
            + TABLE_MYDATA + "( " +
            DATA_ID + " integer primary key autoincrement , "
            + EMAIL + " text not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a database
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MyDataAdapter.class.getName(),"upgraing the database from Version "+oldVersion
                +" to "+newVersion+" which will destory old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYDATA);
        //call function to recreate the table
        onCreate(db);
    }
}
