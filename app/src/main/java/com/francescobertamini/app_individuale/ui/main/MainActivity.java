package com.francescobertamini.app_individuale.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.broadcast_receivers.AddEventReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.AddRacerReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.BootUpReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.EditChampSettingsReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.EditEventReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.RemoveChampReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.RemoveRacerReceiver;
import com.francescobertamini.app_individuale.broadcast_receivers.ResetReceiver;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerSettings;
import com.francescobertamini.app_individuale.services.NotificationService;
import com.francescobertamini.app_individuale.ui.BasicActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

public class MainActivity extends BasicActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static String username;
    public static Toolbar mainToolbar;
    AddRacerReceiver addRacerReceiver;
    RemoveRacerReceiver removeRacerReceiver;
    EditChampSettingsReceiver editChampSettingsReceiver;
    AddEventReceiver addEventReceiver;
    EditEventReceiver editEventReceiver;
    RemoveChampReceiver removeChampReceiver;
    ResetReceiver resetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        username = getIntent().getStringExtra("username");
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_championships, R.id.nav_settings, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        registerReceivers();
        DBManagerSettings dbManagerSettings = new DBManagerSettings(this);
        dbManagerSettings.open();
        Cursor cursor = dbManagerSettings.fetchByUsername(username);
        if (cursor.getInt(cursor.getColumnIndex("notifications")) == 1) {
            startNotificationService();
        }
        if (cursor.getInt(cursor.getColumnIndex("start_at_bootup")) == 1) {
            registerReceiver(new BootUpReceiver(), new IntentFilter("android.intent.action.BOOT_COMPLETED"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void startNotificationService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void registerReceivers () {
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
}

