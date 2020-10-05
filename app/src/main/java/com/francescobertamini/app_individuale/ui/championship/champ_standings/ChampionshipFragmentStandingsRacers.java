package com.francescobertamini.app_individuale.ui.championship.champ_standings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.ui.championship.champ_calendar.ListEventAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionshipFragmentStandingsRacers extends Fragment {

    JsonObject championship;
    JsonArray racerStandings;
    RecyclerView.Adapter adapter;

    @BindView(R.id.champStandingsRacersList)
    RecyclerView _champStandingsRacersList;

    String string_dates[];


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_champ_standings_racers, null);

        ButterKnife.bind(this, root);

        Bundle args = getArguments();
        championship = (JsonObject) JsonParser.parseString(args.getString("championship"));

        Log.e("Campionato", championship.toString());
        racerStandings = championship.get("classifica-piloti").getAsJsonArray();
        adapter = new RacersStandingAdapter(racerStandings, getContext());
        _champStandingsRacersList.setAdapter(adapter);
        _champStandingsRacersList.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

}
