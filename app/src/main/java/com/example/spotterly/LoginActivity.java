package com.example.spotterly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Vistas
        EditText txtUsuarioCorreo = findViewById(R.id.txtUsuarioCorreo);
        EditText txtUsuarioContrasena = findViewById(R.id.txtUsuarioContrasena);

        Button btLogin = findViewById(R.id.btLogin);
        Button btRegistrarse = findViewById(R.id.btRegistrarse);
        CheckBox checkRecuerdame = findViewById(R.id.checkRecuerdame);
        TextView textRecuperarContra = findViewById(R.id.textRecuperarContra);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario= txtUsuarioCorreo.getText().toString();
                String contrasena = txtUsuarioContrasena.getText().toString();
                // Validar las credenciales del usuario
                if (validarLogin(usuario, contrasena)) {
                    // Si las credenciales son correctas, redirigir al MainActivity
                    Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                    startActivity(intent);
                    finish(); // Finalizar esta actividad para no poder volver a ella
                } else {
                    // Si las credenciales son incorrectas, mostrar un mensaje de error
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Boton de registro
        btRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        // ir a recuperar contraseña
        textRecuperarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
                startActivity(intent);
            }
        });
        dbHelper = new DatabaseHelper(this);

        // ... (rest of your code)

        // Button click listener for login
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtUsuarioCorreo.getText().toString();
                String contrasena = txtUsuarioContrasena.getText().toString();

                // Validate credentials using database
                if (dbHelper.validateLogin(usuario, contrasena)) {
                    // Login successful, redirect to MainActivity
                    Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed, show error message
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean validarLogin(String usuario, String contrasena) {
    // Validación de ejemplo (en una aplicación real, esto debería conectarse con la base de datos)
        return usuario.equals("us@gmail.com") && contrasena.equals("1234");
    }
}