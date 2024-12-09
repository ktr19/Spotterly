package com.example.spotterly.ui.consultar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConsultarViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ConsultarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esto es un fragmento para Consultar");
    }

    public LiveData<String> getText() {
        return mText;
    }
}