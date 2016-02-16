package com.example.fai.tpo2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
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

    private SoundPool sonido = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
    private int disparo;
    private int splash;
    private int scream;
    private int woah;
    private JuegoActivity actividadJ;

    private int contador;
    private int vidas;
    private TextView contadorVisual;
    private TextView vidasVisual;
    private ImageView corazones;

    private boolean paused;


    private int numeroMaximoSprites = 12;

    // para controlar efectos de sonido
    private int efectosTic = MainActivity.efectosSilenciados;


    public Juego(JuegoActivity context, final Bundle savedInstanceStat){
        super(context);
        actividadJ = context;

        //Si es rotacion guarda vidas
        this.actualizarVisuales(savedInstanceStat);

        gameLoopThread = new HiloJuego(this);

        final Juego a = this;

        this.cargarSonidos();

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
                    if (gameLoopThread.getState() == Thread.State.TERMINATED) {

                        gameLoopThread = new HiloJuego(a);
                        if (savedInstanceStat != null)
                            comprobarYgenerarSprites(savedInstanceStat);
                        else
                            comprobarYgenerarSprites();
                        gameLoopThread.setRunning(true);
                        gameLoopThread.start();
                    } else {
                        if (savedInstanceStat != null)
                            comprobarYgenerarSprites(savedInstanceStat);
                        else
                            comprobarYgenerarSprites();
                        gameLoopThread.setRunning(true);
                        gameLoopThread.start();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                                           int width, int height) {
                }
            });

        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
    }


    private void actualizarVisuales(Bundle savedInstanceStat){
        if (savedInstanceStat != null){

            contador = savedInstanceStat.getInt("contador");;
            vidas = savedInstanceStat.getInt("vidas");;
        }else {
            contador = 0;
            vidas = 3;
        }
        contadorVisual = (TextView)actividadJ.findViewById(R.id.contador);
        contadorVisual.setText("" + contador);
        vidasVisual = (TextView)actividadJ.findViewById(R.id.vidas);
        vidasVisual.setText("" + vidas);

        corazones = (ImageView)actividadJ.findViewById(R.id.imageViewCorazones);
        actualizarVidas();
    }

    private void cargarSonidos(){
        disparo = sonido.load(actividadJ,R.raw.gun,1);
        splash = sonido.load(actividadJ,R.raw.splat,1);
        scream = sonido.load(actividadJ,R.raw.scream,1);
        woah = sonido.load(actividadJ,R.raw.woah_male,1);
    }

    public int getVidas() {
        return vidas;
    }
    public int getContador() {
        return contador;
    }

    public void actualizarVidas(){
        //corazones = (ImageView)actividadJ.findViewById(R.id.corazones1);
        switch (vidas){
            case 1:
                corazones.setImageResource(R.drawable.cor1);
                //corazones.setBackgroundResource(R.drawable.cor1);
                break;
            case 2:
                corazones.setImageResource(R.drawable.cor2);
                //corazones.setBackgroundResource(R.drawable.cor2);
                break;
            case 3:
                //corazones.setBackgroundResource(R.drawable.cor3);
                corazones.setImageResource(R.drawable.cor3);
                break;
        }
    }



    public void comprobarYgenerarSprites(){

        Random rnd = new Random();

        if(sprites.size()<numeroMaximoSprites){
            if (!comprobarBueno()){
                sprites.add(createSpriteBueno());
            }
            int diferencia = numeroMaximoSprites-sprites.size();

            for(int i = 0; i < diferencia; i++){
                sprites.add(createSprite((rnd.nextInt(10) + 1)));
            }

        }


    }
    public void comprobarYgenerarSprites(Bundle saved){
        System.out.println("LLEGUeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        int tam = saved.getInt("tam");
        List<Sprite> sprites = new ArrayList<Sprite>();

            for(int i = 0; i < tam;i++) {
                ArrayList<String> a = saved.getStringArrayList("sprite"+i);
                if(Integer.parseInt(a.get(5))== 1){
                    sprites.add(new Sprite(this,BitmapFactory.decodeResource(getResources(), R.drawable.general),a));
                }
                else
                    sprites.add(new Sprite(this,obtenerBmp(Integer.parseInt(a.get(0))),a));
            }
        this.sprites=sprites;
    }

    public boolean comprobarBueno(){
        boolean res=false;
        for(Sprite sprite : sprites){
            if(sprite.esBueno())
                res=true;
        }
        return res;
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
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public void pausarHilo(){
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
    public void desPausarHilo(){
        if (gameLoopThread .getState() == Thread.State.TERMINATED)
        {

            gameLoopThread =new HiloJuego(this);
            comprobarYgenerarSprites();
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        }
        else
        {
            comprobarYgenerarSprites();
            gameLoopThread.setRunning(true);
            if(!gameLoopThread.isAlive())
                gameLoopThread.start();
        }
    }


    public void getNickname(){
        Intent intent = new Intent(actividadJ,PopNickname.class);
        intent.putExtra("EXTRA_PUNTAJE", ("" + contador));
        actividadJ.startActivity(intent);
    }


    private Sprite createSprite(int vidas) {

        //Seleccion del sprite dependiendo la vida;


        return new Sprite(this, obtenerBmp(vidas),vidas,false);
    }

    private Bitmap obtenerBmp(int vidas){
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
        return bmp;
    }

    private Sprite createSpriteBueno() {
        int resouce;
        resouce = R.drawable.general;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this, bmp,3,true);
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
        /*for (int i = 0; i < this.getWidth(); i = i  + 16) {
            for (int j = 0; j < this.getHeight(); j = j + 16) {
                Rect src = new Rect(0, 0, 16, 16);
                Rect dst = new Rect(i, j, i + 16, j + 16);
                Bitmap bmpPiso = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
                canvas.drawBitmap(bmpPiso, src, dst, null);
            }
        }
*/
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        for (Sprite sprite : sprites) {

            if(sprite.sobrevivio()){
                if(!sprite.esBueno()){
                    if (efectosTic == 0){
                        sonido.play(scream, 1, 1, 0, 0, 1.5F);
                    }
                    vidas = vidas - 1;
                    actividadJ.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            vidasVisual.setText("" + vidas);
                            actualizarVidas();

                        }
                    });
                 }
                sprite.matarSprite();
            } else {
                sprite.onDraw(canvas);
            }


                }
    }

    public void pausar(){
        this.pausarHilo();
        paused = true;
    }
    public void despausar(){
        this.desPausarHilo();
        paused = false;
    }

    public HiloJuego getGameLoopThread() {
        return gameLoopThread;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!paused) {
            if (System.currentTimeMillis() - lastClick > 50) {
                lastClick = System.currentTimeMillis();
                float x = event.getX();
                float y = event.getY();


                synchronized (getHolder()) {
                    if (!sprites.isEmpty()) {
                        boolean tocado = false;
                        int i = sprites.size() - 1;
                        //for (int i = sprites.size() - 1; i >= 0; i--) {
                        while (tocado == false && i >= 0) {

                            Sprite sprite = sprites.get(i);

                            if (sprite.isCollition(x, y)) {
                                tocado = true;
                                if (sprite.seMurio()) {

                             if(sprite.esBueno()){
                                 vidas = vidas - 1;
                                 actividadJ.runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         vidasVisual.setText("" + vidas);
                                         actualizarVidas();
                                     }
                                 });

                                    } else {
                                        contador++;
                                        contadorVisual.setText("" + contador);
                                    }
                                    if (efectosTic == 0) {
                                        sonido.play(splash, 1, 1, 0, 0, 1.5F);
                                    }
                                    sprites.remove(sprite);
                                    temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                                } else {

                                    if (efectosTic == 0) {
                                        if (sprite.esBueno()) {
                                            sonido.play(woah, 1, 1, 0, 0, 1.5F);
                                        } else {
                                            sonido.play(disparo, 1, 1, 0, 0, 1.5F);
                                        }
                                    }
                                }
                            }
                            //mp.start();

                            //break;

                            i--;
                        }
                    }
                }
            }
        }
        return true;
    }
}
