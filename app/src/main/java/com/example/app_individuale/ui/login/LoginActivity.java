package com.example.app_individuale.ui.login;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.app_individuale.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_individuale.database.dbmanager.DBManagerPreferences;
import com.example.app_individuale.database.dbmanager.DBManagerUser;
import com.example.app_individuale.ui.signup.SignupActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.time.Duration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    Boolean accountCreated= false;

    private DBManagerPreferences dbManagerPreferences = null;
    private DBManagerUser dbManagerUser = null;

    @BindView(R.id.signupUsernameEditText)
    EditText _loginUsername;
    @BindView(R.id.signupUsernameTextInputLayout)
    TextInputLayout _usernameTextInputLayout;
    @BindView(R.id.loginPassword)
    EditText _loginPassword;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout _passwordTextInputLayout;
    @BindView(R.id.rememberMeCheckBox)
    CheckBox _rememberMeCheckBox;
    @BindView(R.id.loginButton)
    Button _loginButton;
    @BindView(R.id.loginProgressBar)
    ProgressBar _loginProgressBar;
    @BindView(R.id.signupLink)
    LinearLayout _signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountCreated = getIntent().getBooleanExtra("accountCreated",false);

        dbManagerPreferences = new DBManagerPreferences(this);
        dbManagerPreferences.open();
        dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();


        Cursor statusCursor = dbManagerPreferences.fetch();
        int rememberMe=statusCursor.getInt(statusCursor.getColumnIndex("remember_me"));


        if (rememberMe==1) {
            dbManagerUser.close();
            dbManagerPreferences.close();


            //Mandare all'activity home

        } else if(rememberMe==0){


            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);

            if(accountCreated){
                Toast toast = Toast.makeText(this, "Account creato!", Toast.LENGTH_LONG);
                toast.show();
            }


            _signupLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent,1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });


            _loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

        }

    }


    private void login() {


        if (_loginUsername.getText().toString().isEmpty() || _loginPassword.getText().toString().isEmpty()) {

            if (_loginUsername.getText().toString().isEmpty()) {
                _usernameTextInputLayout.setHint("Inserisci l'username!");
                _usernameTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));

            }

            if (_loginPassword.getText().toString().isEmpty()) {
                _passwordTextInputLayout.setHint("Inserisci la password!");
                _passwordTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            }
        } else {

            String username = _loginUsername.getText().toString();
            String password = _loginPassword.getText().toString();

            _loginButton.setVisibility(View.GONE);
            _loginProgressBar.setIndeterminate(true);
            _loginUsername.setEnabled(false);
            _loginPassword.setEnabled(false);
            _rememberMeCheckBox.setEnabled(false);
            _loginProgressBar.setVisibility(View.VISIBLE);
            _signupLink.setVisibility(View.GONE);

            Boolean loginResult = checkCredentials(username, password);

            if(loginResult){

                if(_rememberMeCheckBox.isChecked()){
                    dbManagerPreferences.updateRememberMe(1);
                }



                //vai alla home activity


            }

        }
    }

    private Boolean checkCredentials(String username, String password) {

        String toastMessage;

        Toast loginToast=null;


        Cursor userCursor = dbManagerUser.fetch();
        if(userCursor.getCount()<1) {
            loginToast = Toast.makeText(getApplicationContext(), "Utente non trovato", Toast.LENGTH_LONG);
            loginToast.show();

            _loginProgressBar.setVisibility(View.GONE);
            _loginButton.setVisibility(View.VISIBLE);
            _loginUsername.setEnabled(true);
            _loginPassword.setEnabled(true);
            _rememberMeCheckBox.setEnabled(true);
            _signupLink.setVisibility(View.VISIBLE);

            ///cambiare controllo esistenza utente e disailitare e riabilitare bottoni etc.
            return false;

        } else {

            userCursor.moveToFirst();

            String db_username = userCursor.getString(userCursor.getColumnIndex("username"));
            String db_password = userCursor.getString(userCursor.getColumnIndex("password"));


            if (!username.equals(db_username) || !password.equals(db_password)) {

                _loginProgressBar.setVisibility(View.GONE);
                _loginButton.setVisibility(View.VISIBLE);
                _loginUsername.setEnabled(true);
                _loginPassword.setEnabled(true);
                _rememberMeCheckBox.setEnabled(true);

                loginToast.setText("Credenziali errate!");
                loginToast.setDuration(Toast.LENGTH_LONG);
                loginToast.show();

                return false;

            }
        }

            return true;

        }
    }

