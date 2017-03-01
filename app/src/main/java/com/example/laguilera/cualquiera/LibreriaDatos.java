package com.example.laguilera.cualquiera;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Luis Aguilera on 27/1/2017.
 */

public class LibreriaDatos extends SQLiteOpenHelper {

    public LibreriaDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        unique
        db.execSQL("CREATE TABLE " + DefinicionDatos.TABLA_BANDAS + " ( id Integer primary key , nombre Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionPosterior) {
        db.execSQL("DROP TABLE if exist " + DefinicionDatos.TABLA_BANDAS);
        db.execSQL("CREATE TABLE " + DefinicionDatos.TABLA_BANDAS + " ( id Integer primary key , nombre Text)");
    }
}
