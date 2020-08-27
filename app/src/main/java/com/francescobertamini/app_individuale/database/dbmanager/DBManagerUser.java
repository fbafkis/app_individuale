package com.francescobertamini.app_individuale.database.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.francescobertamini.app_individuale.database.DBHelper;

public class DBManagerUser {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManagerUser(Context context) {
        this.context = context;
    }

    public DBManagerUser open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String lastname, String birthdate, String email, String address, String username, String password, int favorite_number, String favorite_car, String favorite_track, String hated_track, boolean has_custom_picture,  byte[] profile_picture, Boolean remember_me) {
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
        contentValues.put("has_custom_picture", has_custom_picture);
        contentValues.put("profile_picture", profile_picture);
        contentValues.put("remember_me", remember_me);
         database.insert("user", null, contentValues);
    }



    public Cursor fetchByUsername(String username) {
        String[] columns = {"name", "lastname", "birthdate", "email", "address", "username", "password", "favorite_number", "favorite_car", "favorite_track", "hated_track","has_custom_picture", "profile_picture", "remember_me"};
        Cursor cursor = database.query("user", columns, "username like '" + username + "'" , null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Cursor fetchByEmail(String email) {
        String[] columns = {"name", "lastname", "birthdate", "email", "address", "username", "password", "favorite_number", "favorite_car", "favorite_track", "hated_track","has_custom_picture", "profile_picture", "remember_me"};
        Cursor cursor = database.query("user", columns, "email like '" + email + "'", null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int update(String name, String lastname, String birthdate, String email, String address, String username, String password, int favorite_number, String favorite_car, String favorite_track, String hated_track, Boolean hasCustomPicture, Byte profile_picture, Boolean remember_me) {
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
        contentValues.put("has_custom_picture", hasCustomPicture);
        contentValues.put("remember_me",  remember_me);
        int columnsAffected = database.update("user", contentValues, null, null);
        return columnsAffected;
    }

    public void delete(String username) {
        database.delete("user", "username=?", new String[]{username});
    }


    public void updateRememberMe(String username, Boolean remember_me) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("remember_me", remember_me);
        database.update("user", contentValues, "username" + "= ?", new String[] {username});



    }


}
