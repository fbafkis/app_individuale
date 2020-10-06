package com.francescobertamini.app_individuale.ui.championships.championships_list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.io.IOException;

public class ChampionshipsAdapter extends FragmentStateAdapter {
    public ChampionshipsAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        if (position == 0) {
            try {
                fragment = new AllChampionshipsFragment();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (position == 1) {
            try {
                fragment = new MyChampionshipsFragment();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

