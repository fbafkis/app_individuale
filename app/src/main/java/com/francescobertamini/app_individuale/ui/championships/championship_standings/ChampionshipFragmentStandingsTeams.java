package com.francescobertamini.app_individuale.ui.championships.championship_standings;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionshipFragmentStandingsTeams extends Fragment {
    JsonObject championship;
    JsonArray teamStandings;
    RecyclerView.Adapter adapter;

    @BindView(R.id.champStandTeamRecycler)
    RecyclerView _champStandingsTeamsList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_champ_standings_teams, null);
        ButterKnife.bind(this, root);
        Bundle args = getArguments();
        championship = (JsonObject) JsonParser.parseString(args.getString("championship"));
        teamStandings = championship.get("classifica-team").getAsJsonArray();
        adapter = new TeamsStandingAdapter(teamStandings, getContext());
        _champStandingsTeamsList.setAdapter(adapter);
        _champStandingsTeamsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

}
