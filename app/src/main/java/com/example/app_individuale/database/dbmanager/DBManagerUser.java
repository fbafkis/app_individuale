package com.example.app_individuale.database.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_individuale.database.DBHelper;

public class DBManagerUser {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManagerUser(Context c) {
        context = c;
    }

    public DBManagerUser open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name , String lastname, String birthdate, String email, String address , String username, String password, int favorite_number, int favorite_car, int favorite_track, int hated_track, byte[] profile_picture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lastname", lastname);
        contentValues.put("birthdate", birthdate);
        contentValues.put("email", email);
        contentValues.put("address", address);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("favorite_number", favorite_number);
        contentValues.put("favorite_car", favorite_car);
        contentValues.put("favorite_track", favorite_track);
        contentValues.put("hated_track", hated_track);
        contentValues.put("profile_picture", profile_picture);
        database.insert("user", null, contentValues);
    }

    public Cursor fetch() {
        String[] columns = {"name" , "lastname", "birthdate", "email", "address", "username", "password", "favorite_number", "favorite_car", "favorite_track", "hated_track", "profile_picture"};
        Cursor cursor = database.query("user", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String name , String lastname, String birthdate, String email, String address , String username, String password, int favorite_number, int favorite_car, int favorite_track, int hated_track, Byte profile_picture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lastname", lastname);
        contentValues.put("birthdate", birthdate);
        contentValues.put("email", email);
        contentValues.put("address", address);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("favorite_number", favorite_number);
        contentValues.put("favorite_car", favorite_car);
        contentValues.put("favorite_track", favorite_track);
        contentValues.put("hated_track", hated_track);
        contentValues.put("profile_picture", profile_picture);
        int columnsAffected = database.update("user", contentValues, null, null);
        return columnsAffected;
    }

    public void delete() {
        database.delete("user", null, null);
    }
}
