package com.francescobertamini.app_individuale.ui.championships.championships_list;

import android.database.Cursor;
import android.os.Bundle;
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
import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.ui.championships.SubChampionshipsDialog;
import com.francescobertamini.app_individuale.utils.RecyclerItemClickListener;

import java.io.IOException;

public class MyChampionshipsFragment extends Fragment {

    private ChampionshipListViewModel championshipListViewModel;

    MyChampionshipsAdapter adapter;
    DBManagerUser dbManagerUser;


    public MyChampionshipsFragment() throws IOException {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        championshipListViewModel =
                ViewModelProviders.of(this).get(ChampionshipListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_championships, container, false);

        championshipListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //   textView.setText(s);
            }
        });

        dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);

        String nameLastname = cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("lastname"));

        JsonExtractorChampionships jsonExtractorChampionships = new JsonExtractorChampionships(this.getContext());

        try {
            adapter = new MyChampionshipsAdapter(jsonExtractorChampionships.readJson(), this.getContext(), nameLastname);
        } catch (IOException e) {
            e.printStackTrace();
        }


        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.myChampsRecycler);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String id = adapter.getItem(position).getAsJsonObject().get("id").getAsString();

                SubChampionshipsDialog dialog = new SubChampionshipsDialog();
                FragmentManager fragmentManager = getParentFragmentManager();
                dialog.display(fragmentManager, id);
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
