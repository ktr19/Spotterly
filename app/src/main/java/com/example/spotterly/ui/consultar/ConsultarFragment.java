package com.example.spotterly.ui.consultar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotterly.databinding.FragmentConsultarBinding;

public class ConsultarFragment extends Fragment {

    private FragmentConsultarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConsultarViewModel consultarViewModel =
                new ViewModelProvider(this).get(ConsultarViewModel.class);

        binding = FragmentConsultarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textConsultar;
        consultarViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}