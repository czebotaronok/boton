package com.example.laguilera.cualquiera;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.laguilera.cualquiera.DefinicionDatos.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener, TextToSpeech.OnInitListener {

    TextView boquita;
    EditText editEMail;
    EditText editNombre;
    CheckBox checkBostero;
    Button botonConfirmar;
    ImageView imagenVer;
    //ImageView imagenOtra;
    RatingBar calificacion;

    AudioManager audio;
    SensorManager sm;
    Sensor sensor;
    TextToSpeech textToSpeech;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Libreria libreria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniciarObjetos();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                libreria.mostrarSnackbar(view, R.string.mensajeEnvio);
                enviarEmail();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

        setearConfiguracion(item);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String textoEvento = String.valueOf(event.values[0]);
        editNombre.setText(textoEvento);
        float valor = Float.parseFloat(textoEvento);

        editNombre.setText(textoEvento);

        if (valor == 0) {
            editNombre.setBackgroundColor(Color.BLUE);
            libreria.PlayAudioLlamada();
        } else {
            editNombre.setBackgroundColor(Color.YELLOW);
        }


        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < 5) {
                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else {
                audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void iniciarObjetos() {
        // Libreria
        libreria = new Libreria(this);

        // Inicio Sensores
        audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // Relacion Objetos
        boquita = (TextView) findViewById(R.id.boquita);
        editEMail = (EditText) findViewById(R.id.editEmail);
        editNombre = (EditText) findViewById(R.id.editNombre);
        checkBostero = (CheckBox) findViewById(R.id.checkBostero);
        botonConfirmar = (Button) findViewById(R.id.buttonConfirmar);
        imagenVer = (ImageView) findViewById(R.id.imageMarcaLibro);
        //imagenOtra = (ImageView) findViewById(R.id.imagenFondo);
        calificacion = (RatingBar) findViewById(R.id.calificacion);


        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        // Seteo Inicial
        boquita.setText(R.string.boquita);
        editEMail.setText(R.string.mailEjemplo);
        checkBostero.setText(R.string.bostero);
        botonConfirmar.setText(R.string.aceptar);
        //imagenVer.setRotationX(100);
        imagenVer.setColorFilter(Color.RED);

        botonConfirmar.setText(R.string.enviarSMS);
        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkBostero.setText(R.string.mensajeTutis);
                checkBostero.setChecked(true);

                calificacion.setNumStars(5);
                calificacion.setRating(2);
                calificacion.setBackgroundColor(Color.BLUE);

                //libreria.mostrarToast(R.string.mensajeTutis);
                libreria.mostrarSnackbar(view, R.string.mensajeTutis);


                enviarSMS();

//                libreria.pronunciarTexto("Guzzi Is a Fucking Asshole!!!");

//                activarSegundaActivity();

            }

        });

    }


    private void enviarSMS() {
        String numeroTelefono = (String) getText(R.string.numeroTelefonoTutis);
        String mensaje = (String) getText(R.string.mensajeEnvio);
        libreria.enviarSMS(numeroTelefono, mensaje);
        //libreria.doPhotoPrint();
    }

    private void setearConfiguracion(MenuItem item) {

        item.setTitle(R.string.magia);
        boquita.setText(R.string.hola);

        libreria.PlayAudioLlamada();

        checkBostero.setChecked(libreria.abrirLectorQR());
    }

    private void enviarEmail() {
        ArrayList<String> destinatarios = new ArrayList<>();
        String emailAsunto = "Que Onda Vieja?";
        String emailMensaje = "Aguante Boquita Wachin!!!";
        destinatarios.add(editEMail.getText().toString());
        destinatarios.add("laguilera@mbsoft.com.ar");
        String[] direcciones = destinatarios.toArray(new String[0]);

        libreria.enviarEmail(direcciones, emailAsunto, emailMensaje);

        //imagenOtra.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));
        imagenVer.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

    }

    private void activarSegundaActivity() {
        Intent intent = new Intent(MainActivity.this, SegundaActivity.class);
        intent.putExtra("Email", editEMail.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onInit(int status) {

    }

    public void alta(View v) {

        LibreriaDatos libDatos = new LibreriaDatos(this, DB_MUSICA, null, 1);
        SQLiteDatabase db = libDatos.getWritableDatabase();
        ContentValues registro = new ContentValues();
        String nombre = editEMail.getText().toString();

        registro.put(CAMPO_NOMBRE, nombre);

        db.insert(TABLA_BANDAS, null, registro);
        db.close();

    }

    public void consulta(View v) {

        LibreriaDatos libDatos = new LibreriaDatos(this, DB_MUSICA, null, 1);
        SQLiteDatabase db = libDatos.getWritableDatabase();

        String id = editNombre.getText().toString();
        String consulta = "SELECT NOMBRE FROM " + TABLA_BANDAS + " WHERE id = " + id;
        editEMail.setText(consulta);

        Cursor registro = db.rawQuery(consulta, null);

        Log.i("Registro:    ", registro.getString(0));

        if (registro.moveToFirst()) {
            editNombre.setText(registro.getString(0));
        } else {
            libreria.mostrarToast(R.string.errorDatosNoExisteRegisto);
        }
        db.close();
    }

    public void borrar(View v) throws IOException {

        LibreriaDatos libDatos = new LibreriaDatos(this, DB_MUSICA, null, 1);
        SQLiteDatabase db = libDatos.getWritableDatabase();

        File archivoDB = getDatabasePath(DB_MUSICA);
        libreria.setCliptext(archivoDB.getPath());
        editEMail.setText(archivoDB.getPath());


        String id = editNombre.getText().toString();

        int registrosBorrador = db.delete(TABLA_BANDAS, "id = " + id, null);

        if (registrosBorrador == 0) {
            libreria.mostrarSnackbar(v, R.string.errorDatosNoPudoBorrarResitro);
        }

        db.close();

        libreria.copiarArchivo(archivoDB);

    }

    public void modificar(View v) {

        LibreriaDatos libDatos = new LibreriaDatos(this, DB_MUSICA, null, 1);
        SQLiteDatabase db = libDatos.getWritableDatabase();
        ContentValues registro = new ContentValues();

        String id = editNombre.getText().toString();
        String nombre = editEMail.getText().toString();

        registro.put(CAMPO_NOMBRE, nombre);

        int registrosActualizados = db.update(TABLA_BANDAS, registro, "ID = " + id, null);
        db.close();

        String hablar = editEMail.getText().toString();

        libreria.speak(textToSpeech, hablar);

        if (registrosActualizados == 0) {
            libreria.mostrarToast(R.string.errorDatosNoPudoModificarResitro);
        }

    }

}

//  Enviar SMS
//  Enviar Email
//  Abrir App
//  Play Audio

//  Ver
//      AsyncTask