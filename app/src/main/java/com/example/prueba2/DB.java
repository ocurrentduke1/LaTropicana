package com.example.prueba2;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DB extends SQLiteOpenHelper {
    private static final String DB_NAME = "tropicana.db";
    private static final int DB_VERSION = 1;
    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dbname) {
        String query = "CREATE TABLE IF NOT EXISTS product (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "price TEXT," +
                "image TEXT," +
                "category TEXT," +
                "description TEXT)";
        /* String user_query = "CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY," +
                "username TEXT," +
                "password TEXT)"; */
        try {
            dbname.execSQL(query);
            // dbname.execSQL(user_query);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(null, "Error en la base de datos", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}