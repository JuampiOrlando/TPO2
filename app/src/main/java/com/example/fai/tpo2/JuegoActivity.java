package com.example.fai.tpo2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class JuegoActivity extends Activity {

    MediaPlayer musicaFondo;
    ToggleButton musica;
    ToggleButton btnPausa;
    private Juego surface_vista;

    private int musicaFondoTic = MainActivity.musicaFondoSilenciada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego_layout);

        this.agregarJuego(savedInstanceState);

        this.agregarMusica();

        this.agregarPausa();

        //Mensaje de Bienvenida
        Context context = getApplicationContext();
        CharSequence txt = getResources().getString(R.string.mensaje);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, txt, duration);
        toast.show();


    }

    private void agregarMusica(){
        if (musicaFondoTic==0){
            musicaFondo = MediaPlayer.create(this,R.raw.musicafondo);
            musicaFondo.setLooping(true);
            musicaFondo.start();

            musica = (ToggleButton) findViewById(R.id.cajaM);
            musica.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (musica.isChecked()) {
                        // si esta pausado regresa al ultimo estado de la musica
                        musicaFondo.start();
                    } else {
                        // pausa la musica
                        musicaFondo.pause();
                    }
                }
            });
        } else{
            musica = (ToggleButton) findViewById(R.id.cajaM);
            ///queda silenciado
            musica.toggle();
            musica.setEnabled(false);
            // tacha reproducir
            musica.setPaintFlags(musica.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }
    private void agregarPausa(){
        btnPausa = (ToggleButton) findViewById(R.id.btnPausa);
        System.out.println("BOTON: "+(btnPausa!=null));
        btnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnPausa.isChecked()) {
                    surface_vista.despausar();
                    if(musicaFondoTic==0 && !musicaFondo.isPlaying() ){
                        musicaFondo.start();
                        musica.toggle();
                    }
                } else {

                    surface_vista.pausar();

                    if( musicaFondoTic==0 && musicaFondo.isPlaying()){
                        musicaFondo.pause();
                        musica.toggle();
                    }


                }
            }
        });
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
        if (musicaFondoTic==0){
          musicaFondo.release();
        }
        //finish();
    }

    private void agregarJuego(Bundle savedInstanceState){
        surface_vista = new Juego(this,savedInstanceState);

        LinearLayout contenedor;
        contenedor = (LinearLayout) findViewById(R.id.linear_juego);
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametros.height=0;
        parametros.weight = (float) 0.9;
        contenedor.addView(surface_vista, parametros);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        List<Sprite> todosSprites = surface_vista.getSprites();

        outState.putInt("vidas",surface_vista.getVidas());
        outState.putInt("contador",surface_vista.getContador());

        outState.putInt("tam", todosSprites.size() );
        //Por cada sprite, almacenamos un arreglo de sus atributos
        for(int i = 0; i < todosSprites.size();i++){
            outState.putStringArrayList("sprite"+i,todosSprites.get(i).toArrayList() );
        }


    }

}
