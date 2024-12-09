package com.example.spotterly.ui.localizacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotterly.databinding.FragmentLocalizacionBinding;

public class LocalizacionFragment extends Fragment {

    private FragmentLocalizacionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LocalizacionViewModel localizacionViewModel =
                new ViewModelProvider(this).get(LocalizacionViewModel.class);

        binding = FragmentLocalizacionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLocalizacion;
        localizacionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}