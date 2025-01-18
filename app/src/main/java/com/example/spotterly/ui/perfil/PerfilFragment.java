package com.example.spotterly.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.spotterly.R;
import com.example.spotterly.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // botón de eliminar cuenta
        binding.btEliminar.setOnClickListener(v -> mostrarDialogoConfirmacion());
        // botón de actualizar nombre
        binding.btActualizarNombre.setOnClickListener(v -> mostrarDialogoActualizarNombre());
        // botón de actualizar contraseña
        binding.btActualizarContrasena.setOnClickListener(v -> mostrarDialogoActualizarContrasena());
        // botón de actualizar pregunta
        binding.btActualizarPreguntaSeguridad.setOnClickListener(v -> mostrarDialogoActualizarPreguntaSeguridad());
        return root;
    }

    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(requireContext());
        constructorDialogo.setTitle("Eliminar Cuenta");
        constructorDialogo.setMessage("¿Estás seguro de que deseas eliminar tu cuenta?");
        constructorDialogo.setPositiveButton("Sí", (dialogo, which) -> {
            ejecutarAccionEliminarCuenta();
        });
        constructorDialogo.setNegativeButton("No", (dialogo, which) -> {
            Toast.makeText(requireContext(), "Acción cancelada, nos alegra tenerte de vuelta :)", Toast.LENGTH_SHORT).show();
            dialogo.dismiss();
        });
        AlertDialog dialogo = constructorDialogo.create();
        dialogo.show();
    }

    private void ejecutarAccionEliminarCuenta() {
        ////////////////////////////////////////////////// Lógica para eliminar la cuenta
        Toast.makeText(requireContext(), "Cuenta eliminada correctamente", Toast.LENGTH_LONG).show();

        // Ejemplo: Borrar de la base de datos y cerrar seison

    }

    private void mostrarDialogoActualizarNombre() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View vistaDialogo = inflater.inflate(R.layout.dialog_actualizar_nombre, null);
        EditText txtNuevoNombre = vistaDialogo.findViewById(R.id.txtNombreActualizar); // EditText donde se introduce el nombre
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(vistaDialogo)
                .setTitle("Actualizar Nombre")
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String nuevoNombre = txtNuevoNombre.getText().toString().trim();

                    if (nuevoNombre.isEmpty()) {
                        Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        //////////////////////////////////////////////// Aquí puedes agregar la lógica para actualizar el nombre
                        Toast.makeText(requireContext(), "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void mostrarDialogoActualizarContrasena() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View vistaDialogo = inflater.inflate(R.layout.dialog_actualizar_contrasena, null);
        EditText txtContrasena = vistaDialogo.findViewById(R.id.txtContrasenaActualizar); // Nueva Contraseña
        EditText txtContrasena2 = vistaDialogo.findViewById(R.id.txtContrasenaActualizar2); // Repetir Contraseña
        EditText txtRespuestaSeguridad = vistaDialogo.findViewById(R.id.txtRespuestaActualizar); // Respuesta de seguridad

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(vistaDialogo)
                .setTitle("Actualizar Contraseña")
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String contrasena = txtContrasena.getText().toString().trim();
                    String contrasena2 = txtContrasena2.getText().toString().trim();
                    String respuestaSeguridad = txtRespuestaSeguridad.getText().toString().trim();

                    /////////////////////////// Verificar que las contraseñas coinciden
                    if (contrasena.isEmpty() || contrasena2.isEmpty() || respuestaSeguridad.isEmpty()) {
                        Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    } else if (!contrasena.equals(contrasena2)) {
                        Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    } else {
                        // Aquí deberías agregar la lógica para verificar la respuesta de seguridad
                        // Supongamos que la respuesta correcta es "miRespuestaSeguridad" para este ejemplo:
                        String respuestaCorrecta = "miRespuestaSeguridad";

                        if (respuestaSeguridad.equals(respuestaCorrecta)) {
                            // Aquí puedes agregar la lógica para actualizar la contraseña
                            // Ejemplo: actualizar la contraseña en la base de datos o SharedPreferences
                            Toast.makeText(requireContext(), "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "La respuesta de seguridad es incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
    private void mostrarDialogoActualizarPreguntaSeguridad() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View vistaDialogo = inflater.inflate(R.layout.dialog_actualizar_pregunta, null);
        EditText txtPregunta = vistaDialogo.findViewById(R.id.txtPreguntaActualizar); // Nueva pregunta
        EditText txtRespuesta = vistaDialogo.findViewById(R.id.txtRespuestaActualizar); // Nueva respuesta
        EditText txtContrasena = vistaDialogo.findViewById(R.id.txtContrasenaActualizar); // Contraseña actual
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(vistaDialogo)
                .setTitle("Actualizar Pregunta de Seguridad")
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String pregunta = txtPregunta.getText().toString().trim();
                    String respuesta = txtRespuesta.getText().toString().trim();
                    String contrasena = txtContrasena.getText().toString().trim();

                    if (pregunta.isEmpty() || respuesta.isEmpty() || contrasena.isEmpty()) {
                        Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    } else {
                        //////////////////////////////////////// Aquí deberías verificar que la contraseña actual sea correcta
                        // Supongamos que la contraseña actual es "miContrasenaActual" para este ejemplo:
                        String contrasenaCorrecta = "miContrasenaActual";

                        if (contrasena.equals(contrasenaCorrecta)) {
                            // Aquí puedes agregar la lógica para actualizar la pregunta y la respuesta de seguridad
                            // Ejemplo: actualizar la pregunta y respuesta en la base de datos o SharedPreferences
                            Toast.makeText(requireContext(), "Pregunta de seguridad actualizada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
