package com.francescobertamini.app_individuale.ui.championships.championship_events;

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

public class ChampionshipFragmentList extends Fragment {

    JsonObject championship;
    JsonArray events;
    RecyclerView.Adapter adapter;

    @BindView(R.id.champEventsListRecycler)
    RecyclerView _champEventsList;

    String string_dates[];


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_champ_events_list, null);

        ButterKnife.bind(this, root);

        Bundle args = getArguments();
        championship = (JsonObject) JsonParser.parseString(args.getString("championship"));

        Log.e("Campionato", championship.toString());
        events = championship.get("calendario").getAsJsonArray();
        adapter = new ListEventAdapter(events, getContext());
        _champEventsList.setAdapter(adapter);
        _champEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

}
