package com.fiki.app.wifi.wifidocumentation.src.domain.repos.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.dbadpater.MyDataAdapter;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.MyDataDAO;

import java.util.HashMap;

/**
 * Created by root on 2017/10/14.
 */

public class MyDataDAOImpl implements MyDataDAO {

    private SQLiteDatabase database;
    private MyDataAdapter dbHelper;

    public MyDataDAOImpl(Context context) {
        dbHelper = new MyDataAdapter(context);
    }

    //open database
    public void open()
    {
        database =  dbHelper.getWritableDatabase();
    }
    //Close database
    public void close()
    {
        database.close();
    }

    private Cursor getRows() {
        //Sql query
        String selectQuery = "SELECT * FROM "+MyDataAdapter.TABLE_MYDATA;

        //open connection
        open();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor == null) return cursor;
        cursor.moveToFirst();
        close();
        return cursor;
    }

    @Override
    public Long create(MyData object) {
        open();

        ContentValues values = new ContentValues();

        //settings URL
        values.put(MyDataAdapter.MAC_ADDRESS, object.getMacAddress());
        values.put(MyDataAdapter.PORT_NO, object.getPortNo());
        values.put(MyDataAdapter.SERIAL_NO, object.getSerialNo());
        values.put(MyDataAdapter.SWITCH_NO, object.getSwitchNo());
        values.put(MyDataAdapter.LOCATION, object.getLocation());

        //Insert row
        Long result = database.insert(MyDataAdapter.TABLE_MYDATA, (String)(null), values);

        //Close database
        close();//Close connection

        return result;
    }

    @Override
    public int update(MyData object) {
        open();//open connection

        ContentValues values = new ContentValues();

        //settings URL
        values.put(MyDataAdapter.MAC_ADDRESS, object.getMacAddress());
        values.put(MyDataAdapter.PORT_NO, object.getPortNo());
        values.put(MyDataAdapter.SERIAL_NO, object.getSerialNo());
        values.put(MyDataAdapter.LOCATION, object.getLocation());
        values.put(MyDataAdapter.SWITCH_NO, object.getSwitchNo());
        values.put(MyDataAdapter.DATA_ID, object.getId());

        //updating row
        int result = database.update(MyDataAdapter.TABLE_MYDATA, values, MyDataAdapter.DATA_ID+
                " = ?", new String[]{String.valueOf(object.getId() )} );

        //Close connection
        close();
        return result;

    }

    @Override
    public int delete(MyData object) {
        open();
        int result = database.delete(MyDataAdapter.TABLE_MYDATA, MyDataAdapter.DATA_ID + " = ?",
                new String[]{String.valueOf(object.getId())});
        close();

        return result;
    }

    @Override
    public int deleteTable() {
        open();
        int result = database.delete(MyDataAdapter.TABLE_MYDATA,null,null);
        close();
        return result;
    }

    @Override
    public MyData findById(Integer id) {
        //open connection first
        open();
        //Object
        MyData result = null;
        Cursor cursor = null;
        try {
            cursor = database.query(MyDataAdapter.TABLE_MYDATA,
                    new String[]{
                            MyDataAdapter.DATA_ID,
                            MyDataAdapter.MAC_ADDRESS,
                            MyDataAdapter.SERIAL_NO,
                            MyDataAdapter.LOCATION,
                            MyDataAdapter.PORT_NO,
                            MyDataAdapter.SWITCH_NO
                    }
                    , MyDataAdapter.DATA_ID + " = ? ",
                    new String[]{String.valueOf(id)},
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                result = new MyData.Builder()
                        .id(cursor.getInt(0))
                        .macAddress(cursor.getString(1))
                        .serialNo(cursor.getString(2))
                        .location(cursor.getString(3))
                        .portNo(cursor.getString(4))
                        .switchNo(cursor.getString(5))
                        .build();
            }

        }
        catch (Exception e){

            result = new MyData.Builder()
                    .id(-1)
                    .build();
        }

        close();

        return result;
    }

    @Override
    public HashMap getList() {
        //Contact object
        MyData result;
        //cursor used to get rows from database
        Cursor cursor;

        //using hashMap because List of objects dont want to work
        HashMap objectList = new HashMap();

        int counter = 0;//used for the keys
        try{

            cursor = getRows();//get data using method from this class

            int size = cursor.getCount();
            if(cursor == null  || size == 0)
            {

                objectList.put("error","yes");
            }

            else{
                if(cursor.moveToFirst())
                {

                    do{

                        result = new MyData.Builder()
                                .id(cursor.getInt(0))
                                .macAddress(cursor.getString(1))
                                .serialNo(cursor.getString(2))
                                .location(cursor.getString(3))
                                .portNo(cursor.getString(4))
                                .switchNo(cursor.getString(5))
                                .build();

                        //Populate your hashMap  maybe instead of the counter use the tutorial id=> cursor.getString(0)
                        objectList.put(counter/*cursor.getString(0)*/,new MyData.Builder().copy(result).build());

                        counter++;//Change key values for hashMap

                    }while(cursor.moveToNext());
                }
            }
        }
        catch(Exception e)
        {

        }

        return objectList;
    }
}
