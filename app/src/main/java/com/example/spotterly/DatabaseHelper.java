package com.example.spotterly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                    "FOREIGN KEY (suscripcionid) REFERENCES suscripcion(suscripcionid)" +
                    ")");

            // Create the 'suscripcion' table
            db.execSQL("CREATE TABLE suscripcion (" +
                    "suscripcionid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tipo VARCHAR(50) NOT NULL," +
                    "fecha_inicio DATE NOT NULL," +
                    "fecha_fin DATE NOT NULL," +
                    "activa BOOLEAN DEFAULT 1," +
                    "usuario_principal INTEGER NOT NULL," +
                    "FOREIGN KEY (usuario_principal) REFERENCES usuario(telefono)" +
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

    // ... (método onUpgrade)

    // Método para insertar un usuario
    public long insertUsuario(Usuario usuario) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TELEFONO, usuario.getTelefono());
        values.put(COLUMN_PASSWORD, usuario.getPassword());
        values.put(COLUMN_NOMBRE, usuario.getNombre());
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
}