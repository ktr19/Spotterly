package com.example.spotterly;

import android.content.Intent;
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

        Button miBoton = findViewById(R.id. btRegistrarse);

        miBoton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });
        // Enlace del boton login
        Button btLogin= findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean register() {
        try {
            DatabaseHelper db = new DatabaseHelper(this);
            Usuario usuarioACrear = new Usuario();

            TextView campoTfono = findViewById(R.id.txtTelefono);
            TextView campoNombre = findViewById(R.id.txtNombre);
            TextView campoContrasena = findViewById(R.id.txtContrasena);

            usuarioACrear.setTelefono(Integer.parseInt(campoTfono.getText().toString()));
            usuarioACrear.setNombre(campoNombre.getText().toString());
            usuarioACrear.setPassword(campoContrasena.getText().toString());
            usuarioACrear.setUsos(this.usosIniciales);
            usuarioACrear.setTieneSuscripcion(false);

            // Verificaciones para comprobar que los campos son validos
            if(db.getUsuarioByTelefono(usuarioACrear.getTelefono()+"") == null && usuarioACrear.getPassword().length() > 8) {
                db.insertUsuario(usuarioACrear);
                Toast.makeText(RegistroActivity.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                //Te lleva otra vez a login
                Intent intent = new Intent(RegistroActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            Toast.makeText(RegistroActivity.this, "Credenciales incorrectas o el usuario existe", Toast.LENGTH_SHORT).show();
            return false;

        } catch(Exception e) {
            Toast.makeText(RegistroActivity.this, "Ha surgido un error", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}