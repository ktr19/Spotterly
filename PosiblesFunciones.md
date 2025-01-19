# Proyecto: Spotterly

## DatabaseHelper

### AÑADIR TABLAS EN LA BASE DE DATOS

El siguiente código muestra cómo se agregan tablas a la base de datos en el método `onCreate` de la clase `DatabaseHelper`:

```java
@Override
public void onCreate(SQLiteDatabase db) {
    // Verifica si la tabla 'usuario' existe
    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='usuario'", null);
    cursor.moveToFirst();
    int count = cursor.getInt(0);
    cursor.close();

    if (count == 0) {
        // Crear la tabla 'usuario'
        db.execSQL("CREATE TABLE usuario (" +
                "telefono INT(9) PRIMARY KEY," +
                "nombre VARCHAR(50)," +
                "password VARCHAR(100) NOT NULL," +
                "tiene_suscripcion BOOLEAN DEFAULT FALSE NOT NULL," +
                "suscripcionid INT(10)," +
                "usos INT(3) DEFAULT 2," +
                "pregunta VARCHAR(50),"+
                "respuesta VARCHAR(50),"+
                "FOREIGN KEY (suscripcionid) REFERENCES suscripcion(suscripcionid)" +
                ")");

        // Crear la tabla 'suscripcion'
        db.execSQL("CREATE TABLE suscripcion (" +
                "suscripcionid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo VARCHAR(50) NOT NULL," +
                "fecha_inicio DATE NOT NULL," +
                "fecha_fin DATE NOT NULL," +
                "activa BOOLEAN DEFAULT 1," +
                "precio FLOAT"+
                ")");

        // Insertar datos en la tabla 'suscripcion'
        String fechaInicio = "2025-01-16"; 
        String fechaFinMensual = "2025-02-16";
        String fechaFinTrimestral = "2025-04-16"; 
        String fechaFinSemestral = "2025-07-16"; 
        String fechaFinAnual = "2026-01-16"; 

        db.execSQL("INSERT INTO suscripcion (tipo, fecha_inicio, fecha_fin, activa, precio) VALUES " +
                "('Mensual', '" + fechaInicio + "', '" + fechaFinMensual + "', 1, 4.99), " +
                "('Trimestral', '" + fechaInicio + "', '" + fechaFinTrimestral + "', 1, 9.99), " +
                "('Semestral', '" + fechaInicio + "', '" + fechaFinSemestral + "', 1, 29.99), " +
                "('Anual', '" + fechaInicio + "', '" + fechaFinAnual + "', 1, 50)");

        // Crear la tabla de relación 'suscripcion_usuario'
        db.execSQL("CREATE TABLE IF NOT EXISTS suscripcion_usuario (" +
                "suscripcionid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "telefono TEXT UNIQUE, " +
                "suscripcionid INTEGER, " +
                "fecha_inicio DATE NOT NULL, " +
                "fecha_fin DATE NOT NULL, " +
                "FOREIGN KEY (telefono) REFERENCES usuario(telefono), " +
                "FOREIGN KEY (suscripcionid) REFERENCES suscripcion(suscripcionid)" +
                ")");
    } else {
        // Si la tabla 'usuario' existe, agregar columna 'usos' si no está presente
        cursor = db.rawQuery("PRAGMA table_info(usuario)", null);
        boolean hasUsosColumn = false;
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals("usos")) {
                hasUsosColumn = true;
                break;
            }
        }
        cursor.close();

        if (!hasUsosColumn) {
            db.execSQL("ALTER TABLE usuario ADD COLUMN usos INT(3) DEFAULT 2");
        }

        // Crear tabla 'ubicaciones' si no existe
        db.execSQL("CREATE TABLE IF NOT EXISTS ubicaciones (" +
                "ubicacionid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitud FLOAT(2,6)," +
                "longitud FLOAT(2,6)," +
                "valoracion INTEGER(1)," +
                "usuario INT(9)," +
                "FOREIGN KEY (usuario) REFERENCES usuario(telefono)" +
                ")");
    }
}
## FUNCIONES
Aquí se muestran las funciones principales que gestionan las ubicaciones y el estacionamiento:
```java
public long guardarUbicacion(double latitud, double longitud, int valoracion, int usuarioTelefono) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("latitud", latitud);
    values.put("longitud", longitud);
    values.put("valoracion", valoracion);
    values.put("usuario", usuarioTelefono);

    return db.insert("ubicaciones", null, values);
}

public long dejarParkingLibre(double latitud, double longitud, int usuarioTelefono) {
    SQLiteDatabase db = this.getWritableDatabase();

    // Eliminar el registro correspondiente
    String whereClause = "latitud = ? AND longitud = ? AND usuario = ?";
    String[] whereArgs = { String.valueOf(latitud), String.valueOf(longitud), String.valueOf(usuarioTelefono) };

    return db.delete("ubicaciones", whereClause, whereArgs);
}
```

# Localización Fragment 
## Ejemplo lógica
### (la lógica de la localización y consulta tienes que hacerla en:
### ui -> LocalizacionFragment (en caso de la localización) 
### ui ->  ConsultarFragment (en caso de la consulta)

Esto es un ejemplo de como podría ser: 

```java
package com.example.spotterly.ui.localizacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spotterly.DatabaseHelper;
import com.example.spotterly.R;
import com.example.spotterly.Usuario;
import com.example.spotterly.databinding.FragmentLocalizacionBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocalizacionFragment extends Fragment implements OnMapReadyCallback {

    private FragmentLocalizacionBinding binding;
    private MapView mapView;
    private GoogleMap mMap;

    private TextView txtPrglocalizaciom;
    private RatingBar valoracion;
    private TextView txtEstrellas1, txtEstrellas2;
    private Button btLibre;

    private boolean isParkingLibre = true;
    private boolean aValorado = false;

    // Instancia de DatabaseHelper
    private DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocalizacionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());

        // Referenciar MapView
        mapView = root.findViewById(R.id.mapViewLocalizacion);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }

        txtPrglocalizaciom = binding.txtPrglocalizaciom;
        valoracion = binding.valoracion;
        txtEstrellas1 = binding.txtEstrellas1;
        txtEstrellas2 = binding.txtEstrellas2;
        btLibre = binding.btLibre;

        float valoracionGuardada = 0;
        valoracion.setRating(valoracionGuardada);

        ocultarValoracion();

        btLibre.setOnClickListener(v -> {
            if (isParkingLibre) {
                mostrarValoracion();
                btLibre.setText("Dejar Parking");
            } else {
                if (aValorado) {
                    valoracion.setRating(0);
                    ocultarValoracion();
                    btLibre.setText("Guardar Parking");

                    // Obtener la ubicación actual del mapa
                    LatLng currentLocation = mMap.getCameraPosition().target;

                    // Guardar la ubicación en la base de datos
                    double latitud = currentLocation.latitude;
                    double longitud = currentLocation.longitude;
                    int valoracionUsuario = (int) valoracion.getRating();
                    int usuarioTelefono = 123456789;  // Aquí debes obtener el teléfono del usuario actual

                    long result = dbHelper.guardarUbicacion(latitud, longitud, valoracionUsuario, usuarioTelefono);

                    if (result != -1) {
                        Toast.makeText(getContext(), "Ubicación guardada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error al guardar la ubicación", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Valora el parking para poder de dejarlo libre.", Toast.LENGTH_SHORT).show();
                }
            }
            isParkingLibre = !isParkingLibre;
        });

        // Función para "dejar el parking libre" (borrar registro)
        btLibre.setOnLongClickListener(v -> {
            // Obtener la ubicación actual del mapa
            LatLng currentLocation = mMap.getCameraPosition().target;

            // Eliminar la ubicación de la base de datos
            double latitud = currentLocation.latitude;
            double longitud = currentLocation.longitude;

            String telefonoConPrefijo = Usuario.sessionData[1];
            String telefonoSoloNumeros = telefonoConPrefijo.replaceAll("\\D+", ""); // Elimina todo lo que no sea número
            String telefonoSinPrefijo = telefonoSoloNumeros.substring(2); // Elimina los dos primeros números
            int telefonoUsuario = Integer.parseInt(telefonoSinPrefijo); // Ahora puedes convertirlo a int
            // Obtener el teléfono del usuario actual

            // Eliminar el registro de la ubicación
            long deleteResult = dbHelper.dejarParkingLibre(latitud, longitud, telefonoUsuario);

            if (deleteResult > 0) {
                Toast.makeText(getContext(), "Parking dejado libre", Toast.LENGTH_SHORT).show();
                btLibre.setText("Guardar Parking");
            } else {
                Toast.makeText(getContext(), "Error al dejar el parking libre", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        valoracion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            aValorado = (rating > 0);
        });

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Establecer una ubicación inicial en el mapa
        LatLng location = new LatLng(-34.0, 151.0);  // Usa las coordenadas adecuadas
        mMap.addMarker(new MarkerOptions().position(location).title("Ubicación de mi coche"));
        mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location, 10));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapView != null) {
            mapView.onDestroy();
        }
        binding = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    private void ocultarValoracion() {
        txtPrglocalizaciom.setVisibility(View.GONE);
        valoracion.setVisibility(View.GONE);
        txtEstrellas1.setVisibility(View.GONE);
        txtEstrellas2.setVisibility(View.GONE);
    }

    private void mostrarValoracion() {
        txtPrglocalizaciom.setVisibility(View.VISIBLE);
        valoracion.setVisibility(View.VISIBLE);
        txtEstrellas1.setVisibility(View.VISIBLE);
        txtEstrellas2.setVisibility(View.VISIBLE);
    }
}
