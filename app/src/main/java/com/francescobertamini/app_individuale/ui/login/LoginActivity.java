package com.francescobertamini.app_individuale.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorStandings;
import com.francescobertamini.app_individuale.ui.BasicActivity;
import com.francescobertamini.app_individuale.ui.main.MainActivity;
import com.francescobertamini.app_individuale.R;

import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerStatus;
import com.francescobertamini.app_individuale.database.dbmanagers.DBManagerUser;
import com.francescobertamini.app_individuale.ui.signup.SignupActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BasicActivity {

    Boolean accountCreated = false;
    private DBManagerStatus dbManagerStatus;
    private DBManagerUser dbManagerUser;
    SharedPreferences preferences;

    @BindView(R.id.loginUsernameEditText)
    EditText _loginUsernameEditText;
    @BindView(R.id.loginUsernameTextInputLayout)
    TextInputLayout _loginUsernameTextInputLayout;
    @BindView(R.id.loginPasswordEditText)
    EditText _loginPasswordEditText;
    @BindView(R.id.loginPasswordTextInputLayout)
    TextInputLayout _loginPasswordTextInputLayout;
    @BindView(R.id.loginRememberMe)
    CheckBox _loginRememberMe;
    @BindView(R.id.loginButton)
    Button _loginButton;
    @BindView(R.id.loginProgressBar)
    ProgressBar _loginProgressBar;
    @BindView(R.id.signupLink)
    LinearLayout _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copyAssets();
        preferences = getSharedPreferences("com.francescobertamini.app_individuale", MODE_PRIVATE);
        accountCreated = getIntent().getBooleanExtra("accountCreated", false);
        dbManagerStatus = new DBManagerStatus(this);
        dbManagerStatus.open();
        dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();
        Cursor statusCursor = dbManagerStatus.fetch();
        int isUserLogged = statusCursor.getInt(statusCursor.getColumnIndex("is_user_logged"));
        Log.e("UserLogged", Integer.toString(isUserLogged));

        if (isUserLogged == 1) {
            String actualUsername = statusCursor.getString(statusCursor.getColumnIndex("user_logged"));
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("username", actualUsername);
            startActivity(intent);
            finish();
            dbManagerUser.close();
            dbManagerStatus.close();

        } else if (isUserLogged == 0) {
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

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getBoolean("firstrun", true)) {
            JsonExtractorChampionships jsonExtractorChampionships = new JsonExtractorChampionships(getApplicationContext());
            JsonExtractorStandings jsonExtractorStandings = new JsonExtractorStandings(getApplicationContext());
            try {
                jsonExtractorChampionships.copyFile();
                jsonExtractorStandings.copyFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            preferences.edit().putBoolean("firstrun", false).commit();
        }
    }

    private void login() {
        if (_loginUsernameEditText.getText().toString().trim().isEmpty() || _loginPasswordEditText.getText().toString().trim().isEmpty()) {
            if (_loginUsernameEditText.getText().toString().trim().isEmpty()) {
                setErrorTheme(_loginUsernameTextInputLayout);
                _loginUsernameTextInputLayout.setError("Inserisci l'username!");
            } else unsetErrorTheme(_loginUsernameTextInputLayout);
            if (_loginPasswordEditText.getText().toString().trim().isEmpty()) {
                setErrorTheme(_loginPasswordTextInputLayout);
                _loginPasswordTextInputLayout.setError("Inserisci la password!");
            } else unsetErrorTheme(_loginPasswordTextInputLayout);
        } else {
            String username = _loginUsernameEditText.getText().toString().trim();
            String password = _loginPasswordEditText.getText().toString().trim();
            loginProgressTheme();
            Boolean loginResult = checkCredentials(username, password);

            if (loginResult) {
                if (_loginRememberMe.isChecked()) {
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
                dbManagerStatus.update(true, actualUsername);
                dbManagerStatus.close();
                dbManagerUser.close();
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
        t.setErrorEnabled(true);
        t.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
    }

    private void unsetErrorTheme(TextInputLayout t) {
        t.setErrorEnabled(false);
    }

    private void loginProgressTheme() {
        _loginButton.setVisibility(View.GONE);
        _loginProgressBar.setIndeterminate(true);
        _loginUsernameEditText.setEnabled(false);
        _loginPasswordEditText.setEnabled(false);
        _loginRememberMe.setEnabled(false);
        _loginProgressBar.setVisibility(View.VISIBLE);
        _signupLink.setVisibility(View.GONE);
    }

    private void loginProgressThemeEnd() {
        _loginProgressBar.setVisibility(View.GONE);
        _loginButton.setVisibility(View.VISIBLE);
        _loginUsernameEditText.setEnabled(true);
        _loginPasswordEditText.setEnabled(true);
        _loginRememberMe.setEnabled(true);
        _signupLink.setVisibility(View.VISIBLE);
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("champs_images");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            File directory = new File(getFilesDir(), "champs_images");
            directory.mkdir();
            try {
                in = assetManager.open("champs_images/" + filename);
                File outFile = new File(getFilesDir(), "champs_images/" + filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}

