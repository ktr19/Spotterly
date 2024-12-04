package com.example.spotterly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instanciamos el helper para la base de datos
        dbHelper = new DatabaseHelper(this);

        // Vistas
        EditText txtUsuarioCorreo = findViewById(R.id.txtUsuarioCorreo);
        EditText txtUsuarioContrasena = findViewById(R.id.txtUsuarioContrasena);

        Button btLogin = findViewById(R.id.btLogin);
        CheckBox checkRecuerdame = findViewById(R.id.checkRecuerdame);
        TextView textRecuperarContra = findViewById(R.id.textRecuperarContra);
        TextView textRegistro= findViewById(R.id.textRegistrarse);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefono = txtUsuarioCorreo.getText().toString();
                String contrasena = txtUsuarioContrasena.getText().toString();

                // Validar las credenciales del usuario
                if (telefono.length() == 9 & dbHelper.validateLogin(telefono, contrasena)) {
                    // Si las credenciales son correctas, redirigir al MainActivity
                    Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                    startActivity(intent);
                    finish(); // Finalizar esta actividad para no poder volver a ella
                } else {
                    if(telefono.length() != 9) {
                        Toast.makeText(LoginActivity.this, "Debe introducir un numero de telefono de 9 caracteres", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                    // Si las credenciales son incorrectas, mostrar un mensaje de error

                }
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

        // ir a registro
        textRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }
}
