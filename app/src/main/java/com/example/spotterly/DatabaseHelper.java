package com.example.spotterly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.spotterly.ui.perfil.PerfilFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos y versión
    private static final String DATABASE_NAME = "spotterly.db";
    private static final int DATABASE_VERSION = 1;

    // Nombres de las tablas y columnas
    private static final String TABLE_USUARIO = "usuario";
    private static final String TABLE_SUSCRIPCION = "suscripcion";
    private static final String TABLE_SUSCRIPCION_USUARIO = "suscripcion_usuario"; // Nueva tabla

    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_PREGUNTA = "pregunta";
    private static final String COLUMN_RESPUESTA = "respuesta";
    private static final String COLUMN_ID_SUSCRIPCION = "suscripcionid";
    private static final String COLUMN_SUSCRIPCION_ID = "idSuscripcionUsuario";
    private static final String COLUMN_FECHA_INICIO = "fecha_inicio";
    private static final String COLUMN_FECHA_FIN = "fecha_fin";

    private static final String COLUMN_TIPO_SUSCRIPCION = "tipo";
    private static final String COLUMN_PRECIO = "precio";
    private static final String COLUMN_ACTIVA = "activa";

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
            // Crear la tabla suscripcion_usuario
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUSCRIPCION_USUARIO + " (" +
                    COLUMN_SUSCRIPCION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TELEFONO + " TEXT, " +
                    COLUMN_ID_SUSCRIPCION + " INTEGER, " +
                    COLUMN_FECHA_INICIO + " DATE NOT NULL, " +
                    COLUMN_FECHA_FIN + " DATE NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_TELEFONO + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_TELEFONO + "), " +
                    "FOREIGN KEY (" + COLUMN_ID_SUSCRIPCION + ") REFERENCES " + TABLE_SUSCRIPCION + "(suscripcionid)" +
                    ")");

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
    public long insertSuscripcionUsuario(String telefono, int suscripcionid, String fechaInicio, String fechaFin) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TELEFONO, telefono);
        values.put(COLUMN_ID_SUSCRIPCION, suscripcionid);
        values.put(COLUMN_FECHA_INICIO, fechaInicio);
        values.put(COLUMN_FECHA_FIN, fechaFin);

        long newRowId = db.insert(TABLE_SUSCRIPCION_USUARIO, null, values);
        db.close();
        return newRowId;
    }
    // Método para obtener la suscripción de un usuario
    public SuscripcionUsuario getSuscripcionUsuario(String telefono) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                COLUMN_SUSCRIPCION_ID,
                COLUMN_ID_SUSCRIPCION,
                COLUMN_FECHA_INICIO,
                COLUMN_FECHA_FIN
        };

        String selection = COLUMN_TELEFONO + " = ?";
        String[] selectionArgs = {telefono};

        Cursor cursor = db.query(
                TABLE_SUSCRIPCION_USUARIO,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        SuscripcionUsuario suscripcionUsuario = null;
        if (cursor.moveToFirst()) {
            suscripcionUsuario = new SuscripcionUsuario();
            suscripcionUsuario.setIdSuscripcionUsuario(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUSCRIPCION_ID)));
            suscripcionUsuario.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)));
            suscripcionUsuario.setSuscripcionId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_SUSCRIPCION)));
            suscripcionUsuario.setFechaInicio(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INICIO)));
            suscripcionUsuario.setFechaFin(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_FIN)));
        }

        cursor.close();
        db.close();
        return suscripcionUsuario;
    }
    public String getSuscripcion(String telefono) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT s.tipo FROM suscripcion_usuario su " +
                "JOIN suscripcion s ON su.suscripcionid = s.suscripcionid " +
                "WHERE su.telefono = ?";
        Cursor cursor = db.rawQuery(query, new String[]{telefono});
        String suscripcion = null;

        if (cursor.moveToFirst()) {
            suscripcion = cursor.getString(0); // Obtener el tipo de la suscripción
        }
        cursor.close();
        db.close();
        return suscripcion; // Retorna "Mensual", "Trimestral", etc.
    }


    public boolean asignarSuscripcionUsuario(String telefono, int idSuscripcion, String tipoSuscripcion) {
        SQLiteDatabase db = getWritableDatabase();

        // Obtener la fecha de inicio (fecha actual)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar inicio = Calendar.getInstance();
        String fechaInicio = dateFormat.format(inicio.getTime());

        // Calcular la fecha de fin en función del tipo de suscripción
        Calendar fin = (Calendar) inicio.clone();
        switch (tipoSuscripcion) {
            case "Mensual":
                fin.add(Calendar.MONTH, 1);
                break;
            case "Trimestral":
                fin.add(Calendar.MONTH, 3);
                break;
            case "Semestral":
                fin.add(Calendar.MONTH, 6);
                break;
            case "Anual":
                fin.add(Calendar.YEAR, 1);
                break;
        }
        String fechaFin = dateFormat.format(fin.getTime());

        // Insertar los valores en la tabla suscripcion_usuario
        ContentValues values = new ContentValues();
        values.put("telefono", telefono);
        values.put("suscripcionid", idSuscripcion);
        values.put("fecha_inicio", fechaInicio);
        values.put("fecha_fin", fechaFin);

        long resultado = db.insert("suscripcion_usuario", null, values);
        db.close();
        return resultado != -1;
    }

    public int cancelarSuscripcionUsuario(String telefono) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "telefono = ?";
        String[] whereArgs = {telefono};

        int filasEliminadas = db.delete("suscripcion_usuario", whereClause, whereArgs);
        db.close();
        return filasEliminadas;
    }





}