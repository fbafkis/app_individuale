package com.example.app_individuale.database.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_individuale.database.DBHelper;

public class DBManagerPreferences {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManagerPreferences(Context c) {
        context = c;
    }

    public DBManagerPreferences open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    //Insert non necessario.

    public Cursor fetch() {
        String[] columns = {"remember_me"};
        Cursor cursor = database.query("preferences", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateRememberMe(int remember_me) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("remember_me", remember_me);
        int columnsAffected = database.update("preferences", contentValues, null, null);
        return columnsAffected;
    }




    //Delete non necessario.


}