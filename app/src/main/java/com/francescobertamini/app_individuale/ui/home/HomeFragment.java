package com.francescobertamini.app_individuale.ui.home;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.francescobertamini.app_individuale.MainActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.francescobertamini.app_individuale.ui.settings.SettingsFragment;
import com.francescobertamini.app_individuale.ui.championship_list.ChampionshipsFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;

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
    @BindView(R.id.homeSettingsLink)
    LinearLayout _homeSettingsLink;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);

        ButterKnife.bind(this, root);

        _homeName.setText(cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("lastname")));
        _homeUsername.setText(MainActivity.username);

        if (cursor.getInt(cursor.getColumnIndex("has_custom_picture")) == 1) {
            String imagePath = cursor.getString(cursor.getColumnIndex("profile_picture"));
            File image = new File(imagePath);
            if (image.exists()) {
                Bitmap bitmapImage = BitmapFactory.decodeFile(image.getAbsolutePath());
                int rotate = 0;
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(image.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_NORMAL:
                        rotate = 0;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }

                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                Bitmap rotateBitmap = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(), bitmapImage.getHeight(), matrix, true);
                _homeProfilePicture.setImageBitmap(rotateBitmap);


            } else
                _homeProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_account_circle_100, null));
        }


        _homeChampionshipLink.setOnClickListener(v -> {

            Fragment fragment = null;
            fragment = new ChampionshipsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
            Animation shake;
            shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            _homeChampionshipLink.startAnimation(shake);
            shake.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation a) {
                }
                public void onAnimationRepeat(Animation a) {
                }
                public void onAnimationEnd(Animation a) {
                    fragmentTransaction.commit();
                }
            });
        });

        _homeSettingsLink.setOnClickListener(v -> {

            Fragment fragment = null;
            fragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
            Animation shake;
            shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            _homeSettingsLink.startAnimation(shake);
            shake.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation a) {
                }
                public void onAnimationRepeat(Animation a) {
                }
                public void onAnimationEnd(Animation a) {
                    fragmentTransaction.commit();
                }
            });
        });

        return root;


    }


}