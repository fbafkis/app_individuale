package com.francescobertamini.app_individuale.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
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
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BasicActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static String username;
    public static Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        username = getIntent().getStringExtra("username");
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        TextView _drawerName = navigationView.getHeaderView(0).findViewById(R.id.drawerName);
        TextView _drawerUsername = navigationView.getHeaderView(0).findViewById(R.id.drawerUsername);
        TextView _drawerEmail = navigationView.getHeaderView(0).findViewById(R.id.drawerEmail);
        TextView _drawerNumber = navigationView.getHeaderView(0).findViewById(R.id.drawerNumber);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_championships, R.id.nav_settings, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        DBManagerSettings dbManagerSettings = new DBManagerSettings(this);
        dbManagerSettings.open();
        Cursor settingsCursor = dbManagerSettings.fetchByUsername(username);
        dbManagerSettings.close();
        if (settingsCursor.getInt(settingsCursor.getColumnIndex("notifications")) == 1) {
            stopNotificationService();
            startNotificationService();
        }

        DBManagerUser dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();
        Cursor userCursor = dbManagerUser.fetchByUsername(username);
        dbManagerUser.close();
        setDrawerPicture(navigationView, userCursor);
        _drawerNumber.setText(userCursor.getString(userCursor.getColumnIndex("favorite_number")));
        _drawerName.setText(userCursor.getString(userCursor.getColumnIndex("name")) + " " +
                userCursor.getString(userCursor.getColumnIndex("lastname")));
        _drawerUsername.setText(username);
        _drawerEmail.setText(userCursor.getString(userCursor.getColumnIndex("email")));
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


    public void startNotificationService() {
        Intent serviceIntent = new Intent(getBaseContext(), NotificationService.class);
        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBManagerSettings dbManagerSettings = new DBManagerSettings(this);
        dbManagerSettings.open();
        Cursor settingsCursor = dbManagerSettings.fetchByUsername(username);
        dbManagerSettings.close();
        if (settingsCursor.getInt(settingsCursor.getColumnIndex("notifications")) == 1) {
            stopNotificationService();
            startNotificationService();
        }
    }

    public void stopNotificationService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    private void setDrawerPicture(NavigationView navigationView, Cursor userCursor) {
        View navHeader = navigationView.getHeaderView(0);
        ImageView _drawerPicture = navHeader.findViewById(R.id.drawerPicture);
        if (userCursor.getInt(userCursor.getColumnIndex("has_custom_picture")) == 1) {
            String imagePath = userCursor.getString(userCursor.getColumnIndex("profile_picture"));
            File image = new File(imagePath);
            if (image.exists()) {
                Bitmap bitmapImage = BitmapFactory.decodeFile(image.getAbsolutePath());
                int rotate = 0;
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(image.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_NORMAL:
                        rotate = 0;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                Bitmap rotateBitmap = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(), bitmapImage.getHeight(), matrix, true);
                _drawerPicture.setImageBitmap(rotateBitmap);
            } else
                _drawerPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_account_circle_100, null));
        }
    }
}

