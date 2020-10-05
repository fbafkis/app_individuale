package com.francescobertamini.app_individuale.ui.championships.championships_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChampionshipListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChampionshipListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is championship list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}