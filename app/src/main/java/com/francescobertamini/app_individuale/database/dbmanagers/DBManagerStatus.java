package com.francescobertamini.app_individuale.database.dbmanagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.francescobertamini.app_individuale.database.DBHelper;

public class DBManagerStatus {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManagerStatus(Context c) {
        context = c;
    }

    public DBManagerStatus open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    //Insert non necessario.
    public Cursor fetch() {
        String[] columns = {"is_user_logged", "user_logged"};
        Cursor cursor = database.query("status", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(boolean isUserLogged, String userLogged) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_user_logged", isUserLogged);
        contentValues.put("user_logged", userLogged);
        int columnsAffected = database.update("status", contentValues, null, null);
        return columnsAffected;
    }
    //Delete non necessario.
}