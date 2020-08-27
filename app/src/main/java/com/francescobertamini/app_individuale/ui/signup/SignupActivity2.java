package com.francescobertamini.app_individuale.ui.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;

import androidx.appcompat.app.AppCompatActivity;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ImagePickerComponentHolder;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.features.imageloader.DefaultImageLoader;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.francescobertamini.app_individuale.ui.login.LoginActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.francescobertamini.app_individuale.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SignupActivity2 extends AppCompatActivity {

    String name;
    String lastname;
    String address;
    String birthdate;
    String email;
    String username;
    String password;
    Image image;

    private ArrayList<Image> images = new ArrayList<>();

    JsonObject campionati;

    int favNumber;
    String favCar;
    String favCirucit;
    String hatedCircuit;

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
    @BindView(R.id.signupFavouriteCircuitEditText)
    EditText _signupFavouriteCircuitEditText;
    @BindView(R.id.signupFavouriteCircuitTextInputLayout)
    TextInputLayout _signupFavouriteCircuitTextInputLayout;
    @BindView(R.id.signupHatedCircuitNameEditText)
    EditText _signupHatedCircuitNameEditText;
    @BindView(R.id.signupHatedCircuitTextInputLayout)
    TextInputLayout _signupHatedCircuitTextInputLayout;
    @BindView(R.id.signupButton)
    Button _signupButton;
    @BindView(R.id.signupEditPictureButton)
    ImageView _editPictureButton;
    @BindView(R.id.signupBackButton2)
    ImageView _signupBackButton2;

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

        _editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        _signupBackButton2.setOnClickListener(new View.OnClickListener() {
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
        Boolean hatedCircuitOK = false;
        Boolean hasCustomPicture = false;

        if (_signupFavouriteNumberEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupFavouriteNumberTextInputLayout);
            _signupFavouriteNumberTextInputLayout.setHint("Inserisci il tuo numero di gara!");
            numberOK = false;

        } else if (_signupFavouriteNumberEditText.getText().length() > 3) {
            setErrorTheme(_signupFavouriteNumberTextInputLayout);
            _signupFavouriteNumberTextInputLayout.setHint("Inserisci un numero valido!");
            numberOK = false;
        } else {

            numberOK = true;
            favNumber = Integer.parseInt(_signupFavouriteNumberEditText.getText().toString().trim());
        }

        if (_signupFavouriteCarEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupFavouriteCarTextInputLayout);
            _signupFavouriteCarTextInputLayout.setHint("Inserisci la tua auto preferita!");
            carOK = false;

        } else {

            carOK = true;
            favCar = _signupFavouriteCarEditText.getText().toString().trim();
        }

        if (_signupFavouriteCircuitEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupFavouriteCircuitTextInputLayout);
            _signupFavouriteCircuitTextInputLayout.setHint("Inserisci il tuo circuito preferito!");
            favCicuitOK = false;

        } else {

            favCicuitOK = true;
            favCirucit = _signupFavouriteCircuitEditText.getText().toString().trim();
        }

        if (_signupHatedCircuitNameEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupHatedCircuitTextInputLayout);
            _signupHatedCircuitTextInputLayout.setHint("Inserisci il tuo circuito più odiato!");
            hatedCircuitOK = false;

        } else {

            hatedCircuitOK = true;
            hatedCircuit = _signupHatedCircuitNameEditText.getText().toString().trim();
        }

        if (images.size() == 0)
            hasCustomPicture = false;
        else
            hasCustomPicture = true;

        if (numberOK && carOK && favCicuitOK && hatedCircuitOK) {

            createUser(hasCustomPicture);

        }


    }

    private void createUser(Boolean hasCustomPicture) {



            DBManagerUser dbManagerUser = new DBManagerUser(this);
            dbManagerUser.open();

            if (hasCustomPicture) {

                image = images.get(0);

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(), bmOptions);
                bitmap = Bitmap.createBitmap(bitmap);
                byte[] byteImage = getBitmapAsByteArray(bitmap);

                dbManagerUser.insert(name, lastname, birthdate, email, address, username, password, favNumber, favCar, favCirucit, hatedCircuit, true, byteImage, false);
            } else {

                dbManagerUser.insert(name, lastname, birthdate, email, address, username, password, favNumber, favCar, favCirucit, hatedCircuit, false, null, false);

            }

            dbManagerUser.close();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("accountCreated", true);
            intent.putExtra("new_user_username", username);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }


    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private void setErrorTheme(TextInputLayout t) {

        t.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));

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
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            printImages(images);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void printImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {

            stringBuffer.append(images.get(i).getPath()).append("\n");
        }
        Glide.with(_signupProfilePictureImageView)
                .load(images.get(0).getPath())
                .into(_signupProfilePictureImageView);
    }


}


