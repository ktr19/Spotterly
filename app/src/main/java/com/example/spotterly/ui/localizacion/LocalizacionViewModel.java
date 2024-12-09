package com.example.spotterly.ui.localizacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocalizacionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LocalizacionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esto es la localizacion");
    }

    public LiveData<String> getText() {
        return mText;
    }
}