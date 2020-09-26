package com.francescobertamini.app_individuale;

import android.app.Activity;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import com.francescobertamini.app_individuale.database.DBHelper;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerStatus;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;

public class BasicActivity extends AppCompatActivity {

    @Override

    protected void onDestroy() {

        super.onDestroy();

        DBManagerUser dbManagerUser = new DBManagerUser(getApplicationContext());
        DBManagerStatus dbManagerStatus = new DBManagerStatus(getApplicationContext());

        dbManagerStatus.open();
        dbManagerUser.open();

        Cursor user = dbManagerUser.fetchByUsername(MainActivity.username);

        if (user.getCount() == 1) {

            int remember_me = user.getInt(user.getColumnIndex("remember_me"));

            if (remember_me == 0) {
                dbManagerStatus.update(0, null);
            }
        }
    }
}
