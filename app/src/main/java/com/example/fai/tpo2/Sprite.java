package com.example.fai.tpo2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Sprite {
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int MAX_SPEED = 5;
    private Juego gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int currentFrame = 0;
    private int width;
    private int height;

    private int vidas;

    public Sprite(Juego gameView, Bitmap bmp,int vidasP) {


        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.gameView = gameView;
        this.bmp = bmp;

        this.vidas = vidasP; //Vidas del Sprite (ingresan por parametro)

        Random rnd = new Random();
        //x = rnd.nextInt(gameView.getWidth() - width);
        x=0;


        //////////////////////////////////////////////////////////////
        //  4 Carriles

        int maximo = gameView.getHeight();
        System.out.println("maximo: " + maximo);
        int divisor = maximo / 4;
        System.out.println("divisor: " + divisor);

        int carril = rnd.nextInt(4);

        System.out.println("carril: " + carril);
        y = carril * divisor;
        System.out.println("y: " + y);




        //////////////////////////
        xSpeed = rnd.nextInt(MAX_SPEED * 5) + 1;
        //ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
        ySpeed = 0;
    }

    private void update() {
        x = x + xSpeed;
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = 0 * height;  //seleccionamos la fila del sprite
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);

    }


    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }

    /*
    * Cada Touch que coincide con el sprite le quito una vida
    * y pregunto si murio
    * */
    public boolean seMurio(){
        vidas--;
        return (vidas==0);
    }

    public void matarSprite(){
        vidas = 0;
    }

    public int getVidas(){
        return vidas;
    }

    public boolean sobrevivio(){
        boolean resultado = false;

        if (x + width >= gameView.getWidth() ){
            resultado = true;
        }

        return resultado;
    }
}
