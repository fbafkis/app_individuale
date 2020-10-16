package com.francescobertamini.app_individuale.ui.main;

import android.os.Bundle;
import android.view.Menu;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.ui.BasicActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

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
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_championships, R.id.nav_settings,R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
}

