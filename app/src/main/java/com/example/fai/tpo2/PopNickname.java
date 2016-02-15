package com.example.fai.tpo2;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

/**
 * Created by juampi9498 on 14/02/16.
 */
public class PopNickname extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_nickname);
/*
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //Le damos la proporcion del popNickname
        getWindow().setLayout((int)(width*.8),(int)(height*.8));*/

        //Obteniendo el puntaje
        Bundle extras = getIntent().getExtras();
        String puntaje = extras.getString("EXTRA_PUNTAJE");


        TextView puntajeVisual = (TextView) findViewById(R.id.puntaje);
        puntajeVisual.setText(puntaje);

    }
    public void onClickEnviar(View v) {

    }
}
