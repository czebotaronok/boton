package com.example.laguilera.cualquiera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Luis Aguilera on 23/1/2017.
 */


public class Libreria {

    private static Activity ma;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    public Libreria(Activity mainActivity) {
        this.ma = mainActivity;
    }

    public static void mostrarToast(int numeroMensaje) {
        String mensaje = getTexto(numeroMensaje);
        Toast.makeText(ma, mensaje, Toast.LENGTH_SHORT).show();
    }

    public static String getTexto(int numeroMensaje) {
        String texto = ma.getText(numeroMensaje).toString();
        return texto;
    }

    public static boolean abrirLectorQR() {
        String app = getTexto(R.string.appPackageNameLectorQR);
        return abrirAppPackageName(app);
    }

    private static boolean abrirAppPackageName(String app) {
        PackageManager manager = ma.getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(app);

        if (intent == null) {
            return false;
        }

        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ma.getBaseContext().startActivity(intent);
        return true;

    }


    public static void PlayAudioLlamada() {
        MediaPlayer mp = MediaPlayer.create(ma.getBaseContext(), R.raw.llamada);
        mp.start();
    }

    public static void enviarSMS(String numeroTelefono, String mensaje) {
        //        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        //        intent.putExtra("sms_body", mensaje);
        //        startActivity(intent);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numeroTelefono, null, mensaje, null, null);

    }

    public static void enviarEmail(String[] direcciones, String emailAsunto, String emailMensaje) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, direcciones);
        intent.putExtra(Intent.EXTRA_SUBJECT, emailAsunto);
        intent.putExtra(Intent.EXTRA_TEXT, emailMensaje);

        try {
            ma.startActivity(Intent.createChooser(intent, "Enviando Email"));
        } catch (ActivityNotFoundException ex) {
            mostrarToast(R.string.error_email_app_install);
        }

    }

    public static boolean getPermisoEscribirAmacenamientoExterno() {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoContactos() {
        String permission = "android.permission.WRITE_CONTACTS";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoCalendario() {
        String permission = "android.permission.WRITE_CALENDAR";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoGrabarAudio() {
        String permission = "android.permission.RECORD_AUDIO";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoNFC() {
        String permission = "android.permission.NFC";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoEstadoBateria() {
        String permission = "android.permission.BATTERY_STATS";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoCamara() {
        String permission = "android.permission.CAMERA";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoInternet() {
        String permission = "android.permission.INTERNET";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean getPermisoEnviarSMS() {
        String permission = "android.permission.SEND_SMS";
        int res = ma.getBaseContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void pronunciarTexto(String textoProdunciar) {

        //FIXME - Probar bien dado que hay varias versiones

        TextToSpeech tts = new TextToSpeech(ma, (TextToSpeech.OnInitListener) ma);
        tts.setLanguage(Locale.ENGLISH);
//        sss = tts.getDefaultLanguage();
        tts.speak(textoProdunciar, TextToSpeech.QUEUE_ADD, null);
        Locale locale = tts.getVoice().getLocale();
        Log.d("Locale:", locale.toString());

        Voice voice = new Voice("englishMan", Locale.ENGLISH, Voice.QUALITY_HIGH, Voice.LATENCY_NORMAL, false, null);
        String idioma = voice.getLocale().getCountry();

        //        Log.i("Idioma:", idioma);
//        Log.w("Idioma:", idioma);
        Log.d("Idioma:", idioma);
//        Log.v("Idioma:", idioma);

        if (locale.getDisplayName().startsWith("Spanish")) {
            Log.i("LOCALE", "Setting Locale to: " + locale.toString());
            tts.setLanguage(locale);
        }

    }

    public void mostrarSnackbar(View view, int mensajeEnvio) {
        Snackbar.make(view, mensajeEnvio, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    public void setCliptext(String texto) {

        ClipboardManager clipboard = (ClipboardManager) ma.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Base Datos", texto);
        clipboard.setPrimaryClip(clip);

    }


    public void copiarArchivo(File archivoOrigen) throws IOException {

//        String out = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Luiss/";
//
//        File outFile1 = new File(out, archivoOrigen.getName());
//
//        copyFile(archivoOrigen, outFile1);

        backup(archivoOrigen);

//        String filename = "myfile";
//        String string = "Hello world!";
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = ma.openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(string.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        String filename;
//
//        try {
//
//            filename = archivoOrigen.getName();
//            String carpeta = Environment.getExternalStorageDirectory().getAbsolutePath() ;
//
////            File outFile = new File(carpeta, filename);
//            File outFile = new File(ma.getExternalFilesDir(null), filename);
//
//
//            FileOutputStream out = new FileOutputStream(outFile);
//            copyFile(archivoOrigen, out);
//            archivoOrigen.close();
//            archivoOrigen = null;
//            out.flush();
//            out.close();
//            out = null;
//        } catch (IOException e) {
//            Log.e("tag", "Failed to copy file: " + filename, e);
//        }
    }

//    private void copyFile(InputStream in, OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        int read;
//        while ((read = in.read(buffer)) != -1) {
//            out.write(buffer, 0, read);
//        }
//    }


    public void backup(File archivo) throws IOException {

        //setCliptext("Inicio Backup");
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String inFileName = archivo.getAbsolutePath();

        //setCliptext(inFileName);

        File dbFile = new File(inFileName);
        FileInputStream fis = null;
        fis = new FileInputStream(dbFile);

        //setCliptext(fis.toString());

        String directorio = ma.getBaseContext().getExternalFilesDir("XYX").toString();
        setCliptext(directorio);

        File d = new File(directorio);
        setCliptext(d.getPath());

        if (!d.exists()) {
            d.mkdir();
        }


        String outFileName = directorio + "/" + archivo.getName() + "_" + timeStamp;
        setCliptext(outFileName);

        OutputStream output = new FileOutputStream(outFileName);

        //setCliptext(output.toString());

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        fis.close();

//        setCliptext("Fin de Copia");

    }

    public void speak(TextToSpeech engineSpeech, String hablar) {

//        engine = new engineSpeech(this, this);


//        engineSpeech.speak(hablar, TextToSpeech.QUEUE_FLUSH, null);

        engineSpeech.setPitch((float) 5);
        engineSpeech.setPitch((float) 1);       // Normal
        engineSpeech.setSpeechRate((float) 2);  //Rapido
        engineSpeech.setSpeechRate((float) 1);  //Normal
        engineSpeech.setSpeechRate((float) 0.5);  //Lento


        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        dir.mkdirs();
        File salida = new File(dir, "Luiss.wav");

//        engineSpeech.synthesizeToFile(hablar, createParams("PruebaArchivo.Wav"), dir, "Dale");


        int speak = engineSpeech.speak(hablar, TextToSpeech.QUEUE_FLUSH, null, null);

        if (speak == 0) {
            Toast.makeText(ma.getBaseContext(), dir.getPath(), Toast.LENGTH_SHORT).show();
        }

    }


    private Bundle createParams(String utterance) {
        Bundle params = new Bundle();
        params.putString("ASD", "FGH");
        return params;
    }

}


//    private void doPhotoPrint() {
//        PrintHelper photoPrinter = new PrintHelper(this.getApplicationContext());
//        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_airplay_black_24dp);
//        photoPrinter.printBitmap("Avioncito - Prueba de Impresion", bitmap);
//    }


