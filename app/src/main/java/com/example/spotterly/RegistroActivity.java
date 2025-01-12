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

            TextView campoTfono = findViewById(R.id.txtTelefono) != null ? findViewById(R.id.txtTelefono) : null;
            TextView campoNombre = findViewById(R.id.txtNombre);
            TextView campoContrasena = findViewById(R.id.txtContrasena);
            if(campoTfono.getText() == null){
                usuarioACrear.setTelefono(1);
            }else{
                usuarioACrear.setTelefono(Integer.parseInt(campoTfono.getText().toString()));
            }

            usuarioACrear.setNombre(campoNombre.getText().toString());
            usuarioACrear.setPassword(campoContrasena.getText().toString());
            usuarioACrear.setUsos(this.usosIniciales);
            usuarioACrear.setTieneSuscripcion(false);
            String tlf = usuarioACrear.getTelefono()+"";
            String pass = usuarioACrear.getPassword() != null ? usuarioACrear.getPassword() : "";
            String name = usuarioACrear.getNombre() != null ? usuarioACrear.getNombre() : "";

            // Verificaciones para comprobar que los campos son validos
            if ((db.getUsuarioByTelefono(tlf) == null && tlf.length() > 0 ) && pass.length() > 8 && tlf.length() == 9 && !name.isEmpty()) {
                db.insertUsuario(usuarioACrear);
                Toast.makeText(RegistroActivity.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                //Te lleva otra vez a login
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (pass.length() < 8 && tlf.length() != 9 && name.length() == 0) {
                Toast.makeText(RegistroActivity.this, "La contraseña debe contener minimo 8 caracteres , el numero debe tener 9 y debe introducir un nombre", Toast.LENGTH_LONG).show();
                return false;
            } else if (pass.length() < 8 && name.length() == 0) {
                Toast.makeText(RegistroActivity.this, "La contraseña debe contener minimo 8 caracteres y debe introducir un nombre", Toast.LENGTH_LONG).show();
                return false;
            } else if (tlf.length() != 9 && name.length() == 0) {
                Toast.makeText(RegistroActivity.this, "El telefono debe contener 9 numeros y debe introducidir un nombre", Toast.LENGTH_LONG).show();
                return false;
            } else if (tlf.length() != 9 && pass.length() < 8) {
                Toast.makeText(RegistroActivity.this, "El telefono debe contener 9 numeros y la contraseña debe contener minimo 8 caracteres", Toast.LENGTH_LONG).show();
                return false;
            }else if(tlf.length() != 9){
                Toast.makeText(RegistroActivity.this, "El telefono debe contener 9 numeros ", Toast.LENGTH_LONG).show();
                return false;
            }else if(name.length() == 0){
                Toast.makeText(RegistroActivity.this, "Debe escribir un nombre", Toast.LENGTH_LONG).show();
                return false;
            }else if(pass.length() < 8){
                Toast.makeText(RegistroActivity.this, "La contraseña debe tener minimo 8 caracteres ", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (Exception e) {
            Toast.makeText(RegistroActivity.this, "Ha surgido un error", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

}