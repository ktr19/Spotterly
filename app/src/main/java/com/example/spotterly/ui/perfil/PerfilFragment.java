package com.example.spotterly.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.spotterly.DatabaseHelper;
import com.example.spotterly.LandingActivity;
import com.example.spotterly.R;
import com.example.spotterly.Usuario;
import com.example.spotterly.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private DatabaseHelper dbHelper;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Nombre del usuario
        TextView txtUsuarioPerfil = binding.txtUsuarioPerfil;
        TextView txtNumPerfil = binding.txtNumPerfil;
        String nombreUsuario = Usuario.sessionData[0];
        String telefonoUsuario = Usuario.sessionData[1];
        if (txtUsuarioPerfil != null) {
            if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                txtUsuarioPerfil.setText("Usuario");  // Valor predeterminado si no hay nombre
            } else {
                txtUsuarioPerfil.setText(nombreUsuario);
            }
        }
        if (txtNumPerfil != null) {
            if (telefonoUsuario == null || telefonoUsuario.isEmpty()) {
                txtNumPerfil.setText("Número de teléfono");
            } else {
                txtNumPerfil.setText(telefonoUsuario);
            }
        }
        binding.btEliminar.setOnClickListener(v -> mostrarDialogoConfirmacion());
        binding.btActualizarNombre.setOnClickListener(v -> mostrarDialogoActualizarNombre());
        binding.btActualizarContrasena.setOnClickListener(v -> mostrarDialogoActualizarContrasena());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar tu cuenta de forma permanente? Esta acción no se puede deshacer.")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    String telefonoConPrefijo = Usuario.sessionData[1];
                    String telefonoSoloNumeros = telefonoConPrefijo.replaceAll("\\D+", "");
                    String telefonoSinPrefijo = telefonoSoloNumeros.substring(2);
                    int telefonoUsuario = Integer.parseInt(telefonoSinPrefijo);
                    dbHelper = new DatabaseHelper(requireContext());
                    int filasEliminadas = dbHelper.deleteUsuario(String.valueOf(telefonoUsuario));

                    if (filasEliminadas > 0) {
                        Toast.makeText(requireContext(), "Cuenta eliminada correctamente", Toast.LENGTH_LONG).show();
                        cerrarSesion(); // Lógica adicional para redirigir al usuario a la pantalla de inicio de sesión
                    } else {
                        Toast.makeText(requireContext(), "Error al eliminar la cuenta. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void cerrarSesion() {
        Usuario.sessionData[0] = null; // Nombre del usuario
        Usuario.sessionData[1] = null; // Teléfono del usuario
        Intent intent = new Intent(requireContext(), LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void mostrarDialogoActualizarNombre() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View vistaDialogo = inflater.inflate(R.layout.dialog_actualizar_nombre, null);
        EditText txtNuevoNombre = vistaDialogo.findViewById(R.id.txtNombreActualizar);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(vistaDialogo)
                .setTitle("Actualizar Nombre")
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String nuevoNombre = txtNuevoNombre.getText().toString().trim();
                    if (nuevoNombre.isEmpty()) {
                        Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        String telefonoConPrefijo = Usuario.sessionData[1];
                        String telefonoSoloNumeros = telefonoConPrefijo.replaceAll("\\D+", ""); // Elimina todo lo que no sea número
                        String telefonoSinPrefijo = telefonoSoloNumeros.substring(2); // Elimina los dos primeros números
                        int telefonoUsuario = Integer.parseInt(telefonoSinPrefijo);
                        dbHelper = new DatabaseHelper(requireContext());
                        int filasActualizadas = dbHelper.actualizarNombreUsuario(telefonoUsuario, nuevoNombre);
                        if (filasActualizadas > 0) {
                            // Actualización exitosa
                            Usuario.sessionData[0] = nuevoNombre; // Actualizar el nombre en los datos de sesión
                            binding.txtUsuarioPerfil.setText(nuevoNombre); // Actualizar el nombre en la vista
                            Toast.makeText(requireContext(), "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                        }
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
        togglePasswordVisibility(txtContrasena);
        togglePasswordVisibility(txtContrasena2);
        String telefonoConPrefijo = Usuario.sessionData[1];
        String telefonoSoloNumeros = telefonoConPrefijo.replaceAll("\\D+", ""); // Elimina todo lo que no sea número
        String telefonoSinPrefijo = telefonoSoloNumeros.substring(2);
        int telefonoUsuario = Integer.parseInt(telefonoSinPrefijo);
        dbHelper = new DatabaseHelper(requireContext());
        TextView txtRecuperarPregunta = vistaDialogo.findViewById(R.id.txtRecuperarPregunta);
        Usuario usuario = dbHelper.getUsuarioByTelefono(String.valueOf(telefonoUsuario));
        txtRecuperarPregunta.setText(usuario.getPregunta());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(vistaDialogo)
                .setTitle("Actualizar Contraseña")
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String contrasena = txtContrasena.getText().toString().trim();
                    String contrasena2 = txtContrasena2.getText().toString().trim();
                    String respuestaSeguridad = txtRespuestaSeguridad.getText().toString().trim();
                    if (contrasena.isEmpty() || contrasena2.isEmpty() || respuestaSeguridad.isEmpty()) {
                        Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    } else if (!contrasena.equals(contrasena2)) {
                        Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    } else if (contrasena.length() < 8) {
                        Toast.makeText(requireContext(), "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    } else {
                        if (usuario != null && respuestaSeguridad.equals(usuario.getRespuesta())) {
                            int filasActualizadas = dbHelper.actualizarPasswordUsuario(telefonoUsuario, contrasena);

                            if (filasActualizadas > 0) {
                                Toast.makeText(requireContext(), "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                            }
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
        togglePasswordVisibility(txtContrasena);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(vistaDialogo)
                .setTitle("Actualizar Pregunta de Seguridad")
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String pregunta = txtPregunta.getText().toString().trim();
                    String respuesta = txtRespuesta.getText().toString().trim();
                    String contrasena = txtContrasena.getText().toString().trim();
                    if (pregunta.isEmpty()) {
                        Toast.makeText(requireContext(), "La pregunta es obligatoria", Toast.LENGTH_SHORT).show();
                    } else if (respuesta.isEmpty()) {
                        Toast.makeText(requireContext(), "La respuesta es obligatoria", Toast.LENGTH_SHORT).show();
                    } else if (contrasena.isEmpty()) {
                        Toast.makeText(requireContext(), "La contraseña es obligatoria", Toast.LENGTH_SHORT).show();
                    } else {
                        String telefonoConPrefijo = Usuario.sessionData[1];
                        String telefonoSoloNumeros = telefonoConPrefijo.replaceAll("\\D+", ""); // Elimina todo lo que no sea número
                        String telefonoSinPrefijo = telefonoSoloNumeros.substring(2); // Elimina los dos primeros números
                        int telefonoUsuario = Integer.parseInt(telefonoSinPrefijo); // Ahora puedes convertirlo a int
                        dbHelper = new DatabaseHelper(requireContext());
                        Usuario usuario = dbHelper.getUsuarioByTelefono(String.valueOf(telefonoUsuario));

                        if (usuario != null && contrasena.equals(usuario.getPassword())) {
                            int filasActualizadas = dbHelper.actualizarPreguntaSeguridad(telefonoUsuario, pregunta, respuesta);
                            if (filasActualizadas > 0) {
                                Toast.makeText(requireContext(), "Pregunta de seguridad actualizada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Error al actualizar la pregunta de seguridad", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableRight = 2; // El ícono está en el lado derecho
                    // Verificamos si el toque está en el área del ícono de ojo
                    if (event.getX() >= (passwordEditText.getWidth() - passwordEditText.getPaddingRight() -
                            passwordEditText.getCompoundDrawables()[drawableRight].getBounds().width())) {
                        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            // Si es tipo contraseña, lo cambiamos a texto visible
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(passwordEditText.getCompoundDrawables()[0], null, getResources().getDrawable(R.drawable.ic_ojoopen), null);
                        } else {
                            // Si es texto visible, lo cambiamos a tipo contraseña
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(passwordEditText.getCompoundDrawables()[0], null, getResources().getDrawable(R.drawable.ic_ojooff), null);
                        }
                        passwordEditText.setSelection(passwordEditText.getText().length()); // Mantener el cursor al final del texto
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
