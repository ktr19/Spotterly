package com.example.spotterly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        miBoton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });
        // Enlace del boton btLanding

    }

    private boolean register() {
        try {
            DatabaseHelper db = new DatabaseHelper(this);
            Usuario usuarioACrear = new Usuario();

            // Obtén referencias a los campos de texto
            TextView campoTelefono = findViewById(R.id.txtTelefono);
            TextView campoNombre = findViewById(R.id.txtNombre);
            TextView campoContrasena = findViewById(R.id.txtContrasena);
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