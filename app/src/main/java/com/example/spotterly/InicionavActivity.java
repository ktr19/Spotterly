package com.example.spotterly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import com.example.spotterly.ui.suscripciones.SuscripcionesFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotterly.databinding.ActivityInicionavBinding;


public class InicionavActivity extends AppCompatActivity implements SuscripcionesFragment.SuscripcionesListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicionavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicionavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Configurar el ActionBar y FAB
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configurar el AppBarConfiguration
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_localizacion, R.id.nav_consultar, R.id.nav_perfil, R.id.nav_configuracion, R.id.nav_suscripciones)
                .setOpenableLayout(drawer)
                .build();

        // Verificar si el usuario tiene suscripción
        boolean tieneSuscripcion = verificarSuscripcionUsuario();
        actualizarMenuSegunSuscripcion(tieneSuscripcion);

        // Si no tiene suscripción, navegar directamente al fragmento de suscripciones
        if (!tieneSuscripcion) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            if (navController.getCurrentDestination().getId() != R.id.nav_suscripciones) {
                navController.navigate(R.id.nav_suscripciones);
            }
        }
        // Configurar el NavController y la navegación
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void actualizarMenuSegunSuscripcion(boolean tieneSuscripcion) {
        NavigationView navigationView = binding.navView;
        Menu menu = navigationView.getMenu();

        menu.findItem(R.id.nav_localizacion).setVisible(tieneSuscripcion);
        menu.findItem(R.id.nav_consultar).setVisible(tieneSuscripcion);

        if (!tieneSuscripcion) {
            menu.findItem(R.id.nav_localizacion).setVisible(false);
            menu.findItem(R.id.nav_consultar).setVisible(false);
        }
    }

    private boolean verificarSuscripcionUsuario() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String telefonoUsuario = limpiarTelefono(Usuario.sessionData[1]);
        boolean suscripcionActiva = dbHelper.verificarUsuarioEnSuscripcion(telefonoUsuario);
        System.out.println(suscripcionActiva);
        return suscripcionActiva;
    }
    @Override
    public void onSuscripcionActualizada(boolean tieneSuscripcion) {
        // Actualiza el menú cuando cambie el estado de la suscripción
        actualizarMenuSegunSuscripcion(tieneSuscripcion);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicionav, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        TextView tituloGrande = findViewById(R.id.primerTitulo);
        TextView tituloPequeno = findViewById(R.id.segundoTitulo);

        if(tituloGrande != null & tituloPequeno !=  null) {
            if(Usuario.sessionData[0] == null || Usuario.sessionData[0].isEmpty()) {
                Usuario.sessionData[0] = "Usuario";
            }
            tituloGrande.setText(Usuario.sessionData[0]);
            tituloPequeno.setText(Usuario.sessionData[1]);
        }

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Maneja las opciones seleccionadas del menú
        int id = item.getItemId();

        if (id == R.id.cerrar) {
            logout(); // Llamamos al método logout para cerrar la sesión
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        // Puedes añadir lógica para borrar datos persistentes como SharedPreferences aquí

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(InicionavActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Evita que el usuario regrese a la pantalla anterior
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }

    private String limpiarTelefono(String telefonoConPrefijo) {
        String telefonoSoloNumeros = telefonoConPrefijo.replaceAll("\\D+", "");
        if (telefonoSoloNumeros.length() > 2) {
            return telefonoSoloNumeros.substring(2);
        }
        return telefonoSoloNumeros;
    }
}