package com.francescobertamini.app_individuale.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.francescobertamini.app_individuale.services.NotificationService;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Trying to start service on boot", Toast.LENGTH_SHORT).show();
        Intent startIntent = new Intent(context, NotificationService.class);
        context.startForegroundService(startIntent);
    }
}
