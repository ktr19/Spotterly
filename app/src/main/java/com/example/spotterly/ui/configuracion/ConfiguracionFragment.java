package com.example.spotterly.ui.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.example.spotterly.R;
import com.example.spotterly.databinding.FragmentConfiguracionBinding;

public class ConfiguracionFragment extends Fragment {

    private FragmentConfiguracionBinding binding;
    private Button btPreguntaSeguridad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfiguracionViewModel configuracionViewModel =
                new ViewModelProvider(this).get(ConfiguracionViewModel.class);

        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textConfiguracion;
        configuracionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Obtiene el botón desde la vista
        btPreguntaSeguridad = binding.btPreguntaSeguridad;

        // Configura el listener para el botón
        btPreguntaSeguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(ConfiguracionFragment.this);
                navController.navigate(R.id.action_CuartoFragment_to_SecurityFragment);
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