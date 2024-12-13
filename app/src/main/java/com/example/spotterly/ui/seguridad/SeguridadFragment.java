package com.example.spotterly.ui.seguridad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotterly.databinding.FragmentSeguridadBinding;

public class SeguridadFragment extends Fragment {

    private FragmentSeguridadBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SeguridadViewModel seguridadViewModel =
                new ViewModelProvider(this).get(SeguridadViewModel.class);

        binding = FragmentSeguridadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSeguridad;
        seguridadViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}