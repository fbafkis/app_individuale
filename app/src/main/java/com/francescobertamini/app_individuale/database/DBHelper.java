package com.francescobertamini.app_individuale.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="APPDB";


    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE user (name TEXT, lastname TEXT, birthdate TEXT, email TEXT, address TEXT, username TEXT, password TEXT, favorite_number INTEGER, favorite_car TEXT, favorite_track TEXT, hated_track TEXT, has_custom_picture INTEGER, profile_picture BLOB, remember_me INTEGER)";
        String sql2 = "CREATE TABLE status (is_user_logged INTEGER, user_logged TEXT)";
        String sql3 = "INSERT INTO status (is_user_logged, user_logged) VALUES (0, null)";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
