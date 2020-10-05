package com.francescobertamini.app_individuale.ui.championship.champ_standings;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.ui.championship.champ_calendar.ChampionshipFragmentCalendar;
import com.francescobertamini.app_individuale.ui.championship.champ_calendar.ChampionshipFragmentList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionshipStandingsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    JsonObject championship;

    @BindView(R.id.champStandingsTitle)
    TextView _champStandingsTitle;
    @BindView(R.id.champStandingsBottomNavigation)
    BottomNavigationView _champStandingsBottomNavigation;
    @BindView(R.id.backToChampFromStandings)
    ImageButton _backToChampFromStandings;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        championship = (JsonObject) JsonParser.parseString(getIntent().getStringExtra("championship"));

        setContentView(R.layout.activity_championship_standings);
        ButterKnife.bind(this);

        _backToChampFromStandings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _champStandingsTitle.setText("Classifiche di: " + championship.get("nome").getAsString());


        BottomNavigationView navigation = _champStandingsBottomNavigation;
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new ChampionshipFragmentStandingsRacers());


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigationChampStandingsRacer:
                fragment = new ChampionshipFragmentStandingsRacers();
                break;

            case R.id.navigationChampStandingsTeams:
                fragment = new ChampionshipFragmentStandingsTeams();
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
                    .replace(R.id.champ_standings_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
