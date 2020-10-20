package com.francescobertamini.app_individuale.ui.settings;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.francescobertamini.app_individuale.ui.main.MainActivity;
import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.utils.ImagePickerActivity;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_IMAGE = 100;
    private SettingsViewModel accountSettingsViewModel;

    @BindView(R.id.settingsProfilePicture)
    ImageView _settingsProfilePicture;
    @BindView(R.id.settingsEditProfilePicButton)
    ImageView _settingsEditPictureButton;
    @BindView(R.id.settingsName)
    TextView _settingsName;
    @BindView(R.id.settingsMail)
    TextView _settingsMail;
    @BindView(R.id.settingsUsername)
    TextView _settingsUsername;
    @BindView(R.id.editMailButton)
    FloatingActionButton _editMail;
    @BindView(R.id.settingsBottomNavigation)
    BottomNavigationView _settingsBottomNavigation;
    @BindView(R.id.navigationProfileSettings)
    BottomNavigationItemView _navigationProfileSettings;
    @BindView(R.id.navigationAccountSettings)
    BottomNavigationItemView _navigationAccountSettings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountSettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navView);
        navigationView.getMenu().getItem(2).setChecked(true);
        ButterKnife.bind(this, root);
        MainActivity.mainToolbar.setTitle("Impostazioni");
        DBManagerUser dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();
        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);
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
                _settingsProfilePicture.setImageBitmap(rotateBitmap);
            } else
                _settingsProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_account_circle_100, null));
        }

        _settingsProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {

                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    Log.e("cristo", "cristo");

                                    showImagePickerOptions();
                                } else {
                                    Toast.makeText(getContext(), "Devi concedere i permessi all'applicazione!", Toast.LENGTH_LONG);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        _settingsName.setText(cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("lastname")));
        _settingsMail.setText(cursor.getString(cursor.getColumnIndex("email")));
        _settingsUsername.setText(cursor.getString(cursor.getColumnIndex("username")));
        _editMail.setOnClickListener(v -> {
            editMail();
        });
        loadFragment(new ProfileSettingsFragment());
        BottomNavigationView navigation = _settingsBottomNavigation;
        navigation.setOnNavigationItemSelectedListener(this);
        dbManagerUser.close();
        return root;
    }

    private void showImagePickerOptions() {
        Log.e("diocan", "diocan");
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settingsFragmentContainer, fragment)
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
                    int result = dbManagerUser.updateMail(MainActivity.username, newEmail);
                    if (result > 0) {
                        _settingsMail.setText(newEmail);
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Email aggiornata correttamente", Toast.LENGTH_LONG);
                        toast.show();
                        dbManagerUser.close();
                    } else {
                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare l'email.", Toast.LENGTH_LONG);
                        toast.show();
                        dbManagerUser.close();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                dbManagerUser.open();
                dbManagerUser.updatePicture(MainActivity.username, true, uri.getPath());
                dbManagerUser.close();
                Bitmap image = BitmapFactory.decodeFile(uri.getPath());
                _settingsProfilePicture.setImageBitmap(image);
            }
        }
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