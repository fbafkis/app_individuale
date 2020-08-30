package com.francescobertamini.app_individuale.ui.account_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccountSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account settings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }



}