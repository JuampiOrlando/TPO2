package com.example.fai.tpo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cabezaa on 14/02/2016.
 */
public class DBranking extends SQLiteOpenHelper {


    //Clase constructora de la Base de Datos.

    public static final String DATABASE_NAME = "Ranking.db";
    public static final int DATABASE_VERSION = 1;

    public DBranking(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREAMOS LA TABLA RANKING EN LA BASE DE DATOS APENAS SE CREA LA MISMA
        db.execSQL(esquemaBDranking.SCRIPT_CREACION_ESQUEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ACA PODEMOS INSERTAR CODIGO SI QUEREMOS EJECUTAR ALGO CUANDO LA VERSION DE LA BASE DE DATOS QUE POSEE EL USUARIO NO CONCUERDA CON LA VERSION ACTUAL DE LA APLICACION.
    }
}
