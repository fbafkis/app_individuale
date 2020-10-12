package com.francescobertamini.app_individuale.ui;

import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerStatus;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.ui.main.MainActivity;

public class BasicActivity extends AppCompatActivity {
    DBManagerUser dbManagerUser;
    DBManagerStatus dbManagerStatus;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManagerUser = new DBManagerUser(getApplicationContext());
        dbManagerStatus = new DBManagerStatus(getApplicationContext());
        dbManagerStatus.open();
        dbManagerUser.open();
        Cursor user = dbManagerUser.fetchByUsername(MainActivity.username);
        if (user.getCount() == 1) {
            int remember_me = user.getInt(user.getColumnIndex("remember_me"));
            if (remember_me == 0) {
                dbManagerStatus.update(false, null);
            }
        }
    }
}

