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

public class SuscripcionesFragment extends Fragment {

    private FrameLayout frameMensual, frameTrimestral, frameSemestral, frameAnual;
    private Button btCancelarSuscripcion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suscripciones, container, false);
        btCancelarSuscripcion = rootView.findViewById(R.id.btCancelarSuscripcion);
        frameMensual = rootView.findViewById(R.id.frameMensual);
        frameTrimestral = rootView.findViewById(R.id.frameTrimestral);
        frameSemestral = rootView.findViewById(R.id.frameSemestral);
        frameAnual = rootView.findViewById(R.id.frameAnual);
        btCancelarSuscripcion.setVisibility(View.GONE);
        configurarEventos();
        return rootView;
    }

    private void configurarEventos() {
        frameMensual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertaCompra("Mensual");
            }
        });

        frameTrimestral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertaCompra("Trimestral");
            }
        });

        frameSemestral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertaCompra("Semestral");
            }
        });

        frameAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertaCompra("Anual");
            }
        });

        btCancelarSuscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertaCancelar();
            }
        });
    }

    private void mostrarAlertaCancelar() {
        // Crear el AlertDialog para cancelar la suscripción
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Estás seguro de que deseas cancelar la suscripción?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Suscripción cancelada", Toast.LENGTH_SHORT).show();
                        mostrarSuscripciones(); // Volver a mostrar las opciones de suscripción
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "No se canceló la suscripción", Toast.LENGTH_SHORT).show();
                        ocultarSuscripciones();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void mostrarAlertaCompra(String tipo) {
        // Crear el AlertDialog para confirmar la compra de la suscripción
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Quieres comprar la opción " + tipo + "?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Acción al confirmar la compra
                        Toast.makeText(getActivity(), "Compra de suscripción " + tipo + " confirmada", Toast.LENGTH_SHORT).show();
                        procesarSuscripcion(tipo);
                        ocultarSuscripciones();
                        btCancelarSuscripcion.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Acción al cancelar la compra
                        Toast.makeText(getActivity(), "Lo tienes al alcance de tu mano", Toast.LENGTH_SHORT).show();
                        mostrarSuscripciones();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void procesarSuscripcion(String tipo) {
        ///////////////////////// Lógica para procesar la suscripción seleccionada
        Toast.makeText(getActivity(), "Procesando suscripción: " + tipo, Toast.LENGTH_SHORT).show();
    }

    private void ocultarSuscripciones() {
        frameMensual.setVisibility(View.GONE);
        frameTrimestral.setVisibility(View.GONE);
        frameSemestral.setVisibility(View.GONE);
        frameAnual.setVisibility(View.GONE);
    }

    private void mostrarSuscripciones() {
        frameMensual.setVisibility(View.VISIBLE);
        frameTrimestral.setVisibility(View.VISIBLE);
        frameSemestral.setVisibility(View.VISIBLE);
        frameAnual.setVisibility(View.VISIBLE);
        btCancelarSuscripcion.setVisibility(View.GONE);
    }
}
