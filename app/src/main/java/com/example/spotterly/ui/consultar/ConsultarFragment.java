package com.example.spotterly.ui.consultar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.spotterly.R;
import com.example.spotterly.databinding.FragmentConsultarBinding;

public class ConsultarFragment extends Fragment {

    private FragmentConsultarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConsultarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Barra
        SeekBar seekBar = binding.seekBar;
        seekBar.setProgress(5);
        seekBar.setEnabled(false);

        //Botón que redirecciona
        Button btRedirigir = binding.btverGuardarParking;
        btRedirigir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_localizacion);
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