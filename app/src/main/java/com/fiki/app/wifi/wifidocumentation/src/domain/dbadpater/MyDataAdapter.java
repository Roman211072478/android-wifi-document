package com.fiki.app.wifi.wifidocumentation.src.domain.dbadpater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 2017/10/14.
 */

public class MyDataAdapter extends SQLiteOpenHelper {

    private SQLiteOpenHelper sqLiteOpenHelper;
    //set table name
    public final static String TABLE_MYDATA = "MyData";


    //set Collums
    public static final String MAC_ADDRESS= "macAddress";
    public static final String SERIAL_NO= "serialNo";
    public static final String PORT_NO= "portNo";
    public static final String SWITCH_NO= "switchNo";
    public static final String LOCATION= "location";
    public static final String DATA_ID= "id";
    private static final String DATABASE_NAME = "myData.db";
    private static final int DATABASE_VERSION = 2;

    //Constructor
    public MyDataAdapter(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    //DATABASE CREATION SQL
    private static final String CREATE_USER_TABLE = "create TABLE IF NOT EXISTS "
            + TABLE_MYDATA + "( " +
            DATA_ID + " integer primary key autoincrement , "
            + MAC_ADDRESS +" text, "
            + SERIAL_NO + " text not null, " + LOCATION + " text not null, "
            + PORT_NO +" text not null, "
            + SWITCH_NO +" text not null);";

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
