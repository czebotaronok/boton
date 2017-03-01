package com.example.laguilera.cualquiera;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class SegundaActivity extends AppCompatActivity  {

    TextView texto;
    CheckBox chkContactos;
    CheckBox chkEscritura;
    CheckBox chkInternet;
    CheckBox chkSMS;

    private Libreria libreria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        libreria = new Libreria(this);

        // Inicio Elementos de la Pantalla
        texto = (TextView) findViewById(R.id.email);
        chkContactos = (CheckBox) findViewById(R.id.chkContactos);
        chkEscritura = (CheckBox) findViewById(R.id.chkEscritura);
        chkInternet = (CheckBox) findViewById(R.id.chkInternet);
        chkSMS = (CheckBox) findViewById(R.id.chkSMS);

        // Leo los valores pasados
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String email = extras.getString("Email");
            texto.setText(email);
        }

        chkEscritura.setChecked(libreria.getPermisoEscribirAmacenamientoExterno());
        chkContactos.setChecked(libreria.getPermisoContactos());
        chkInternet.setChecked(libreria.getPermisoInternet());
        chkSMS.setChecked(libreria.getPermisoEnviarSMS());

    }

}
