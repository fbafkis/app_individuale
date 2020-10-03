package com.francescobertamini.app_individuale.ui.championship.champ_calendar;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.ui.settings.AccountSettingsFragment;
import com.francescobertamini.app_individuale.ui.settings.ProfileSettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionshipEventsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    JsonObject championship;

    @BindView(R.id.champCalendarTitle)
    TextView _champCalendarTitle;
    @BindView(R.id.champCalendarBottomNavigation)
    BottomNavigationView _champCalendarBottomNavigation;
    @BindView(R.id.backToChampFromCal)
    ImageButton _backToChampFromCal;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        championship = (JsonObject) JsonParser.parseString(getIntent().getStringExtra("championship"));

        setContentView(R.layout.activity_championship_events);
        ButterKnife.bind(this);

        _backToChampFromCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _champCalendarTitle.setText("Eventi del campionato: " + championship.get("nome").getAsString());


        BottomNavigationView navigation = _champCalendarBottomNavigation;
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new ChampionshipFragmentList());


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigationChampEventsList:
                fragment = new ChampionshipFragmentList();
                break;

            case R.id.navigationChampEventsCalendar:
                fragment = new ChampionshipFragmentCalendar();
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        Bundle args = new Bundle();
        args.putString("championship", championship.toString());
        fragment.setArguments(args);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.champ_calendar_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
