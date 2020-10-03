package com.francescobertamini.app_individuale.ui.settings;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.francescobertamini.app_individuale.MainActivity;
import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileSettingsFragment extends Fragment {


    @BindView(R.id.settingsFavouriteNumber)
    Chip _settingsFavouriteNumber;
    @BindView(R.id.settingsFavouriteCar)
    TextView _settingsFavouriteCar;
    @BindView(R.id.settingsFavouriteTrack)
    TextView _settingsFavouriteTrack;
    @BindView(R.id.settingsHatedTrack)
    TextView _settingsHatedTrack;

    @BindView(R.id.editFavNumber)
    ImageButton _editFavNumber;
    @BindView(R.id.editFavCar)
    ImageButton _editFavCar;
    @BindView(R.id.editFavTrack)
    ImageButton _editFavTrack;
    @BindView(R.id.editHatedTrack)
    ImageButton _editHatedTrack;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        ButterKnife.bind(this, root);

        DBManagerUser dbManagerUser = new DBManagerUser(getContext());
        dbManagerUser.open();

        Cursor cursor = dbManagerUser.fetchByUsername(MainActivity.username);

        _settingsFavouriteNumber.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex("favorite_number"))));

        _settingsFavouriteCar.setText(cursor.getString(cursor.getColumnIndex("favorite_car")));

        _settingsFavouriteTrack.setText(cursor.getString(cursor.getColumnIndex("favorite_track")));

        _settingsHatedTrack.setText(cursor.getString(cursor.getColumnIndex("hated_track")));

        _editFavNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFavNumber();
            }
        });

        _editFavCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFavCar();
            }
        });

        _editFavTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFavTrack();
            }
        });

        _editHatedTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editHatedTrack();
            }
        });

        return root;
    }

    private void editFavNumber() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modifica numero di gara preferito");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_fav_number, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editFavNumEditText);
        TextInputLayout textInputLayout = customLayout.findViewById(R.id.editFavNumTextInputLayout);


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

                String newNumber = editText.getText().toString();

                if (newNumber.isEmpty()) {
                    setErrorTheme(textInputLayout);
                    textInputLayout.setError("Inserisci il tuo numero di gara!");
                } else if (editText.getText().length() > 3) {
                    setErrorTheme(textInputLayout);
                    textInputLayout.setError("Inserisci un numero valido!");
                } else {

                    unsetErrorTheme(textInputLayout);
                    DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                    dbManagerUser.open();
                    int result = dbManagerUser.updateFavoriteNumber(Integer.parseInt(newNumber), MainActivity.username);

                    if (result > 0) {
                        _settingsFavouriteNumber.setText(newNumber.toString());
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Numero di gara aggiornato correttamente", Toast.LENGTH_LONG);
                        toast.show();
                    } else {

                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare il numero di gara.", Toast.LENGTH_LONG);
                        toast.show();

                    }

                }
            }
        });


    }

    private void editFavCar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modifica la tua auto preferita");

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

    private void editFavTrack() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modificala il tuo circuito preferito");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_fav_track, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editFavTrackEditText);
        TextInputLayout textInputLayout = customLayout.findViewById(R.id.editFavTrackTextInputLayout);


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

                String newTrack = editText.getText().toString();

                if (newTrack.isEmpty()) {
                    setErrorTheme(textInputLayout);
                    textInputLayout.setError("Inserisci il tuo circuito preferito!");
                } else {

                    unsetErrorTheme(textInputLayout);
                    DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                    dbManagerUser.open();
                    int result = dbManagerUser.updateFavoriteTrack(newTrack, MainActivity.username);

                    if (result > 0) {
                        _settingsFavouriteTrack.setText(newTrack.toString());
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Circuito preferito aggiornato correttamente", Toast.LENGTH_LONG);
                        toast.show();
                    } else {

                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare il circuito preferito.", Toast.LENGTH_LONG);
                        toast.show();

                    }

                }
            }
        });


    }

    private void editHatedTrack() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modificala il tuo circuito più odiato");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_hated_track, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editHatedTrackEditText);
        TextInputLayout textInputLayout = customLayout.findViewById(R.id.editHatedTrackTextInputLayout);


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

                String newTrack = editText.getText().toString();

                if (newTrack.isEmpty()) {
                    setErrorTheme(textInputLayout);
                    textInputLayout.setError("Inserisci il tuo circuito più odiato!");
                } else {

                    unsetErrorTheme(textInputLayout);
                    DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                    dbManagerUser.open();
                    int result = dbManagerUser.updateHatedTrack(newTrack, MainActivity.username);

                    if (result > 0) {
                        _settingsHatedTrack.setText(newTrack.toString());
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Circuito odiato aggiornato correttamente", Toast.LENGTH_LONG);
                        toast.show();
                    } else {

                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare il circuito odiato.", Toast.LENGTH_LONG);
                        toast.show();

                    }

                }
            }
        });


    }

    private void setErrorTheme(TextInputLayout t) {

        t.setErrorEnabled(true);
        t.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
    }

    private void unsetErrorTheme(TextInputLayout t) {
        t.setErrorEnabled(false);
    }


}
