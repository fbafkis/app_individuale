package com.francescobertamini.app_individuale.ui.account_settings;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.francescobertamini.app_individuale.MainActivity;
import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.google.android.material.chip.Chip;

public class ProfileSettingsFragment extends Fragment {


    @BindView(R.id.settingsFavouriteNumber)
    Chip _settingsFavouriteNumber;
    @BindView(R.id.settingsFavouriteCar)
    TextView _settingsFavouriteCar;
    @BindView(R.id.settingsFavouriteTrack)
    TextView _settingsFavouriteTrack;
    @BindView(R.id.settingsHatedTrack)
    TextView _settingsHatedTrack;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile_settings, container, false);


        ButterKnife.bind(this,root);

        DBManagerUser dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);


        _settingsFavouriteNumber.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex("favorite_number"))));

        _settingsFavouriteCar.setText(cursor.getString(cursor.getColumnIndex("favorite_car")));

        _settingsFavouriteTrack.setText(cursor.getString(cursor.getColumnIndex("favorite_track")));

        _settingsHatedTrack.setText(cursor.getString(cursor.getColumnIndex("hated_track")));


        return root;


    }

}
