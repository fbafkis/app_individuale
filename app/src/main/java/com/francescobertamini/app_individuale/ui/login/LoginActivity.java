package com.francescobertamini.app_individuale.ui.login;

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

import com.francescobertamini.app_individuale.MainActivity;
import com.francescobertamini.app_individuale.R;

import androidx.appcompat.app.AppCompatActivity;

import com.francescobertamini.app_individuale.database.dbmanager.DBManagerStatus;
import com.francescobertamini.app_individuale.database.dbmanager.DBManagerUser;
import com.francescobertamini.app_individuale.ui.home.HomeFragment;
import com.francescobertamini.app_individuale.ui.signup.SignupActivity;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    Boolean accountCreated = false;

    private DBManagerStatus dbManagerStatus = null;
    private DBManagerUser dbManagerUser = null;

    @BindView(R.id.loginUsernameEditText)
    EditText _loginUsernameEditText;
    @BindView(R.id.loginUsernameTextInputLayout)
    TextInputLayout _loginUsernameTextInputLayout;
    @BindView(R.id.loginPasswordEditText)
    EditText _loginPasswordEditText;
    @BindView(R.id.loginPasswordTextInputLayout)
    TextInputLayout _loginPasswordTextInputLayout;
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

        accountCreated = getIntent().getBooleanExtra("accountCreated", false);

        dbManagerStatus = new DBManagerStatus(this);
        dbManagerStatus.open();
        dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();

        Cursor statusCursor = dbManagerStatus.fetch();
        Boolean isUserLogged = ((statusCursor.getInt(statusCursor.getInt(statusCursor.getColumnIndex("is_user_logged")))) == 1);

        if (isUserLogged) {

            dbManagerUser.close();
            dbManagerStatus.close();

            //Mandare all'activity home

        } else if (!isUserLogged) {

            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);

            if (accountCreated) {
                Toast toast = Toast.makeText(this, "Account creato!", Toast.LENGTH_LONG);
                toast.show();

                _loginUsernameEditText.setText(getIntent().getStringExtra("new_user_username"));
            }

            _loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

        }

    }

    private void login() {

        if (_loginUsernameEditText.getText().toString().trim().isEmpty() || _loginPasswordEditText.getText().toString().trim().isEmpty()) {

            if (_loginUsernameEditText.getText().toString().trim().isEmpty()) {
                _loginUsernameTextInputLayout.setHint("Inserisci l'username!");
                setErrorTheme(_loginUsernameTextInputLayout);

            }

            if (_loginPasswordEditText.getText().toString().trim().isEmpty()) {
                _loginPasswordTextInputLayout.setHint("Inserisci la password!");
                setErrorTheme(_loginPasswordTextInputLayout);
            }
        } else {

            String username = _loginUsernameEditText.getText().toString().trim();
            String password = _loginPasswordEditText.getText().toString().trim();

            loginProgressTheme();

            Boolean loginResult = checkCredentials(username, password);

            if (loginResult) {

                if (_rememberMeCheckBox.isChecked()) {
                    int columnAffected = dbManagerUser.updateRememberMeByUsername(username, true);
                    if (columnAffected == 0)
                        dbManagerUser.updateRememberMeByEmail(username, true);
                }

                String actualUsername;
                Cursor cursor = dbManagerUser.fetchByUsername(username);

                if (cursor.getCount() > 0)
                    actualUsername = username;
                else {
                    cursor = dbManagerUser.fetchByEmail(username);
                    actualUsername = cursor.getString(cursor.getColumnIndex("username"));
                }

                dbManagerStatus.close();
                dbManagerUser.close();



                System.out.println("Login effettuato");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                intent.putExtra("username", actualUsername);
                startActivity(intent);
                loginProgressThemeEnd();
                finish();
            }


        }

    }

    private Boolean checkCredentials(String username, String password) {

        Cursor userCursor = dbManagerUser.fetchByUsername(username);
        if (userCursor.getCount() < 1) {
            userCursor = dbManagerUser.fetchByEmail(username);
            if (userCursor.getCount() < 1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Utente non trovato", Toast.LENGTH_LONG);
                toast.show();
                loginProgressThemeEnd();
                return false;
            } else {

                userCursor.moveToFirst();
                String db_email = userCursor.getString(userCursor.getColumnIndex("email"));
                String db_password = userCursor.getString(userCursor.getColumnIndex("password"));

                if (!username.equals(db_email) || !password.equals(db_password)) {
                    loginProgressThemeEnd();
                    Toast toast = Toast.makeText(getApplicationContext(), "Password errata!", Toast.LENGTH_LONG);
                    toast.show();
                    return false;
                }
                return true;
            }

        } else {

            userCursor.moveToFirst();
            String db_username = userCursor.getString(userCursor.getColumnIndex("username"));
            String db_password = userCursor.getString(userCursor.getColumnIndex("password"));
            if (!username.equals(db_username) || !password.equals(db_password)) {
                loginProgressThemeEnd();
                Toast toast = Toast.makeText(getApplicationContext(), "Password errata!", Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
            return true;

        }
    }

    private void setErrorTheme(TextInputLayout t) {

        t.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));

    }

    private void loginProgressTheme() {

        _loginButton.setVisibility(View.GONE);
        _loginProgressBar.setIndeterminate(true);
        _loginUsernameEditText.setEnabled(false);
        _loginPasswordEditText.setEnabled(false);
        _rememberMeCheckBox.setEnabled(false);
        _loginProgressBar.setVisibility(View.VISIBLE);
        _signupLink.setVisibility(View.GONE);
    }

    private void loginProgressThemeEnd() {

        _loginProgressBar.setVisibility(View.GONE);
        _loginButton.setVisibility(View.VISIBLE);
        _loginUsernameEditText.setEnabled(true);
        _loginPasswordEditText.setEnabled(true);
        _rememberMeCheckBox.setEnabled(true);
        _signupLink.setVisibility(View.VISIBLE);

    }
}
