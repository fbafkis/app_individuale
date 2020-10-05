package com.francescobertamini.app_individuale.ui.championships.championships_list;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.ui.main.MainActivity;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.ui.championships.SubChampionshipsDialog;
import com.francescobertamini.app_individuale.ui.championships.UnsubChampionshipsDialog;
import com.francescobertamini.app_individuale.utils.RecyclerItemClickListener;
import com.google.gson.JsonArray;

import java.io.IOException;

public class AllChampionshipsFragment extends Fragment {

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

        JsonExtractorChampionships jsonExtractorChampionships = new JsonExtractorChampionships(this.getContext());

        try {
            adapter = new AllChampionshipsAdapter(jsonExtractorChampionships.readJson(), this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }


        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.allChampsRecycler);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String id = adapter.getItem(position).getAsJsonObject().get("id").getAsString();
                Log.e("Championship", adapter.getItem(position).toString());

                JsonArray partecipants = adapter.getItem(position).getAsJsonObject().get("piloti-iscritti").getAsJsonArray();

                Log.e("Partecipants", partecipants.toString());

                DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                dbManagerUser.open();
                Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);
                String complete_name = cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("lastname"));
                Log.e("Database name",complete_name);

                Boolean has_user_sub = false;

                for (int i = 0; i < partecipants.size(); i++) {
                    if (partecipants.get(i).getAsJsonObject().get("nome").getAsString().equals(complete_name)) {
                        has_user_sub = true;
                    }
                }

                Log.e("Boolean sub", has_user_sub.toString());


                if (has_user_sub) {
                    SubChampionshipsDialog dialog = new SubChampionshipsDialog();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    dialog.display(fragmentManager, id);
                } else {
                    UnsubChampionshipsDialog dialog = new UnsubChampionshipsDialog();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    dialog.display(fragmentManager, id);
                }
            }


            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));

        recyclerView.setLayoutManager(new

                LinearLayoutManager(this.getContext()));


        return root;
    }


}

