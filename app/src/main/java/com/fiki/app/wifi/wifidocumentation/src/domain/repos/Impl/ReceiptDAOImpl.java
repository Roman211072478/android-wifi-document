package com.fiki.app.wifi.wifidocumentation.src.domain.repos.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.fiki.app.wifi.wifidocumentation.src.domain.dbadpater.ReceipentsAdapter;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ReceipentsModel;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.ReceiptDAO;

import java.util.HashMap;

/**
 * Created by fiki on 2017/11/23.
 */

public class ReceiptDAOImpl implements ReceiptDAO {
    private SQLiteDatabase database;
    private ReceipentsAdapter dbHelper;

    public ReceiptDAOImpl(Context context) {
        dbHelper = new ReceipentsAdapter(context);
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
        String selectQuery = "SELECT * FROM "+ReceipentsAdapter.TABLE_MYDATA;

        //open connection
        open();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor == null) return cursor;
        cursor.moveToFirst();
        close();
        return cursor;
    }

    @Override
    public Long create(ReceipentsModel object) {
        open();

        ContentValues values = new ContentValues();

        //settings URL
        values.put(ReceipentsAdapter.EMAIL, object.getEmail());

        //Insert row
        Long result = database.insert(ReceipentsAdapter.TABLE_MYDATA, (String)(null), values);

        //Close database
        close();//Close connection

        return result;
    }

    @Override
    public int update(ReceipentsModel object) {
        open();//open connection

        ContentValues values = new ContentValues();

        //settings URL
        values.put(ReceipentsAdapter.EMAIL, object.getEmail());
        values.put(ReceipentsAdapter.DATA_ID, object.getId());

        //updating row
        int result = database.update(ReceipentsAdapter.TABLE_MYDATA, values, ReceipentsAdapter.DATA_ID+
                " = ?", new String[]{String.valueOf(object.getId() )} );

        //Close connection
        close();
        return result;
    }

    @Override
    public int delete(ReceipentsModel object) {
        open();
        int result = database.delete(ReceipentsAdapter.TABLE_MYDATA, ReceipentsAdapter.DATA_ID + " = ?",
                new String[]{String.valueOf(object.getId())});
        close();

        return result;
    }

    @Override
    public int deleteTable() {
        open();
        int result = database.delete(ReceipentsAdapter.TABLE_MYDATA,null,null);
        close();
        return result;
    }

    @Override
    public ReceipentsModel findById(Integer id) {
        //open connection first
        open();
        //Object
        ReceipentsModel result = null;
        Cursor cursor = null;
        try {
            cursor = database.query(ReceipentsAdapter.TABLE_MYDATA,
                    new String[]{
                            ReceipentsAdapter.DATA_ID,
                            ReceipentsAdapter.EMAIL
                    }
                    , ReceipentsAdapter.DATA_ID + " = ? ",
                    new String[]{String.valueOf(id)},
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                result = new ReceipentsModel.Builder()
                        .email(cursor.getString(1))
                        .id(cursor.getInt(0))
                        .build();
            }
        }
        catch (Exception e){

            result = new ReceipentsModel.Builder()
                    .id(-1)
                    .build();
        }
        close();

        return result;
    }

    @Override
    public HashMap getList() {
        //Contact object
        ReceipentsModel result;
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

                        result = new ReceipentsModel.Builder()
                                .email(cursor.getString(1))
                                .id(cursor.getInt(0))
                                .build();

                        //Populate your hashMap  maybe instead of the counter use the tutorial id=> cursor.getString(0)
                        objectList.put(counter/*cursor.getString(0)*/,new ReceipentsModel.Builder().copy(result).build());

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
