package com.example.fai.tpo2;

import android.graphics.Canvas;

/**
 * Created by morexlt on 2/3/16.
 */

public class HiloJuego extends Thread {
    static final long FPS = 10;
    private Juego view;
    private boolean running = false;

    private boolean terminar = false;

    public HiloJuego(Juego view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    public boolean getTerminar() {
        return terminar;
    }

    public void setTerminar(boolean terminar) {
        this.terminar = terminar;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    //if(view.juegoTerminado()) {
                    if(this.terminar){
                        System.out.println("JUEGO TERMINAOD :D ");
                        view.getNickname();
                        view.terminar();
                    }


                    view.onDraw(c);
                    view.actualizarArregloSprites();
                    view.comprobarYgenerarSprites();


                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }

        }
}
