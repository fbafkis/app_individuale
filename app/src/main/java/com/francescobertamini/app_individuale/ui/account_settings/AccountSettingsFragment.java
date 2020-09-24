package com.francescobertamini.app_individuale.ui.account_settings;

import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.francescobertamini.app_individuale.MainActivity;
import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountSettingsFragment extends Fragment {

    @BindView(R.id.settingsBirthdate)
    TextView _settingsBirthdate;
    @BindView(R.id.settingsAddress)
    TextView _settingsAddress;
    @BindView(R.id.settingsPasswordHidden)
    TextView _settingsPasswordHidden;
    @BindView(R.id.settingsPasswordShown)
    TextView _settingsPasswordShown;
    @BindView(R.id.settingsRememberMe)
    CheckBox _settingsRememberMe;
    @BindView(R.id.showPassword)
    ImageButton _showPassword;

    @BindView(R.id.editAddress)
    ImageButton _editAddress;
    @BindView(R.id.editPassword)
    ImageButton _editPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View root = inflater.inflate(R.layout.fragment_account_settings, null);

        ButterKnife.bind(this, root);

        DBManagerUser dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);

        _settingsBirthdate.setText(cursor.getString(cursor.getColumnIndex("birthdate")));

        _settingsAddress.setText(cursor.getString(cursor.getColumnIndex("address")));

        _settingsPasswordHidden.setText(cursor.getString(cursor.getColumnIndex("password")));
        _settingsPasswordShown.setText(cursor.getString(cursor.getColumnIndex("password")));


        _showPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (_settingsPasswordHidden.getVisibility() == View.VISIBLE) {
                    _settingsPasswordHidden.setVisibility(View.GONE);
                    _settingsPasswordShown.setVisibility(View.VISIBLE);
                } else if (_settingsPasswordHidden.getVisibility() == View.GONE) {
                    _settingsPasswordShown.setVisibility(View.GONE);
                    _settingsPasswordHidden.setVisibility(View.VISIBLE);
                }

            }


        });

        int rememberMe = cursor.getInt(cursor.getColumnIndex("remember_me"));

        if (rememberMe == 1)
            _settingsRememberMe.setChecked(true);
        else _settingsRememberMe.setChecked(false);


        _settingsRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                


            }
        });


        _editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // editAddress();

            }
        });


        return root;

    }

/*private void editAddress() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modificala tua auto preferita");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_fav_car, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editFavCarEditText);
        TextInputLayout textInputLayout = customLayout.findViewById(R.id.editFavCarTextInputLayout);


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

                String newCar = editText.getText().toString();

                if (newCar.isEmpty()) {
                    setErrorTheme(textInputLayout);
                    textInputLayout.setError("Inserisci la tua auto preferita!");
                } else {

                    unsetErrorTheme(textInputLayout);
                    DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                    dbManagerUser.open();
                    int result = dbManagerUser.updateFavoriteCar(newCar, MainActivity.username);

                    if (result > 0) {
                        _settingsFavouriteCar.setText(newCar.toString());
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Auto preferita aggiornata correttamente", Toast.LENGTH_LONG);
                        toast.show();
                    } else {

                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare la tua auto preferita.", Toast.LENGTH_LONG);
                        toast.show();

                    }

                }
            }
        });


    }
*/

    private void setErrorTheme(TextInputLayout t) {

        t.setErrorEnabled(true);
        t.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
    }

    private void unsetErrorTheme(TextInputLayout t) {
        t.setErrorEnabled(false);
    }


}
