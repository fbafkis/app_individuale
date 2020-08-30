package com.francescobertamini.app_individuale.ui.account_settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountSettingsFragment extends Fragment {

    private ArrayList<Image> images = new ArrayList<>();

    private AccountSettingsViewModel accountSettingsViewModel;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountSettingsViewModel =
                ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account_settings, container, false);

        ButterKnife.bind(this, root);


        DBManagerUser dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);

        if (cursor.getInt(cursor.getColumnIndex("has_custom_picture")) == 1) {
            byte[] byteImage = cursor.getBlob(cursor.getColumnIndex("profile_picture"));
            _settingsProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));
        }

        _settingsName.setText(cursor.getString(cursor.getColumnIndex("name"))+" "+ cursor.getString(cursor.getColumnIndex("lastname")));

        _settingsMail.setText(cursor.getString(cursor.getColumnIndex("email")));

        _settingsUsername.setText(cursor.getString(cursor.getColumnIndex("username")));



        _settingsEditPictureButton.setOnClickListener(v -> {

            start();

        });




        return root;

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

            DBManagerUser dbManagerUser=  new DBManagerUser(getContext());
            dbManagerUser.open();

            image = images.get(0);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(), bmOptions);
            bitmap = Bitmap.createBitmap(bitmap);
            byte[] byteImage = getBitmapAsByteArray(bitmap);
            dbManagerUser.updatePicture(MainActivity.username,true, byteImage);
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
}