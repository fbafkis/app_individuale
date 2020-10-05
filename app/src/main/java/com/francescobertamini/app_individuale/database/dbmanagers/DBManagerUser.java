package com.francescobertamini.app_individuale.database.dbmanagers;

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

    public void insert(String name, String lastname, String birthdate, String email, String address, String username, String password, int favorite_number, String favorite_car, String favorite_track, String hated_track, boolean has_custom_picture, String profile_picture, boolean remember_me) {
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
        String[] columns = {"name", "lastname", "birthdate", "email", "address", "username", "password", "favorite_number", "favorite_car", "favorite_track", "hated_track", "has_custom_picture", "profile_picture", "remember_me"};
        Cursor cursor = database.query("user", columns, "username like '" + username + "'", null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchByEmail(String email) {
        String[] columns = {"name", "lastname", "birthdate", "email", "address", "username", "password", "favorite_number", "favorite_car", "favorite_track", "hated_track", "has_custom_picture", "profile_picture", "remember_me"};
        Cursor cursor = database.query("user", columns, "email like '" + email + "'", null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String name, String lastname, String birthdate, String email, String address, String username, String password, int favorite_number, String favorite_car, String favorite_track, String hated_track, Boolean hasCustomPicture, String profile_picture, Boolean remember_me) {
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
        contentValues.put("remember_me", remember_me);
        int columnsAffected = database.update("user", contentValues, null, null);
        return columnsAffected;
    }

    public int updatePicture(String username, Boolean hasCustomPicture, String profile_picture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("profile_picture", profile_picture);
        contentValues.put("has_custom_picture", hasCustomPicture);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateMail(String username, String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateAddress(String address, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updatePassword(String password, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateRemeberMe(Boolean rememberMe, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("remember_me", rememberMe);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateFavoriteNumber(int favoriteNumber, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("favorite_number", favoriteNumber);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateFavoriteCar(String favoriteCar, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("favorite_car", favoriteCar);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateFavoriteTrack(String favoriteCircuit, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("favorite_track", favoriteCircuit);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateHatedTrack(String hatedCircuit, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("hated_track", hatedCircuit);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int delete(String username) {
        int columnsAffected = database.delete("user", "username=?", new String[]{username});
        return columnsAffected;
    }

    public int updateRememberMeByUsername(String username, Boolean remember_me) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("remember_me", remember_me);
        int columnsAffected = database.update("user", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateRememberMeByEmail(String email, Boolean remember_me) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("remember_me", remember_me);
        int columnsAffected = database.update("user", contentValues, "email" + "= ?", new String[]{email});
        return columnsAffected;
    }
}
