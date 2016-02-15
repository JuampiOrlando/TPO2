package com.example.fai.tpo2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Juego extends SurfaceView {

    private HiloJuego gameLoopThread;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private List<TempSprite> temps = new ArrayList<TempSprite>();
    private long lastClick;
    private Bitmap bmpBlood;

    //private MediaPlayer mp;
    private SoundPool sonido = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
    private int sonidito;
    private JuegoActivity actividadJ;
    private TextView puntos;

    private int contador;
    private int vidas;
    private TextView contadorVisual;
    private TextView vidasVisual;



    private int numeroMaximoSprites = 12;

    // para controlar efectos de sonido
    private int efectosTic = MainActivity.efectosSilenciados;


    public Juego(JuegoActivity context) {
        super(context);
        actividadJ = context;

        contador = 0;
        vidas = 3;

        contadorVisual = (TextView)actividadJ.findViewById(R.id.contador);
        vidasVisual = (TextView)actividadJ.findViewById(R.id.vidas);
        gameLoopThread = new HiloJuego(this);


        //mp = MediaPlayer.create(context, R.raw.smw_coin);

        sonidito = sonido.load(context,R.raw.bum,1);

        getHolder().addCallback(new SurfaceHolder.Callback() {


            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                System.out.println("Termino?");
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                comprobarYgenerarSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    /*private void createSprites() {
        sprites.add(createSprite(R.drawable.asd, 5));
        sprites.add(createSprite(R.drawable.asd,1));
        sprites.add(createSprite(R.drawable.asd,1));
        sprites.add(createSprite(R.drawable.asd,1));
        sprites.add(createSprite(R.drawable.asd,1));
        sprites.add(createSprite(R.drawable.asd,1));
        sprites.add(createSprite(R.drawable.asd,1));
        sprites.add(createSprite(R.drawable.asd,1));
        sprites.add(createSprite(R.drawable.asd, 1));
        sprites.add(createSprite(R.drawable.asd, 1));
        sprites.add(createSprite(R.drawable.asd, 1));
        sprites.add(createSprite(R.drawable.asd,1));
    }*/


    public void comprobarYgenerarSprites(){

        Random rnd = new Random();

        if(sprites.size()<numeroMaximoSprites){

            int diferencia = numeroMaximoSprites-sprites.size();

            for(int i = 0; i < diferencia; i++){
                sprites.add(createSprite((rnd.nextInt(10) + 1)));
            }
        }


    }

    public boolean juegoTerminado(){
        boolean fin = vidas<=0;
        return fin;
    }

    public void terminar(){

        sprites.clear();
        temps.clear();
        gameLoopThread.setRunning(false);
        actividadJ.finish();
        gameLoopThread.setTerminar(true);

    }

    public void pausarHilo(){
        gameLoopThread.setRunning(false);
    }
    public void desPausarHilo(){
        gameLoopThread.setRunning(true);
    }

    public void getNickname(){
        Intent intent = new Intent(actividadJ,PopNickname.class);
        intent.putExtra("EXTRA_PUNTAJE", ("" + contador));
        actividadJ.startActivity(intent);
    }


    private Sprite createSprite(int vidas) {

        //Seleccion del sprite dependiendo la vida;
        int resouce;
        switch (vidas){
            case 0:
                resouce = R.drawable.mini_good1;
                break;
            case 1:
                resouce = R.drawable.mini_good2;
                break;
            case 2:
                resouce = R.drawable.mini_good3;
                break;
            case 3:
                resouce = R.drawable.mini_good4;
                break;
            case 4:
                resouce = R.drawable.mini_good5;
                break;
            case 5:
                resouce = R.drawable.mini_good6;
                break;
            case 6:
                resouce = R.drawable.mini_bad1;
                break;
            case 7:
                resouce = R.drawable.mini_bad2;
                break;
            case 8:
                resouce = R.drawable.mini_bad3;
                break;
            case 9:
                resouce = R.drawable.mini_bad4;
                break;
            case 10:
                resouce = R.drawable.mini_bad5;
                break;
            default:
                resouce = R.drawable.mini_bad6;

        }

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this, bmp,vidas);
    }

    public void actualizarArregloSprites(){
        for (int i = 0; i < sprites.size() ; i++) {

            if(sprites.get(i).getVidas() == 0){
                sprites.remove(i);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //contadorVisual.setText(""+contador);

        canvas.drawColor(Color.BLACK);
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        for (Sprite sprite : sprites) {

            if(sprite.sobrevivio()){
                vidas = vidas - 1;
                actividadJ.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vidasVisual.setText("" + vidas);
                    }
                });
                        sprite.matarSprite();

                    }

                    else

                    {
                        sprite.onDraw(canvas);
                    }


                }
            }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (System.currentTimeMillis() - lastClick > 50) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();


            synchronized (getHolder()) {
                if( !sprites.isEmpty()){
                   boolean tocado= false;
                   int i = sprites.size() - 1;
                   //for (int i = sprites.size() - 1; i >= 0; i--) {
                   while( tocado==false && i>=0){

                      Sprite sprite = sprites.get(i);

                      if (sprite.isCollition(x, y)) {
                         tocado=true;
                         if(sprite.seMurio()){
                             sprites.remove(sprite);

                             contador++;
                             contadorVisual.setText(""+contador);

                             temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                        }

                         //mp.start();

                         // Si no hay tic, se reproducen efectos de sonido
                         if (efectosTic == 0){
                             sonido.play(sonidito, 1, 1, 0, 0, 1.5F);
                         }

                         //break;
                      }
                      i--;
                   }
                }
            }
        }
        return true;
    }
}
