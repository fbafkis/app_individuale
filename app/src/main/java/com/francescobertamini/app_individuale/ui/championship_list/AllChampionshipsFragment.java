package com.francescobertamini.app_individuale.ui.championship_list;

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

import com.francescobertamini.app_individuale.data_managing.AllChampionshipsAdapter;
import com.francescobertamini.app_individuale.data_managing.JsonExtractor;

import com.francescobertamini.app_individuale.R;

import java.io.IOException;

public class AllChampionshipsFragment extends Fragment{

    private ChampionshipListViewModel championshipListViewModel;

    AllChampionshipsAdapter adapter;

    public AllChampionshipsFragment() throws IOException {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        championshipListViewModel =
                ViewModelProviders.of(this).get(ChampionshipListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all_championships, container, false);

        championshipListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });

        JsonExtractor jsonExtractor = new JsonExtractor(this.getContext());

        try {
            adapter = new AllChampionshipsAdapter(jsonExtractor.readJson(), this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }


        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.championships_view);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return root;
    }





}

