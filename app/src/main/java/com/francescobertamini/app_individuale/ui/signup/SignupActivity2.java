package com.francescobertamini.app_individuale.ui.signup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerSettings;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.ui.login.LoginActivity;
import com.francescobertamini.app_individuale.utils.ImagePickerActivity;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.francescobertamini.app_individuale.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SignupActivity2 extends AppCompatActivity {
    String name;
    String lastname;
    String address;
    String birthdate;
    String email;
    String username;
    String password;
    String image;
    Boolean hasCustomPicture = false;
    int favNumber;
    String favCar;
    String favTrack;
    String hatedTrack;
    public static final int REQUEST_IMAGE = 100;

    @BindView(R.id.signupProfilePictureImageView)
    ImageView _signupProfilePictureImageView;
    @BindView(R.id.signupFavouriteCarEditText)
    EditText _signupFavouriteCarEditText;
    @BindView(R.id.signupFavouriteCarTextInputLayout)
    TextInputLayout _signupFavouriteCarTextInputLayout;
    @BindView(R.id.signupFavouriteNumberEditText)
    EditText _signupFavouriteNumberEditText;
    @BindView(R.id.signupFavouriteNumberTextInputLayout)
    TextInputLayout _signupFavouriteNumberTextInputLayout;
    @BindView(R.id.signupFavouriteTrackEditText)
    EditText _signupFavouriteTrackEditText;
    @BindView(R.id.signupFavouriteTrackTextInputLayout)
    TextInputLayout _signupFavouriteTrackTextInputLayout;
    @BindView(R.id.signupHatedTrackNameEditText)
    EditText _signupHatedTrackNameEditText;
    @BindView(R.id.signupHatedTrackTextInputLayout)
    TextInputLayout _signupHatedTrackTextInputLayout;
    @BindView(R.id.signupButton)
    Button _signupButton;
    @BindView(R.id.signupEditPictureButton)
    ImageView _signupEditPictureButton;
    @BindView(R.id.signup2BackButton)
    ImageView _signup2BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("name");
        lastname = getIntent().getStringExtra("lastname");
        address = getIntent().getStringExtra("address");
        birthdate = getIntent().getStringExtra("birthdate");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        _signupEditPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(SignupActivity2.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Devi concedere i permessi all'applicazione!", Toast.LENGTH_LONG);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        _signup2BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void checkFields() {
        Boolean carOK = false;
        Boolean numberOK = false;
        Boolean favCicuitOK = false;
        Boolean hatedTrackOK = false;

        if (_signupFavouriteNumberEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupFavouriteNumberTextInputLayout);
            _signupFavouriteNumberTextInputLayout.setError("Inserisci il tuo numero di gara!");
            numberOK = false;
        } else if (_signupFavouriteNumberEditText.getText().length() > 3) {
            setErrorTheme(_signupFavouriteNumberTextInputLayout);
            _signupFavouriteNumberTextInputLayout.setError("Inserisci un numero valido!");
            numberOK = false;
        } else {
            numberOK = true;
            unsetErrorTheme(_signupFavouriteNumberTextInputLayout);
            favNumber = Integer.parseInt(_signupFavouriteNumberEditText.getText().toString().trim());
        }

        if (_signupFavouriteCarEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupFavouriteCarTextInputLayout);
            _signupFavouriteCarTextInputLayout.setError("Inserisci la tua auto preferita!");
            carOK = false;
        } else {
            carOK = true;
            unsetErrorTheme(_signupFavouriteCarTextInputLayout);
            favCar = _signupFavouriteCarEditText.getText().toString().trim();
        }

        if (_signupFavouriteTrackEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupFavouriteTrackTextInputLayout);
            _signupFavouriteTrackTextInputLayout.setError("Inserisci il tuo circuito preferito!");
            favCicuitOK = false;
        } else {
            favCicuitOK = true;
            unsetErrorTheme(_signupFavouriteTrackTextInputLayout);
            favTrack = _signupFavouriteTrackEditText.getText().toString().trim();
        }

        if (_signupHatedTrackNameEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupHatedTrackTextInputLayout);
            _signupHatedTrackTextInputLayout.setError("Inserisci il tuo circuito più odiato!");
            hatedTrackOK = false;
        } else {
            hatedTrackOK = true;
            unsetErrorTheme(_signupHatedTrackTextInputLayout);
            hatedTrack = _signupHatedTrackNameEditText.getText().toString().trim();
        }

        if (numberOK && carOK && favCicuitOK && hatedTrackOK) {
            createUser(hasCustomPicture);
        }
    }

    private void createUser(Boolean hasCustomPicture) {
        DBManagerUser dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();
        if (hasCustomPicture) {
            dbManagerUser.insert(name, lastname, birthdate, email, address, username, password, favNumber, favCar, favTrack, hatedTrack, true, image, false);
        } else {
            dbManagerUser.insert(name, lastname, birthdate, email, address, username, password, favNumber, favCar, favTrack, hatedTrack, false, null, false);
        }
        dbManagerUser.close();
        DBManagerSettings dbManagerSettings = new DBManagerSettings(this);
        dbManagerSettings.open();
        dbManagerSettings.insert(username, true, true, true, true, true, true);
        dbManagerSettings.close();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("accountCreated", true);
        intent.putExtra("new_user_username", username);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void setErrorTheme(TextInputLayout t) {
        t.setErrorEnabled(true);
        t.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
    }

    private void unsetErrorTheme(TextInputLayout t) {
        t.setErrorEnabled(false);
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(SignupActivity2.this, new ImagePickerActivity.PickerOptionListener() {
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
        Intent intent = new Intent(SignupActivity2.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(SignupActivity2.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                image = uri.getPath();
                hasCustomPicture = true;
                Bitmap image = BitmapFactory.decodeFile(uri.getPath());
                _signupProfilePictureImageView.setImageBitmap(image);
            }
        }
    }
}


