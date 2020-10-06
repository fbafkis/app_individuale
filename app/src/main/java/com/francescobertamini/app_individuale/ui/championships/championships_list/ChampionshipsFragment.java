package com.francescobertamini.app_individuale.ui.championships.championships_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.francescobertamini.app_individuale.ui.main.MainActivity;
import com.francescobertamini.app_individuale.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChampionshipsFragment extends Fragment {
    ViewPager2 viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_championships, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navView);
        navigationView.getMenu().getItem(1).setChecked(true);
        viewPager = view.findViewById(R.id.championshipsPager);
        viewPager.setAdapter(new ChampionshipsAdapter(this));
        TabLayout tabLayout = view.findViewById(R.id.championshipsListTabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position==0){
                tab.setText("Tutti i campionati");
            }
            if(position==1){
                tab.setText("I tuoi campionati");
            }
        }
        ).attach();
        MainActivity.mainToolbar.setTitle("Campionati");
    }
}