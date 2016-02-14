package com.example.fai.tpo2;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class JuegoActivity extends Activity {

    MediaPlayer musicaFondo;
    ToggleButton musica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //setContentView(new Juego(this));
        setContentView(R.layout.juego_layout);

        this.agregarJuego();

        musicaFondo = MediaPlayer.create(this,R.raw.musicafondo);
        musicaFondo.setLooping(true);
        musicaFondo.start();

        musica = (ToggleButton) findViewById(R.id.cajaM);
        musica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (musica.isChecked()) {
                    // mediaplayer is already muted, so needs be to unmuted
                    musicaFondo.start();
                } else {
                    // mute media player
                    musicaFondo.pause();
                }
            }
        });

        Context context = getApplicationContext();
        CharSequence txt = getResources().getString(R.string.mensaje);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, txt, duration);
        toast.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override

    protected void onPause(){
        super.onPause();
        musicaFondo.release();
        finish();
    }

    private void agregarJuego(){
        Juego surface_vista = new Juego(this);
        LinearLayout contenedor;
        contenedor = (LinearLayout) findViewById(R.id.linear_juego);
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametros.height=0;
        parametros.weight = (float) 0.9;
        contenedor.addView(surface_vista,parametros);
    }

}
