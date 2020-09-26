package com.francescobertamini.app_individuale.ui.signup;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.francescobertamini.app_individuale.BasicActivity;
import com.francescobertamini.app_individuale.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.francescobertamini.app_individuale.database.DBHelper;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.francescobertamini.app_individuale.R.*;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(id.signupNameTextInputLayout)
    TextInputLayout _signupNameTextInputLayout;
    @BindView(id.signupNameEditText)
    EditText _signupNameEditText;
    @BindView(id.signupLastnameTextInputLayout)
    TextInputLayout _signupLastnameTextInputLayout;
    @BindView(id.signupLastnameEditText)
    EditText _signupLastnameEditText;
    @BindView(id.signupUsernameTextInputLayout)
    TextInputLayout _signupUsernameTextInputLayout;
    @BindView(id.signupUsernameEditText)
    EditText _signupUsernameEditText;
    @BindView(id.signupBirthdateInputLayout)
    TextInputLayout _signupBirthdateInputLayout;
    @BindView(id.signupBirthDateEditText)
    EditText _signupBirthDateEditText;
    @BindView(id.signupAddressTextInputLayout)
    TextInputLayout _signupAddressTextInputLayout;
    @BindView(id.signupAddressEditText)
    EditText _signupAddressEditText;
    @BindView(id.signupEmailTextInputLayout)
    TextInputLayout _signupEmailTextInputLayout;
    @BindView(id.signupEmailEditText)
    EditText _signupEmailEditText;
    @BindView(id.signupPasswordTextInputLayout)
    TextInputLayout _signupPasswordTextInputLayout;
    @BindView(id.signupPasswordEditText)
    EditText _signupPasswordEditText;
    @BindView(id.signupConfirmPasswordTextInputLayout)
    TextInputLayout _signupConfirmPasswordTextInputLayout;
    @BindView(id.signupConfirmPasswordEditText)
    EditText _signupConfirmPasswordEditText;
    @BindView(id.signupDatePickerButton)
    ImageButton _signupDatePickerButton;
    @BindView(id.signupNextButton)
    Button _signupNextButton;
    @BindView(id.signupBackButton)
    ImageButton _signupBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_signup);
        ButterKnife.bind(this);

        _signupDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        _signupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(anim.slide_in_left, anim.slide_out_right);

            }
        });

        _signupNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String Year = Integer.toString(year);
        String Month = Integer.toString(month + 1);
        String DayOfMonth = Integer.toString(dayOfMonth);

        if (month < 10) {
            Month = "0" + Month;
        }
        if (dayOfMonth < 10) {
            DayOfMonth = "0" + DayOfMonth;
        }

        String currentDateString = DayOfMonth + "/" + Month + "/" + Year;
        _signupBirthDateEditText.setText(currentDateString);
    }

    public void checkFields() {

        Boolean birthdateOK = false;
        Boolean emailOK = false;
        Boolean passwordOK = false;
        Boolean passwordConfirmOK = false;
        Boolean nameOK = false;
        Boolean lastnameOK = false;
        Boolean usernameOK = false;
        Boolean addressOK = false;

        String name = "";
        String lastname = "";
        String address = "";
        String birthdate = "";
        String username = "";
        String password = "";
        String email = "";

        if (_signupNameEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupNameTextInputLayout);
            _signupNameTextInputLayout.setError("Inserisci il tuo nome!");
            nameOK = false;
        } else if (!validateName(_signupNameEditText.getText().toString().trim())) {
            setErrorTheme(_signupNameTextInputLayout);
            _signupNameTextInputLayout.setError("Inserisci un nome valido!");
            nameOK = false;
        } else {
            nameOK = true;
            unsetErrorTheme(_signupNameTextInputLayout);
            name = _signupNameEditText.getText().toString().trim();
        }

        if (_signupLastnameEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupLastnameTextInputLayout);
            _signupLastnameTextInputLayout.setError("Inserisci il tuo cognome!");
            lastnameOK = false;
        } else if (!validateName(_signupLastnameEditText.getText().toString().trim())) {
            setErrorTheme(_signupLastnameTextInputLayout);
            _signupLastnameTextInputLayout.setError("Inserisci un cognome valido!");
            lastnameOK = false;
        } else {
            lastnameOK = true;
            unsetErrorTheme(_signupLastnameTextInputLayout);
            lastname = _signupLastnameEditText.getText().toString().trim();
        }

        if (_signupAddressEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupAddressTextInputLayout);
            _signupAddressTextInputLayout.setError("Inserisci il tuo indirizzo!");
            addressOK = false;
        } else {
            addressOK = true;
            unsetErrorTheme(_signupAddressTextInputLayout);
            address = _signupAddressEditText.getText().toString().trim();
        }

        if (_signupBirthDateEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupBirthdateInputLayout);
            _signupBirthdateInputLayout.setError("Inserisci ls data di nascita!");
            birthdateOK = false;
        } else if (!validateBirthDate(_signupBirthDateEditText.getText().toString().trim())) {
            setErrorTheme(_signupBirthdateInputLayout);
            _signupBirthdateInputLayout.setError("Inserisci una data in formato gg/mm/aaaa!");
            birthdateOK = false;
        } else {
            birthdateOK = true;
            unsetErrorTheme(_signupBirthdateInputLayout);
            birthdate = _signupBirthDateEditText.getText().toString().trim();
        }

        if (_signupEmailEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupEmailTextInputLayout);
            _signupEmailTextInputLayout.setError("Inserisci la tua mail!");
            emailOK = false;
        } else if (!validateMail(_signupEmailEditText.getText().toString().trim())) {
            setErrorTheme(_signupEmailTextInputLayout);
            _signupEmailTextInputLayout.setError("Inserisci una mail valida!");
            emailOK = false;
        } else {
            emailOK = true;
            unsetErrorTheme(_signupEmailTextInputLayout);
            email = _signupEmailEditText.getText().toString().trim();
        }

        if (_signupUsernameEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupUsernameTextInputLayout);
            _signupUsernameTextInputLayout.setError("Scegli l'username!");
            usernameOK = false;
        } else {
            usernameOK = true;
            unsetErrorTheme(_signupUsernameTextInputLayout);
            username = _signupUsernameEditText.getText().toString().trim();
        }

        if (_signupPasswordEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupPasswordTextInputLayout);
            _signupPasswordTextInputLayout.setError("Inserisci la tua password!");
            passwordOK = false;
        } else if (_signupPasswordEditText.getText().toString().trim().length() < 8) {
            setErrorTheme(_signupPasswordTextInputLayout);
            _signupPasswordTextInputLayout.setError("Almeno 8 caratteri");
            passwordOK = false;
        } else {
            passwordOK = true;
            unsetErrorTheme(_signupPasswordTextInputLayout);
        }

        if (_signupConfirmPasswordEditText.getText().toString().trim().isEmpty()) {
            setErrorTheme(_signupConfirmPasswordTextInputLayout);
            _signupConfirmPasswordTextInputLayout.setError("Conferma la tua password!");
            passwordConfirmOK = false;
        } else if (!_signupPasswordEditText.getText().toString().trim().equals(_signupConfirmPasswordEditText.getText().toString().trim())) {
            setErrorTheme(_signupConfirmPasswordTextInputLayout);
            _signupConfirmPasswordTextInputLayout.setError("Password non corrispondenti!");
            passwordConfirmOK = false;
        } else {
            passwordConfirmOK = true;
            _signupConfirmPasswordTextInputLayout.setHint("Conferma password");
        }
        if (passwordOK && passwordConfirmOK) {
            password = _signupPasswordEditText.getText().toString().trim();
        }

        if (nameOK && lastnameOK && addressOK && birthdateOK && emailOK && usernameOK && passwordOK && passwordConfirmOK) {

            if (checkDuplicateUsers(username, email) == 1) {

                Toast duplicateUsernameWarningToast = Toast.makeText(this, "Esiste già un utente con username uguale!", Toast.LENGTH_LONG);
                duplicateUsernameWarningToast.show();

            } else if (checkDuplicateUsers(username, email) == 2) {

                Toast duplicateEmailWarningToast = Toast.makeText(this, "Esiste già un utente con email uguale!", Toast.LENGTH_LONG);
                duplicateEmailWarningToast.show();

            } else if (checkDuplicateUsers(username, email) == 3) {

                Toast duplicateUserWarningToast = Toast.makeText(this, "Esiste già un utente con username e email uguali!", Toast.LENGTH_LONG);
                duplicateUserWarningToast.show();

            } else {

                Intent intent = new Intent(getApplicationContext(), SignupActivity2.class);
                intent.putExtra("name", name);
                intent.putExtra("lastname", lastname);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                intent.putExtra("username", username);
                intent.putExtra("birthdate", birthdate);
                intent.putExtra("password", password);

                overridePendingTransition(anim.slide_in_right, anim.slide_out_left);
                startActivity(intent);


            }

        }

    }

    private void setErrorTheme(TextInputLayout t) {

        t.setErrorEnabled(true);
        t.setErrorIconDrawable(drawable.ic_baseline_error_outline_24);
    }

    private void unsetErrorTheme(TextInputLayout t) {
        t.setErrorEnabled(false);
    }

    private boolean validateBirthDate(String birthdate) {
        boolean checkFormat = false;
        boolean checkNumber = false;

        if (birthdate.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
            checkFormat = true;
        else
            checkFormat = false;

        if (checkFormat) {
            String[] t = birthdate.split("/");
            checkNumber = (Integer.valueOf(t[0]) <= 31 && Integer.valueOf(t[1]) <= 12 && Integer.valueOf(t[2]) >= 1900 && Integer.valueOf(t[2]) <= 2100);
        }
        return (checkNumber && checkFormat);
    }

    private boolean validateName(String name) {

        String regex = "^[A-Za-z]+$";
        return name.matches(regex);

    }

    private boolean validateMail(String email) {

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }

    private int checkDuplicateUsers(String username, String email) {

        DBHelper dbHelper = new DBHelper(this);
        DBManagerUser dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();

        Cursor userCursor = dbManagerUser.fetchByUsername(username);
        Cursor emailCursor = dbManagerUser.fetchByEmail(email);

        if (userCursor.getCount() > 0)
            return 1;
        if (emailCursor.getCount() > 0)
            return 2;
        if (userCursor.getCount() > 0 && emailCursor.getCount() > 0)
            return 3;

        return 0;


    }
}
