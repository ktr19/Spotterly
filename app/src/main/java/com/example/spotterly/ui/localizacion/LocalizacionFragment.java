package com.example.spotterly.ui.localizacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotterly.databinding.FragmentLocalizacionBinding;

public class LocalizacionFragment extends Fragment {

    private FragmentLocalizacionBinding binding;

    // Declarar las vistas
    private TextView txtPrglocalizaciom;
    private RatingBar valoracion;
    private TextView txtEstrellas1, txtEstrellas2;
    private Button btLibre;

    // Variable para gestionar el estado del parking (si está ocupado o libre)
    private boolean isParkingLibre = true;
    private boolean aValorado = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocalizacionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        txtPrglocalizaciom = binding.txtPrglocalizaciom;
        valoracion = binding.valoracion;
        txtEstrellas1 = binding.txtEstrellas1;
        txtEstrellas2 = binding.txtEstrellas2;
        btLibre = binding.btLibre;
        float valoracionGuardada = 0;
        valoracion.setRating(valoracionGuardada);

        // Ocultar las vistas de la valoración por defecto
        ocultarValoracion();

        // Acción del botón
        btLibre.setOnClickListener(v -> {
            if (isParkingLibre) {
                mostrarValoracion();
                btLibre.setText("Dejar Parking");
            } else {
                if (aValorado) {
                    /*Logica para guardar la valoracion
                    float rating = valoracion.getRating();
                    databaseHelper.guardarValoracion(rating);*/

                    valoracion.setRating(0);
                    ocultarValoracion();
                    btLibre.setText("Guardar Parking");
                } else {
                    Toast.makeText(getContext(), "Valora el parking para poder de dejarlo libre.", Toast.LENGTH_SHORT).show();
                }
            }
            isParkingLibre = !isParkingLibre;
        });
        valoracion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            aValorado = (rating > 0);
        });

        return root;
    }

    private void ocultarValoracion() {
        txtPrglocalizaciom.setVisibility(View.GONE);
        valoracion.setVisibility(View.GONE);
        txtEstrellas1.setVisibility(View.GONE);
        txtEstrellas2.setVisibility(View.GONE);
    }

    private void mostrarValoracion() {
        txtPrglocalizaciom.setVisibility(View.VISIBLE);
        valoracion.setVisibility(View.VISIBLE);
        txtEstrellas1.setVisibility(View.VISIBLE);
        txtEstrellas2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
