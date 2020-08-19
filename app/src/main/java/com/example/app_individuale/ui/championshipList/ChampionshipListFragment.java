package com.example.app_individuale.ui.championshipList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_individuale.data_managing.ChampionshipsAdapter;
import com.example.app_individuale.data_managing.JsonExtractor;

import com.example.app_individuale.R;

import java.io.IOException;

public class ChampionshipListFragment extends Fragment {

    private ChampionshipListViewModel championshipListViewModel;

    ChampionshipsAdapter championshipListAdapter;

    public ChampionshipListFragment() throws IOException {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        championshipListViewModel =
                ViewModelProviders.of(this).get(ChampionshipListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_championship_list, container, false);
        championshipListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });

        JsonExtractor jsonExtractor = new JsonExtractor(this.getContext());

        try {
             championshipListAdapter = new ChampionshipsAdapter(jsonExtractor.readJson(), this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }


        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.championships_view);

        recyclerView.setAdapter(championshipListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return root;
    }



}

