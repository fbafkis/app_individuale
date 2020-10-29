package com.francescobertamini.app_individuale.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerSettings;
import com.francescobertamini.app_individuale.services.NotificationService;
import com.francescobertamini.app_individuale.ui.main.MainActivity;

public class BootUpReceiver extends BroadcastReceiver {
    DBManagerSettings dbManagerSettings;

    @Override
    public void onReceive(Context context, Intent intent) {

        dbManagerSettings= new DBManagerSettings(context);
        dbManagerSettings.open();
        Cursor cursor = dbManagerSettings.fetchByUsername(MainActivity.username);
        if(cursor.getInt(cursor.getColumnIndex("start_at_bootup"))==1) {
            Intent startIntent = new Intent(context, NotificationService.class);
            ContextCompat.startForegroundService(context, startIntent);
        }
        dbManagerSettings.close();
    }
}
