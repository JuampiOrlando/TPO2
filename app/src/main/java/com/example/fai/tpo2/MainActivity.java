package com.example.fai.tpo2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {
    //private EditText textBox;
    //private TextView puntitos;
    static final int READ_BLOCK_SIZE = 100;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //PARA LA CREACION DE LA BASE DE DATOS
    private esquemaBDranking dataSource;

    //si var en 0 musica de fondo activada, en 1 musica silenciada
    public static int musicaFondoSilenciada=0;

    //si var en 0 efectos de sonido activados, en 1 efectos de sonido silenciados
    public static int efectosSilenciados=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_principal);


        //CREAMOS LA BASE DE DATOS.
        dataSource = new esquemaBDranking(this);

       // textBox = (EditText) findViewById(R.id.txtText1);
        //puntitos = (TextView) findViewById(R.id.Puntos);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClickJugar(View v) {
/*

        //Podriamos usar un Toast si quisieramos
        Context context = getApplicationContext();
        CharSequence text = "Bienvenido!!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
*/
        //Mensaje de Bienvenida
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        String msj = getResources().getString(R.string.begin);
        builder1.setMessage(msj);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                getResources().getString(R.string.begin2),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //Invocando la otra Activity
                        Intent intent = new Intent(MainActivity.this, JuegoActivity.class);
                        //intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                        //finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    public void onClickCheckBox1(View v){
        //control musica de fondo
        CheckBox checkBox1 = (CheckBox) v;

                if (checkBox1.isChecked()) {
                    //tic en caja
                    musicaFondoSilenciada = 1;

                } else {
                    musicaFondoSilenciada = 0;
                 }
    }

    public void onClickCheckBox2(View v){
        //control efectos de sonido
        CheckBox checkBox2 = (CheckBox) v;

                if(checkBox2.isChecked()){
                    //tic en caja
                    efectosSilenciados=1;

                }else {
                    efectosSilenciados=0;
                }

    }

    public void onClickSalir(View v){
        //resetear variables al salir
        musicaFondoSilenciada=0;
        efectosSilenciados=0;
        finish();
    }

    /*public void onClickGuardar(View v) {
        String str = textBox.getText().toString();
        try {
            FileOutputStream fos = openFileOutput("textFile.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            // Escribimos el String en el archivo
            osw.write("\n" + str);
            osw.flush();
            osw.close();

            // Mostramos que se ha guardado
            Toast.makeText(getBaseContext(), "Guardado", Toast.LENGTH_SHORT).show();

            textBox.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }*/

   /* public void onClickRanking(View v) {
        try {
            FileInputStream fis = openFileInput("textFile.txt");
            InputStreamReader isr = new InputStreamReader(fis);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";

            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                // Convertimos los char a String
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;

                inputBuffer = new char[READ_BLOCK_SIZE];
            }

            // Establecemos en el visor el texto que hemos leido
            puntitos.setText(s);

            // Mostramos un Toast con el proceso completado
            Toast.makeText(getBaseContext(), "Cargado", Toast.LENGTH_SHORT).show();

            isr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    */
    //Esto se autogenero solo! Dsps debemos revisar!
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.fai.tpo2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // esto es para cuando se sale del juego sin clickear el boton del menu salir
        // las variables se deben resetear al salir
        musicaFondoSilenciada=0;
        efectosSilenciados=0;

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.fai.tpo2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}