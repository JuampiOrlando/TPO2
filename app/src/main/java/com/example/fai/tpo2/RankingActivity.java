package com.example.fai.tpo2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by juampi9498 on 15/02/16.
 */
public class RankingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ranking);
        Cursor c=MainActivity.getAllRecords();
        if(c != null) {

            //LayoutInflater factory = getLayoutInflater();

            TableLayout tl = (TableLayout)findViewById(R.id.tabla_ranking);

            while (c.moveToNext()) {
                String nombre = c.getString(c.getColumnIndex("nombre"));
                String puntaje = c.getString(c.getColumnIndex("puntaje"));

                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView nombreVisual = new TextView(this);
                nombreVisual.setText(nombre);


                TextView puntajeVisual = new TextView(this);
                puntajeVisual.setText(puntaje);
                puntajeVisual.setGravity(Gravity.CENTER);

                tr.addView(nombreVisual);
                tr.addView(puntajeVisual);

                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }

    }
}
