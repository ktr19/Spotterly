package com.example.spotterly;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class RecuperarContrasenaActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private String telefonoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_contrasena);

        // Aplicar el ajuste de márgenes para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);

        // Obtener las vistas necesarias
        EditText txtTelefono = findViewById(R.id.txtTelefonoRecuperar);
        TextView txtRecuperarPregunta = findViewById(R.id.txtRecuperarPregunta);
        EditText txtRespuestaSeguridad = findViewById(R.id.txtRecuperarRespuesta);
        EditText nuevaContrasena = findViewById(R.id.txtNuevaContrasena);
        Button btnConfirmar = findViewById(R.id.btConfirmarNuevaContr);

        // Establecer un TextWatcher para detectar cambios en el teléfono
        txtTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Si el teléfono ha cambiado, realizar la consulta a la base de datos
                String telefono = charSequence.toString().replaceAll("\\D+", ""); // Eliminar caracteres no numéricos
                if (telefono.length() >= 9) {  // Verificar si el número tiene al menos 9 dígitos
                    // Realizar la consulta en la base de datos
                    Usuario usuario = dbHelper.getUsuarioByTelefono(telefono);

                    // Si el usuario existe, actualizar el TextView
                    if (usuario != null) {
                        txtRecuperarPregunta.setText(usuario.getPregunta());
                        telefonoUsuario = telefono; // Guardar el número de teléfono del usuario
                    } else {
                        txtRecuperarPregunta.setText("Usuario no encontrado");
                        telefonoUsuario = null; // Resetear si no se encuentra el usuario
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario hacer nada aquí
            }
        });

        // Configurar el botón de Confirmar
        btnConfirmar.setOnClickListener(v -> {
            if (telefonoUsuario != null && !telefonoUsuario.isEmpty()) {
                String respuestaSeguridad = txtRespuestaSeguridad.getText().toString().trim();

                // Comprobar si la respuesta de seguridad coincide con la de la base de datos
                Usuario usuario = dbHelper.getUsuarioByTelefono(telefonoUsuario);

                if (usuario != null) {

                    if (respuestaSeguridad.equals(usuario.getRespuesta())) {
                        String nuevaContraseña = nuevaContrasena.getText().toString();
                        dbHelper.actualizarPasswordUsuario(usuario.getTelefono(),nuevaContraseña);
                        Toast.makeText(this, "Contraseña actualizada.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RecuperarContrasenaActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        // Si la respuesta es incorrecta
                        Toast.makeText(this, "Respuesta de seguridad incorrecta.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Usuario no encontrado. Verifique el teléfono.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, ingrese un número de teléfono válido.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
