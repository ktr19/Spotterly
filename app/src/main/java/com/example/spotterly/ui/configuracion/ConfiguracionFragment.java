package com.example.spotterly.ui.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotterly.databinding.FragmentConfiguracionBinding;

public class ConfiguracionFragment extends Fragment {

    private FragmentConfiguracionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Por defecto invisible
        binding.cbxSeguridad.setVisibility(View.INVISIBLE);
        binding.txtSeguridad.setVisibility(View.INVISIBLE);
        binding.txtPreguntas.setVisibility(View.INVISIBLE);
        // btPreguntaSeguridad
        binding.btPreguntaSeguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cbxSeguridad.setVisibility(View.VISIBLE);
                binding.txtSeguridad.setVisibility(View.VISIBLE);
                binding.txtPreguntas.setVisibility(View.INVISIBLE);
            }
        });

        // btPreguntasFrecuentes
        binding.btPreguntasFrecuentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtPreguntas.setVisibility(View.VISIBLE);
                binding.cbxSeguridad.setVisibility(View.INVISIBLE);
                binding.txtSeguridad.setVisibility(View.INVISIBLE);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}