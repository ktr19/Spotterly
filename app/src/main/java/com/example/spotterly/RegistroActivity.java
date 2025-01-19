package com.example.spotterly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroActivity extends AppCompatActivity {
    private final int usosIniciales = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button miBoton = findViewById(R.id.btRegistrarse);
        miBoton.setOnClickListener(v -> register());

        // Obtener la vista del EditText de la contraseña
        EditText campoContrasena = findViewById(R.id.txtContrasena);

        // Establecer el OnTouchListener para cambiar la visibilidad de la contraseña
        campoContrasena.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableRight = 2;  // Índice para drawableRight (el ícono del ojo)
                    if (event.getX() >= (campoContrasena.getWidth() - campoContrasena.getPaddingRight() -
                            campoContrasena.getCompoundDrawables()[drawableRight].getBounds().width())) {
                        // Cambiar la visibilidad de la contraseña
                        if (campoContrasena.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            // Mostrar la contraseña
                            campoContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            // Cambiar el ícono del ojo a abierto
                            campoContrasena.setCompoundDrawablesWithIntrinsicBounds(campoContrasena.getCompoundDrawables()[0], null,
                                    getResources().getDrawable(R.drawable.ic_ojoopen), null);
                        } else {
                            // Ocultar la contraseña
                            campoContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            // Cambiar el ícono del ojo a cerrado
                            campoContrasena.setCompoundDrawablesWithIntrinsicBounds(campoContrasena.getCompoundDrawables()[0], null,
                                    getResources().getDrawable(R.drawable.ic_ojooff), null);
                        }

                        // Mover el cursor al final del texto
                        campoContrasena.setSelection(campoContrasena.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private boolean register() {
        try {
            DatabaseHelper db = new DatabaseHelper(this);
            Usuario usuarioACrear = new Usuario();

            // Obtén referencias a los campos de texto
            TextView campoTelefono = findViewById(R.id.txtTelefono);
            TextView campoNombre = findViewById(R.id.txtNombre);
            EditText campoContrasena = findViewById(R.id.txtContrasena);  // Cambié a EditText
            TextView campoPregunta = findViewById(R.id.txtPregunta);
            TextView campoRespuesta = findViewById(R.id.txtRespuesta);

            // Valida que los campos no sean nulos
            if (campoTelefono == null || campoNombre == null || campoContrasena == null ||
                    campoPregunta == null || campoRespuesta == null) {
                Toast.makeText(this, "Error interno: campos no encontrados.", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Obtén valores ingresados por el usuario
            String telefono = campoTelefono.getText().toString().trim();
            String nombre = campoNombre.getText().toString().trim();
            String contrasena = campoContrasena.getText().toString().trim();
            String pregunta = campoPregunta.getText().toString().trim();
            String respuesta = campoRespuesta.getText().toString().trim();

            // Construye un mensaje de error dinámico
            StringBuilder errorMessage = new StringBuilder();

            if (telefono.length() != 9) {
                errorMessage.append("El teléfono debe contener 9 dígitos.\n");
            }

            if (nombre.isEmpty()) {
                errorMessage.append("Debe introducir un nombre.\n");
            }

            if (contrasena.length() < 8) {
                errorMessage.append("La contraseña debe tener al menos 8 caracteres.\n");
            }

            if (pregunta.isEmpty()) {
                errorMessage.append("Debe escribir una pregunta de seguridad.\n");
            }

            if (respuesta.isEmpty()) {
                errorMessage.append("Debe escribir una respuesta de seguridad.\n");
            }

            // Si hay errores, muestra el mensaje y no continúa
            if (errorMessage.length() > 0) {
                Toast.makeText(this, errorMessage.toString().trim(), Toast.LENGTH_LONG).show();
                return false;
            }

            // Verifica que el usuario no exista ya en la base de datos
            if (db.getUsuarioByTelefono(telefono) != null) {
                Toast.makeText(this, "El teléfono ya está registrado.", Toast.LENGTH_LONG).show();
                return false;
            }

            // Crea el usuario y almacénalo en la base de datos
            usuarioACrear.setTelefono(Integer.parseInt(telefono));
            usuarioACrear.setNombre(nombre);
            usuarioACrear.setPassword(contrasena);
            usuarioACrear.setPregunta(pregunta);
            usuarioACrear.setRespuesta(respuesta);
            usuarioACrear.setUsos(this.usosIniciales);
            usuarioACrear.setTieneSuscripcion(false);

            db.insertUsuario(usuarioACrear);

            Toast.makeText(this, "Registro correcto", Toast.LENGTH_LONG).show();

            // Navega a la pantalla de login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;

        } catch (Exception e) {
            Toast.makeText(this, "Ha ocurrido un error durante el registro.", Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Opcional: para depuración
            return false;
        }
    }
}
