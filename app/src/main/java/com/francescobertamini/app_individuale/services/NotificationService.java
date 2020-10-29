package com.francescobertamini.app_individuale.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.broadcast_receivers.AddEventReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.AddRacerReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.EditChampSettingsReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.EditEventReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.RemoveChampReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.RemoveRacerReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.ResetReceiver;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerSettings;
import com.francescobertamini.app_individuale.ui.main.MainActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NotificationService extends Service {
    NotificationManager notificationManager;
    String notificationChannelsIds[] = new String[4];
    JsonObject baseChampsJsonObject;
    static FileObserver fileObserver;

    private int NOTIFICATION = 69;
    private final int CHAMP_ADDED = 0;
    private final int CHAMP_REMOVED = 1;
    private final int EVENT_ADDED = 2;
    private final int EVENT_REMOVED = 3;
    private final int EVENT_MODIFIED = 4;
    private final int CHAMP_SETTINGS_MODIFIED = 5;
    private final int RACER_ADDED = 6;
    private final int RACER_REMOVED = 7;

    AddRacerReceiver addRacerReceiver;
    RemoveRacerReceiver removeRacerReceiver;
    EditChampSettingsReceiver editChampSettingsReceiver;
    AddEventReceiver addEventReceiver;
    EditEventReceiver editEventReceiver;
    RemoveChampReceiver removeChampReceiver;
    ResetReceiver resetReceiver;

    @Override
    public void onCreate() {

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationChannelsIds[0] = "champs_notification_channel";
        notificationChannelsIds[1] = "events_notification_channel";
        notificationChannelsIds[2] = "champ_settings_notification_channel";
        notificationChannelsIds[3] = "racers_notification_channel";
        createNotificationChannels();
        File file = new File(getApplicationContext().getFilesDir(), "campionati.json");
        fileObserver = new FileObserver(file) {
            @Override
            public void onEvent(int event, String file) {
                if (event == CLOSE_WRITE) {
                    try {
                        checkDifferences();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceivers();
        try {
            baseChampsJsonObject = new JsonExtractorChampionships(getApplicationContext()).getJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileObserver.startWatching();
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        NotificationChannel foregroudNotificationChannel = new NotificationChannel("service_notification_channel", "Service Notification", NotificationManager.IMPORTANCE_MIN);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(foregroudNotificationChannel);
        Notification serviceNotification = new NotificationCompat.Builder(getApplicationContext(), "service_notification_channel")
                .setContentTitle("Sim Career Notifications")
                .setContentText("Puoi ricevere le notifiche di Sim Career")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, serviceNotification);
        DBManagerSettings dbManagerSettings = new DBManagerSettings(getApplicationContext());
        dbManagerSettings.open();
        Cursor settingsCursor = dbManagerSettings.fetchByUsername(MainActivity.username);
        notificationManager.deleteNotificationChannel("service_notification_channel");
        if (settingsCursor.getInt(settingsCursor.getColumnIndex("notifications")) == 0) {
            notificationManager.deleteNotificationChannel("service_notification_channel");
        }
        if (settingsCursor.getInt(settingsCursor.getColumnIndex("championships_notifications")) == 0) {
            notificationManager.deleteNotificationChannel("champs_notification_channel");
        }
        if (settingsCursor.getInt(settingsCursor.getColumnIndex("events_notifications")) == 0) {
            notificationManager.deleteNotificationChannel("events_notification_channel");
        }
        if (settingsCursor.getInt(settingsCursor.getColumnIndex("champ_settings_notifications")) == 0) {
            notificationManager.deleteNotificationChannel("champ_settings_notification_channel");
        }
        if (settingsCursor.getInt(settingsCursor.getColumnIndex("racers_notifications")) == 0) {
            notificationManager.deleteNotificationChannel("racers_notification_channel");
        }
        Toast.makeText(getApplicationContext(), "SimCareer Notification Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceivers();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        unregisterReceivers();
    }

    private void showNotification(int type, String title, String text) {

        ///CORREGGERE PROBLEMI NEL LANCIO DI INTENT

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        ///////

        NotificationCompat.Builder builder = null;
        switch (type) {
            case CHAMP_ADDED:
            case CHAMP_REMOVED:
                builder = new NotificationCompat.Builder(getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_cup)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                break;
            case EVENT_ADDED:
            case EVENT_MODIFIED:
            case EVENT_REMOVED:
                builder = new NotificationCompat.Builder(getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_outline_calendar_today_24)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                break;
            case CHAMP_SETTINGS_MODIFIED:
                builder = new NotificationCompat.Builder(getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_outline_settings_applications_24)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                break;
            case RACER_ADDED:
            case RACER_REMOVED:
                builder = new NotificationCompat.Builder(getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_helmet)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            switch (type) {
                case CHAMP_ADDED:
                    builder.setChannelId(notificationChannelsIds[0]);
                    break;
                case CHAMP_REMOVED:
                    builder.setChannelId(notificationChannelsIds[0]);
                    break;
                case EVENT_ADDED:
                    builder.setChannelId(notificationChannelsIds[1]);
                    break;
                case EVENT_MODIFIED:
                    builder.setChannelId(notificationChannelsIds[1]);
                    break;
                case EVENT_REMOVED:
                    builder.setChannelId(notificationChannelsIds[1]);
                    break;
                case CHAMP_SETTINGS_MODIFIED:
                    builder.setChannelId(notificationChannelsIds[2]);
                    break;
                case RACER_ADDED:
                    builder.setChannelId(notificationChannelsIds[3]);
                    break;
                case RACER_REMOVED:
                    builder.setChannelId(notificationChannelsIds[3]);
                    break;
            }
        }
        notificationManager.notify(NOTIFICATION, builder.build());
    }

    void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(
                    notificationChannelsIds[0],
                    "Championships Notifications",
                    NotificationManager.IMPORTANCE_HIGH));
            notificationManager.createNotificationChannel(new NotificationChannel(
                    notificationChannelsIds[1],
                    "Events Notifications",
                    NotificationManager.IMPORTANCE_HIGH));
            notificationManager.createNotificationChannel(new NotificationChannel(
                    notificationChannelsIds[2],
                    "Championship Settings Notifications",
                    NotificationManager.IMPORTANCE_HIGH));
            notificationManager.createNotificationChannel(new NotificationChannel(
                    notificationChannelsIds[3],
                    "Racers Notifications",
                    NotificationManager.IMPORTANCE_HIGH));
        }
    }

    void checkDifferences() throws IOException {
        JsonObject tempChampsJsonObject = new JsonExtractorChampionships(getApplicationContext()).getJsonObject();
        ArrayList<JsonObject> baseChamps = new ArrayList<>();
        ArrayList<JsonObject> tempChamps = new ArrayList<>();
        if (!baseChampsJsonObject.equals(tempChampsJsonObject)) {
            for (int i = 0; i < baseChampsJsonObject.get("campionati").getAsJsonArray().size(); i++) {
                baseChamps.add(baseChampsJsonObject.get("campionati").getAsJsonArray().get(i).getAsJsonObject());
            }
            for (int i = 0; i < tempChampsJsonObject.get("campionati").getAsJsonArray().size(); i++) {
                tempChamps.add(tempChampsJsonObject.get("campionati").getAsJsonArray().get(i).getAsJsonObject());
            }
            if (baseChamps.size() != tempChamps.size()) {
                JsonObject champ = null;
                if (baseChamps.size() < tempChamps.size()) {
                    for (int i = 0; i < tempChamps.size(); i++) {
                        if (!baseChamps.contains(tempChamps.get(i))) {
                            champ = tempChamps.get(i);
                        }
                    }
                    if (champ != null)
                        showNotification(CHAMP_ADDED, "Aggiunto nuovo campionato", "E' stato aggiunto il nuovo campionato: " + champ.get("nome").getAsString());
                } else if (baseChamps.size() > tempChamps.size()) {
                    for (int i = 0; i < baseChamps.size(); i++) {
                        if (!tempChamps.contains(baseChamps.get(i))) {
                            champ = baseChamps.get(i);
                        }
                    }
                    if (champ != null)
                        showNotification(CHAMP_REMOVED, "Campionato rimosso", "E' stato rimosso il campionato: " + champ.get("nome").getAsString());
                }
            } else {
                ArrayList<JsonObject> newChamps = new ArrayList<>();
                ArrayList<JsonObject> oldChamps = new ArrayList<>();
                for (int i = 0; i < tempChamps.size(); i++) {
                    if (!(tempChamps.get(i).getAsJsonObject().equals(baseChamps.get(i).getAsJsonObject()))) {
                        newChamps.add(tempChamps.get(i));
                        oldChamps.add(baseChamps.get(i));
                    }
                }
                for (int i = 0; i < oldChamps.size(); i++) {
                    JsonObject newChamp = newChamps.get(i);
                    JsonObject oldChamp = oldChamps.get(i);
                    if (newChamp.get("calendario").getAsJsonArray() != oldChamp.get("calendario").getAsJsonArray()) {
                        JsonArray newCalendar = newChamp.get("calendario").getAsJsonArray();
                        JsonArray oldCalendar = oldChamp.get("calendario").getAsJsonArray();
                        if (newCalendar.size() != oldCalendar.size()) {
                            JsonObject event = null;
                            if (oldCalendar.size() < newCalendar.size()) {
                                for (int e = 0; e < newCalendar.size(); e++) {
                                    if (!oldCalendar.contains(newCalendar.get(e))) {
                                        event = newCalendar.get(e).getAsJsonObject();
                                    }
                                }
                                if (event != null)
                                    showNotification(EVENT_ADDED, "Aggiunto nuovo evento", "E' stato aggiunto il nuovo evento " + event.get("circuito").getAsString()
                                            + " al campionato " + newChamp.get("nome").getAsString()
                                            + " in data " + event.get("data").getAsString());
                            } else if (newCalendar.size() < oldCalendar.size()) {
                                for (int e = 0; e < oldCalendar.size(); e++) {
                                    if (!newCalendar.contains(oldCalendar.get(e))) {
                                        event = oldCalendar.get(e).getAsJsonObject();
                                    }
                                }
                                if (event != null)
                                    showNotification(EVENT_REMOVED, "Evento rimosso", "E' stato rimosso l'evento: " + event.get("circuito").getAsString()
                                            + " dal campionato " + newChamp.get("nome").getAsString()
                                            + " che era pianificato in data " + event.get("data").getAsString());
                            }
                        } else {
                            ArrayList<JsonObject> newEvents = new ArrayList<>();
                            ArrayList<JsonObject> oldEvents = new ArrayList<>();
                            for (int e = 0; e < oldCalendar.size(); e++) {
                                if (!newCalendar.get(e).getAsJsonObject().toString().equals(oldCalendar.get(e).getAsJsonObject().toString())) {
                                    newEvents.add(newCalendar.get(e).getAsJsonObject());
                                    oldEvents.add(oldCalendar.get(e).getAsJsonObject());
                                }
                            }
                            JsonObject event = null;
                            for (int e = 0; e < oldEvents.size(); e++) {
                                if (!newEvents.get(e).toString().equals(oldEvents.get(e).toString())) {
                                    event = newEvents.get(e);
                                }
                            }
                            if (event != null) {
                                showNotification(EVENT_MODIFIED, "Evento modificato", "E' stato modificato l'evento: " + event.get("circuito").getAsString()
                                        + " del campionato " + newChamp.get("nome").getAsString()
                                        + ", che ora ha data " + event.get("data").getAsString());
                            }
                        }
                    }
                    if (!(oldChamp.get("impostazioni-gioco").getAsJsonArray().toString().equals(newChamp.get("impostazioni-gioco").getAsJsonArray().toString()))) {
                        showNotification(CHAMP_SETTINGS_MODIFIED, "Impostazioni di gioco modificate", "Sono state modificate " +
                                "le impostazioni di gioco del campionato "
                                + newChamp.get("nome").getAsString());
                    }
                    if (!(newChamp.get("piloti-iscritti").getAsJsonArray().equals(oldChamp.get("piloti-iscritti").getAsJsonArray()))) {
                        JsonArray newRacers = newChamp.get("piloti-iscritti").getAsJsonArray();
                        JsonArray oldRacers = oldChamp.get("piloti-iscritti").getAsJsonArray();
                        if (newRacers.size() != oldRacers.size()) {
                            JsonObject racer = null;
                            if (oldRacers.size() < newRacers.size()) {
                                for (int e = 0; e < newRacers.size(); e++) {
                                    if (!oldRacers.contains(newRacers.get(e))) {
                                        racer = newRacers.get(e).getAsJsonObject();
                                    }
                                }
                                if (racer != null)
                                    showNotification(RACER_ADDED, "Nuovo pilota", "Il pilota " + racer.get("nome").getAsString()
                                            + " del team " + racer.get("team").getAsString()
                                            + " si è iscritto al campionato " + newChamp.get("nome").getAsString());
                            } else if (newRacers.size() < oldRacers.size()) {
                                for (int e = 0; e < oldRacers.size(); e++) {
                                    if (!newRacers.contains(oldRacers.get(e))) {
                                        racer = oldRacers.get(e).getAsJsonObject();
                                    }
                                }
                                if (racer != null)
                                    showNotification(RACER_REMOVED, "Pilota disiscritto", "Il pilota " + racer.get("nome").getAsString()
                                            + " del team " + racer.get("team").getAsString()
                                            + " si è  disiscritto dal campionato " + newChamp.get("nome").getAsString());
                            }
                        }
                    }
                }
            }
            baseChampsJsonObject = tempChampsJsonObject;
        }
    }

    private void registerReceivers() {
        addRacerReceiver = new AddRacerReceiver();
        removeRacerReceiver = new RemoveRacerReceiver();
        editChampSettingsReceiver = new EditChampSettingsReceiver();
        addEventReceiver = new AddEventReceiver();
        editEventReceiver = new EditEventReceiver();
        removeChampReceiver = new RemoveChampReceiver();
        resetReceiver = new ResetReceiver();
        IntentFilter racerAdded = new IntentFilter("com.francescobertamini.perform.addRacer");
        registerReceiver(addRacerReceiver, racerAdded);
        IntentFilter racerRemoved = new IntentFilter("com.francescobertamini.perform.removeRacer");
        registerReceiver(removeRacerReceiver, racerRemoved);
        IntentFilter champSettingsEdited = new IntentFilter("com.francescobertamini.perform.editChampSettings");
        registerReceiver(editChampSettingsReceiver, champSettingsEdited);
        IntentFilter eventAdded = new IntentFilter("com.francescobertamini.perform.addEvent");
        registerReceiver(addEventReceiver, eventAdded);
        IntentFilter eventEdited = new IntentFilter("com.francescobertamini.perform.editEvent");
        registerReceiver(editEventReceiver, eventEdited);
        IntentFilter championshipRemoved = new IntentFilter("com.francescobertamini.perform.removeChampionship");
        registerReceiver(removeChampReceiver, championshipRemoved);
        IntentFilter resetted = new IntentFilter("com.francescobertamini.perform.reset");
        registerReceiver(resetReceiver, resetted);
    }
    private void unregisterReceivers() {
        unregisterReceiver(addRacerReceiver);
        unregisterReceiver(removeRacerReceiver);
        unregisterReceiver(editChampSettingsReceiver);
        unregisterReceiver(addEventReceiver);
        unregisterReceiver(editEventReceiver);
        unregisterReceiver(removeChampReceiver);
        unregisterReceiver(resetReceiver);
    }

}

