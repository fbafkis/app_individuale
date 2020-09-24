package com.francescobertamini.app_individuale.ui.home;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.francescobertamini.app_individuale.MainActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.francescobertamini.app_individuale.ui.championship_list.ChampionshipsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;

    DBManagerUser dbManagerUser;

    @BindView(R.id.homeProfilePicture)
    CircleImageView _homeProfilePicture;
    @BindView(R.id.homeUsername)
    TextView _homeUsername;
    @BindView(R.id.homeName)
    TextView _homeName;
    @BindView(R.id.homeChampionshipLink)
    LinearLayout _homeChampionshipLink;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);

        ButterKnife.bind(this, root);

        _homeName.setText(cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("lastname")));
        _homeUsername.setText(MainActivity.username);

        if (cursor.getInt(cursor.getColumnIndex("has_custom_picture")) == 1) {
            byte[] byteImage = cursor.getBlob(cursor.getColumnIndex("profile_picture"));
            _homeProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));
        }

        _homeChampionshipLink.setOnClickListener(v -> {
            Fragment fragment = null;

                fragment = new ChampionshipsFragment();


            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();

        });




        return root;


    }


}