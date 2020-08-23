package com.example.app_individuale.ui.signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import com.example.app_individuale.R;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.signupNameTextInputLayout)
    TextInputLayout _signupNameTextInputLayout;
    @BindView(R.id.signupNameEditText)
    EditText _signupNameEditText;
    @BindView(R.id.signupLastnameTextInputLayout)
    TextInputLayout _signupLastnameTextInputLayout;
    @BindView(R.id.signupLastnameEditText)
    EditText _signupLastnameEditText;
    @BindView(R.id.signupUsernameTextInputLayout)
    TextInputLayout _signupUsernameTextInputLayout;
    @BindView(R.id.signupUsernameEditText)
    EditText _signupUsernameEditText;
    @BindView(R.id.signupBirthdateInputLayout)
    TextInputLayout _signupBirthdateInputLayout;
    @BindView(R.id.signupBirthDateEditText)
    EditText _signupBirthDateEditText;
    @BindView(R.id.signupAddressTextInputLayout)
    TextInputLayout _signupAddressTextInputLayout;
    @BindView(R.id.signupAddressEditText)
    EditText _signupAddressEditText;
    @BindView(R.id.signupEmailTextInputLayout)
    TextInputLayout _signupEmailTextInputLayout;
    @BindView(R.id.signupEmailEditText)
    EditText _signupEmailEditText;
    @BindView(R.id.signupPasswordTextInputLayout)
    TextInputLayout _signupPasswordTextInputLayout;
    @BindView(R.id.signupPasswordEditText)
    EditText _signupPasswordEditText;
    @BindView(R.id.signupConfirmPasswordTextInputLayout)
    TextInputLayout _signupConfirmPasswordTextInputLayout;
    @BindView(R.id.signupConfirmPasswordEditText)
    EditText _signupConfirmPasswordEditText;
    @BindView(R.id.signupDatePickerButton)
    ImageButton _signupDatePickerButton;
    @BindView(R.id.signupNextButton)
    Button _signupNextButton;
    @BindView(R.id.signupBackButton)
    ImageButton _signupBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        _signupNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        _signupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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

        String name="";
        String lastname="";
        String address="";
        String birthdate="";
        String username="";
        String password="";
        String email="";



        if (_signupNameEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupNameTextInputLayout);
            _signupNameTextInputLayout.setHint("Inserisci il tuo nome!");
            nameOK = false;
        } else if (!validateName(_signupNameEditText.getText().toString())) {
            setErrorTheme(_signupNameTextInputLayout);
            _signupNameTextInputLayout.setHint("Inserisci un nome valido!");
            nameOK = false;
        } else {
            nameOK = true;
            name =_signupNameEditText.getText().toString();
        }


        if (_signupLastnameEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupLastnameTextInputLayout);
            _signupLastnameTextInputLayout.setHint("Inserisci il tuo cognome!");
            lastnameOK = false;
        } else if (!validateName(_signupLastnameEditText.getText().toString())) {
            setErrorTheme(_signupLastnameTextInputLayout);
            _signupLastnameTextInputLayout.setHint("Inserisci un cognome valido!");
            lastnameOK = false;
        } else {
            lastnameOK = true;
            lastname = _signupLastnameEditText.getText().toString();
        }


        if (_signupAddressEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupAddressTextInputLayout);
            _signupAddressTextInputLayout.setHint("Inserisci il tuo indirizzo!");
            addressOK = false;
        } else {

            addressOK=true;
            address= _signupAddressEditText.getText().toString();

        }


        if (_signupBirthDateEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupBirthdateInputLayout);
            _signupBirthdateInputLayout.setHint("Inserisci ls data di nascita!");
            birthdateOK = false;
        } else if (!validateBirthDate(_signupBirthDateEditText.getText().toString())) {
            setErrorTheme(_signupBirthdateInputLayout);
            _signupBirthdateInputLayout.setHint("Inserisci una data valida!");
            birthdateOK = false;
        } else {
            birthdateOK = true;
            birthdate = _signupBirthDateEditText.getText().toString();
        }


        if (_signupEmailEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupEmailTextInputLayout);
            _signupEmailTextInputLayout.setHint("Inserisci la tua mail!");
            emailOK = false;
        } else if (!validateMail(_signupEmailEditText.getText().toString())) {
            setErrorTheme(_signupEmailTextInputLayout);
            _signupEmailTextInputLayout.setHint("Inserisci una mail valida!");
            emailOK = false;
        } else {
            emailOK = true;
            email = _signupEmailEditText.getText().toString();
        }


        if (_signupUsernameEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupUsernameTextInputLayout);
            _signupUsernameTextInputLayout.setHint("Scegli l'username!");
            usernameOK = false;
        } else {
            usernameOK = true;
            username = _signupUsernameEditText.getText().toString();
        }


        if (_signupPasswordEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupPasswordTextInputLayout);
            _signupPasswordTextInputLayout.setHint("Inserisci la tua password!");
            passwordOK = false;
        } else if (_signupPasswordEditText.getText().toString().length() < 8) {
            setErrorTheme(_signupPasswordTextInputLayout);
            _signupPasswordTextInputLayout.setHint("Almeno 8 caratteri");
            passwordOK = false;
        } else {
            passwordOK = true;
        }


        if (_signupConfirmPasswordEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signupConfirmPasswordTextInputLayout);
            _signupConfirmPasswordTextInputLayout.setHint("Conferma la tua password!");
            passwordConfirmOK = false;
        } else if (!_signupPasswordEditText.getText().toString().equals(_signupConfirmPasswordEditText.getText().toString())) {
            setErrorTheme(_signupConfirmPasswordTextInputLayout);
            _signupConfirmPasswordTextInputLayout.setHint("Password non corrispondenti!");
            passwordConfirmOK = false;
        } else {
            passwordConfirmOK = true;
        }
        if(passwordOK && passwordConfirmOK){
            password= _signupPasswordEditText.getText().toString();
        }


        if(nameOK && lastnameOK && addressOK && birthdateOK && emailOK && usernameOK && passwordOK && passwordConfirmOK){

            Intent intent = new Intent(getApplicationContext(), SignupActivity2.class);
            intent.putExtra("name", name);
            intent.putExtra("lastname", lastname);
            intent.putExtra("email", email);
            intent.putExtra("address", address);
            intent.putExtra("username", username);
            intent.putExtra("birthdate", birthdate);
            intent.putExtra("password",password);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

       }


    }


    private void setErrorTheme(TextInputLayout t) {

        t.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));

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
}
