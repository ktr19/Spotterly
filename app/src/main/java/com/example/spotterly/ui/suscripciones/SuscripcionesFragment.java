package com.example.spotterly.ui.suscripciones;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotterly.databinding.FragmentSuscripcionesBinding;

public class SuscripcionesFragment extends Fragment {

    private FragmentSuscripcionesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSuscripcionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener la referencia al botón de cancelar suscripción
        Button btCancelarSuscripcion = binding.btCancelarSuscripcion;

        // Establecer el OnClickListener para el botón
        btCancelarSuscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlerta();
            }
        });

        return root;
    }

    private void mostrarAlerta() {
        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Estás seguro de que deseas cancelar la suscripción?")
                .setCancelable(false)  // El diálogo no se puede cancelar tocando fuera
                .setPositiveButton("Sí", (dialog, id) -> {
                    // Acción si el usuario presiona "Sí"
                    Toast.makeText(getActivity(), "Suscripción cancelada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    // Acción si el usuario presiona "No"
                    dialog.dismiss();  // Simplemente cierra el diálogo
                });

        // Mostrar el diálogo
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}