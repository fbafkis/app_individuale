package com.francescobertamini.app_individuale.ui.championships.championships_list;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.ui.championships.ChampionshipsDialog;
import com.francescobertamini.app_individuale.ui.main.MainActivity;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.utils.RecyclerItemClickListener;
import com.google.gson.JsonArray;

import java.io.IOException;

public class AllChampionshipsFragment extends Fragment {
    AllChampionshipsAdapter adapter;
    Fragment championshipsFragment;

    public AllChampionshipsFragment(Fragment championshipsFragment) throws IOException {
        this.championshipsFragment = championshipsFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_championships, container, false);
        try {
            adapter = new AllChampionshipsAdapter(this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.allChampsRecycler);
        recyclerView.setAdapter(adapter);
        TextView noElements = root.findViewById(R.id.allChampNoElements);
        if(adapter.getItemCount()==0)
            noElements.setVisibility(View.VISIBLE);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = adapter.getItem(position).getAsJsonObject().get("id").getAsString();
                JsonArray partecipants = adapter.getItem(position).getAsJsonObject().get("piloti-iscritti").getAsJsonArray();

                Log.e("Partecipants", partecipants.toString());

                DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                dbManagerUser.open();
                Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);
                String complete_name = cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("lastname"));
                Log.e("Database name", complete_name);

                Boolean has_user_sub = false;

                for (int i = 0; i < partecipants.size(); i++) {
                    if (partecipants.get(i).getAsJsonObject().get("nome").getAsString().equals(complete_name)) {
                        has_user_sub = true;
                    }
                }

                Log.e("Boolean sub", has_user_sub.toString());


                if (has_user_sub) {
                    ChampionshipsDialog dialog = new ChampionshipsDialog();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    dialog.display(fragmentManager, id, true, championshipsFragment, 0);

                } else {
                    ChampionshipsDialog dialog = new ChampionshipsDialog();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    dialog.display(fragmentManager, id, false, championshipsFragment, 0);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return root;
    }


}

