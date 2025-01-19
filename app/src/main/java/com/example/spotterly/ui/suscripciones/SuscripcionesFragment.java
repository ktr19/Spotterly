package com.example.spotterly.ui.suscripciones;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotterly.R;

import com.example.spotterly.DatabaseHelper;

public class SuscripcionesFragment extends Fragment {

    private FrameLayout frameMensual, frameTrimestral, frameSemestral, frameAnual;
    private Button btCancelarSuscripcion;
    private DatabaseHelper dbHelper;
    private String telefonoUsuario = "123456789"; // Número de teléfono del usuario logueado (puedes cambiar esto dinámicamente)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suscripciones, container, false);
        btCancelarSuscripcion = rootView.findViewById(R.id.btCancelarSuscripcion);
        frameMensual = rootView.findViewById(R.id.frameMensual);
        frameTrimestral = rootView.findViewById(R.id.frameTrimestral);
        frameSemestral = rootView.findViewById(R.id.frameSemestral);
        frameAnual = rootView.findViewById(R.id.frameAnual);
        dbHelper = new DatabaseHelper(getActivity()); // Inicializar el DatabaseHelper

        configurarEventos();

        // Mostrar las opciones de suscripción según el estado del usuario
        mostrarSuscripciones();

        return rootView;
    }


    private void configurarEventos() {
        frameMensual.setOnClickListener(v -> mostrarAlertaCompra("Mensual"));
        frameTrimestral.setOnClickListener(v -> mostrarAlertaCompra("Trimestral"));
        frameSemestral.setOnClickListener(v -> mostrarAlertaCompra("Semestral"));
        frameAnual.setOnClickListener(v -> mostrarAlertaCompra("Anual"));

        btCancelarSuscripcion.setOnClickListener(v -> mostrarAlertaCancelar());
    }

    private void mostrarAlertaCancelar() {
        String tipoSuscripcion = dbHelper.getSuscripcion(telefonoUsuario); // Obtener la suscripción actual

        if (tipoSuscripcion == null || tipoSuscripcion.isEmpty()) {
            Toast.makeText(getActivity(), "No tienes ninguna suscripción activa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar la alerta con el tipo de suscripción
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Estás seguro de que deseas cancelar tu suscripción " + tipoSuscripcion + "?")
                .setCancelable(false)
                .setPositiveButton("Sí", (dialog, id) -> {
                    // Cancelar la suscripción
                    int resultado = dbHelper.cancelarSuscripcionUsuario(telefonoUsuario);
                    if (resultado > 0) {
                        Toast.makeText(getActivity(), "Tu suscripción " + tipoSuscripcion + " ha sido cancelada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "No se encontró ninguna suscripción activa para cancelar", Toast.LENGTH_SHORT).show();
                    }
                    mostrarSuscripciones(); // Volver a mostrar las opciones de suscripción
                })
                .setNegativeButton("No", (dialog, id) -> {
                    Toast.makeText(getActivity(), "No se canceló la suscripción", Toast.LENGTH_SHORT).show();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void mostrarAlertaCompra(String tipo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Quieres comprar la opción " + tipo + "?")
                .setCancelable(false)
                .setPositiveButton("Sí", (dialog, id) -> {
                    procesarSuscripcion(tipo);
                    ocultarSuscripciones();
                    btCancelarSuscripcion.setVisibility(View.VISIBLE);
                })
                .setNegativeButton("No", (dialog, id) -> mostrarSuscripciones());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void procesarSuscripcion(String tipo) {
        // Determinar los valores según el tipo de suscripción
        int idSuscripcion = 0; // ID que corresponde al tipo de suscripción en la tabla
        switch (tipo) {
            case "Mensual":
                idSuscripcion = 1;
                break;
            case "Trimestral":
                idSuscripcion = 2;
                break;
            case "Semestral":
                idSuscripcion = 3;
                break;
            case "Anual":
                idSuscripcion = 4;
                break;
        }

        // Insertar la suscripción del usuario con fechas calculadas
        boolean resultado = dbHelper.asignarSuscripcionUsuario(telefonoUsuario, idSuscripcion, tipo);
        if (resultado) {
            Toast.makeText(getActivity(), "Suscripción " + tipo + " adquirida con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Error al adquirir la suscripción", Toast.LENGTH_SHORT).show();
        }
    }


    private void ocultarSuscripciones() {
        frameMensual.setVisibility(View.GONE);
        frameTrimestral.setVisibility(View.GONE);
        frameSemestral.setVisibility(View.GONE);
        frameAnual.setVisibility(View.GONE);
    }

    private void mostrarSuscripciones() {
        // Verificar si el usuario tiene una suscripción activa
        String suscripcionActual = dbHelper.getSuscripcion(telefonoUsuario);

        if (suscripcionActual != null && !suscripcionActual.isEmpty()) {
            // Si hay una suscripción activa, mostrar solo el botón para cancelar
            ocultarSuscripciones();
            btCancelarSuscripcion.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Suscripción activa: " + suscripcionActual, Toast.LENGTH_SHORT).show();
        } else {
            // Si no hay una suscripción activa, mostrar todas las opciones
            frameMensual.setVisibility(View.VISIBLE);
            frameTrimestral.setVisibility(View.VISIBLE);
            frameSemestral.setVisibility(View.VISIBLE);
            frameAnual.setVisibility(View.VISIBLE);
            btCancelarSuscripcion.setVisibility(View.GONE);
        }
    }

}