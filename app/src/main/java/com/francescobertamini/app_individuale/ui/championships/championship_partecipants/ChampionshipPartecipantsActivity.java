package com.francescobertamini.app_individuale.ui.championships.championship_partecipants;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionshipPartecipantsActivity extends AppCompatActivity {
    JsonObject championship;
    JsonArray partecipants;
    PartecipantsAdapter adapter;

    @BindView(R.id.champPartecipantsRecycler)
    RecyclerView _champPartecipants;
    @BindView(R.id.champPartecipantsTitle)
    TextView _champPartecipantTitle;
    @BindView(R.id.backToChampFromPart)
    ImageButton _backToChampFromPart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        championship = (JsonObject) JsonParser.parseString(getIntent().getStringExtra("championship"));
        partecipants = championship.get("piloti-iscritti").getAsJsonArray();
        setContentView(R.layout.activity_championship_partecipants);
        ButterKnife.bind(this);
        _backToChampFromPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        _champPartecipantTitle.setText("Piloti del campionato: " + championship.get("nome").getAsString());
        adapter = new PartecipantsAdapter(partecipants, getApplicationContext());
        _champPartecipants.setAdapter(adapter);
        _champPartecipants.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
