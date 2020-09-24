package com.francescobertamini.app_individuale.ui.account_settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ImagePickerComponentHolder;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.features.imageloader.DefaultImageLoader;
import com.esafirm.imagepicker.model.Image;
import com.francescobertamini.app_individuale.MainActivity;
import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Image> images = new ArrayList<>();

    private SettingsViewModel accountSettingsViewModel;

    Image image;


    @BindView(R.id.settingsProfilePicture)
    ImageView _settingsProfilePicture;
    @BindView(R.id.settingsEditPictureButton)
    ImageView _settingsEditPictureButton;

    @BindView(R.id.settingsName)
    TextView _settingsName;
    @BindView(R.id.settingsMail)
    TextView _settingsMail;
    @BindView(R.id.settingsUsername)
    TextView _settingsUsername;
    @BindView(R.id.editMail)
    FloatingActionButton _editMail;


    /*
    @BindView(R.id.settingsBirthdate)
    TextView _settingsBirthdate;
    @BindView(R.id.settingsAddress)
    TextView _settingsAddress;
    @BindView(R.id.settingsPassword)
    TextView _settingsPassword;
    @BindView(R.id.settingsRememberMe)
    CheckBox _settingsRememberMe;
     */
    @BindView(R.id.settingsBottomNavigation)
    BottomNavigationView _settingsBottomNavigation;
    @BindView(R.id.navigationProfileSettings)
    BottomNavigationItemView _navigationProfileSettings;
    @BindView(R.id.navigationAccountSettings)
    BottomNavigationItemView _navigationAccountSettings;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountSettingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        LayoutInflater layoutInflater = SettingsFragment.this.getLayoutInflater();
        View emailEditDialog = inflater.inflate(R.layout.dialog_edit_mail, null);


        ButterKnife.bind(this, root);


        DBManagerUser dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);

        if (cursor.getInt(cursor.getColumnIndex("has_custom_picture")) == 1) {
            byte[] byteImage = cursor.getBlob(cursor.getColumnIndex("profile_picture"));
            _settingsProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));
        }


        _settingsEditPictureButton.setOnClickListener(v -> {

            start();

        });


        _settingsName.setText(cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("lastname")));

        _settingsMail.setText(cursor.getString(cursor.getColumnIndex("email")));

        _settingsUsername.setText(cursor.getString(cursor.getColumnIndex("username")));


        _editMail.setOnClickListener(v -> {

            editMail();


        });


/*





        _settingsBirthdate.setText(cursor.getString(cursor.getColumnIndex("birthdate")));

        _settingsAddress.setText(cursor.getString(cursor.getColumnIndex("address")));

        _settingsPassword.setText(cursor.getString(cursor.getColumnIndex("password")));

        if(cursor.getInt(cursor.getColumnIndex("remember_me"))==1){
            _settingsRememberMe.setChecked(true);
        } else _settingsRememberMe.setChecked(false);

        _settingsFavouriteNumber.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex("favorite_number"))));

        _settingsFavouriteCar.setText(cursor.getString(cursor.getColumnIndex("favorite_car")));

        _settingsFavouriteTrack.setText(cursor.getString(cursor.getColumnIndex("favorite_track")));

        _settingsHatedTrack.setText(cursor.getString(cursor.getColumnIndex("hated_track")));


*/


        loadFragment(new ProfileSettingsFragment());

        BottomNavigationView navigation = _settingsBottomNavigation;
        navigation.setOnNavigationItemSelectedListener(this);


        dbManagerUser.close();
        return root;

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    private void editMail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modifica email");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_mail, null);
        builder.setView(customLayout);
        EditText _editMailEditText = customLayout.findViewById(R.id.editMailEditText);
        TextInputLayout _editMailtextInputLayout = customLayout.findViewById(R.id.editMailTextInputLayout);


        builder.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = _editMailEditText.getText().toString();

                if (email.isEmpty()) {
                    setErrorTheme(_editMailtextInputLayout);
                    _editMailtextInputLayout.setError("Inserisci la tua mail!");

                } else if (!validateMail(email)) {
                    setErrorTheme(_editMailtextInputLayout);
                    _editMailtextInputLayout.setError("Inserisci una mail valida!");
                } else {

                    unsetErrorTheme(_editMailtextInputLayout);
                    String newEmail = _editMailEditText.getText().toString();
                    DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                    dbManagerUser.open();
                    int result = dbManagerUser.updateMailByUsername(MainActivity.username, newEmail);

                    if (result > 0) {
                        _settingsMail.setText(newEmail);
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Email aggiornata correttamente", Toast.LENGTH_LONG);
                        toast.show();
                        dbManagerUser.close();
                    } else {

                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare l'email.", Toast.LENGTH_LONG);
                        toast.show();

                    }

                }
            }
        });


    }


    private ImagePicker getImagePicker() {

        @SuppressLint("ResourceAsColor") ImagePicker imagePicker = ImagePicker.create(this)
                .language("in") // Set image picker language

                .returnMode(ReturnMode.ALL)

                .folderMode(false) // set folder mode (false by default)
                .includeVideo(false) // include video (false by default)
                .onlyVideo(false) // include video (false by default)
                .toolbarArrowColor(R.color.colorPrimary) // set toolbar arrow up color
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarDoneButtonText("DONE"); // done button text

        ImagePickerComponentHolder.getInstance()
                .setImageLoader(new DefaultImageLoader());

        imagePicker.single();

        return imagePicker.limit(1) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()); // can be full path
    }

    private void start() {
        getImagePicker().start(); // start image picker activity with request code
    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            printImages(images);

            DBManagerUser dbManagerUser = new DBManagerUser(getContext());
            dbManagerUser.open();

            image = images.get(0);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(), bmOptions);
            bitmap = Bitmap.createBitmap(bitmap);
            byte[] byteImage = getBitmapAsByteArray(bitmap);
            dbManagerUser.updatePicture(MainActivity.username, true, byteImage);
            dbManagerUser.close();

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private void printImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {

            stringBuffer.append(images.get(i).getPath()).append("\n");
        }
        Glide.with(_settingsProfilePicture)
                .load(images.get(0).getPath())
                .into(_settingsProfilePicture);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigationProfileSettings:
                fragment = new ProfileSettingsFragment();
                break;

            case R.id.navigationAccountSettings:
                fragment = new AccountSettingsFragment();
                break;

        }

        return loadFragment(fragment);
    }


    private void setErrorTheme(TextInputLayout t) {

        t.setErrorEnabled(true);
        t.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
    }

    private void unsetErrorTheme(TextInputLayout t) {
        t.setErrorEnabled(false);
    }


    private boolean validateMail(String email) {

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }


}