package com.example.fai.tpo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
/**
 * Created by Cabezaa on 14/02/2016.
 */
public class esquemaBDranking {


    //Esquema de la Base de datos.

    //Variables para manipulación de datos
    private DBranking openHelper;
    private SQLiteDatabase database;

    //Metainformación de la base de datos
    public static final String RANKING_TABLE_NAME = "Ranking";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";

    //Campos de la tabla Ranking
    public static class ColumnasRanking{
        public static final String ID_RANKING = BaseColumns._ID;
        public static final String NOMBRE_RANKING = "nombre";
        public static final String PUNTAJE_RANKING = "puntaje";

    }

    //Script de Creación de la tabla Ranking
    public static final String SCRIPT_CREACION_ESQUEMA =
            "create table "+RANKING_TABLE_NAME+"(" +
                    ColumnasRanking.ID_RANKING+" "+INT_TYPE+" primary key autoincrement," +
                    ColumnasRanking.NOMBRE_RANKING+" "+STRING_TYPE+" not null," +
                    ColumnasRanking.PUNTAJE_RANKING+" "+INT_TYPE+" not null)";



    //CONSTRUCTOR DEL ESQUEMA

    public esquemaBDranking(Context context) {
        //Creando una instancia de la BD
        openHelper = new DBranking(context);
        database = openHelper.getWritableDatabase();
    }


    public void insertarTupla(String nombre,String puntaje){
        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        //Almacenamos el nick del jugador y su puntaje en el contenedor
        values.put(ColumnasRanking.NOMBRE_RANKING,nombre);
        values.put(ColumnasRanking.PUNTAJE_RANKING,puntaje);

        //Insertando en la base de datos
        database.insert(RANKING_TABLE_NAME,null,values);
    }

}
