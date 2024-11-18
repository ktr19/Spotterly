package com.example.spotterly;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    }

    private boolean register() {
        TextView notificationLabel = findViewById(R.id.lbIncorrecto);
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
            if(usuarioACrear.getPassword().length() > 8 ) {
                db.insertUsuario(usuarioACrear);
                notificationLabel.setText("Registro existoso");
                return true;
            }

            notificationLabel.setText("Verifica los campos");
            return false;

        } catch(Exception e) {
            notificationLabel.setText("Ha surgido un error al registrar");
            return false;
        }
    }

}