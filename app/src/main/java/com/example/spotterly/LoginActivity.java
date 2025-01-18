package com.example.spotterly;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
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
        TextView textRecuperarContra = findViewById(R.id.textRecuperarContra);
        TextView textRegistro= findViewById(R.id.textRegistrarse);
        //Subrayado
        TextView textView = findViewById(R.id.textRegistrarse);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView textView1 = findViewById(R.id.textRecuperarContra);
        textView1.setPaintFlags(textView1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // Añadir subrayado

        //Ver contraseña
        EditText passwordEditText = findViewById(R.id.txtUsuarioContrasena);
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableRight = 2;
                    if (event.getX() >= (passwordEditText.getWidth() - passwordEditText.getPaddingRight() -
                            passwordEditText.getCompoundDrawables()[drawableRight].getBounds().width())) {
                        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(passwordEditText.getCompoundDrawables()[0], null, getResources().getDrawable(R.drawable.ic_ojoopen), null);
                        } else {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(passwordEditText.getCompoundDrawables()[0], null, getResources().getDrawable(R.drawable.ic_ojooff), null);
                        }
                        passwordEditText.setSelection(passwordEditText.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefono = txtUsuarioCorreo.getText().toString();
                String contrasena = txtUsuarioContrasena.getText().toString();

                // Validar las credenciales del usuario
                if (telefono.length() == 9 & dbHelper.validateLogin(telefono, contrasena)) {
                    // Establecer las variables de sesion para mostrar el nombre en el menu de la app
                    Usuario currentSession = dbHelper.getUsuarioByTelefono(telefono);
                    Usuario.sessionData[0] = currentSession.getNombre();
                    Usuario.sessionData[1] = "+34 "+currentSession.getTelefono();

                    // Si las credenciales son correctas, redirigir al MainActivity
                    Intent intent = new Intent(LoginActivity.this, InicionavActivity.class);
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
