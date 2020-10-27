package com.francescobertamini.app_individuale.database.dbmanagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.francescobertamini.app_individuale.database.DBHelper;

public class DBManagerSettings {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManagerSettings(Context context) {
        this.context = context;
    }

    public DBManagerSettings open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String username, boolean notifications, boolean start_at_bootup, boolean championships_notifications, boolean events_notifications, boolean champ_settings_notifications, boolean racers_notifications) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("notifications", notifications);
        contentValues.put("start_at_bootup", start_at_bootup);
        contentValues.put("championships_notifications", championships_notifications);
        contentValues.put("events_notifications", events_notifications);
        contentValues.put("champ_settings_notifications", champ_settings_notifications);
        contentValues.put("racers_notifications", racers_notifications);
        database.insert("settings", null, contentValues);
    }

    public Cursor fetchByUsername(String username) {
        String[] columns = {"username", "notifications", "start_at_bootup", "championships_notifications", "events_notifications", "champ_settings_notifications", "racers_notifications"};
        Cursor cursor = database.query("settings", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateNotifications(String username, boolean notifications) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("notifications", notifications);
        int columnsAffected = database.update("settings", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateStartAtBootup(String username, boolean start_at_bootup) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("start_at_bootup", start_at_bootup);
        int columnsAffected = database.update("settings", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateChampionshipsNotifications(String username, boolean championships_notifications) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("championships_notifications", championships_notifications);
        int columnsAffected = database.update("settings", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateChampSettingsNotifications(String username, boolean champ_settings_notifications) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("champ_settings_notifications", champ_settings_notifications);
        int columnsAffected = database.update("settings", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateEventsNotifications(String username, boolean events_notifications) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("events_notifications", events_notifications);
        int columnsAffected = database.update("settings", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int updateRacersNotifications(String username, boolean racers_notifications) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("racers_notifications", racers_notifications);
        int columnsAffected = database.update("settings", contentValues, "username" + "= ?", new String[]{username});
        return columnsAffected;
    }

    public int delete(String username) {
        int columnsAffected = database.delete("settings", "username=?", new String[]{username});
        return columnsAffected;
    }

}
