package com.example.fai.tpo2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class RankingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ranking);
        Cursor c=MainActivity.getAllRecords();
        if(c != null) {

            //LayoutInflater factory = getLayoutInflater();

            TableLayout tl = (TableLayout)findViewById(R.id.tabla_ranking);

            int posicion = 1;

            while (c.moveToNext()) {
                String nombre = c.getString(c.getColumnIndex("nombre"));
                String puntaje = c.getString(c.getColumnIndex("puntaje"));

                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView posicionVisual = new TextView(this);
                posicionVisual.setText("" + posicion);
                posicionVisual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                posicionVisual.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView columna1 = new TextView(this);

                TextView nombreVisual = new TextView(this);
                nombreVisual.setText(nombre);
                nombreVisual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                TextView columna2 = new TextView(this);


                TextView puntajeVisual = new TextView(this);
                puntajeVisual.setText(puntaje);
                puntajeVisual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                puntajeVisual.setGravity(Gravity.CENTER);

                tr.addView(posicionVisual);
                tr.addView(columna1);
                tr.addView(nombreVisual);
                tr.addView(columna2);
                tr.addView(puntajeVisual);

                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                posicion++;
            }
        }

    }
}
