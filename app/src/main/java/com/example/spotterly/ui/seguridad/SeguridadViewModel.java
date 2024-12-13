package com.example.spotterly.ui.seguridad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SeguridadViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SeguridadViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esto es seguridad");
    }

    public LiveData<String> getText() {
        return mText;
    }
}