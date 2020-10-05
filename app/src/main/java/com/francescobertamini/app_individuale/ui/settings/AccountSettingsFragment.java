package com.francescobertamini.app_individuale.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.francescobertamini.app_individuale.ui.main.MainActivity;
import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerStatus;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.ui.login.LoginActivity;
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
    @BindView(R.id.logoutButton)
    Button _logoutButton;
    @BindView(R.id.deleteAccountButton)
    Button _deleteAccountButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                    _showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_24px, null));
                } else if (_settingsPasswordHidden.getVisibility() == View.GONE) {
                    _settingsPasswordShown.setVisibility(View.GONE);
                    _settingsPasswordHidden.setVisibility(View.VISIBLE);
                    _showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_outline_remove_red_eye_24, null));
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
                if (isChecked) {
                    dbManagerUser.open();
                    dbManagerUser.updateRemeberMe(true, MainActivity.username);
                    Toast toast = Toast.makeText(getContext(), "\"Ricordami\" attivo, non dovrai efettuare il login.", Toast.LENGTH_LONG);
                    toast.show();
                    Log.e("RememberMe", "On");
                    dbManagerUser.close();
                } else if (!isChecked) {
                    dbManagerUser.open();
                    dbManagerUser.updateRemeberMe(false, MainActivity.username);
                    Toast toast = Toast.makeText(getContext(), "\"Ricordami\" non attivo, dovrai efettuare il login.", Toast.LENGTH_LONG);
                    toast.show();
                    Log.e("RememberMe", "Off");
                    dbManagerUser.close();
                }
            }
        });
        _editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddress();
            }
        });
        _editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassword();
            }
        });
        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Effettuare il logout? ");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
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
                        DBManagerStatus dbManagerStatus = new DBManagerStatus(getContext());
                        dbManagerStatus.open();
                        int result = dbManagerStatus.update(false, null);
                        if (result == 1) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            Toast toast = Toast.makeText(getContext(), "Logout effettuato correttamente", Toast.LENGTH_LONG);
                            toast.show();
                            startActivity(intent);
                            getActivity().finish();
                        } else if (result == 0) {
                            Toast toast = Toast.makeText(getContext(), "Errore: impossibile effettuare il logout", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        dbManagerStatus.close();
                    }
                });
            }
        });

        _deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Eliminare l'account definitivamente? Tutti i dati andranno distrutti.");
                builder.setTitle("Attenzione!");
                builder.setPositiveButton("Elimina account", new DialogInterface.OnClickListener() {
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
                        DBManagerStatus dbManagerStatus = new DBManagerStatus(getContext());
                        dbManagerStatus.open();
                        int result = dbManagerStatus.update(false, null);
                        if (result == 1) {
                            dbManagerUser.open();
                            int result2 = dbManagerUser.delete(MainActivity.username);
                            if (result2 == 1) {
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Toast toast = Toast.makeText(getContext(), "Account eliminato correttamente", Toast.LENGTH_LONG);
                                toast.show();
                                startActivity(intent);
                                getActivity().finish();
                            } else if (result2 == 0) {
                                Toast toast = Toast.makeText(getContext(), "Errore: impossibile eliminare l'account", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else if (result == 0) {
                            Toast toast = Toast.makeText(getContext(), "Errore: impossibile aggiornare lo stato dell'applicazione", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        dbManagerUser.close();
                        dbManagerStatus.close();
                    }
                });
            }
        });
        return root;
    }

    private void editAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modifica il tuo indirizzo");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_address, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.editAddressEditText);
        TextInputLayout textInputLayout = customLayout.findViewById(R.id.editAddressTextInputLayout);
        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
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
                String newAddress = editText.getText().toString();
                if (newAddress.isEmpty()) {
                    setErrorTheme(textInputLayout);
                    textInputLayout.setError("Inserisci il tuo indirizzo!");
                } else {
                    unsetErrorTheme(textInputLayout);
                    DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                    dbManagerUser.open();
                    int result = dbManagerUser.updateAddress(newAddress, MainActivity.username);
                    if (result > 0) {
                        _settingsAddress.setText(newAddress.toString());
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Indirizzo aggiornato correttamente", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare l'indirizzo.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    dbManagerUser.close();
                }
            }
        });
    }

    private void editPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modifica la tua password");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_password, null);
        builder.setView(customLayout);
        EditText passwordEditText = customLayout.findViewById(R.id.editPasswordEditText);
        EditText passwordConfirmEditText = customLayout.findViewById(R.id.editConfirmPasswordEditText);
        TextInputLayout passwordInputLayout = customLayout.findViewById(R.id.editPasswordTextInputLayout);
        TextInputLayout passwordConfirmTextInputLayout = customLayout.findViewById(R.id.editConfirmPasswordTextInputLayout);
        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
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
                String password;
                Boolean passwordOK;
                Boolean passwordConfirmOK;
                if (passwordEditText.getText().toString().trim().isEmpty()) {
                    setErrorTheme(passwordInputLayout);
                    passwordInputLayout.setError("Inserisci la tua password!");
                    passwordOK = false;
                } else if (passwordEditText.getText().toString().trim().length() < 8) {
                    setErrorTheme(passwordInputLayout);
                    passwordInputLayout.setError("Almeno 8 caratteri");
                    passwordOK = false;
                } else {
                    passwordOK = true;
                    unsetErrorTheme(passwordInputLayout);
                }

                if (passwordConfirmEditText.getText().toString().trim().isEmpty()) {
                    setErrorTheme(passwordConfirmTextInputLayout);
                    passwordConfirmTextInputLayout.setError("Conferma la tua password!");
                    passwordConfirmOK = false;
                } else if (!passwordEditText.getText().toString().trim().equals(passwordConfirmEditText.getText().toString().trim())) {
                    setErrorTheme(passwordConfirmTextInputLayout);
                    passwordConfirmTextInputLayout.setError("Password non corrispondenti!");
                    passwordConfirmOK = false;
                } else {
                    passwordConfirmOK = true;
                    passwordConfirmTextInputLayout.setHint("Conferma password");
                }

                if (passwordOK && passwordConfirmOK) {
                    password = passwordConfirmEditText.getText().toString().trim();
                    DBManagerUser dbManagerUser = new DBManagerUser(getContext());
                    dbManagerUser.open();
                    int result = dbManagerUser.updatePassword(password, MainActivity.username);
                    if (result > 0) {
                        _settingsPasswordHidden.setText(password);
                        _settingsPasswordShown.setText(password);
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getContext(), "Password aggiornata correttamente", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getContext(), "Errore nell'aggiornare la password", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    dbManagerUser.close();
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
