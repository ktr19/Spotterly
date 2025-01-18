package com.example.spotterly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.spotterly.ui.perfil.PerfilFragment;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos y versión
    private static final String DATABASE_NAME = "spotterly.db";
    private static final int DATABASE_VERSION = 1;

    // Nombres de las tablas y columnas
    private static final String TABLE_USUARIO = "usuario";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_ID_SUSCRIPCION = "suscripcionid";
    private static final String COLUMN_PREGUNTA = "pregunta";
    private static final String COLUMN_RESPUESTA = "respuesta";

    // ... (otros campos)

//    private static final String TABLE_SUSCRIPCION = "suscripcion";
//    private static final String COLUMN_SUSCRIPCION_ID = ;
//    private static final String COLUMN_EMAIL = ;
//    private static final String COLUMN_NOMBRE = ;
//    // ... (otros campos)

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Check if the 'usuario' table exists
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='usuario'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            // Create the 'usuario' table
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

            // Create the 'suscripcion' table
            db.execSQL("CREATE TABLE suscripcion (" +
                    "suscripcionid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tipo VARCHAR(50) NOT NULL," +
                    "fecha_inicio DATE NOT NULL," +
                    "fecha_fin DATE NOT NULL," +
                    "activa BOOLEAN DEFAULT 1," +
                    "precio FLOAT"+
                    ")");
            // Insert the subscription plans
            String fechaInicio = "2025-01-16"; // Puedes ajustar esta fecha
            String fechaFinMensual = "2025-02-16"; // Fecha de fin para la suscripción mensual
            String fechaFinTrimestral = "2025-04-16"; // Fecha de fin para la suscripción trimestral
            String fechaFinSemestral = "2025-07-16"; // Fecha de fin para la suscripción semestral
            String fechaFinAnual = "2026-01-16"; // Fecha de fin para la suscripción anual

            db.execSQL("INSERT INTO suscripcion (tipo, fecha_inicio, fecha_fin, activa, precio) VALUES " +
                    "('Mensual', '" + fechaInicio + "', '" + fechaFinMensual + "', 1, 4.99), " +
                    "('Trimestral', '" + fechaInicio + "', '" + fechaFinTrimestral + "', 1, 9.99), " +
                    "('Semestral', '" + fechaInicio + "', '" + fechaFinSemestral + "', 1, 29.99), " +
                    "('Anual', '" + fechaInicio + "', '" + fechaFinAnual + "', 1, 50)");
        } else {
            // Check if the 'usos' column exists in the 'usuario' table
            cursor = db.rawQuery("PRAGMA table_info(usuario)", null);
            boolean hasUsosColumn = false;
            while (cursor.moveToNext()) {
                if (cursor.getString(1).equals("usos")) {
                    hasUsosColumn = true;
                    break;
                }
            }
            cursor.close();

            // Add the 'usos' column if it doesn't exist
            if (!hasUsosColumn) {
                db.execSQL("ALTER TABLE usuario ADD COLUMN usos INT(3) DEFAULT 2");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // ... (método onUpgrade)

    // Método para insertar un usuario
    public long insertUsuario(Usuario usuario) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, usuario.getNombre());
        values.put(COLUMN_TELEFONO, usuario.getTelefono());
        values.put(COLUMN_PASSWORD, usuario.getPassword());
        values.put(COLUMN_NOMBRE, usuario.getNombre());
        values.put(COLUMN_PREGUNTA, usuario.getPregunta());
        values.put(COLUMN_RESPUESTA, usuario.getRespuesta());
        // ... (otros campos)
        long newRowId = db.insert(TABLE_USUARIO, null, values);
        db.close();
        return newRowId;
    }

    // Método para obtener un usuario por teléfono
    public Usuario getUsuarioByTelefono(String telefono) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                COLUMN_TELEFONO,
                COLUMN_PASSWORD,
                COLUMN_NOMBRE,
                COLUMN_PREGUNTA,
                COLUMN_RESPUESTA
        };
        String selection = COLUMN_TELEFONO + " = ?";
        String[] selectionArgs = { telefono };
        Cursor cursor = db.query(
                TABLE_USUARIO,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setTelefono(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))));
            usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
            usuario.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            usuario.setPregunta(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PREGUNTA)));
            usuario.setRespuesta(cursor.getString((cursor.getColumnIndexOrThrow(COLUMN_RESPUESTA))));
        }
        cursor.close();
        db.close();
        return usuario;
    }
    public boolean validateLogin(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {usuario, contrasena};

        // Consulta para buscar al usuario por el nombre de usuario y contraseña
        String selectQuery = "SELECT * FROM " + TABLE_USUARIO + " WHERE telefono = ? AND password = ?";

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        int count = cursor.getCount();
        cursor.close();

        // Si se encuentra un usuario, la autenticación es exitosa
        return count > 0;
    }
    // Método para eliminar un usuario por su número de teléfono
    public int deleteUsuario(String telefono) {
        SQLiteDatabase db = getWritableDatabase();
        // Condición de eliminación
        String whereClause = COLUMN_TELEFONO + " = ?";
        String[] whereArgs = { telefono };

        // Eliminar el registro y devolver el número de filas afectadas
        int filasEliminadas = db.delete(TABLE_USUARIO, whereClause, whereArgs);
        db.close();
        return filasEliminadas;
    }
    // Método para actualizar la contraseña de un usuario
    public int actualizarPasswordUsuario(int telefono, String nuevaPassword) {
        SQLiteDatabase db = getWritableDatabase();

        // Crear los valores que se van a actualizar
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, nuevaPassword);

        // Condición de actualización
        String whereClause = COLUMN_TELEFONO + " = ?";
        String[] whereArgs = { String.valueOf(telefono) };

        // Actualizar el registro y devolver el número de filas afectadas
        int filasActualizadas = db.update(TABLE_USUARIO, values, whereClause, whereArgs);
        db.close();
        return filasActualizadas;
    }

    public int actualizarSuscripcionUsuario(int telefono, int idSuscripcion) {
        SQLiteDatabase db = getWritableDatabase();

        // Los valores que se van a actualizar
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_SUSCRIPCION, idSuscripcion);

        // Condición de actualización
        String whereClause = COLUMN_TELEFONO + " = ?";
        String[] whereArgs = { String.valueOf(telefono) };

        // Actualizar el registro y devolver el número de filas afectadas
        int filasActualizadas = db.update(TABLE_USUARIO, values, whereClause, whereArgs);
        db.close();
        return filasActualizadas;
    }
    // Método para actualizar la pregunta y respuesta de seguridad de un usuario
    public int actualizarPreguntaSeguridad(int telefono, String nuevaPregunta, String nuevaRespuesta) {
        SQLiteDatabase db = getWritableDatabase();

        // Crear los valores que se van a actualizar
        ContentValues values = new ContentValues();
        values.put("pregunta", nuevaPregunta);
        values.put("respuesta", nuevaRespuesta);

        // Condición de actualización
        String whereClause = COLUMN_TELEFONO + " = ?";
        String[] whereArgs = { String.valueOf(telefono) };

        // Actualizar el registro y devolver el número de filas afectadas
        int filasActualizadas = db.update(TABLE_USUARIO, values, whereClause, whereArgs);
        db.close();
        return filasActualizadas;
    }
    // Método para actualizar el nombre de un usuario
    public int actualizarNombreUsuario(int telefono, String nuevoNombre) {
        SQLiteDatabase db = getWritableDatabase();

        // Crear los valores que se van a actualizar
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nuevoNombre);

        // Condición de actualización
        String whereClause = COLUMN_TELEFONO + " = ?";
        String[] whereArgs = { String.valueOf(telefono) };

        // Actualizar el registro y devolver el número de filas afectadas
        int filasActualizadas = db.update(TABLE_USUARIO, values, whereClause, whereArgs);
        db.close();
        return filasActualizadas;
    }




}