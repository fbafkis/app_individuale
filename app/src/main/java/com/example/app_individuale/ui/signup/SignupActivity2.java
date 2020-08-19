package com.example.app_individuale.ui.signup;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.app_individuale.R;


public class SignupActivity2 extends AppCompatActivity {

    String name;
    String lastname;
    String address;
    String birthdate;
    String email;
    String username;
    String password;

    JsonObject campionati;

    int favNumber;
    String favCar;
    String favCirucit;
    String hatedCircuit;

    @BindView(R.id.signup2ProfilePictureImageView)
    ImageView _signup2ProfilePictureImageView;
    @BindView(R.id.signup2FavouriteCarEditText)
    EditText _signup2FavouriteCarEditText;
    @BindView(R.id.signup2FavouriteCarTextInputLayout)
    TextInputLayout _signup2FavouriteCarTextInputLayout;
    @BindView(R.id.signup2FavouriteNumberEditText)
    EditText _signup2FavouriteNumberEditText;
    @BindView(R.id.signup2FavouriteNumberTextInputLayout)
    TextInputLayout _signup2FavouriteNumberTextInputLayout;
    @BindView(R.id.signup2FavouriteCircuitEditText)
    EditText _signup2FavouriteCircuitEditText;
    @BindView(R.id.signup2FavouriteCircuitTextInputLayout)
    TextInputLayout _signup2FavouriteCircuitTextInputLayout;
    @BindView(R.id.signup2HatedCircuitNameEditText)
    EditText _signup2HatedCircuitNameEditText;
    @BindView(R.id.signup2HatedCircuitTextInputLayout)
    TextInputLayout _signup2HatedCircuitTextInputLayout;
    @BindView(R.id.signup2SignupButton)
    Button _signup2SignupButton;



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


        _signup2SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });


    }

    public void checkFields (){

        Boolean carOK = false;
        Boolean numberOK = false;
        Boolean favCicuitOK = false;
        Boolean hatedCirucitOK =  false;

        if (_signup2FavouriteNumberEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signup2FavouriteNumberTextInputLayout);
            _signup2FavouriteNumberTextInputLayout.setHint("Inserisci il tuo numero di gara!");
            numberOK = false;

        } else if (_signup2FavouriteNumberEditText.getText().length()>3) {
            setErrorTheme(_signup2FavouriteNumberTextInputLayout);
            _signup2FavouriteNumberTextInputLayout.setHint("Inserisci un numero valido!");
            numberOK = false;
        } else {

            numberOK = true;
            favNumber = Integer.parseInt(_signup2FavouriteNumberEditText.getText().toString());
        }



        if (_signup2FavouriteCarEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signup2FavouriteCarTextInputLayout);
            _signup2FavouriteCarTextInputLayout.setHint("Inserisci la tua auto preferita!");
            carOK = false;

        } else {

            carOK = true;
            favCar =_signup2FavouriteCarEditText.getText().toString();
        }



        if (_signup2FavouriteCircuitEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signup2FavouriteCircuitTextInputLayout);
            _signup2FavouriteCircuitTextInputLayout.setHint("Inserisci il tuo circuito preferito!");
            favCicuitOK = false;

        } else {

            favCicuitOK = true;
            favCirucit =_signup2FavouriteCircuitEditText.getText().toString();
        }


        if (_signup2FavouriteCircuitEditText.getText().toString().isEmpty()) {
            setErrorTheme(_signup2FavouriteCircuitTextInputLayout);
            _signup2FavouriteCircuitTextInputLayout.setHint("Inserisci il tuo circuito preferito!");
            favCicuitOK = false;

        } else {

            favCicuitOK = true;
            favCirucit =_signup2FavouriteCircuitEditText.getText().toString();
        }


    }

    private void setErrorTheme(TextInputLayout t) {

        t.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));

    }







}
