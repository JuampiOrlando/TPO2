package com.example.fai.tpo2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

    private int contador;
    private int vidas;
    private TextView contadorVisual;
    private TextView vidasVisual;

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
                createSprites();
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

    private void createSprites() {
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
        sprites.add(createSprite(R.drawable.asd));
    }

    public boolean juegoTerminado(){
        boolean fin = sprites.isEmpty();
        return fin;
    }

    public void terminar(){

        sprites.clear();
        temps.clear();
        gameLoopThread.setRunning(false);
        actividadJ.finish();

    }

    public void getNickname(){
        System.out.println("pedimos los datos!!!!!!!!");
    }


    private Sprite createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this, bmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //contadorVisual.setText(""+contador);

        canvas.drawColor(Color.BLACK);
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 50) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();


            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollition(x, y)) {
                        sprites.remove(sprite);
                        contador++;
                        contadorVisual.setText(""+contador);

                        //mp.start();
                        sonido.play(sonidito, 1, 1, 0, 0, 1.5F);

                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));

                        break;
                    }
                }
            }
        }
        return true;
    }
}
