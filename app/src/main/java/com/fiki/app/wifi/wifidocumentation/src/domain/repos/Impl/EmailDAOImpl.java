package com.fiki.app.wifi.wifidocumentation.src.domain.repos.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;
import com.fiki.app.wifi.wifidocumentation.src.domain.dbadpater.EmailAdapter;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.EmailDAO;

import java.util.HashMap;

/**
 * Created by root on 2017/10/14.
 */

public class EmailDAOImpl implements EmailDAO {
    private SQLiteDatabase database;
    private EmailAdapter dbHelper;

    public EmailDAOImpl(Context context) {
        dbHelper = new EmailAdapter(context);
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
        String selectQuery = "SELECT * FROM "+EmailAdapter.TABLE_My_details;

        //open connection
        open();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor == null) return cursor;
        cursor.moveToFirst();
        close();
        return cursor;
    }

    @Override
    public Long create(EmailDetails object) {
        open();

        ContentValues values = new ContentValues();

        //settings URL
        values.put(EmailAdapter.EMAIL, object.getEmail());
        //values.put(EmailAdapter.ID, object.getPassword());
        values.put(EmailAdapter.PASSWORD, object.getPassword());

        //Insert row
        Long result = database.insert(EmailAdapter.TABLE_My_details, (String)(null), values);

        //Close database
        close();//Close connection

        return result;
    }

    @Override
    public int update(EmailDetails object) {
        open();//open connection

        ContentValues values = new ContentValues();

        //settings URL
        values.put(EmailAdapter.PASSWORD, object.getPassword());
        values.put(EmailAdapter.EMAIL, object.getEmail());
        values.put(EmailAdapter.ID, object.getId());

        //updating row
        int result = database.update(EmailAdapter.TABLE_My_details, values, EmailAdapter.ID+
                " = ?", new String[]{String.valueOf(object.getId() )} );

        //Close connection
        close();
        return result;
    }

    @Override
    public int delete(EmailDetails object) {
        open();
        int result = database.delete(EmailAdapter.TABLE_My_details, EmailAdapter.ID + " = ?",
                new String[]{String.valueOf(object.getId())});
        close();

        return result;
    }

    @Override
    public int deleteTable() {
        open();
        int result = database.delete(EmailAdapter.TABLE_My_details,null,null);
        close();
        return result;
    }

    @Override
    public EmailDetails findById(Integer id) {
        //open connection first
        open();
        //Object
        EmailDetails result = null;
        Cursor cursor = null;
        try {
            cursor = database.query(EmailAdapter.TABLE_My_details,
                    new String[]{
                            EmailAdapter.ID,
                            EmailAdapter.EMAIL,
                            EmailAdapter.PASSWORD
                    }
                    , EmailAdapter.ID + " = ? ",
                    new String[]{String.valueOf(id)},
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                result = new EmailDetails.Builder()
                        .password(cursor.getString(2))
                        .email(cursor.getString(1))
                        .id(cursor.getInt(0))
                        .build();

            }

        }
        catch (Exception e){

            result = new EmailDetails.Builder()
                    .id(-1)
                    .build();
        }

        close();

        return result;

    }

    @Override
    public HashMap getList() {
        //Contact object
        EmailDetails result;
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

                        result = new EmailDetails.Builder()
                                .password(cursor.getString(2))
                                .email(cursor.getString(1))
                                .id(cursor.getInt(0))
                                .build();

                        //Populate your hashMap  maybe instead of the counter use the tutorial id=> cursor.getString(0)
                        objectList.put(counter/*cursor.getString(0)*/,new EmailDetails.Builder().copy(result).build());

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
